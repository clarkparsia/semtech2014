(defproject oslc-quality "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [matsu "0.1.2"]
                 [knakk/query-bank "0.7"]
                 [clj-http "0.6.3"]
                 [clj-sparql "0.1.0"]
                 [cheshire "5.0.1"]
                 [stardog-clj "0.6"]
                 [ring-server "0.3.1"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler oslc-quality.handler/app
         :init oslc-quality.handler/init
         :destroy oslc-quality.handler/destroy}
  :aot :all
  :main oslc-quality.handler
  :profiles
  {:production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:main oslc-quality.handler/-dev-main
    :dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.2.1"]]}})
