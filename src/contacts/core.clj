(ns contacts.core
  (:require [contacts.core
             [memory-store :as store]
             [reader :as reader]]))

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

(defn import-contact
  "Imports a single contact from a single delimited string"
  [s]
  (when-let [delimiter (reader/determine-delimiter s)]
    (reader/read-contact-line s delimiter reader/default-field-order)))

(defn import-contacts
  [system input-file {:keys [sort]
                      :or {sort :email}}]
  {:pre [(some #{sort} [:email :birth-date :last-name])]}
  (->> (slurp input-file)
       reader/read-contact-file-contents
       (sort-contacts sort)))

(defn validate-contact
  "Determines whether a contact is valid. Returns a map with :valid? true | false
   and optionally :error"
  [contact system]
  (let [error (cond
                (not (contains? contact :last-name)) "Email is Required"
                (not (contains? contact :first-name)) "First Name is Required"
                (not (contains? contact :last-name)) "Last Name is Required"
                (not (contains? contact :birth-date)) "Birth Date is Required"
                :else nil)]
    {:valid? (nil? error)
     :error error}))

(defn store-contact!
  "Takes a contact and adds it to storage"
  [contact {:keys [store] :as system}]
  (let [{:keys [valid? error]} (validate-contact contact system)]
    (when valid? (store/add-contact! contact store))
    {:success? valid?
     :error error}))

(defn find-contacts
  [store {:keys [sort]}]
  (let [records (store/all-contacts store)]
    (if sort
      (sort-contacts sort records)
      records)))

