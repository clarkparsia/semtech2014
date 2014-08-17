#_(defdeps
    [[org.clojure/clojure "1.6.0"]
     [clj-http "0.6.3"]
     [cheshire "5.0.1"]
     [matsu "0.1.2"]])


;; Example from: https://github.com/boutros/matsu/blob/master/doc/example.clj
;; Original copyright and license statement:
;; Copyright © 2012 Petter Goksøyr Åsen
;;
;; Distributed under the Eclipse Public License, the same as Clojure.

;; Example REPL session querying remote SPARQL endpoints (DBpedia & LinkedMDB)
;; ============================================================================
;; Uses the following dependencies in addition to matsu:
;;
;;   [clj-http "0.6.3"]
;;   [cheshire "5.0.1"]

(ns boutros.matsu.example
  (:refer-clojure :exclude [filter concat group-by max min count])
  (:require [clj-http.client :as client]
            [boutros.matsu.sparql :refer :all]
            [boutros.matsu.core :refer [register-namespaces]]
            [boutros.matsu.util :as util]
            [clojure.xml :as xml]
            [clojure.zip :as zip :refer [down right left node children]]
            [cheshire.core :refer [parse-string]]
            [clojure.walk :refer [keywordize-keys]])
  (:import java.net.URI))


;; Convenience function to parse xml strings
(defn zip-str [s]
  (zip/xml-zip (xml/parse (java.io.ByteArrayInputStream. (.getBytes s)))))


;; DBpedia sparql endpoint
(def dbpedia "http://dbpedia.org/sparql")

;; some common prefixes
(register-namespaces {:prop "<http://dbpedia.org/property/>"
                      :dbpedia "<http://dbpedia.org/resource/>"
                      :foaf    "<http://xmlns.com/foaf/0.1/>"
                      :rdfs    "<http://www.w3.org/2000/01/rdf-schema#>"})


;; Is the Amazon river longer than the Nile River?
(defquery amazon-vs-nile []
  (ask [:dbpedia :Amazon_River] [:prop :length] :amazon \.
       [:dbpedia :Nile] [:prop :length] :nile \.
       (filter :amazon > :nile)))

;; send HTTP request
(def req
  (client/get dbpedia
              {:query-params {"query" (amazon-vs-nile)}}))

(:body req)
; => "false"
(println "Is the Amazon river longer than the Nile River?")
(println (util/pprint (amazon-vs-nile)))
(println (str "\n=> "(:body req)))
(println "-----------------------------------------------\n")


;; When was Jimi Hendrix born?
(defquery hendrix []
  (select-distinct :bday)
  (where [:dbpedia :Jimi_Hendrix] [:prop :dateOfBirth] :bday))

(def req
  (client/get dbpedia
              {:query-params {"query" (hendrix)}}))

;; Dbpedia response format defaults to xml
(def birthday (-> (zip-str (:body req)) down right down down down down node))
;=> "1942-11-27"
(println "When was Jimi Hendrix born?")
(println (util/pprint (hendrix)))
(println (str "\n=>" birthday "\n\n"))
(println "-----------------------------------------------")
;; Don't like to parse XML? You can also ask for JSON if you prefer

;; Let's find Elvis' birthday this time
(defquery elvis []
  (select :bday)
  (where [:dbpedia :Elvis_Presley] [:prop :dateOfBirth] :bday))

(def req
  (client/get dbpedia
              {:query-params
              { "query" (elvis) "format" "application/sparql-results+json"}}))


(def answer (parse-string (:body req)))
(def answer-clean (->> answer keywordize-keys :results :bindings first :bday :value))
; => "1935-01-08"
(println "Let's find Elvis' birthday this time")
(println (util/pprint (elvis)))
(println (str "\n=>" answer-clean))
(println "-----------------------------------------------")


;; Let's try something a litle more involving:

;; Find movies starring actors born in Tokyo,
;; limit to 10, ordered by longest running time:
(defquery movies []
  (select-distinct :title)
  (where :p [:prop :birthPlace] [:dbpedia :Tokyo] \.
         :movie [:prop :starring] :p \;
                [:rdfs :label] :title \;
                [:prop :runtime] :runtime
         (filter (lang-matches (lang :title) "en")))
  (order-by-desc :runtime)
  (limit 10))

(def req
  (client/get dbpedia
              {:query-params
              { "query" (movies) "format" "application/sparql-results+json"}}))

(def answer (parse-string (:body req)))

;; Iterate over the bindings and get titles
(def movie-list (for [movie (->> answer keywordize-keys :results :bindings)]
  (->> movie :title :value)))


(println "Find movies starring actors born in Tokyo, limit to 10, ordered by longest running time:")
(println (util/pprint (movies)))
(println (str "\n=>" (pr-str movie-list)))
(println "-----------------------------------------------")

; => ("Kamen Rider Decade: All Riders vs. Dai-Shocker" "Pokémon: Mewtwo Returns"
;      "The Last Emperor" "Ran (film)""Keroro Gunso the Super Movie 3: Keroro vs.
;       Keroro Great Sky Duel" "The Fall of Ako Castle" "Eijanaika (film)"
;      "The Users (TV movie)" "Runin: Banished" "Swallowtail Butterfly (film)")


;; Speaking of movies, let's try the linked data version of IMDB:

;; LinkedMoveDatabase SPARQL endpoint
(def linkedmdb "http://linkedmdb.org/sparql")

;; Find actors who acted in both movies directed by Kubrick and by Spielberg
(defquery actors []
  (base (URI. "http://data.linkedmdb.org/resource/movie/"))
  (select-distinct :actorname)
  (where :dir1 [:director_name] "Steven Spielberg" \.
         :dir2 [:director_name] "Stanley Kubrick" \.
         :dir1film [:director] :dir1 \;
                   [:actor] :actor \.
         :dir2film [:director] :dir2 \;
                   [:actor] :actor \.
         :actor [:actor_name] :actorname \.))

(def req
  (client/get linkedmdb
              {:accept "application/sparql-results+json"
               :query-params { "query" (actors)}}))

(def answer (parse-string (:body req)))

(def actor-list (for [actor (->> answer keywordize-keys :results :bindings)]
  (->> actor :actorname :value)))

(println "Find actors who acted in both movies directed by Kubrick and by Spielberg")
(println (util/pprint (actors)))
(println (str "\n=>" (pr-str actor-list) "\n"))
(println "-----------------------------------------------")

; => ("Wolf Kahler" "Slim Pickens" "Tom Cruise" "Arliss Howard"
;      "Slim Pickens" "Ben Johnson" "Scatman Crothers" "Philip Stone")
