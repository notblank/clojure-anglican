(ns lecture4.core
  (:gen-class)
  (:require [org.nfrac.cljbox2d.core :refer [position]])
  (:use [anglican [state 
                  :only [get-predicts 
                         get-log-weight 
                         get-result]]]))

(require '[lecture4.bounce :refer [create-world
                                   show-world-simulation
                                   display-static-world
                                   balls-in-box]])

;; finish:

(test-fn 5)
(print circ-attr)

