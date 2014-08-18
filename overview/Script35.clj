#_(defdeps
    [[org.clojure/clojure "1.6.0"]
     [stardog-clj "0.5"]
     [com.complexible.stardog/common-rdf "2.2.1"]
     [com.complexible.stardog/openrdf-utils "2.2.1"]
     [com.complexible.stardog/shared "2.2.1"]
     [org.openrdf.sesame/sesame "2.7.12"]
     [com.complexible.stardog/api "2.2.1"]
     [com.complexible.stardog.protocols.snarl/client "2.2.1"]
     [com.complexible.stardog.protocols.http/client "2.2.1"]
     [com.complexible.stardog.reasoning.snarl/client "2.2.1"]
     [com.complexible.stardog.reasoning.http/client "2.2.1"]
     [com.complexible.stardog.search.snarl/client "2.2.1"]
     [com.complexible.stardog.search.http/client "2.2.1"]
     [com.complexible.stardog.icv.snarl/client "2.2.1"]
     [com.complexible.stardog.icv.http/client "2.2.1"]])

;; uses lein oneoff, e.g. lein oneoff Script21.clj

(ns example
 (:require
            [stardog.core :refer :all]
            [stardog.values :refer :all]))


(def db-spec (create-db-spec "oslc" "http://localhost:5820/" "admin" "admin" "none"))


(defn create-req-data
  "Creates a vector of triple data given a start and end range"
  [start end]
  (let [the-range (range start end)]
  (reduce (fn [out-vector item]
            (conj out-vector
              [(str "http://doors.example.com/reqs/" item) "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" (as-uri "http://open-services.net/ns/rm#Requirement")]
              [(str "http://doors.example.com/reqs/" item) "http://purl.org/dc/terms/title" (str "Requirement " item)]))
          [] the-range)))

(def req-data (create-req-data 100 105))

(with-open [c (connect db-spec)]
  (with-transaction [c]
    (doseq [req-item req-data]
      (remove! c req-item))))

(println "Done")








