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
    SELECT ?resultUri ?resName {?resultUri oslc_qm:status ?resultStatus .
                                ?resultUri dcterms:title ?resName . }
"))


(with-connection-pool [conn ds]
  (println "SUCCESSFUL: ")
  (doall
   (map println
     (query conn sparql-query
       { :parameters {"?resultStatus" "SUCCESS"}})))
  (update conn "DELETE { ?a ?b \"SUCCESS\" } INSERT { ?a ?b \"FAIL\" } WHERE { ?a ?b \"SUCCESS\"  }"
                         {:parameters {"?b" "http://open-services.net/ns/qm#status"}})

  (println "FAILING:")
  (doall
   (map println
     (query conn sparql-query
       { :parameters {"?resultStatus" "FAIL"}})))
  (update conn "DELETE { ?a ?b \"FAIL\" } INSERT { ?a ?b \"SUCCESS\" } WHERE { ?a ?b \"FAIL\"  }"
                         {:parameters {"?b" "http://open-services.net/ns/qm#status"}})

  (println "SUCCESSFUL: ")
  (doall
   (map println
     (query conn sparql-query
       { :parameters {"?resultStatus" "SUCCESS"}}))))

