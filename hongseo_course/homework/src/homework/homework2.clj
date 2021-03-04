
(ns anglican.language
  (:use [anglican core emit runtime])
  (:require [gorilla-plot.core :as plot]))

(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io])

;; distriburion over births:

(defquery pandas-births [first-birth]
  (let [ 
        species (sample (categorical 
                       {:A (/ 1 2) :B (/ 1 2)}))
        birth (if (= species :A)
                (sample (categorical { :one (/ 9 10) :two (/ 1 10)}))
                (sample (categorical { :one (/ 8 10) :two (/ 2 10)}))
                )
        ]
    ( if (= species :A)
      ;; P(2nd-birth | first-birth)
      (observe (categorical { :one (/ 9 10) :two (/ 1 10)}) first-birth)
      (observe (categorical { :one (/ 8 10) :two (/ 2 10)}) first-birth)
      )
    birth))

;; samples:

(defn ot-zo [birth]
  (if (= birth :one) 0 1))

(defn AB-zo [species]
  (if (= species :A) 0 1))

(ot-zo :two)
(AB-zo :A)

(def pandas-birth-samples 
  (let [ s (take 1000 (doquery :lmh pandas-births [:two]))
         r (map :result s)
         zo (map ot-zo r)]
    zo))

(reduce + pandas-birth-samples)


;; P(2nd birth , species | 1st birth):

(defquery pandas-species [first-birth]
  (let [ 
        species (sample (categorical 
                       {:A (/ 1 2) :B (/ 1 2)}))
        birth (if (= species :A)
                (sample (categorical { :one (/ 9 10) :two (/ 1 10)}))
                (sample (categorical { :one (/ 8 10) :two (/ 2 10)}))
                )
        ]
    ( if (= species :A)
      ;; P(2nd-birth | first-birth)
      (observe (categorical { :one (/ 9 10) :two (/ 1 10)}) first-birth)
      (observe (categorical { :one (/ 8 10) :two (/ 2 10)}) first-birth)
      )
    {:species species :birth birth}))

(def panda-samples 
  (map :result 
       (take 1000 (drop 1000 (doquery :lmh pandas-species [:two])))))

;; P( 2nd birth = two | 1st = two)
(reduce + (map ot-zo (map :birth panda-samples)))

;; P( A | 2nd = one, 1st = two)
(reduce + 
        (map AB-zo 
             (map :species (filter (comp #{:one} :birth) panda-samples))))




;; seven measurements model

(def m [-27.020 3.570 8.191 9.898 9.603 9.945 10.056])

(defquery seven-meas []
  (let [ mu (sample (normal 0 50))
         sd1 (sample (uniform-continuous 0 25)) 
         sd2 (sample (uniform-continuous 0 25)) 
         sd3 (sample (uniform-continuous 0 25)) 
         sd4 (sample (uniform-continuous 0 25))
         sd5 (sample (uniform-continuous 0 25))
         sd6 (sample (uniform-continuous 0 25))
         sd7 (sample (uniform-continuous 0 25))
         ]
    (observe (normal mu sd1) (nth m 0))
    (observe (normal mu sd2) (nth m 1))
    (observe (normal mu sd3) (nth m 2))
    (observe (normal mu sd4) (nth m 3))
    (observe (normal mu sd5) (nth m 4))
    (observe (normal mu sd6) (nth m 5))
    (observe (normal mu sd7) (nth m 6))
    {:mu mu 
     :sd1 sd1 :sd2 sd2 :sd3 sd3 :sd4 sd4 :sd5 sd5 :sd6 sd6 :sd7 sd7}))

(def seven-meas-samples 
  (take-nth 100
          (take 100000 (drop 1000 (doquery :lmh seven-meas [])))))

(def mu-samples 
  (map :mu
       (map :result seven-meas-samples)))

(let [ nr (count mu-samples)
      mean (/ nr (reduce + mu-samples))]
  mean)


;; QUESTION 2:
;; reading data and multiples rv.

(require '[clojure.edn :as edn])

;; message count data:
(def msg-counts-data 
  ;;(map (comp read-string first)
  (map (comp edn/read-string first)
       (with-open [reader (io/reader "./src/homework/txtdata.csv")]
         (doall (csv/read-csv (slurp reader))))))


(Math/ceil (nth msg-counts-data 1))

(print msg-counts-data)

(defquery change-habits [data]
  (let [;; days:
        tau (sample (uniform-discrete 1 76))
        ;; avg counts before/after day tau
        lam1 (sample (exponential 0.05))    
        lam2 (sample (exponential 0.05))   
         ] 
    ;; condition on all data:
    ;; first create the list, then execute:
    (doall 
      (map 
        (fn [x i] (if (> tau i) 
                    (observe (poisson lam1) x)
                    (observe (poisson lam2) x)
                    )
          )
        data (range (count data))))

    tau))

(doquery :lmh change-habits [msg-counts-data])

