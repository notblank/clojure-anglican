;; Question 1
(ns anglican.language
  (:use [anglican core emit runtime])
  (:require [gorilla-plot.core :as plot]))

(defn get-digits [n]
  (map (fn [c] (- (int c) (int \0))) (str n))) 

(defn get-first [n]
  (first (get-digits  n)))

(get-digits 59)
(get-first 59)

;; distribution of digits in Fib seq:

;; using external functions inside defquery:

(defquery fib_digits []
  (let [n (sample (poisson 10))]
    (loop [i 2 
           r0 1 
           r1 1]
      (if (= i n)
        r1
        (recur (+' i 1) ;; set i <- i + 1
               r1 ;; r0 <- r1
               (+' r0 r1) ;; r1 <- r0 + r1
               )))
    ))

;; to get :key use (map :key samples)

(def samples_fib_first
  (let [s (doquery :importance fib_digits [])
        fib_nums (map :result s)
        fib_dig (map get-first fib_nums)]
    (take 1000 fib_dig)))

(count samples_fib_first)
(print samples_fib_first)

(plot/histogram samples_fib_first)

;; Question 2

;; todo: model

(defquery island_pzl [s2]
  (let [ s1 (sample (categorical 
                       {:lie (/ 2 3) :truth (/ 1 3)}))]
    (if (= s1 :lie)
      (observe (categorical 
                 { :lie (/ 1 3) :truth (/ 2 3)}) s2)
      (observe (categorical 
                 { :lie (/ 2 3) :truth (/ 1 3)}) s2)
      )
    s1))


(defn tl_zo [s]
  (if (= s :truth) 1 0))

(tl_zo :lie)
(tl_zo :truth)

(def island_pzl_samples 
  (let [ s (take 1000 (doquery :lmh island_pzl [:truth]))
         r (map :result s)
         zo (map tl_zo r)]
    zo))

(/ (reduce + island_pzl_samples) 1000)


