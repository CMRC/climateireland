(ns ci.views.netcdfmap
  (:use netcdf.geo-grid))
  
(def netcdfmap
  (with-open-geo-grid [grid "http://badc.nerc.ac.uk/help/formats/netcdf/simple.nc" "temp"]
    (apply str "http://maps.googleapis.com/maps/api/staticmap?sensor=true&size=400x400"
           (map #(str "&markers=color:blue|label:" (% :value)
                      "|" (.getLatitude (% :location))
                      "," (.getLongitude (% :location)))
                (read-seq grid)))))
  

