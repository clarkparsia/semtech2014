#_(defdeps
    [[org.clojure/clojure "1.6.0"]
     [clj-sparql "0.2.0"]
     [matsu "0.1.2"]])

;; uses lein oneoff, e.g. lein oneoff Script21.clj

(ns example
 (:require
            [clj-sparql.core :as sparql]
            [boutros.matsu.sparql :refer [query-with-prefixes query select where optional a]]
            [boutros.matsu.core :refer [register-namespaces]]))

(def config {:endpoint "http://localhost:5820/oslc/query" :user "admin" :pass "admin"})

(def ns-list         {:oslc    "<http://open-services.net/ns/core#>"
                      :rdfs    "<http://www.w3.org/2000/01/rdf-schema#>"
                      :rdf     "<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                      :dcterms "<http://purl.org/dc/terms/>"
                      :oslcqm  "<http://open-services.net/ns/qm#>"
                      :oslccm  "<http://open-services.net/ns/cm#>"
                      :oslcrm  "<http://open-services.net/ns/rm#>"
                      :xsd     "<http://www.w3.org/2001/XMLSchema#>"
                      :reqqm   "<http://jazz.net/xmlns/alm/qm/v0.1/>"})


(def sparql-query
  (query-with-prefixes ns-list
     (select :cr :status :title)
     (where :cr a [:oslccm :ChangeRequest] \.
            :cr [:dcterms :title] :title \.
            (optional :cr [:oslccm :status] :status))))

(println sparql-query)

(doall
 (map
  println
  (sparql/query config sparql-query)))
