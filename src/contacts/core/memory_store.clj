(ns contacts.core.memory-store)

(defn new-memory-store []
  {:contacts (atom [])})

(defn all-contacts
  [{!records :contacts :as store}]
  @!records)

(defn add-contact!
  [contact {!records :contacts :as store}]
  (swap! !records conj contact))
