(ns ci.views.welcome
  (:require [noir.content.pages :as pages])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
	net.cgrand.enlive-html
        netcdf.geo-grid
        clojure.pprint))

(deftemplate welcome "ci/views/welcome.html"  
  [blurb map]

  [:#blurb]
  (content blurb)
  
  [:#map]
  (content map))

(defpage "/welcome" []
  (welcome "Welcome to hello-noir"
           (with-open-geo-grid [grid "http://badc.nerc.ac.uk/help/formats/netcdf/simple.nc" "temp"]
             (apply str (interpose \, (read-seq grid))))))