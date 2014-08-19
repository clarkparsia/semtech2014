(ns oslc-quality.views.layout
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.core :refer [html h]]
            [ring.util.codec :refer [base64-encode]]))


(defn new-item []
  (html
  [:form.form-horizontal
   {:method "POST" :action "/items"}
   [:div.form-group
    [:label.control-label.col-sm-2 {:for :title-input}
     "Title"]
    [:div.col-sm-10
     [:input#title-input.form-control
      {:name :title
       :placeholder "Title"}]]]
   [:div.form-group
    [:label.control-label.col-sm-2 {:for :title-input}
     "Plan ID"]
    [:div.col-sm-10
     [:input#planid-input.form-control
      {:name :planid
       :placeholder "Plan ID"}]]]
    [:div.form-group
     [:div.col-sm-offset-2.col-sm-10
      [:input.btn.btn-primary
       {:type :submit
        :value "New Plan"}]]]]))

(defn delete-item-form [id]
  (html
   [:form
    {:method "POST" :action "/items-delete"}
    [:input {:type :hidden
              :name :planid
              :value id }]
    [:div.btn-group
     [:input.btn.btn-danger.btn-xs
      {:type :submit
       :value "Delete"}]]]))

(defn items-page [items]
  (html5 {:lang :en}
         [:head
          [:title "Test Plans"]
          [:meta {:name :viewport
                  :content "width=device-width, initial-scale=1.0"}]
          [:link {:href "/bootstrap/css/bootstrap.min.css"
                  :rel :stylesheet}]]
          [:body
           [:div.container
            [:h1 "Test Plans"]
            [:div.row
             (if (seq items)
               [:table.table.table-striped
                [:thead
                 [:tr
                  [:th.col-sm-2]
                  [:th "Name"]
                  [:th "ID"]]]
                [:tbody
                 (for [i items]
                   [:tr
                    [:td (delete-item-form (:planid i))]
                    [:td (h (:title i))]
                    [:td (h (:planid i))]])]])]
            [:div.col-sm-6
             [:h2 "Create a new test plan"]
             (new-item)]]
           [:script {:src "/jquery-1.10.2.js"}]
           [:script {:src "/bootstrap/js/bootstrap.min.js"}]]))
