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
