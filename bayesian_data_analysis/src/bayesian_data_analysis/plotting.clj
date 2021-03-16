(ns bayesian-plots
  (:use [anglican core emit runtime])
  (:require [gorilla-plot.core :as plot]
             [oz.core :as oz]))

;; oz: (vega-lite)
(oz/start-plot-server!) 

;;data:
(def data [0.8 1.2 7.8 2.4 8.2 10.7 5.3 2.6 1.2 3.4 5.6])

(def data-list (map (fn [d] (assoc {} :x d)) data))

;;plot
(def ticks-plot
  {:data {:values data-list}
   :mark "tick"
   :encoding {:x {:field "x"}}
   })

(def hist-plot
  {:data {:values data-list}
   :mark "bar"
   :encoding {
              :x {:bin {:binning true, :step 2.5},
                  :field "x"},
              :y {:aggregate "count"}
              }
   })

;; Render the plot
(oz/v! hist-plot) 

(defquery gauss-mix []
  (let [ p (sample :p (beta 1 1)) 
         n1 (sample :n1 (normal 0 5))
         n2 (sample :n2 (normal 0 5))
         f (fn[y] 
             (if (sample (flip p))
               (observe (normal n1 1.) y)
               (observe (normal n2 1.) y)))]
    (map f data)
    {:p p :n1 n1 :n2 n2}))

(def gauss-mix-samples 
  (take-nth 100
          (take 100000 (drop 1000 (doquery :lmh gauss-mix [])))))

(map :result gauss-mix-samples) 

