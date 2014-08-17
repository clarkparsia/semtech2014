#_(defdeps
    [[org.clojure/clojure "1.6.0"]
     [clj-sparql "0.2.0"]])

;; uses lein oneoff, e.g. lein oneoff Script21.clj

(ns example
 (:require
            [clj-sparql.core :refer :all]))

(def config {:endpoint "http://localhost:5820/oslc/query" :user "admin" :pass "admin"})

(def update-config {:endpoint "http://localhost:5820/oslc/update" :user "admin" :pass "admin"})

(def prefixes "
    PREFIX oslc: <http://open-services.net/ns/core#>
    PREFIX dcterms: <http://purl.org/dc/terms/>
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    PREFIX oslc_qm: <http://open-services.net/ns/qm#>
    PREFIX oslc_cm: <http://open-services.net/ns/cm#>
    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
    PREFIX rep_qm: <http://jazz.net/xmlns/alm/qm/v0.1/>
")

(println "Plans")

(def sparql-query (str prefixes "
    SELECT ?planUri {?planUri rdf:type oslc_qm:TestPlan}
"))


(def plan-uri (:planUri (first (query config sparql-query))))

(println (str "PLAN URI: " plan-uri))

(def insert-query (str prefixes "
                       INSERT {
                         <" plan-uri "> dcterms:subject \"Automated Test\" .
                         <" plan-uri "> dcterms:subject \"Geb Test\"
                       } WHERE { }

                       "))

(update update-config insert-query)

(println (ask config (str "ASK { <"plan-uri"> ?p \"Automated Test\" }")))

