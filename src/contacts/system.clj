(ns contacts.system
  (:require [contacts.http :as http]))

(def current-system (atom nil))

(defn get-system []
  @current-system)

(defn new-system
  [opts]
  {:dev? true
   :log-level :INFO})

(defn start-api-system
  [{:keys [api-port] :as opts}]
  (let [system (cond-> (new-system opts)
                 (number? api-port) (assoc :api/port api-port))]
    (reset! current-system (http/start-api system))))

(defn stop-api-system
  [system]
  (reset! current-system (http/stop-api system)))
