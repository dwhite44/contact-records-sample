(ns contacts.writer
  (:require [clojure.string :as str]
            [contacts.dates :as dt]))

(defn- print-horizontal-line
  "Prints - n times"
  [n]
  (println (str/join (repeat n "-"))))

(defn- print-column
  [s size]
  (let [padding (- size (count s))
        col-str (cond
                  (= 0 padding) s
                  (pos? padding) (str s (str/join (repeat padding " ")))
                  :else (subs s 0 size))]
     (str col-str " | ")))

(defn- print-contact-header
  []
  (println (print-column "Last Name" 13)
           (print-column "First Name" 11)
           (print-column "Email" 20)
           (print-column "Color" 8)
           (print-column "Birth Date" 10)))

(defn- print-contact
  [{:keys [first-name last-name email favorite-color birth-date]}]
  (println (print-column last-name 13)
           (print-column first-name 11)
           (print-column email 20)
           (print-column favorite-color 8)
           (print-column (dt/date->str birth-date) 10)))

(defn print-contacts
  [contacts]
  (print-horizontal-line 80)
  (print-contact-header)
  (print-horizontal-line 80)
  (doall (map print-contact contacts))
  (print-horizontal-line 80))
