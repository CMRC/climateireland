(ns ci.views.netcdfmap
  (:use [netcdf.geo-grid]
        [clojure.pprint]
        [analemma.charts :only [emit-svg xy-plot add-points]]
	[analemma.svg]
	[analemma.xml]))
  
(defn cross [& seqs]
  (when seqs
    (if-let [s (first seqs)]
      (if-let [ss (next seqs)]
        (for [x  s
              ys (apply cross ss)]
          (cons x ys))
        (map list s)))))

(defn netcdfmap []
  (with-open-geo-grid [grid "resources/CLM4_Data/CLM4_A1B_1/CLM4_A1B_1_mean.nc" "PS"]
    (apply str "http://maps.googleapis.com/maps/api/staticmap?sensor=true&size=400x400&maptype=terrain"
           (map #(str "&markers=color:blue|label:" (% :value)
                      "|" (.getLatitude (% :location))
                      "," (.getLongitude (% :location)))
                (read-seq grid)))))
  
(defn netcdf-avg
  ([]
     (netcdf-avg 51 54 -10 -5 "PS"))
  ([lats latn longw longe var]
     (with-open-geo-grid [grid "resources/CLM4_Data/CLM4_A1B_1/CLM4_A1B_1_mean.nc" "PS"]
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
                   (cross (range lats latn) (range longw longe)))))))

(netcdf-avg 50 55 -10 -6 "PS")

(defn netcdf-gmaps []
  (let [mean (/ (reduce #(+ %1 %2) 0 (map #(Float/parseFloat (% :value)) (netcdf-avg)))
                (count (netcdf-avg)))]
    (doall (map #(str "&path=color:0x00000000|weight:5|fillcolor:"
                      (if (< (Float/parseFloat (% :value)) mean) "0xFFFF0033" "red")
                      "|" (% :lat) "," (% :long)
                      "|" (+ 1 (% :lat)) "," (% :long)
                      "|" (+ 1 (% :lat)) "," (+ 1 (% :long))
                      "|" (% :lat) "," (+ 1 (% :long)))
                (netcdf-avg)))))

(defn netcdf-svg []
  (emit-svg
   (-> (xy-plot
        :xmin -10 :xmax -4
        :ymin 50 :ymax 55
        :width 500 :height 500
        :label-points? true)
       (add-points (vec (doall (map #(vector (% :long)
                                             (% :lat))
                                    (netcdf-avg))))
                   :size 1
                   :fill (rgb 255 0 0)))))

