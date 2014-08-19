(ns oslc-quality.handler
  (:require [compojure.core :refer [defroutes routes ANY GET POST PUT DELETE]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.reload :refer [wrap-reload]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.handler.dump :refer [handle-dump]]
            [stardog.core :refer [create-db-spec]]
            [oslc-quality.routes.home :refer [home-routes]]))

(def db-spec (create-db-spec "oslc" "http://localhost:5820/" "admin" "admin" "none"))

(defn init []
  (println "oslc-quality is starting"))

(defn destroy []
  (println "oslc-quality is shutting down"))



(defn wrap-stardog [hdlr]
  (fn [req]
    (hdlr (assoc req :webdev/db db-spec))))

(def sim-methods {"PUT" :put
                  "DELETE" :delete})

(defn wrap-simulated-methods [hdlr]
  (fn [req]
    (if-let [method (and (= :post (:request-method req))
                         (sim-methods (get-in req [:params :_method])))]
      (hdlr (assoc req :request-method method))
      (hdlr req))))


(defroutes app-routes
  (ANY "/request" [] handle-dump)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (->
    (routes home-routes app-routes)
    (handler/site)
    (wrap-stardog)
    (wrap-resource "static")
    (wrap-file-info)
    (wrap-base-url)))



(defn -main [port]
   (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
