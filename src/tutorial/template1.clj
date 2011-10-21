(ns tutorial.template1
  (:require [net.cgrand.enlive-html :as html])
  (:use [net.cgrand.moustache :only [app]]
        [tutorial.utils]]))

(html/deftemplate index "tutorial/CI_Initial_2.html"
  [ctxt]
  [:div#blurb] (maybe-content (:message ctxt) "hello"))

;; ========================================
;; The App
;; ========================================

(def routes
  (app
   [""]       (fn [req] (render-to-response
                         (index {})))
   ["change"] (fn [req] (render-to-response
                         (index {:message "We changed the message!"})))
   [&]        {:status 404
               :body "Page Not Found"}
   ["css"]    (file-response "tutorial/css")))
 
(defonce *server* (run-server routes))