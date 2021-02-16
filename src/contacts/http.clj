(ns contacts.http
  (:require [contacts.http.service :as svc]
            [ring.adapter.jetty :as jetty]))

(defn start-api
  [{port :api/port :as system}]
  {:api/server (jetty/run-jetty (svc/api-service system) {:port (or port 80)
                                                          :join? false})})

(defn stop-api
  [{server :aspi/server :as system}]
  (when server
    (assoc system :api/server (.stop server))))
