(ns bayesian-plots
  (:use [anglican core emit runtime])
  (:require [gorilla-plot.core :as plot]
             [oz.core :as oz]
             [clojure.set :refer [rename-keys]]))

;; oz: (vega-lite)
(oz/start-plot-server!) 

(defn split-map-by-keys [m ks]
  [(apply dissoc m ks)
   (select-keys m ks)])

;;data:
(def data [0.8 1.2 7.8 2.4 8.2 10.7 5.3 2.6 1.2 3.4 5.6])

(def data-list (map (fn [d] (assoc {} :x d)) data))

;;plot
(def ticks-plot
  {:data {:values data-list}
   :mark "tick"
   :encoding {:x {:field "x"}}
   })

(def obs-hist-plot
  {:data {:values data-list}
   :mark "bar"
   :encoding {
              :x {:bin {:binning true, :step 2.5},
                  :field "x"},
              :y {:aggregate "count"}
              }
   })

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

(def mcmc-samples 
  (map :result (take 100000 (doquery :lmh gauss-mix []))))

(def sample-num (map (fn [d] (assoc {} :t d)) (range 100000)))

(def mcmc-check-data (map merge sample-num mcmc-samples))

(first mcmc-check-data)

(def mcmc-conv-plot
  {:data {:values mcmc-check-data}
   :layer [
           {
            :mark "line"
            :encoding {
                :x {:field "t"},
                :y {:field "n1", :type "quantitative"}}
            },
           {
            :mark "line"
            :encoding {
                :x {:field "t"},
                :y {:field "n2", :type "quantitative"}}
            }
           ]
   })

(def gauss-mix-samples 
  (take-nth 100
          (take 100000 (drop 2000 (doquery :lmh gauss-mix [])))))

;; split, change keys and add a key for color: 
;; rename-keys is in clojure.set

(def means-samples-seq 
  (split-map-by-keys 
    (dissoc (first (map :result gauss-mix-samples)) :p) [:n2]))

;; divide samples in two, then join after processing:
(assoc {:a 1} :t 2)
;; (map assoc (map :result gauss-mix-samples) :n1 "n1")

(for [m (map :result gauss-mix-samples)]
  (split-map-by-keys (dissoc m :p) [:n2]))

(for [m means-samples-seq]
  (rename-keys m {:n1 :n, :n2 :n}))

;; plots:
(def viz
  [:div {:style {:display "flex" :flex-direction "row"}}
   [:vega-lite hist-plot]
   [:vega-lite mcmc-conv-plot]])

(oz/v! viz) 




