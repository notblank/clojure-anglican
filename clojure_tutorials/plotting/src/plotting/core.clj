;; https://functional.christmas/2019/17
;; read plot data
(ns plotting.core
   (:require [quil.core :as q]
             [gorilla-plot.core :as plot]
             [oz.core :as oz]
             [clojure.data.json :as json]))

;; reading json
(defn keep-interesting [b]
  ;; select keys from bike-data.json:
  (select-keys b
               [:start_station_latitude
                :start_station_longitude
                :end_station_latitude
                :end_station_longitude
                :duration]))


(def bike-data
  (let [parsed (-> "bike-data.json"
                   slurp
                   (json/read-str :key-fn keyword))]
    (map keep-interesting parsed)))

(println (first bike-data))

;; quil:
(defn setup []
  (q/color-mode :hsb 100 100 100)
  (q/background 0.)
  (q/stroke 100. 10)
  (q/stroke-weight 1))

(defn draw []
  (q/ellipse 500 500 300 300)
  (q/no-loop))

(q/defsketch quil-drawings
  :title "Bysykler"
  :size [1000 1000]
  :setup setup
  :draw draw
  :features [:keep-on-top :no-bind-output])

;; incanter:
;; does not work with gorilla:
(use '(incanter core stats charts io))
(view (histogram (sample-normal 1000)))

;; oz: (vega-lite)
(oz/start-plot-server!) 

;;data:
(defn play-data [& names]
  (for [n names
        i (range 20)]
    {:time i :item n :quantity (+ (Math/pow (* i (count n)) 0.8) (rand-int (count n)))}))

;;plot
(def line-plot
  {:data {:values (play-data "monkey" "slipper" "broom")}
   :encoding {:x {:field "time"}
              :y {:field "quantity"}
              :color {:field "item" :type "nominal"}}
   :mark "line"})

;; Render the plot
(oz/v! line-plot)



