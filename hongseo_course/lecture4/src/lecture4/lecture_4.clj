;; gorilla-repl.fileformat = 1

;; **
;;; # 2D physics example from lecture 4
;; **

(use 'nstools.ns)

(ns bounce-worksheet
     (:like anglican-user.worksheet)
     (:require [org.nfrac.cljbox2d.core :refer [position]])
     (:use [anglican [stat 
                      :only [get-predicts 
                             get-log-weight 
                             get-result]]]))

(require '[bounce :refer [create-world
                          show-world-simulation
                          simulate-world
                          display-static-world
                          balls-in-box]] 
         :reload)

(def bumper-location1 (list))

(show-world-simulation bumper-location1)

(def bumper-location2 (list (list -3 6) (list 7 4)))

(show-world-simulation bumper-location2)

(def bumper-location-example
  (list (list -3 6) (list 2 5) (list 7 4) (list 12 3)))

(show-world-simulation bumper-location-example)

(def example-world
  (create-world bumper-location-example))

(def example-world-final-state 
  (simulate-world example-world))

(balls-in-box example-world-final-state)

(with-primitive-procedures
  [create-world simulate-world balls-in-box]
  (defquery physics0 []
    (let [n-bumpers 8
          f (fn [] (list 
                     (sample (uniform-continuous -5 14))
                     (sample (uniform-continuous 0 10))))
          bs (repeatedly n-bumpers f)
          w0 (create-world bs)
          w1 (simulate-world w0)
          num-balls (balls-in-box w1)]
      (list num-balls bs))))

(def lazy-samples0 
  (doquery :importance physics0 []))
(def samples0 
  (map :result (take-nth 100 (take 2000 (drop 1000 lazy-samples0))))) 
(def best-sample0 
  (reduce (fn [acc x] (if (> (first x) (first acc)) x acc)) 
          samples0))
best-sample0

(show-world-simulation (first (rest best-sample0)))

(with-primitive-procedures
  [create-world simulate-world balls-in-box]
  (defquery physics1 []
    (let [n-bumpers 8
          f (fn [] (list 
                     (sample (uniform-continuous -5 14))
                     (sample (uniform-continuous 0 10))))
          bs (repeatedly n-bumpers f)
          w0 (create-world bs)
          w1 (simulate-world w0)
          num-balls (balls-in-box w1)]
      (observe (normal num-balls 1) 20)
      (list num-balls bs))))

(def lazy-samples1 
  (doquery :lmh physics1 []))
(def samples1 
  (map :result (take-nth 100 (take 2000 (drop 1000 lazy-samples1)))))
(def best-sample1 
  (reduce (fn [acc x] (if (> (first x) (first acc)) x acc))
          samples1))
best-sample1

(show-world-simulation (first (rest best-sample1)))

(with-primitive-procedures
  [create-world simulate-world balls-in-box]
  (defquery physics2 []
    (let [n-bumpers (sample (poisson 6))
          f (fn [] (list 
                     (sample (uniform-continuous -5 14))
                     (sample (uniform-continuous 0 10))))
          bs (repeatedly n-bumpers f)
          w0 (create-world bs)
          w1 (simulate-world w0)
          num-balls (balls-in-box w1)]
      (observe (normal n-bumpers 2) 0)
      (observe (normal num-balls 1) 20)
      (list num-balls bs))))


(def lazy-samples2 
  (doquery :lmh physics2 []))
(def samples2 
  (map :result (take-nth 100 (take 2000 (drop 1000 lazy-samples2)))))
(defn is-better [x y]
  (let [num-bumpers-less (< (count (second x)) (count (second y)))
        num-balls-more (> (first x) (first y))
        num-balls-equal (= (first x) (first y))
        x-above-threshold (> (first x) 15)
        y-above-threshold (> (first x) 15)]
    (or (and x-above-threshold num-bumpers-less)
        (and num-balls-equal num-bumpers-less)
        num-balls-more)))
(def best-sample2 
  (reduce (fn [acc x] (if (is-better x acc) x acc)) 
          samples2))
best-sample2

(show-world-simulation (first (rest best-sample2)))
