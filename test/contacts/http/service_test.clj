(ns contacts.http.service-test
  (:require [clojure.test :refer [deftest testing is]]
            [contacts.core :as core]
            [contacts.core.memory-store :as store]
            [contacts.http
             [service :as svc]
             [test-util :as util]]
            [ring.mock.request :as mock]))

(defn- test-store [] (store/new-memory-store))

(defn- api-svc [store]
  (svc/api-service {:dev? false
                    :log-level :INFO
                    :store store}))

(def csv-input "CSV, Test, csv@test.com, blue, 2/22/2012")
(def pipe-input "Pipe | Test | pipe@test.com | orange | 3/3/1990")
(def space-input "Space Test space@test.com orange 12/26/2009")

(deftest health-handler-tests
  (let [svc (api-svc nil)
        response (svc (mock/request :get "/health"))
        content-type (get-in response [:headers "Content-Type"])]
    (is (= 200 (:status response)))
    (is (.startsWith content-type "application/json"))))

(deftest create-record-handler-tests
  (testing "Create with csv"
    (let [store (test-store)
          svc (api-svc store)
          response (svc (-> (mock/request :post "/records")
                            (mock/content-type "text/csv")
                            (mock/body csv-input)))
          contacts (store/all-contacts store)]
      (is (= 301 (:status response)))
      (is (= 1 (count contacts)))
      (is (= "csv@test.com" (:email (first contacts))))))

  (testing "Create with pipe-delimited"
    (let [store (test-store)
          svc (api-svc store)
          response (svc (-> (mock/request :post "/records")
                            (mock/content-type "text/plain")
                            (mock/body pipe-input)))
          contacts (store/all-contacts store)]
      (is (= 301 (:status response)))
      (is (= 1 (count contacts)))
      (is (= "pipe@test.com" (:email (first contacts))))))

  (testing "Create with space-delimited"
    (let [store (test-store)
          svc (api-svc store)
          response (svc (-> (mock/request :post "/records")
                            (mock/body space-input)))
          contacts (store/all-contacts store)]
      (is (= 301 (:status response)))
      (is (= 1 (count contacts)))
      (is (= "space@test.com" (:email (first contacts))))))

  (testing "Missing birth-date errors"
    (let [input "Invalid Test invalid@test.com black"
          svc (api-svc (test-store))
          response (svc (-> (mock/request :post "/records")
                            (mock/body input)))]
      (is (= 422 (:status response)))
      (is (= "Birth Date is Required" (:body response))))))

(deftest get-record-handler-tests
  (let [store (test-store)
        svc (api-svc store)
        system {:store store}]
    ;; Store 3 test contacts
    (core/store-contact! (core/import-contact csv-input) system)
    (core/store-contact! (core/import-contact pipe-input) system)
    (core/store-contact! (core/import-contact space-input) system)

    (testing "Sort records by email"
      (let [response (svc (mock/request :get "/records/email"))
            body (util/json-body-from-response response)
            records (get body "records")
            emails (map #(get % "email") records)]
        (is (= 200 (:status response)))
        (is (= "success" (get body "status")))
        (is (= '("space@test.com" "pipe@test.com" "csv@test.com") emails))))

    (testing "Sort records by birth-date"
      (let [response (svc (mock/request :get "/records/birthdate"))
            body (util/json-body-from-response response)
            records (get body "records")
            dates (map #(get % "birth-date") records)]
        (is (= 200 (:status response)))
        (is (= "success" (get body "status")))
        (is (= '("3/3/1990" "12/26/2009" "2/22/2012") dates))))

    (testing "Sort records by last name"
      (let [response (svc (mock/request :get "/records/name"))
            body (util/json-body-from-response response)
            records (get body "records")
            names (map #(get % "last-name") records)]
        (is (= 200 (:status response)))
        (is (= "success" (get body "status")))
        (is (= '("Space" "Pipe" "CSV") names))))))

(deftest not-found-handler-tests
  (let [svc (api-svc nil)]
    (is (= 404 (:status (svc (mock/request :get "/some-page")))))))
