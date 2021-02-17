(ns contacts.core.reader
  (:require [clojure.string :as str]
            [contacts.dates :as dt]))

(def default-field-order [:last-name :first-name :email :favorite-color :birth-date])

(defn determine-delimiter
  "Takes a string and determines the character it is delimited by. Returns
   the character (Pipe, comma, or space"
  [header-line]
  (cond
    (empty? header-line) nil
    (str/index-of header-line "|") "\\|"
    (str/index-of header-line ",") ","
    :else " "))

(defn determine-header-order
  "Takes a string and calculates the field order from the field names.
   Hardcoded for now as input files have fixed order. Could change to
   allow input files with columns in any order"
  [header-line delimiter]
  default-field-order)

(defn- format-value
  [k v]
  (let [val (str/trim v)]
    (if (= k :birth-date)
      (dt/str->date val)
      val)))

(defn read-contact-line
  "Takes a line of input, the delimiter, and field order and creats a map"
  [s delimiter field-order]
  (let [fields (str/split s (re-pattern delimiter))]
    (reduce (fn [m n]
              (let [k (nth field-order n)
                    v (nth fields n)]
                (assoc m k (format-value k v))))
            {}
            (range (count fields)))))

(defn read-contact-file-contents
  "Takes string contents of a file and returns a collection of contacts"
  [contents]
  {:pre [(string? contents)]}
  (when (not-empty contents)
    (let [rows (str/split-lines contents)
          header (first rows)
          delimiter (determine-delimiter header)
          field-order (determine-header-order header delimiter)]
      (map #(read-contact-line % delimiter field-order) (rest rows)))))
