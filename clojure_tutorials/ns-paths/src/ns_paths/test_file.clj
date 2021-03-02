;; paths and ns:
;; 
;; name-dir -> name_dir
;; name-dir.core -> name_dir/core.clj
;; 
;; runned lein from zee-files/ns-paths


;; can use aliases inside a require by appending :as new-name

(ns ns-paths.core)
(require 'ns-paths.tt)
(refer 'ns-paths.tt)
(t-f 5)

