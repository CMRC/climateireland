(ns ci.views.welcome
  (:require [noir.content.pages :as pages]
            [noir.response :as response])

  (:use noir.core
        hiccup.core
        hiccup.page-helpers
	net.cgrand.enlive-html
        ci.views.netcdfmap)
  (:import java.net.URLDecoder))

(deftemplate welcome "ci/views/welcome.html"  
  [blurb map]
  
  [:#blurb]
  (content blurb)
  
  [:#map]
  (content map))

(defpage "/welcome" []
  (welcome {:tag :img
            :attrs {:src "http://upload.wikimedia.org/wikipedia/commons/d/d7/Ireland.svg"
                    :width "480"
                    :height 600}}
           {:tag :img
            :attrs {:src (str "http://maps.googleapis.com/maps/api/staticmap?sensor=true&size=480x600"
                              (reduce str netcdf-gmaps))}}))

(deftemplate kml "ci/views/hello.kml"
  []

  [:Placemark]
  (clone-for [point netcdf-kml]
             [:name]
             (content (:value point))
             [:Point :coordinates]
             (content (:coordinates point))))
  
(defpage "/kml" []
  (kml))
