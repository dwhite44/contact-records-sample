(ns contacts.http.service
  (:require [compojure
             [core :refer [defroutes GET POST]]
             [route :as route]]
            [contacts.core :as contacts]
            [contacts.http.middleware :as middleware]
            [ring.util.response :as resp]))

(defn health-handler
  [req]
  ;;TODO: Test that database or any other dependencies are running
  (resp/response {:status "OK"}))

(defn create-record-handler
  [{:keys [body-str system] :as req}]
  (let [contact (contacts/import-contact body-str)
        {:keys [success? error]} (when contact
                                   (contacts/store-contact! contact system))]
    (cond
      (nil? contact) (-> (resp/response "Invalid Input")
                         (resp/status 422))
      (false? success?) (-> (resp/response error)
                            (resp/status 422))
      :else (-> (resp/response {:status "success"})
                (resp/status 301)))))

(defroutes api-routes
  (GET "/health" req (health-handler req))
  (POST "/records" req (create-record-handler req))
  (route/not-found "Page Not Found"))

(defn api-service
  [system]
  (middleware/wrap-base #'api-routes system))
