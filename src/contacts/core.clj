(ns contacts.core
  (:require [contacts.reader :as reader]))

(defn sort-contacts
  "Sorts contacts by the given sort key:
   :email: sorts by email descending, last name ascending
   :birth-date: sorts by birth date ascending
   :last-name: sorts by last name descending"
  [sort-key coll]
  (case sort-key
    :email (sort #(let [email-compare (compare (:email %2) (:email %1))]
                    (if (= 0 email-compare)
                      (compare (:last-name %1) (:last-name %2))
                      email-compare))
                 coll)
    :birth-date (sort-by :birth-date coll)
    :last-name (sort-by :last-name #(compare %2 %1) coll)
    :else (throw (Exception. (str "Invalid sort key " sort-key)))))

(defn import-contacts
  [system input-file {:keys [sort]
                      :or {sort :email}}]
  {:pre [(some #{sort} [:email :birth-date :last-name])]}
  (->> (slurp input-file)
       reader/read-contact-file-contents
       (sort-contacts sort)))
