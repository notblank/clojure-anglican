(ns anglican.language
  (:use [anglican core emit runtime])
  (:require [gorilla-plot.core :as plot]))

;; coin model:

;; defquery -> doquery

(defquery coin-model [coin-series]
  (let [prob (sample (beta 1 1))] ;; 1. prior
    (loop [[f & r :as s] coin-series]
      (when (seq s)
        (observe (flip prob) f) ;; 2. incorporate data
        (recur r)))
    prob))

((doquery :smc coin-model [[true]] :number-of-particles 10000)
   (take 10)
   (map :result))



;; soft constrainsts:

(defquery sum-two
  (let [a (- (poisson 100) 100)
        b (- (poisson 100) 100)]
    (observe (normal (+ a b) 0.00001) 7)
    (list a b)))

(doquery :smc sum-two :number-of-particles 10000)

;; sample 

(defquery example
  (let [bet (sample (beta 5 3))]
    (observe (flip bet) true)
    (> bet 0.7)))


;; Bayesian linear regression:
(defquery linear-regression []
  (let [s (sample (normal 0.0 2.0))
        b (sample (normal 0.0 6.0))]
    [s b]))

(def samples 
  (map :result
  	(take 1000 (take-nth 10 (drop 10 (doquery :lmh linear-regression []))))))

(count samples)


;; soft constrainsts:

(defquery sum-two []
  (let [a (- (poisson 100) 100)
        b (- (poisson 100) 100)]
    (observe (normal (+ a b) 0.00001) 7)
    (list a b) 
    )
  )

(def samples 
  (map :result
  	(take 10 (take-nth 10 (drop 10 (doquery :lmh sum-two []))))))

(print samples)
