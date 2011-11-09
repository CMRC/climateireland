(ns ci.views.netcdfmap
  (:use netcdf.geo-grid
	net.cgrand.enlive-html
        clojure.pprint))
  
(defn cross [& seqs]
  (when seqs
    (if-let [s (first seqs)]
      (if-let [ss (next seqs)]
        (for [x  s
              ys (apply cross ss)]
          (cons x ys))
        (map list s)))))

(def netcdfmap
  (with-open-geo-grid [grid "/home/anthony/CLM4-Barry/CLM4_Data/CLM4_A1B_1/CLM4_A1B_1_mean.nc" "PS"]
    (apply str "http://maps.googleapis.com/maps/api/staticmap?sensor=true&size=400x400"
           (map #(str "&markers=color:blue|label:" (% :value)
                      "|" (.getLatitude (% :location))
                      "," (.getLongitude (% :location)))
                (read-seq grid)))))
  
(def netcdf-kml
  (with-open-geo-grid [grid "/home/anthony/CLM4-Barry/CLM4_Data/CLM4_A1B_1/CLM4_A1B_1_mean.nc" "PS"]
    (doall (map (fn [[lat long]]
                  (assoc {}
                    :value (str (/ (reduce #(+ %1 %2)
                                           0
                                           (map #(% :value)
                                                (filter #(and (> (.getLatitude (% :location)) lat)
                                                              (< (.getLatitude (% :location)) (+ 1 lat))
                                                              (> (.getLongitude (% :location)) long)
                                                              (< (.getLongitude (% :location)) (+ 1 long)))
                                                        (filter-records (read-seq grid)))))
                                   (count (filter #(and (> (.getLatitude (% :location)) lat)
                                                              (< (.getLatitude (% :location)) (+ 1 lat))
                                                              (> (.getLongitude (% :location)) long)
                                                              (< (.getLongitude (% :location)) (+ 1 long)))
                                                  (filter-records (read-seq grid))))))
                    :coordinates (str (+ 0.5 long) "," (+ 0.5 lat))
                    :lat lat
                    :long long))
                (cross (range 51 56) (range -10 -5))))))

(def netcdf-gmaps
  (let [mean (/ (reduce #(+ %1 %2) 0 (map #(Float/parseFloat (% :value)) netcdf-kml))
                (count netcdf-kml))]
    (doall (map #(str "&path=color:0x00000000|weight:5|fillcolor:"
                      (if (< (Float/parseFloat (% :value)) mean) "0xFFFF0033" "red")
                      "|" (% :lat) "," (% :long)
                      "|" (+ 1 (% :lat)) "," (% :long)
                      "|" (+ 1 (% :lat)) "," (+ 1 (% :long))
                      "|" (% :lat) "," (+ 1 (% :long)))
                netcdf-kml))))
