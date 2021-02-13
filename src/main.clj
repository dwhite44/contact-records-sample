(ns main
  (:require [contacts.core :as core])
  (:gen-class))

(defn -main
  [& args]
  (core/import "test-file.csv" {}))
