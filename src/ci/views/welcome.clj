(ns ci.views.welcome
  (:require [noir.content.pages :as pages]
            [noir.response :as response])

  (:use noir.core
        hiccup.core
        hiccup.page-helpers
	net.cgrand.enlive-html
        ci.views.netcdfmap
        remvee.ring.middleware.basic-authentication)
  (:import java.net.URLDecoder
           java.lang.Integer))

(defn authenticated? [name pass] (and (= name "foo") (= pass "bar")))

(deftemplate welcome "ci/views/welcome.html"  
  [blurb map]
  
  [:#blurb]
  (content blurb)
  
  [:#map]
  (content map))

(defpage "/welcome" []
  (welcome {:tag :img
            :attrs {:src "/svg"
                    :width "480"
                    :height 600}}
           {:tag :img
            :attrs {:src (str "http://maps.googleapis.com/maps/api/staticmap"
                              "?sensor=true&size=480x600&maptype=terrain"
                              (reduce str (netcdf-gmaps)))}})
  )

(deftemplate kml "ci/views/hello.kml"
  [lats latn longw longe var]

  [:Placemark]
  (clone-for [point (netcdf-avg lats latn longw longe var)]
             [:name]
             (content (:value point))
             [:Point :coordinates]
             (content (:coordinates point))))
  
(defpage [:get "/kml/:lats/:latn/:longw/:longe/:var"] {:keys [lats latn longw longe var]}
  (kml lats latn longw longe var))

(defpage "/svg" []
  (netcdf-svg))

(deftemplate csv-table "ci/views/table.html"  
  [lats latn longw longe var]
  [:#body]
  (clone-for [cell (netcdf-avg lats latn longw longe var)]
             [:#lat]
             (content (str (:lat cell)))
             [:#long]
             (content (str (:long cell)))
             [:#val]
             (content (:value cell))))

(defpage [:get "/csv/:lats/:latn/:longw/:longe/:var"] {:keys [lats latn longw longe var]}
  (csv-table (Integer/parseInt lats)
             (Integer/parseInt latn)
             (Integer/parseInt longw)
             (Integer/parseInt longe)
             var))