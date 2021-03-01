;; importing anglican:
(ns anglican.language
  (:use [anglican core emit runtime])
  (:require [gorilla-plot.core :as plot]))


;; lecture 2
;; coin toss and samples:
(defquery fl_ex []
  (let [c (sample (flip 0.5))]
    c))

(doquery :importance fl_ex [])

(def samples_fl 
  (let [s (doquery :importance fl_ex [])]
    (map :result (take 1000 s))))

(count samples_fl) 


;; lecture 3:
;; Two bins: one red, one blue with different proportions of apples and oranges.
;; Pick a bin and take a fruit out at random.

(defquery puzl [fruit]
  (let [bin (sample 
              (categorical {:red (/ 1 6), :blue (/ 5 6)}))]
    (if (= bin :red)
      (observe (categorical {:apple (/ 2 8), :orange (/ 6 8)})
               ;; given:
               fruit) ;; write fruit to condition on the fruit argument.
      (observe (categorical {:apple (/ 3 4), :orange (/ 1 4)})
               ;; given:
               fruit)
    )
  bin))

;; to sample:
(doquery :importance puzl [:orange])

;; store 1000 first samples
;;(take 10 (range 100))

(def samples
  (take 1000
        (doquery :importance puzl [:orange]))) ;; proba of getting an orange



