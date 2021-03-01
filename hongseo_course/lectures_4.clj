
(ns anglican.language
  (:use [anglican core emit runtime])
  (:require [gorilla-plot.core :as plot]))

;; balls and bumpers exercise:

(def bumpers (list (list -3 6) (list 2 5))) 
(def start-w (create-world bumpers))
(def end-w (simulate-world start-w))
(def num-balls (balls-in-box end-w))

;; Importing functions:
(with-primitive-procedures
  [create-world
   simulate-world
   balls-inbox]
  (defquery physics [] ))
