(ns contacts.core.reader-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer [deftest testing is]]
            [contacts.core.reader :as reader]
            [contacts.dates :as dt]))

(deftest read-contact-file-contents-tests
  (testing "Reading in csv format"
    (let [contents (slurp (io/resource "test_input/mavericks.csv"))
          data (reader/read-contact-file-contents contents)
          expected-rec {:last-name "Dončić"
                        :first-name "Luka"
                        :email "luka@mavs.com"
                        :favorite-color "blue"
                        :birth-date (dt/new-date 1999 2 28)}]
      (is (= 5 (count data)))
      (is (= expected-rec (first data)))))

  (testing "Reading in pipe format"
    (let [contents (slurp (io/resource "test_input/clippers_pipe.txt"))
          data (reader/read-contact-file-contents contents)
          expected-rec {:last-name "Ibaka"
                        :first-name "Serge"
                        :email "serge@clippers.com"
                        :favorite-color "orange"
                        :birth-date (dt/new-date 1989 9 18)}]
      (is (= 5 (count data)))
      (is (= expected-rec (nth data 3)))))

  (testing "Reading in spaces format"
    (let [contents (slurp (io/resource "test_input/nuggets_spaces.txt"))
          data (reader/read-contact-file-contents contents)
          expected-rec {:last-name "Porter"
                        :first-name "Michael"
                        :email "mpj@nuggets.com"
                        :favorite-color "black"
                        :birth-date (dt/new-date 1998 6 29)}]
      (is (= 5 (count data)))
      (is (= expected-rec (nth data 2))))))
