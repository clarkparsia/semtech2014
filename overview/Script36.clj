#_(defdeps
    [[org.clojure/clojure "1.6.0"]
     [stardog-clj "0.6"]])

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
