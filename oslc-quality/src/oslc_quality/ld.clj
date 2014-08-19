(ns oslc-quality.ld
  (:require [clj-sparql.core :refer :all])
  (:use oslc-quality.handler
        ring.server.standalone
        [ring.middleware file-info file]))

(def config {:endpoint "http://localhost:5820/testdb/query" :user "admin" :pass "admin"})

(def a (query config
 "select ?s ?p ?o WHERE { ?s ?p ?o } LIMIT 10"))

(:p (first a))

