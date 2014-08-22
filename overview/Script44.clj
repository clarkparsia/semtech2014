#_(defdeps
    [[org.clojure/clojure "1.6.0"]
     [stardog-clj "0.6"]])

;; uses lein oneoff, e.g. lein oneoff Script21.clj

(ns example
 (:require
            [stardog.core :refer :all]))


(def db-spec (create-db-spec "oslc" "http://localhost:5820/" "admin" "admin" "none"))


(with-open [c (connect db-spec)]
  (doall (map println (list-namespaces c))))

(println "Adding OSLC-RM Namespace")
(with-open [c (connect db-spec)]
   (add-ns! c "oslc_rm" "http://open-services.net/ns/rm#"))


(def sparql-query  "
    SELECT ?reqUri {?reqUri rdf:type oslc_rm:Requirement}
")

(with-open [c (connect db-spec)]
  (doall (map println (query c sparql-query))))

(println "Removing OSLC-RM Namespace")
(with-open [c (connect db-spec)]
   (remove-ns! c "oslc_rm"))
