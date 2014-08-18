#_(defdeps
    [[org.clojure/clojure "1.6.0"]
     [clj-sparql "0.2.0"]])

;; uses lein oneoff, e.g. lein oneoff Script21.clj

(ns example
 (:require
            [clj-sparql.core :refer :all]))

(def config {:endpoint "http://localhost:5820/oslc/query" :user "admin" :pass "admin"})

(def prefixes "
    PREFIX oslc: <http://open-services.net/ns/core#>
    PREFIX dcterms: <http://purl.org/dc/terms/>
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    PREFIX oslc_qm: <http://open-services.net/ns/qm#>
    PREFIX oslc_cm: <http://open-services.net/ns/cm#>
    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
    PREFIX rep_qm: <http://jazz.net/xmlns/alm/qm/v0.1/>
    PREFIX hitss: <http://hitss.nasa.gov/>
")



(def testcase-query (str prefixes "
    SELECT ?caseUri ?caseName {
        ?planUri rdf:type oslc_qm:TestPlan .
        ?planUri dcterms:title \"Geb Automated Tests\"^^xsd:string .
        ?planUri oslc_qm:usesTestCase ?caseUri .
        ?caseUri dcterms:title ?caseName
    }
"))


(println "Test Cases:")

(def testcases (query config testcase-query))

(doall (map (fn [results]
                (let [caseuri (:caseUri results)
                      casename (:caseName results)
                      result-query (str prefixes
                                         "SELECT ?resultUri ?resName ?browser ?resultStatus ?terUri ?terName ?html ?png  {
                                            ?resultUri a oslc_qm:TestResult .
                                            ?resultUri oslc_qm:reportsOnTestCase <" caseuri "> ."
                                           "?resultUri oslc_qm:status ?resultStatus .
                                            ?resultUri dcterms:title ?resName .
                                            ?resultUri oslc_qm:producedByTestExecutionRecord ?terUri .
                                            ?terUri oslc_qm:runsOnTestEnvironment ?env .
                                            ?terUri dcterms:title ?terName .
                                            ?env rep_qm:name ?browser .
                                            OPTIONAL {?resultUri hitss:producesHtmlSnapshot ?html} .
                                            OPTIONAL {?resultUri hitss:producesPngSnapshot ?png} .

                                        }
                                        ORDER BY ?resName")
                      results2 (query config result-query)]
                    (println (str "Test Case: " casename))
                    (doall (map (fn [input]
                     (let [{:keys [resName browser resultStatus]} input]
                       (println
                        (str resultStatus ": " resName  " on browser: " browser )))) results2)))) testcases))

