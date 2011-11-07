(ns ci.views.netcdfmap
  (:use netcdf.geo-grid
	net.cgrand.enlive-html
        clojure.pprint))
  
(def netcdfmap
  (with-open-geo-grid [grid "/home/anthony/CLM4-Barry/CLM4_Data/CLM4_A1B_1/CLM4_A1B_1_mean.nc" "PS"]
    (apply str "http://maps.googleapis.com/maps/api/staticmap?sensor=true&size=400x400"
           (map #(str "&markers=color:blue|label:" (% :value)
                      "|" (.getLatitude (% :location))
                      "," (.getLongitude (% :location)))
                (read-seq grid)))))
  
(with-open-geo-grid [grid "/home/anthony/CLM4-Barry/CLM4_Data/CLM4_A1B_1/CLM4_A1B_1_mean.nc" "PS"]
  (doall (map (fn [long]
                (/ (reduce #(+ %1 %2)
                           0
                           (map #(% :value) (filter #(> (.getLatitude (% :location)) long)
                                                    (filter-records (read-seq grid)))))
                   (count (filter #(> (.getLatitude (% :location)) long)
                                  (filter-records (read-seq grid))))))
              (range 50 57))))

(def netcdf-kml
  (with-open-geo-grid [grid "/home/anthony/CLM4-Barry/CLM4_Data/CLM4_A1B_1/CLM4_A1B_1_mean.nc" "PS"]
    (doall (map (fn [long]
                  (assoc {}
                    :value (/ (reduce #(+ %1 %2)
                                      0
                                      (map #(% :value) (filter #(> (.getLatitude (% :location)) long)
                                                               (filter-records (read-seq grid)))))
                              (count (filter #(> (.getLatitude (% :location)) long)
                                             (filter-records (read-seq grid)))))
                    :long long))
                (range 50 57)))))

(with-open-geo-grid [grid "/home/anthony/CLM4-Barry/CLM4_Data/CLM4_A1B_1/CLM4_A1B_1_mean.nc" "PS"]
  (doall (map (fn [long]
                (assoc {}
                  :value (/ (reduce #(+ %1 %2)
                                    0
                                    (map #(% :value) (filter #(> (.getLatitude (% :location)) long)
                                                             (filter-records (read-seq grid)))))
                            (count (filter #(> (.getLatitude (% :location)) long)
                                           (filter-records (read-seq grid)))))
                  :long long))
              (range 50 57))))

