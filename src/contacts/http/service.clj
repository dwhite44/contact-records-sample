(ns contacts.http.service
  (:require [compojure
             [core :refer [defroutes GET POST]]
             [route :as route]]
            [contacts.http.middleware :as middleware]
            [ring.util.response :as resp]))

(defn health-handler
  [req]
  ;;TODO: Test that database or any other dependencies are running
  (resp/response {:status "OK"}))

(defroutes api-routes
  (GET "/health" [] (health-handler))
  (route/not-found "Page Not Found"))

(defn api-service
  [system]
  (middleware/wrap-base #'api-routes system))
