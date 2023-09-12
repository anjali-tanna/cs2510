;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname |Homework 11|) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
; Homework 11

; Exercise 1

; list-prefix? : [List-of Number] [List-of Number] -> Boolean
; returns #true if the first n elements of the second list are the
; elements of the first list, in the given order of the first list.
(check-expect (list-prefix? (list 1 2 3 4 5) (list 1 2 3 4 5 6 7 8 9)) #true)
(check-expect (list-prefix? (list 1 1 1) (list 1 1 1)) #true)
(check-expect (list-prefix? '() '()) #true)
(check-expect (list-prefix? '() (list 1 2 3)) #true)
(check-expect (list-prefix? (list -1 -2 -3) (list -1 -2 -3 -4 -5)) #true)
(check-expect (list-prefix? (list 1 11 111 1111 11111) (list 1 11 111 1111 11111 0 00 000)) #true)
(check-expect (list-prefix? (list 3 6 9 12) (list 2 4 6 8)) #false)
(check-expect (list-prefix? (list 2 4 6) (list 1 2 4 6 8 10)) #false)
(check-expect (list-prefix? (list 1 2 3) '()) #false)
(check-expect (list-prefix? (list -1 -2 -3) (list -1 -2)) #false)

(define (list-prefix? list1 list2)
  (cond
    [(and (empty? list1) (empty? list2)) #true]
    [(and (empty? list1) (cons? list2)) #true]
    [(and (cons? list1) (empty? list2)) #false]
    [(and (cons? list1) (cons? list2))
     (if (and (= (first list1) (first list2))
              (list-prefix? (rest list1) (rest list2))) #true #false)]))

; Exercise 2

; max-splice : [List-of Number] [List-of Number] -> [List-of Number]
; returns a list with all elements of the first list in order and elements of the
; second list order, while eliminating any overlap of the two lists.
(check-expect (max-splice '() '()) '())
(check-expect (max-splice '() (list 1 2 3)) (list 1 2 3))
(check-expect (max-splice (list 1 2 3) '()) (list 1 2 3))
(check-expect (max-splice '(1 2 3 4) '(2 3 4 5)) '(1 2 3 4 5))
(check-expect (max-splice '(1 2 3 4) '(2 2 3 4 5)) '(1 2 3 4 2 2 3 4 5))
(check-expect (max-splice (list 2 4 6 8) (list 4 6 8 10 12)) (list 2 4 6 8 10 12))
(check-expect (max-splice (list -2 -3 -4) (list -4 -5)) (list -2 -3 -4 -5))
(check-expect (max-splice (list 10 20 30 40) (list 40 40 50 60)) (list 10 20 30 40 50 60))
(check-expect (max-splice (list 3 4 5 6) (list 4 4 5 6 7)) (list 3 4 5 6 4 4 5 6 7))

(define (max-splice list1 list2)
  (cond
    [(and (empty? list1) (empty? list2)) '()]
    [(and (empty? list1) (cons? list2)) list2]
    [(and (cons? list1) (empty? list2)) list1]
    [(and (cons? list1) (cons? list2))
     (if (list-prefix? list1 list2)
         (max-splice list1 (rest list2))
         (cons (first list1)
               (max-splice (rest list1) list2)))]))

; Exercise 3

; valid-results? : [List-of Numbers] [List-of Functions] [List-of Numbers] -> Boolean
; returns #true if the output result matches the expected results when
; the [List-of Functions] is applied to the [List-of Numbers].
(check-expect (valid-results? '() '() '()) #true)
(check-expect (valid-results? (list 1 2 3) (list add1 add1 add1) (list 2 3 4)) #true)
(check-expect (valid-results? (list 5 10 15 20 25)
                              (list (lambda (x) (* x 2))
                                    (lambda (x) (+ x 10))
                                    (lambda (x) (* x 3))
                                    (lambda (x) (- x 20))
                                    (lambda (x) (+ x 5)))
                              (list 10 20 45 0 30)) #true)
(check-expect (valid-results? (list 0 0 0)
                              (list add1
                                    (lambda (x) (* x 5))
                                    number->string)
                              (list 1 2 3)) #false)
(check-expect (valid-results? (list 2 4 6 8)
                              (list (lambda (x) (* x 2))
                                    (lambda (x) (* x 2))
                                    (lambda (x) (* x 2))
                                    (lambda (x) (* x 2)))
                              (list 2 4 6 8)) #false)

(define (valid-results? loi lof lor)
  (cond
    [(and (empty? loi) (empty? lof) (empty? lor)) #t]
    [(and (cons? loi) (cons? lof))
     (and (= ((first lof) (first loi)) (first lor))
          (valid-results? (rest loi) (rest lof) (rest lor)))]))

; Exercise 4

(define-struct assignment (role person))
; An Assignment is a (make-assignment Symbol [Maybe String])

; assign : [List-of Symbol] [List-of [Maybe String]] -> [List-of Assignment]
; returns a [List-of Assignment] where roles are matched up with people.
; Unfilled roles are paired with #false and unpaired people are ignored.

(check-expect (assign '() '()) '())

(check-expect (assign (list 'manager 'cashier 'cashier 'assistant 'assistant)
                      (list "James" "Dixie" "Noah" "Charli" "Chase"))
              (list (make-assignment 'manager "James")
                    (make-assignment 'cashier "Dixie")
                    (make-assignment 'cashier "Noah")
                    (make-assignment 'assistant "Charli")
                    (make-assignment 'assistant "Chase")))

(check-expect (assign (list 'proffessor 'proffessor 'ta 'student 'student 'student 'student)
                      (list "Arjun" "Ben" "Aislin" "Anjali" "Tomas"))
              (list (make-assignment 'proffessor "Arjun")
                    (make-assignment 'proffessor "Ben")
                    (make-assignment 'ta "Aislin")
                    (make-assignment 'student "Anjali")
                    (make-assignment 'student "Tomas")
                    (make-assignment 'student #false)
                    (make-assignment 'student #false)))

(check-expect (assign (list 'surgeon 'doctor 'nurse 'nurse 'patient)
                      (list "Derek" "Meredith" "Alex" "Jo" "April" "Bailey" "Cristina"))
              (list (make-assignment 'surgeon "Derek")
                    (make-assignment 'doctor "Meredith")
                    (make-assignment 'nurse "Alex")
                    (make-assignment 'nurse "Jo")
                    (make-assignment 'patient "April")))
                      
(define (assign los lop)
  (cond
    [(and (empty? los) (empty? lop)) '()]
    [(and (cons? los) (cons? lop))
     (cons (make-assignment (first los) (first lop))
           (assign (rest los) (rest lop)))]
    [(and (cons? los) (empty? lop)) (cons (make-assignment (first los) #false)
                                          (assign (rest los) lop))]
    [(and (empty? los) (cons? lop)) '()]))

; Exercise 5

(define-struct bt [value left right])
; A BT (Binary Tree) is one of:
; - 'none
; - (make-bt Symbol BT BT)
;
; A BT can be 'none or a (make-bt v l r), where _v_ is the value
; of the tree, and _l_ is the left value which is also a BT and
; _r_ is the right value which is also an BT.

(define tree-ex-1 (make-bt 'uhh 'none 'none))
(define tree-ex-2 (make-bt 'hello
                           (make-bt 'num1 'none 'none)
                           (make-bt 'num2 'none 'none)))
(define tree-ex-3 (make-bt 'hello
                           (make-bt 'num2 'none 'none)
                           (make-bt 'num1 'none 'none)))
(define tree-ex-4 (make-bt 'hello
                           (make-bt 'num2 'none 'none)
                           (make-bt 'num3 'none 'none)))
(define tree-ex-5 (make-bt 'hi
                           (make-bt 'num2 'none 'none)
                           (make-bt 'num3 'none 'none)))
(define tree-ex-6 (make-bt 'hi tree-ex-3 tree-ex-2))
(define tree-ex-7 (make-bt 'hi tree-ex-2 tree-ex-3))
(define tree-ex-8 (make-bt 'hello
                           (make-bt 'num3 'none 'none)
                           (make-bt 'num1 'none 'none)))
(define tree-ex-9 (make-bt 'hello 'none tree-ex-7))
(define tree-ex-10 (make-bt 'hello tree-ex-7 'none))
(define tree-ex-11 (make-bt 'hello (make-bt 'hi 'none tree-ex-3) 'none))
(define tree-ex-12 (make-bt 'hello (make-bt 'hi tree-ex-2 'none) 'none))

; bt-temp : BT -> ???
(define (bt-temp bt)
  (cond
    [(symbol? bt) ...]
    [(bt? bt) (... (bt-value bt) ...
                   (bt-temp (bt-left bt)) ...
                   (bt-temp (bt-right bt)) ...)]))

; tree-quiv : BT BT -> Boolean
; Compares two BT for alignment; allows any two BT to
; be considered equivalent if each contains subtrees equivalent
; to one of the subtrees in the corresponding other BT.
(check-expect (tree-equiv 'none tree-ex-1) #false)
(check-expect (tree-equiv tree-ex-2 tree-ex-3) #true)
(check-expect (tree-equiv tree-ex-2 tree-ex-2) #true)
(check-expect (tree-equiv tree-ex-2 tree-ex-4) #false)
(check-expect (tree-equiv tree-ex-3 tree-ex-4) #false)
(check-expect (tree-equiv tree-ex-5 tree-ex-4) #false)
(check-expect (tree-equiv tree-ex-6 tree-ex-7) #true)
(check-expect (tree-equiv tree-ex-2 tree-ex-8) #false)

(define (tree-equiv tree1 tree2)
  (cond
    [(or (symbol? tree1) (symbol? tree2)) #false]
    [(or (symbol? (bt-left tree1)) (symbol? (bt-right tree1))
         (symbol? (bt-left tree2)) (symbol? (bt-right tree2)))
     (and (symbol? (bt-left tree1)) (symbol? (bt-right tree1))
          (symbol? (bt-left tree2)) (symbol? (bt-right tree2)))]
    [(not (eq? (bt-value tree1) (bt-value tree2))) #false]
    [(eq? (bt-value (bt-left tree1)) (bt-value (bt-left tree2)))
     (if (eq? (bt-value (bt-right tree1)) (bt-value (bt-right tree2)))
         (and (tree-equiv (bt-left tree1) (bt-left tree2))
              (tree-equiv (bt-right tree1) (bt-right tree2)))
         #false)]
    [(eq? (bt-value (bt-left tree1)) (bt-value (bt-right tree2)))
     (if (eq? (bt-value (bt-right tree1)) (bt-value (bt-left tree2)))
         (and (tree-equiv (bt-left tree1) (bt-right tree2))
              (tree-equiv (bt-right tree1) (bt-left tree2)))
         #false)]
    [else #false]))

; Exercise 6

; find-subtree : BT BT -> Boolean
; Sees if a subtree (seceond paramter) exists in a BT (first paramter).
(check-expect (find-subtree tree-ex-7 tree-ex-3) #true)
(check-expect (find-subtree tree-ex-6 tree-ex-3) #true)
(check-expect (find-subtree tree-ex-7 tree-ex-2) #true)
(check-expect (find-subtree tree-ex-6 tree-ex-2) #true)
(check-expect (find-subtree tree-ex-9 tree-ex-2) #true)
(check-expect (find-subtree tree-ex-10 tree-ex-2) #true)
(check-expect (find-subtree tree-ex-10 tree-ex-5) #false)

(define (find-subtree maintree subtree)
  (cond
    [(eq? maintree subtree) #true]
    [(and (symbol? (bt-left maintree)) (symbol? (bt-right maintree))) #false]
    [(symbol? (bt-left maintree)) (find-subtree (bt-right maintree) subtree)]
    [(symbol? (bt-right maintree)) (find-subtree (bt-left maintree) subtree)]
    [else (or (find-subtree (bt-right maintree) subtree)
              (find-subtree (bt-left maintree) subtree))]))

; Exercise 7

; tree-val : BT -> Symbol
; returns a value of a BT, and 'none if theres none
(define (tree-val bt)
  (if (symbol? bt)
      'none
      (bt-value bt)))

; max-common-tree : BT BT -> BT
; Finds a BT that shares the maximum number of nodes with two source BT.
(check-expect (max-common-tree tree-ex-7 tree-ex-3) 'none)
(check-expect (max-common-tree tree-ex-6 tree-ex-3) 'none)
(check-expect (max-common-tree tree-ex-9 tree-ex-2) (make-bt 'hello 'none 'none))
(check-expect (max-common-tree tree-ex-10 tree-ex-11)
              (make-bt 'hello
                       (make-bt 'hi
                                'none
                                (make-bt 'hello
                                         (make-bt 'num2 'none 'none)
                                         (make-bt 'num1 'none 'none)))
                       'none))
(check-expect (max-common-tree tree-ex-10 tree-ex-12)
              (make-bt 'hello
                       (make-bt 'hi
                                (make-bt 'hello
                                         (make-bt 'num1 'none 'none)
                                         (make-bt 'num2 'none 'none))
                                'none)
                       'none))

(define (max-common-tree tree1 tree2)
  (cond
    [(or (symbol? tree1) (symbol? tree2)) 'none]
    [(not (eq? (tree-val tree1) (tree-val tree2))) 'none]
    [(not (or (eq? (tree-val (bt-left tree1)) (tree-val (bt-left tree2)))
              (eq? (tree-val (bt-right tree1)) (tree-val (bt-right tree2)))))
     (make-bt (tree-val tree1) 'none 'none)]
    [(and (eq? (tree-val (bt-left tree1)) (tree-val (bt-left tree2)))
          (eq? (tree-val (bt-right tree1)) (tree-val (bt-right tree2))))
     (make-bt (tree-val tree1)
              (max-common-tree (bt-left tree1) (bt-left tree2))
              (max-common-tree (bt-right tree1) (bt-right tree2)))]
    [(eq? (tree-val (bt-left tree1)) (tree-val (bt-left tree2)))
     (make-bt (tree-val tree1)
              (max-common-tree (bt-left tree1) (bt-left tree2))
              'none)]
    [(eq? (tree-val (bt-right tree1)) (tree-val (bt-right tree2)))
     (make-bt (tree-val tree1)
              'none
              (max-common-tree (bt-right tree1) (bt-right tree2)))]))

; Exercise 8

(define-struct numbt [value left right])
; A NumBT (Binary Tree) is one of:
; - 'none
; - (make-bt Number BT BT)

(define bst-ex-1 (make-numbt 50 'none 'none))
(define bst-ex-2 (make-numbt 30 'none 'none))
(define bst-ex-3 (make-numbt 40 bst-ex-2 bst-ex-1))
(define bst-ex-4 (make-numbt 20 'none bst-ex-3))
(define bst-ex-5 (make-numbt 25 bst-ex-4 'none))

; valid-bst-path? : BST Number [List-of Dir] -> Boolean
; Validate a search algorithm’s ([List-of Dir]) decisions as it descends a BST.
(check-expect (valid-bst-path? bst-ex-3 30 (list 'left)) #true)
(check-expect (valid-bst-path? bst-ex-4 50 (list 'right 'right)) #true)
(check-expect (valid-bst-path? bst-ex-4 50 (list 'left 'right)) #false)
(check-expect (valid-bst-path? bst-ex-5 20 (list 'left 'right)) #false)

(define (valid-bst-path? bst num lod)
  (cond
    [(empty? lod) (= (numbt-value bst) num)]
    [(eq? (first lod) 'left) (if (symbol? (numbt-left bst))
                                 #false
                                 (valid-bst-path? (numbt-left bst) num (rest lod)))]
    [else (if (symbol? (numbt-right bst))
              #false
              (valid-bst-path? (numbt-right bst) num (rest lod)))]))

; Exercise 9

; merge : OrderedList OrderedList ComparisonFunction -> OrderedList
; Takes two ordered lists containing elements of the same type,
; and produces a single resulting ordered list
(check-expect (merge (list 2 4 6 8) (list 1 3 5 7) <) (list 1 2 3 4 5 6 7 8))
(check-expect (merge (list "a" "b" "f") (list "c" "d" "e") string<?) (list "a" "b" "c" "d" "e" "f"))
(check-expect (merge (list 1 42 57 78) (list 4 22 55 1222) <) (list 1 4 22 42 55 57 78 1222))

(define (merge ol1 ol2 cmpfunct)
  (if (cmpfunct (first ol1) (first ol2))
      (merge-helper (rest ol1) ol2 cmpfunct (list (first ol1)))
      (merge-helper ol1 (rest ol2) cmpfunct (list (first ol2)))))

; merge-helper : OrderedList OrderedList ComparisonFunction OrderedList -> OrderedList
; Helper function for merge, uses a third OrderedList as a placeholder to build the list.
(check-expect (merge-helper (list 2 4 6 8) (list 1 3 5 7) < (list)) (list 1 2 3 4 5 6 7 8))
(check-expect (merge-helper (list "a" "b" "f") (list "c" "d" "e") string<? (list))
              (list "a" "b" "c" "d" "e" "f"))
(check-expect (merge-helper (list 1 42 57 78) (list 4 22 55 1222) < (list))
              (list 1 4 22 42 55 57 78 1222))

(define (merge-helper ol1 ol2 cmpfunct outlist)
  (cond
    [(or (empty? ol1) (empty? ol2))
     (if (empty? ol1)
         (append outlist ol2)
         (append outlist ol1))]
    [else (if (cmpfunct (first ol1) (first ol2))
              (merge-helper (rest ol1) ol2 cmpfunct (append outlist (list (first ol1))))
              (merge-helper ol1 (rest ol2) cmpfunct (append outlist (list (first ol2)))))]))
