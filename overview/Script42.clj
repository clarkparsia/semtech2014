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
