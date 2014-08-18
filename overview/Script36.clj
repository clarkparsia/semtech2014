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
            [stardog.core :refer :all]))


(def db-spec (create-db-spec "oslc" "http://localhost:5820/" "admin" "admin" "none"))

(def ds (make-datasource db-spec))


(with-open [c (connect db-spec)]
  (let [r (query c "select ?s ?p ?o WHERE { ?s ?p ?o } LIMIT 5"
                 {:key-converter #(.toString %)})]
    (println (pr-str (keys (first r))))))
