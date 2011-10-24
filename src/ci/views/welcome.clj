(ns ci.views.welcome
  (:require [noir.content.pages :as pages])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
	net.cgrand.enlive-html))

(deftemplate welcome "ci/views/welcome.html"  
  [blurb]

  [:#blurb]
  (content blurb))

(defpage "/welcome" []
         (welcome "Welcome to hello-noir"))
