(defproject climateireland "0.1.0-SNAPSHOT"
  :description "Climate Ireland Climate Information Platform"
  :repositories {"osgeo-geotools" "http://download.osgeo.org/webdav/geotools"}
  
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [enlive "1.0.0"]
                 [noir "1.2.0"]
                 [netcdf-clj "0.0.3-SNAPSHOT"]
                 [org.geotools/gt-main "2.6.4"]
                 [org.geotools/gt-shapefile "2.6.4"]
                 [org.geotools/gt-epsg-hsql "2.6.4"]
                 [org.geotools/gt-swing "2.6.4"]]
  :dev-dependencies [[swank-clojure "1.4.0-SNAPSHOT"]]
  :main ci.server)