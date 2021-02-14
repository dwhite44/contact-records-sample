(ns contacts.dates
  (:import [java.text SimpleDateFormat]))

(def formatter (SimpleDateFormat. "M/d/yyyy"))

(defn new-date
  [year month day]
  (.parse formatter (str month "/" day "/" year)))

(defn str->date
  [s]
  (.parse formatter s))

(defn date->str
  [date]
  (if (instance? java.util.Date date)
    (.format formatter date)
    ""))
