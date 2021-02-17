(ns contacts.http.service-test
  (:require [clojure.test :refer [deftest testing is]]
            [contacts.core.memory-store :as store]
            [contacts.http.service :as svc]
            [ring.mock.request :as mock]))

(def test-store (store/new-memory-store))
(def api-svc (svc/api-service {:dev? false
                               :log-level :INFO
                               :store test-store}))

(deftest health-handler-tests
  (let [response (api-svc (mock/request :get "/health"))
        content-type (get-in response [:headers "Content-Type"])]
    (is (= 200 (:status response)))
    (is (.startsWith content-type "application/json"))))

(deftest create-record-handler-tests
  (testing "Create with csv"
    (let [input "CSV, Test, csv@test.com, blue, 2/22/2012"
          response (api-svc (-> (mock/request :post "/records")
                                (mock/content-type "text/csv")
                                (mock/body input)))
          contacts (store/all-contacts test-store)]
      (is (= 301 (:status response)))
      (is (= 1 (count contacts)))
      (is (= "csv@test.com" (:email (first contacts))))))

  (testing "Create with pipe-delimited"
    (let [input "Pipe | Test | pipe@test.com | orange | 12/26/2009"
          response (api-svc (-> (mock/request :post "/records")
                                (mock/content-type "text/plain")
                                (mock/body input)))
          contacts (store/all-contacts test-store)]
      (is (= 301 (:status response)))
      (is (= 2 (count contacts)))
      (is (= "pipe@test.com" (:email (last contacts))))))

  (testing "Create with space-delimited"
    (let [input "Space Test space@test.com orange 3/3/1990"
          response (api-svc (-> (mock/request :post "/records")
                                (mock/body input)))
          contacts (store/all-contacts test-store)]
      (is (= 301 (:status response)))
      (is (= 3 (count contacts)))
      (is (= "space@test.com" (:email (last contacts))))))

  (testing "Missing birth-date errors"
    (let [input "Invalid Test invalid@test.com black"
          response (api-svc (-> (mock/request :post "/records")
                                (mock/body input)))]
      (is (= 422 (:status response)))
      (is (= "Birth Date is Required" (:body response))))))

(deftest not-found-handler-tests
  (is (= 404 (:status (api-svc (mock/request :get "/some-page"))))))
