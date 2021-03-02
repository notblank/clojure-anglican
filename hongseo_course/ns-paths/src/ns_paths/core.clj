;; paths and ns:
;; 
;; name-dir -> name_dir
;; name-dir.core -> name_dir/core.clj
;; 
;; runned lein from zee-files/ns-paths


;; can use aliases inside a require by appending :as new-name

(ns ns-paths.core)
;; Ensure that the SVG code is evaluated
(require 'ns-paths.visualization.svg)
;; Refer the namespace so that you don't have to use the 
;; fully qualified name to reference svg functions
(refer 'ns-paths.visualization.svg)
;; require then refer is equivalent to use
;; (use 'ns-paths.visualization.svg)

(def heists [{:location "Cologne, Germany"
              :cheese-name "Archbishop Hildebold's Cheese Pretzel"
              :lat 50.95
              :lng 6.97}
             {:location "Zurich, Switzerland"
              :cheese-name "The Standard Emmental"
              :lat 47.37
              :lng 8.55}
             {:location "Marseille, France"
              :cheese-name "Le Fromage de Cosquer"
              :lat 43.30
              :lng 5.37}
             {:location "Zurich, Switzerland"
              :cheese-name "The Lesser Emmental"
              :lat 47.37
              :lng 8.55}
             {:location "Vatican City"
              :cheese-name "The Cheese of Turin"
              :lat 41.90
              :lng 12.45}])

(defn -main
  [& args]
  (println (points heists)))

;; use a function inside 'svg:
(alias 'svg 'ns-paths.visualization.svg)
(svg/points heists)

;; ns Marcro:
;; combines all the above. 
;; refers clojure-core by default.

;; (ns ns-paths.core
;;    (:refer-clojure :exclude [println])
;; is equivalent to:
;; (in-ns 'ns-paths.core)
;; (refer 'clojure.core :exclude ['println])
;;
;; 6 possible references within ns:
;; :require, etc.
;; to require multiple libraries do:
;; (:require [lib1 :as ...] [lib2 :as ...])
;; inisde :require you can refer to names:
;; (:require [ns-paths.visualization.svg :refer [points]])
;; can :refer to all symbols :all
;; not recommended to use :use.











