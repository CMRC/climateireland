(ns ci.server
  (:require [noir.server :as server])
  (:use remvee.ring.middleware.basic-authentication))

(defn authenticated? [name pass] (and (= name "foo") (= pass "bar")))

(server/load-views "src/ci/views/")
;;(server/add-middleware (wrap-basic-authentication authenticated?))

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'ci})))

