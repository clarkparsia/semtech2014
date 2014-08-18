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


(def db-spec (create-db-spec "oslc" "http://localhost:5820/" "admin" "admin" "QL"))
(def no-reasoning-spec (create-db-spec "oslc" "http://localhost:5820/" "admin" "admin" "none"))

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
;(with-connection-pool [conn ds]
;  (doall (map println (query conn sparql-query ))))

(with-open [c (connect no-reasoning-spec)]
  (doall (map println (query conn sparql-query ))))



