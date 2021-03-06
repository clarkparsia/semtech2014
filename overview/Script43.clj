#_(defdeps
    [[org.clojure/clojure "1.6.0"]
     [stardog-clj "0.6"]])

;; uses lein oneoff, e.g. lein oneoff Script21.clj

(ns example
 (:require
            [stardog.core :refer :all]
            [stardog.values :refer :all]))


(def db-spec (create-db-spec "oslc" "http://localhost:5820/" "admin" "admin" "none"))

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


(def items-as-requirements
         (str prefixes
              " CONSTRUCT {
                 ?crUri a oslc_rm:Requirement .
                 ?crUri oslc_rm:verifiedBy ?testPlanUri .
               } WHERE {
                 ?crUri a oslc_cm:ChangeRequest .
                 ?cUri oslc_cm:relatedTestPlan ?testPlanUri
               }"))


(with-open [c (connect db-spec)]
   (let [g (graph c items-as-requirements)]
      (doall (map println g))))
