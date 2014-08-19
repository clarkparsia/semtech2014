(ns oslc-quality.models.qa
  (:require [stardog.core :refer :all]
            [stardog.values :refer :all]))


(defn create-plan-uri [id]
  (str "http://semanticweb.com/qa/plan/" id))

(defn create-plan-data [id title]
  (vector [(create-plan-uri id)
          "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
          (as-uri "http://open-services.net/ns/qm#TestPlan")]
          [(create-plan-uri id)
           "http://purl.org/dc/terms/title"
           title]))

(defn create-plan [db id title]
  (with-open [c (connect db)]
    (let [data (create-plan-data id title)
          _ (println (pr-str data))]
    (with-transaction [c]
      (doseq [item data]
        (insert! c item))))))

(defn read-plans [db]
  (with-open [c (connect db)]
    (doall (query c "select ?planid ?title where {
           ?planid a oslc_qm:TestPlan .
           ?planid dcterms:title ?title }"))))

(defn remove-plan-query [id]
  (str
   "DELETE WHERE { <"id"> ?p ?o } ;
    DELETE WHERE { ?s ?q <" id "> }"))

(defn remove-plan [db id]
  (with-open [c (connect db)]
    (update c (remove-plan-query id)
            {:parameters {"?s" id}})))
