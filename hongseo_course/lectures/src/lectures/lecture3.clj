;; importing anglican:
(ns anglican.language
  (:use [anglican core emit runtime])
  (:require [gorilla-plot.core :as plot]))


(defquery pzl [fruit]
  (let [ bin (sample (categorical 
                       {:blue (/ 5 6) :pink (/ 1 6)}))]
    (if (= bin :blue)
      (observe (categorical 
                 { :orange (/ 1 4) :apple (/ 3 4)}) fruit)
      (observe (categorical 
                 { :orange (/ 6 8) :apple (/ 2 8)}) fruit)
      )
    bin))


(defn bp_zo [bin]
  (if (= bin :blue) 1 0))

(bp_zo :blue)
(bp_zo :pink)

(def pzl_samples 
  (let [ s (doquery :importance pzl [:orange])
         s1 (take 1000 s)
         r (map :result s1)
         zo (map bp_zo r)]
    zo))

(/ (reduce + pzl_samples) 1000)
