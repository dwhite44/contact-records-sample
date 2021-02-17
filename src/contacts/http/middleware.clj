(ns contacts.http.middleware
  (:require [ring.middleware
             [defaults :as defaults]
             [reload :as reload]
             [format :as format]]))

(defn wrap-dev
  [handler {:keys [dev?]}]
  (if dev?
    (reload/wrap-reload handler)
    handler))

(defn wrap-system
  [handler system]
  (fn [req]
    (let [new-req (assoc req :system system)]
      (handler new-req))))

(defn wrap-body-string
  [handler]
  (fn [req]
    (let [body (:body req)
          new-req (cond-> req
                    body (assoc :body-str (try
                                            (slurp body)
                                            (catch Throwable t nil))))]
      (handler new-req))))

(defn wrap-formats
  [handler]
  (format/wrap-restful-format handler {:formats [:json]}))

(defn wrap-base
  [handler system]
  (-> handler
      (wrap-dev system)
      wrap-formats
      wrap-body-string
      (wrap-system system)
      (defaults/wrap-defaults defaults/api-defaults)))
