(ns anglican.language
  (:use [anglican core emit runtime])
  (:require [gorilla-plot.core :as plot]))

;; works but no plot: (open in browser)
;; (plot/histogram [1 2 2 2 3])

;;  model:
(defquery linear-regression []
  (let [s (sample (normal 0.0 2.0))
        b (sample (normal 0.0 6.0))
        f (fn [x] (+ (* s x) b))]
    (observe (normal (f 0.0) 0.5) 0.6)
    (observe (normal (f 1.0) 0.5) 0.7)
    (observe (normal (f 2.0) 0.5) 1.2)
    (observe (normal (f 3.0) 0.5) 3.2)
    (observe (normal (f 4.0) 0.5) 6.8)
    (observe (normal (f 5.0) 0.5) 8.2)
    (observe (normal (f 6.0) 0.5) 8.4)
    f))

;; samples - functions:
(doquery :lmh linear-regression [])

(def samples 
  (map :result
  	(take 1000 (take-nth 100 (drop 1000 (doquery :lmh linear-regression []))))))

(count samples)

(defn cont [x y] x)

(def state nil)

(defn apply-f 
  [f x]
  (trampoline (f cont state x)))

;; trampoline - mutual recursion:

;; use fn name before def:
(declare rec2)

(defn rec1 [n]
  (if (= n 0)
    ()
    (do
      (println (str n "th in rec1"))
      (rec2 (- n 1)))))

(defn rec2 [n]
  (if (= n 0)
    ()
    (do
      (println (str n "th in rec2"))
      (rec1 (- n 1)))))


(trampoline rec1 10)

;;

(def xs (range -1 7 0.1))

(defn draw [f]
  (let [ys (map (partial apply-f f) xs)
        xys (map (fn [x y] [x y]) xs ys)]
    (plot/list-plot xys :joined true :opacity 0.02 :plot-range [[0 6] [0 10]])))



(def xss [0 1 2 3 4 5 6])
(def drawpoints 
  (let [ys [0.6 0.7 1.2 3.2 6.8 8.2 8.4]
        xys (map (fn [x y] [x y]) xss ys)]
    (plot/list-plot xys :joined false :color "black" :plot-range [[0 6] [0 1]])))

(reduce plot/compose (concat (map draw samples) [drawpoints]))

















