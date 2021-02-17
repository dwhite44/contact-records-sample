(ns contacts.http.test-util
  (:require [clojure.data.json :as json]))

(defn json-body-from-response
  "Takes response, gets the body of request and converts it to clojure object"
  [response]
  (->> response
       :body
       slurp
       json/read-str))
