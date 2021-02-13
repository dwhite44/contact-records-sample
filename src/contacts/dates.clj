(ns contacts.dates
  (:import [java.text SimpleDateFormat]))

(def formatter (SimpleDateFormat. "M/d/yyyy"))

(defn new-date
  [year month day]
  (.parse formatter (str month "/" day "/" year)))

(defn str->date
  [s]
  (.parse formatter s))
