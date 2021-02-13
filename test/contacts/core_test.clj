(ns contacts.core-test
  (:require [clojure.test :refer :all]
            [contacts.core :as contacts]))

(deftest contacts-sort-tests
  (let [test-data [{:last-name "Leonard",
                    :first-name "Kawhi",
                    :email "kawhi@clippers.com",
                    :favorite-color "blue",
                    :birth-date #inst "1991-06-29T05:00:00.000-00:00"}
                   {:last-name "Lue",
                    :first-name "Tyronn",
                    :email "coach@clippers.com",
                    :favorite-color "red",
                    :birth-date #inst "1977-05-03T05:00:00.000-00:00"}
                   {:last-name "Williams",
                    :first-name "Lou",
                    :email "lou.williams@clippers.com",
                    :favorite-color "black",
                    :birth-date #inst "1986-10-27T06:00:00.000-00:00"}
                   {:last-name "Ibaka",
                    :first-name "Serge",
                    :email "serge@clippers.com",
                    :favorite-color "orange",
                    :birth-date #inst "1989-09-18T05:00:00.000-00:00"}
                   {:last-name "Drew",
                    :first-name "Larry",
                    :email "coach@clippers.com",
                    :favorite-color "green",
                    :birth-date #inst "1958-04-02T06:00:00.000-00:00"}]]

    (testing "sort by email (descending), last-name (ascending)"
      (let [results (contacts/sort-contacts :email test-data)
            first-names (into [] (map :first-name results))]
        (is (= first-names ["Serge" "Lou" "Kawhi" "Larry" "Tyronn"]))))

    (testing "sort by birth date (ascending)"
      (let [results (contacts/sort-contacts :birth-date test-data)
            first-names (into [] (map :first-name results))]
        (is (= first-names ["Larry" "Tyronn" "Lou" "Serge" "Kawhi"]))))

    (testing "sort by last name(descending)"
      (let [results (contacts/sort-contacts :last-name test-data)
            first-names (into [] (map :first-name results))]
        (is (= first-names ["Lou" "Tyronn" "Kawhi" "Serge" "Larry"]))))))
