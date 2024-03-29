(ns tutorial.template1
  (:require [net.cgrand.enlive-html :as html])
  (:use [net.cgrand.moustache :only [app]]
        [ring.util.response :only [response file-response]]
        [tutorial.utils]))

(html/deftemplate index "tutorial/CI_Initial_2.html"
  [ctxt]
  [:#blurb] (maybe-content (:blurb ctxt))
  [:#map] (maybe-content (:map ctxt)))

;; ========================================
;; The App
;; ========================================

(def routes
  (app
   [""]       (fn [req] (render-to-response
                         (index {})))
   ["change"] (fn [req] (render-to-response
                         (index {:blurb "We changed the message!"
                                 :map (str (html/emit* (html/content "<img src='http://cmrcprojects.ucc.ie/coralfish/r' />")))})))
   [&]        {:status 404
               :body "Page Not Found"}
   ["css"]    (file-response "tutorial/css")))
 
(defonce *server* (run-server routes))