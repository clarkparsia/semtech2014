(ns oslc-quality.routes.home
  (:require [compojure.core :refer :all]
            [oslc-quality.models.qa :as qa]
            [oslc-quality.views.layout :refer [items-page]]
            [ring.util.codec :refer [base64-decode]]))

(defn home []
  "Hello World3!")


(defn handle-index-items [req]
  (let [db (:webdev/db req)
        items (qa/read-plans db)]
    {:status 200
     :headers {}
     :body (items-page items)}))

(defn handle-create-plan [req]
  (let [title (get-in req [:params :title])
        description (get-in req [:params :description])
        planid (get-in req [:params :planid])
        db (:webdev/db req)
        _ (qa/create-plan db planid title)]
    {:status 302
     :headers {"Location" "/items"}
     :body ""}))

(defn handle-remove-plan [req]
  (let [planid (get-in req [:params :planid])
        db (:webdev/db req)
        exists? (qa/remove-plan db planid)]
    (if exists?
      {:status 302
       :headers {"Location" "/items"}
       :body ""}
    {:status 404
     :body "Plan not found"
     :headers {}})))

(defroutes home-routes
  (GET "/items" [] handle-index-items)
  (POST "/items" [] handle-create-plan)
  (POST "/items-delete" [] handle-remove-plan)
  (GET "/" [] (home)))
