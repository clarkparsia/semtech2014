#_(defdeps
    [[org.clojure/clojure "1.6.0"]
     [stardog-clj "0.6"]])

;; uses lein oneoff, e.g. lein oneoff Script21.clj

(ns example
 (:require
            [stardog.core :refer :all]))


(def db-spec (create-db-spec "oslc" "http://localhost:5820/" "admin" "admin" "none"))

(def ds (make-datasource db-spec))

(def prefixes "
    PREFIX oslc: <http://open-services.net/ns/core#>
    PREFIX dcterms: <http://purl.org/dc/terms/>
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    PREFIX oslc_qm: <http://open-services.net/ns/qm#>
    PREFIX oslc_cm: <http://open-services.net/ns/cm#>
    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
    PREFIX rep_qm: <http://jazz.net/xmlns/alm/qm/v0.1/>
")


(def sparql-query (str prefixes "
  select ?crUri ?title ?status {
        ?crUri a oslc_cm:ChangeRequest .
        OPTIONAL { ?crUri dcterms:title ?title } .
        OPTIONAL { ?crUri oslc_cm:status ?status } .
    }
  "))

(with-open [c (connect db-spec)]
  (let [r (query c sparql-query
                 {:converter #(str "Work Item: " (.toString %))})]
    (doall (map #(println (:crUri %)) r))))
