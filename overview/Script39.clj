#_(defdeps
    [[org.clojure/clojure "1.6.0"]
     [stardog-clj "0.6"]])

;; uses lein oneoff, e.g. lein oneoff Script21.clj

(ns example
 (:require
            [stardog.core :refer :all]
            [stardog.values :refer :all]))


(def db-spec (create-db-spec "oslc" "http://localhost:5820/" "admin" "admin" "QL"))

(def ds (make-datasource db-spec))

(def subclass ["http://open-services.net/ns/rm#Requirement" "http://www.w3.org/2000/01/rdf-schema#subClassOf" (as-uri "http://open-services.net/ns/cm#ChangeRequest")])

(with-connection-pool [conn ds]
  (with-transaction [conn]
    (insert! conn subclass)))

(def prefixes "
    PREFIX oslc: <http://open-services.net/ns/core#>
    PREFIX dcterms: <http://purl.org/dc/terms/>
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    PREFIX oslc_qm: <http://open-services.net/ns/qm#>
    PREFIX oslc_cm: <http://open-services.net/ns/cm#>
    PREFIX oslc_rm: <http://open-services.net/ns/rm#>
    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
    PREFIX rep_qm: <http://jazz.net/xmlns/alm/qm/v0.1/>
")

(def sparql-query (str prefixes "
    SELECT ?reqUri {?reqUri a oslc_cm:ChangeRequest}
"))


;; Now that requirement is a subclass of change request, we should be able to
;; retrieve all change requests and requirements
(with-open [conn (connect db-spec)]
  (doall (map println (query conn sparql-query {:reasoning "QL"}))))
