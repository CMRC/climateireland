(ns demo.web
  (:use ring.adapter.jetty))

(defn app [req]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello, world"})

(defn -main []
  (let [port (Integer/parseInt (System/getenv "PORT"))]
    (run-jetty app {:port port})))

(ns demo.web
  (:require [net.cgrand.enlive-html :as html]))

(html/deftemplate index "template.html"
  [ctxt]
  [:p#message] (html/content (:message ctxt)))
