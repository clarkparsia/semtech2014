#_(defdeps
    [[org.clojure/clojure "1.6.0"]
     [stardog-clj "0.6"]])

;; uses lein oneoff, e.g. lein oneoff Script21.clj

(ns example
 (:require
            [stardog.core :refer :all]
            [stardog.values :refer :all]))


(def db-spec (create-db-spec "oslc" "http://localhost:5820/" "admin" "admin" "none"))

(def ds (make-datasource db-spec))

(defn create-foaf-records [conn]
  (let [foaf "http://xmlns.com/foaf/0.1/"
        rtype "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"]
    (insert! conn ["http://example.org/Bob" rtype (as-uri (str foaf "Person"))])
    (insert! conn ["http://example.org/Bob" (str foaf "firstName") "Bob"])
    (insert! conn ["http://example.org/Bob" (str foaf "surname") "Smith"])))


(transact ds create-foaf-records)

(with-connection-pool [c ds]
  (doall (map println (query c "select ?p ?o WHERE { <http://example.org/Bob> ?p ?o }"))))
