;; clojure tutorial:
;; https://www.youtube.com/watch?v=zFPiPBIkAcQ

(type true)

(def a 19)

;; recur:

(defn more_less [a b]
  (print a b)
  (more_less (inc a) (dec b)))

(more_less 5 2) ;; stackoverfow

;; could change last line to (recur () ())
;; since inf-recursion, can use recur inside an if:


(defn more_less_r [a b]
  (print a b)
  (if (< a 9)
    (recur (inc a) (dec b))
    ))

(more_less_r 5 2) ;; stackoverfow

;; some examples:

(defn factorial [n]
  (if (= n 0) 1
    (* n (factorial (dec n)))
    ))

;; big-int use *'

(defn factorial-bi [n]
  (if (= n 0) 1
    (*' n (factorial (dec n)))
    ))

;; does NOT work - recur is not in tail possition
;; should read recur (...)

(defn factorial-bi-recur [n]
  (if (= n 0) 1
    (*' n (recur (dec n)))
    )
  )

;; use loop to have recur in tail pos:
(defn factorial-bi-loop [n]
  (if (= n 0) 1
    (loop [val n i n] ;; bind n to val and n to i
      (if (<= i 1) val
        ;;next iter val = val * i-1 and i = i-1
        (recur (*' val (dec i)) (dec i))
        )
      )
    )
  )



;; cond test1 -> exp1 test2 -> exp2 ... true expn

;; sequences:
;; returns list of elemets:

(seq [1 2 3 4])

(seq {"hola" 1, "chau" 2})

;; iteration over collections:

(loop [x [1 2 3 4]]
  (print (first x))
  (print (rest x))
  )

;; lazy seqs:

(range)
(range 3)

;; repeat 5 inf times:
(repeat 5)

;; 4 rand numbers:
(repeatedly 4 rand)

(cycle [1 2 3])

(iterate dec 2)

;; general lazy-seq
(defn zeroes []
  (lazy-seq
            (cons 0 (zeroes)) ;; 0 -> zeroes
            )
  )

(first (zeroes))
(rest (zeroes))

;; more seq functions:

(next [2 3 4])
(nth '(4 3 2 1) 2)

(apply + [1 2 3])

(map - [1 2 2] [1 2 3])
(map * [1 1] [1 1] [1 2 3])

(reduce + [1 2 3 4])

(reductions + [1 2 3 4])

(filter even? (range 100))
(remove even? (range 100))

(take 3 (range 100))
(take-last 3 (range 100))
(take-nth 3 (range 100))

(interleave [1 2] [0 1])
(interpose 10 [0 1 2])

(flatten [1 2 [3 4] 5 [6 [7 8]]])

(sort [3 1 5 9 8])
(sort > [3 1 5 9 8])


;; namspaces:

;; destructuring:

(def s [1 2 3])
(let [[x y] s]
  (+ x y))

(def m {:a 1 :b 2 :c 3})

(let [{x :a} m]
  (+ x))

;; same names as keys:
(let [{:keys [a b]} m]
  (+ a b))






