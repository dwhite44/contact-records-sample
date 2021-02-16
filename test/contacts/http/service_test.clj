(ns contacts.http.service-test
  (:require [clojure.test :refer [deftest testing is]]
            [contacts.http.service :as svc]
            [ring.mock.request :as mock]))

(def api-svc (svc/api-service {:dev? false :log-level :INFO}))

(deftest health-handler-tests
  (let [response (api-svc (mock/request :get "/health"))
        content-type (get-in response [:headers "Content-Type"])]
    (is (= 200 (:status response)))
    (is (.startsWith content-type "application/json"))))

(deftest not-found-handler-tests
  (is (= 404 (:status (api-svc (mock/request :get "/some-page"))))))
