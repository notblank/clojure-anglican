;; https://www.braveclojure.com/organization/

;; print current namespace
(ns-name *ns*)


;; def stores objects

(def a-book ["A Visit from the goon squad"])
; #'sum/a-book

;; interning a var:
;; shows vars in the current (*ns*) namespace
(ns-interns *ns*)

;; get a particular var:
(get (ns-interns *ns*) 'a-book)

;; full map:
(ns-map *ns*)

;; #' to grab the var corresponding to the symbol sum/a-book
(deref #'user/a-book)
;; normally just use the symbol:
(print a-book)

;; namespaces:
;; usually use ns but for now:
; create
(create-ns 'new-books)
; go into:
(in-ns 'new-books)

; new book in new-books:
(def a-new-book ["The sisters brothers"])

(in-ns 'user)
(print a-book)
; print a symbol in the new-books ns:
(print new-books/a-new-book)


;; refer: refer to objects in new-books without the new-books/
(clojure.core/refer 'new-books)
(print a-new-book)
;; can use :only, :exclude and :rename
;; (clojure.core/refer 'new-books :only ['book1, 'book2, ...])
;; (clojure.core/refer 'new-books :rename {'book1 'new-book1})

;; alias: change the name of a ns
;; (clojure.core/alias 'books 'new-books)

