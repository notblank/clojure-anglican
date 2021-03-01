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
(defn lie? [s sI]
  (if (= sI true) s (not s)))

(lie? true true)
(map lie? [true false true] [false true true])


;; todo: model
(with-primitive-procedures [lie?]
  (defquery island_pzl [statement]
    (let [s1 (sample (flip (/ 1 3)))
          sI (sample (flip (/ 1 3)))
          s2 (lie? s1 sample (flip (/ 1 3)))]
      s1)))

(doquery :importance island_pzl [true]) 

(def n_samples 1000)

(def samples_island 
  (let [ s (doquery :importance island_pzl [true])
         r (map :result s)
         zo (map {false 0 true 1} r)]
    (take n_samples zo)))

(/ (reduce + samples_island) n_samples)





