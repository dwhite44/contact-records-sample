(ns contacts.http.service
  (:require [compojure
             [core :refer [defroutes GET POST]]
             [route :as route]]
            [contacts.core :as contacts]
            [contacts.dates :as dt]
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

(defn get-record-handler
  [{:keys [system] :as req} sort-key]
  (let [contacts (contacts/find-contacts (:store system) {:sort sort-key})
        data (map #(update % :birth-date dt/date->str) contacts)]
    (-> (resp/response {:status "success"
                        :records data})
        (resp/content-type :json))))

(defroutes api-routes
  (GET "/health" req (health-handler req))
  (POST "/records" req (create-record-handler req))
  (GET "/records/email" req (get-record-handler req :email))
  (GET "/records/birthdate" req (get-record-handler req :birth-date))
  (GET "/records/name" req (get-record-handler req :last-name))
  (route/not-found "Page Not Found"))

(defn api-service
  [system]
  (middleware/wrap-base #'api-routes system))
