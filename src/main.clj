(ns main
  (:require [clojure.string :as str]
            [contacts
             [core :as core]
             [system :as sys]]
            [contacts.core.writer :as writer])
  (:gen-class))

(defn- print-usage
  []
  (println "To run the importer:
importer [options] <input-file>
  --sort How to sort the output (email | birth | lastname)

api [options]
  --port Port to run api server on (defaults to 80)"))

(defn- validate-import-args
  [args]
  (let [arg-count (count args)
        opts (when (> arg-count 1) (->> args
                                        (take (dec arg-count))))
        sort-str (when (and (= 2 (count opts))
                            (= "--sort" (str/lower-case (first opts))))
                   (str/lower-case (second opts)))
        sort (case (str/lower-case sort-str)
               "email" :email
               "birth" :birth-date
               "lastname" :last-name
               nil)
        input-file (last args)
        err (cond
              (nil? input-file) "Requires an input file"
              (and sort-str (nil? sort)) (str "Invalid sort \"" sort-str "\"")
              (and (not-empty opts) (nil? sort)) (str "Invalid options " (str/join " " opts))
              :else nil)]

    (cond-> {:input-file input-file}
      sort (assoc :sort sort)
      err (assoc :error err))))

(defn- run-import
  [args]
  (let [{:keys [input-file error] :as opts} (validate-import-args args)]
    (if error
      (do
        (prn error)
        (System/exit -1))
      (let [system (sys/new-system {})]
        (->> (core/import-contacts system input-file (select-keys opts [:sort]))
             writer/print-contacts)))))

(defn- run-api
  [args]
  (let [port-str (when (and (= 2 (count args))
                            (= "--port" (str/lower-case (first args))))
                   (second args))
        port (when port-str (try
                              (Integer/parseInt port-str)
                              (catch Throwable t
                                (println "Invalid port: must be numeric")
                                (System/exit -1))))]
    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. (fn []
                                 (sys/stop-api-system (sys/get-system)))))
    (sys/start-api-system {:api-port port})))

(defn -main
  [& args]
  (let [run-type (first args)]
    (cond
      (= "import" run-type) (run-import (rest args))
      (= "api" run-type) (run-api (rest args))
      (= "help" run-type) (print-usage)
      (empty? run-type) (do
                          (print-usage)
                          (System/exit -1))
      :else (println (str "Invalid run type \"" run-type "\"")))))
