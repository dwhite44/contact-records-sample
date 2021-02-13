(ns contacts.core)

(defn import
  [input-file {:keys [sort]
               :or {sort :email}}]
  {:pre [(some #{sort} [:email :birth-date :last-name])]}
  (println "Going to read" input-file "and sort by" (name sort)))
