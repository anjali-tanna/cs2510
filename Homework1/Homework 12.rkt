;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname |Homework 12|) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
; Homework 12

; Exercise 1                                

(define-struct elnode [label edges])
(define-struct edge [direction endnode])
; An ELGraph is a [List-of (make-elnode String [List-of (make-edge String String)])]
; An ELGraph is a [List-of Node] which includes a [List-of Edge]. It is a graph of
; nodes and edges shown as strings.
; 
; A Node is (make-elnode String [List-of Edge])
; - where _label_ is the name of the intersection the node is located at,
; - and _edges_ is the [List-of Edge] it connects to. 
;
; An Edge is (make-edge String String)
; - where _direction_ is the direction of the Node it points to,
; - and _endnode_ is the Node it points to. 
;
; An ELGraph is a list of Nodes,
; where a Node has a label and a list of Edges it connects to,
; and an Edge has a label and the Node it connects to.

; ELGraph Examples
(define elgraph1 (list
                  (make-elnode "A" (list
                                    (make-edge "Elm St" "B")))
                  (make-elnode "B" (list
                                    (make-edge "Elm St" "B")
                                    (make-edge "Birch Dr" "C")))
                  (make-elnode "C" (list
                                    (make-edge "Birch Dr" "C")))))


; A --------------> B --------------> C
;        Elm St         Birch Dr

(define elgraph2 (list
                  (make-elnode "A" (list
                                    (make-edge "peach" "A")
                                    (make-edge "apple" "B")))
                  (make-elnode "B" (list
                                    (make-edge "apple" "B")
                                    (make-edge "banana" "C")))
                  (make-elnode "C" (list
                                    (make-edge "banana" "C")
                                    (make-edge "pear" "D")))
                  (make-elnode "D" (list
                                    (make-edge "pear" "D")
                                    (make-edge "peach" "A")))))
                            
; A -------> B
; ^  apple  |
; |         |
; | peach   | banana                               
; |         | 
; |  pear   v
; D <------ C
              
(define elgraph3 (list
                  (make-elnode "A" (list
                                    (make-edge "north" "A")
                                    (make-edge "east" "B")))
                  (make-elnode "B" (list
                                    (make-edge "east" "B")
                                    (make-edge "northeast" "B")))
                  (make-elnode "C" (list
                                    (make-edge "north" "A")
                                    (make-edge "northeast" "B")))))
                                                                    
; A --------> B
; ^  east  /^                                 
; |       /
; |      /                                 
; |north/
; |    /
; |   / northeast
; |  /                                        
; | /
; C


; Node Examples
(define node1 (make-elnode "Anjali" (list
                                     (make-edge "north to" "Steve")
                                     (make-edge "south to" "Shank"))))
(define node2 (make-elnode "A" (list
                                (make-edge "north" "B")
                                (make-edge "south" "C")
                                (make-edge "west" "D")
                                (make-edge "east" "E"))))
(define node3 (make-elnode "B" (list
                                (make-edge "banana" "C")
                                (make-edge "pear" "D"))))

; elnode-temp : Node -> ???
(define (elnode-temp elnode)
  (... (elnode-label) ...
       (elnode-edges) ...))

; Edge Examples
(define edge1 (make-edge "north" "Fundies"))
(define edge2 (make-edge "south" "My grade"))
(define edge3 (make-edge "west" "Northeastern"))

; edge-temp : Edge -> ???
(define (edge-temp edge)
  (... (edge-direction) ...
       (edge-endnode) ...))

; Exercise 2

(define given-street-graph
  (list
   (make-elnode "Forsyth Way and Hemenway"
                (list
                 (make-edge "south to" "Huntington and Forsyth Way")
                 (make-edge "east to" "Hemenway and Forsyth St")))
   (make-elnode "Hemenway and Forsyth St"
                (list
                 (make-edge "west to" "Forsyth Way and Hemenway")
                 (make-edge "south to" "Forsyth St and Huntington")
                 (make-edge "east to" "Hemenway and Gainsborough")))
   (make-elnode "Hemenway and Gainsborough"
                (list
                 (make-edge "west to" "Hemenway and Forsyth St")
                 (make-edge "south to" "Gainsborough and St Stephen")))
   (make-elnode "Gainsborough and St Stephen"
                (list
                 (make-edge "north to" "Hemenway and Gainsborough")
                 (make-edge "west to" "St Stephen and Opera")))
   (make-elnode "St Stephen and Opera"
                (list
                 (make-edge "east to" "Gainsborough and St Stephen")
                 (make-edge "south to" "Opera and Huntington")))
   (make-elnode "Opera and Huntington"
                (list
                 (make-edge "north to" "St Stephen and Opera")
                 (make-edge "west to" "Forsyth St and Hungtinton")))
   (make-elnode "Forsyth St and Huntington"
                (list
                 (make-edge "east to" "Opera and Huntington")
                 (make-edge "north to" "Hemenway and Forsyth St")
                 (make-edge "west to" "Huntington and Forsyth Way")))
   (make-elnode "Huntington and Forsyth Way"
                (list
                 (make-edge "east to" "Forsyth St and Huntington")
                 (make-edge "north to" "Forsyth Way and Hemenway")))))

; Link to map: https://bit.ly/33XuwlP                                            

(define my-street-graph
  (list
   (make-elnode "Commonwealth and Hereford"
                (list
                 (make-edge "east to" "Fairfield and Commonwealth")
                 (make-edge "south to" "Hereford and Newbury")))
   (make-elnode "Hereford and Newbury"
                (list
                 (make-edge "north to" "Commonwealth and Hereford")
                 (make-edge "east to" "Newbury and Dartmouth")))
   (make-elnode "Newbury and Dartmouth"
                (list
                 (make-edge "west to" "Hereford and Newbury")
                 (make-edge "north to" "Dartmouth and Marlborough")))
   (make-elnode "Dartmouth and Marlborough"
                (list
                 (make-edge "south to" "Newbury and Dartmouth")
                 (make-edge "west to" "Marlborough and Fairfield")))
   (make-elnode "Marlborough and Fairfield"
                (list
                 (make-edge "east to" "Dartmouth and Marlborough")
                 (make-edge "south to" "Fairfield and Commonwealth")))
   (make-elnode "Fairfield and Commonwealth"
                (list
                 (make-edge "north to" "Marlborough and Fairfield")
                 (make-edge "west to" "Commonwealth and Hereford")))))

; Exercise 3

; driving-directions : ELGraph String String -> [List-of String]
; returns a list of directions from the first given intersection to
; the second given intersection.
(check-expect (driving-directions my-street-graph "Hereford and Newbury" "Dartmouth and Marlborough")
              (list
               "north to Commonwealth and Hereford"
               "east to Fairfield and Commonwealth"
               "north to Marlborough and Fairfield"
               "east to Dartmouth and Marlborough"))
(check-expect (driving-directions given-street-graph
                                  "Forsyth Way and Hemenway" "Opera and Huntington")
              (list
               "south to Huntington and Forsyth Way"
               "east to Forsyth St and Huntington"
               "east to Opera and Huntington"))
(check-expect (driving-directions given-street-graph
                                  "Huntington and Forsyth Way" "Gainsborough and St Stephen")
              (list
               "east to Forsyth St and Huntington"
               "east to Opera and Huntington"
               "north to St Stephen and Opera"
               "east to Gainsborough and St Stephen"))

(define (driving-directions elgraph int1 int2)
  (local [; dir-acc : ELGraph String String [List-of String] -> [List-of String]
          ; takes an ELGraph and a starting point String and end point String,
          ; returns directions of the intersections passed through from this
          ; starting point to the ending point.
          ; ACCUMULATOR : seen takes note of all intersections passed in a list.
          (define (dir-acc elgraph int1 int2 seen)
            (cond [(string=? int1 int2) '()]
                  [else (next-path elgraph
                                   (locate-node elgraph int1) int2 seen)]))
          ; next-path : ELGraph Node String -> [List-of String]
          ; determines if there is a path from the starting point to the ending point
          ; by going through all the edges of the nodes. 
          (define (next-path elgraph bgn int2 seen)
            (if (and (path-exists? elgraph (edge-endnode (first (elnode-edges bgn))) int2)
                     (not (member? (edge-endnode (first (elnode-edges bgn))) seen)))
                (cons (string-append (edge-direction (first (elnode-edges bgn)))
                                     " "
                                     (edge-endnode (first (elnode-edges bgn))))
                      (dir-acc elgraph (edge-endnode (first (elnode-edges bgn)))
                               int2 (cons (edge-endnode (first (elnode-edges bgn))) seen)))
                (next-path elgraph (make-node (elnode-label bgn) (rest (elnode-edges bgn)))
                           int2 seen)))]
    (dir-acc elgraph int1 int2 '())))

; path-exists? : ELGraph String String -> Boolean
; determines if a path exists in a given ELGraph between two given Nodes.
(check-expect (path-exists? given-street-graph "Forsyth St and Huntington" "Opera and Huntington")
              #true)
(check-expect (path-exists? elgraph1 "A" "B")
              #true)
(check-expect (path-exists? elgraph3 "A" "C")
              #false)

(define (path-exists? elgraph int1 int2)
  (local [; path-helper : ELGraph Node Node [List-of Node] -> Boolean
          ; determines if a path is found.
          ; ACCUMULATOR : already-seen keeps track of the nodes visited so far on this path
          (define (path-helper elgraph int1 int2 already-seen)
            (local [(define edges-of-int1
                      (map (lambda (edge)
                             (edge-endnode edge))
                           (elnode-edges (locate-node elgraph int1))))]
              (cond [(string=? int1 int2) #true]
                    [(member? int1 already-seen) ;; stuck in a cycle
                     #false]
                    [(member? int2 edges-of-int1) ;; path found
                     #true]
                    [else ;; not a direct neighbor 
                     (ormap (λ (edge) (path-helper elgraph edge int2 (cons int1 already-seen)))
                            edges-of-int1)])))]
    (path-helper elgraph int1 int2 '()))) 

; locate-node : ELGraph Node -> [List-of String]
; locates a Node in the given ELGraph based on the intersection given.
(check-expect (locate-node elgraph1 "A")
              (make-elnode "A" (list (make-edge "Elm St" "B"))))
(check-expect (locate-node elgraph2 "B")
              (make-elnode "B" (list (make-edge "apple" "B") (make-edge "banana" "C"))))
(check-expect (locate-node given-street-graph "Forsyth St and Huntington")
              (make-elnode
               "Forsyth St and Huntington"
               (list
                (make-edge "east to" "Opera and Huntington")
                (make-edge "north to" "Hemenway and Forsyth St")
                (make-edge "west to" "Huntington and Forsyth Way"))))

(define (locate-node elgraph int1)
  (cond
    [(empty? elgraph) (error "intersection not found")]
    [(cons? elgraph) (first (filter (λ (x) (string=? (elnode-label x) int1))
                                    elgraph))]))


; Exercise 4

; fully-connected? : ELGraph -> Boolean
; returns #true if there is a path between all pairs of points
; in an ELGraph.
(check-expect (fully-connected? elgraph1) #false)
(check-expect (fully-connected? elgraph2) #true)
(check-expect (fully-connected? elgraph3) #false)
(check-expect (fully-connected? my-street-graph) #true)

(define (fully-connected? elgraph)
  (andmap (lambda (node1)
            (andmap (lambda (node2) (path-exists? elgraph (elnode-label node1)
                                                  (elnode-label node2)))
                    elgraph)) elgraph))

; Exercise 5

(define-struct node [left value right])
(define-struct leaf [value])
; A [BT X Y] is one of:
; - (make-leaf Y)
; - (make-node [BT X Y] X [BT X Y])
; Interpretation: A [BT X Y] represents a binary tree with values _X_ at each
; node and values _Y_ at each leaf.
; Note: that a [BT X Y] may not be a binary search tree.

(define bt-ex-1 (make-node (make-leaf 100)
                           0
                           (make-node (make-leaf 5) -3 (make-leaf 100))))
(define bt-ex-2 (make-node (make-node (make-leaf 4) 43 (make-leaf 3))
                           3
                           (make-node bt-ex-1 -3 bt-ex-1)))
(define bt-ex-3 (make-node bt-ex-2
                           23
                           bt-ex-1))
(define bt-ex-4 (make-node (make-node (make-leaf 4) 43 (make-leaf 3))
                           22
                           (make-leaf 234)))
(define bt-ex-5
  (make-node (make-leaf 10)
             15
             (make-node (make-leaf 4) 20
                        (make-node (make-leaf 3) 30
                                   (make-node (make-leaf 2) 40
                                              (make-node (make-leaf 1) 50 (make-leaf 60)))))))


; A Path is:
; - [List-of Number]
; a [List-of Number] represents a path to a leaf.
(define path-ex-1 (list 0 100))
(define path-ex-2 (list 0 -3 5))
(define path-ex-3 (list 0 -3 100))
(define path-ex-4 (list 43 4))
(define path-ex-5 (list 0 -3 43 3))

; shortest-path-to-leaf : {X} [BT X X] -> Path
; Produces the shortest path to a leaf.
(check-expect (shortest-path-to-leaf bt-ex-1) path-ex-1)
(check-expect (shortest-path-to-leaf bt-ex-2) (list 3 43 4))
(check-expect (shortest-path-to-leaf bt-ex-3) (list 23 0 100))
(check-expect (shortest-path-to-leaf bt-ex-4) (list 22 234))

(define (shortest-path-to-leaf bt)
  (cond
    [(and (node? (node-left bt)) (node? (node-right bt)))
     (if (> (length (append (list (node-value bt)) (shortest-path-to-leaf (node-left bt))))
            (length (append (list (node-value bt)) (shortest-path-to-leaf (node-right bt)))))
         (append (list (node-value bt)) (shortest-path-to-leaf (node-right bt)))
         (append (list (node-value bt)) (shortest-path-to-leaf (node-left bt))))]
    [(and (leaf? (node-left bt)) (leaf? (node-right bt)))
     (list (node-value bt) (leaf-value (node-left bt)))]
    [else (if (leaf? (node-left bt))
              (list (node-value bt) (leaf-value (node-left bt)))
              (list (node-value bt) (leaf-value (node-right bt))))]))

; Exercise 6

; A BST is a [BT Number String] that is constrained to be a binary search tree:
; At every _(make-node l v r)_ in a BST, all node-values in _l_ are less than
;  _v_ and all node-values in _r_ are greater than _v_.

; is-bt-a-bst? : [BT Number String] -> Boolean
; produces #true if its argument is a BST.
(check-expect (is-bt-a-bst? bt-ex-5) #t)
(check-expect (is-bt-a-bst? bt-ex-4) #f)
(check-expect (is-bt-a-bst? bt-ex-3) #f)

(define (is-bt-a-bst? bt)
  (local [; left-acc : BT Nat -> Boolean
          ; determines whether or not the value is less than the last value.
          ; ACCUMULATOR : lastval keeps track of the last value.
          (define (left-acc bt lastval)
            (cond [(leaf? bt) (< (leaf-value bt) lastval)]
                  [(node? bt) (and (< (node-value bt) lastval)
                                   (left-acc (node-left bt) (node-value bt))
                                   (right-acc (node-right bt) (node-value bt)))]))
          ; right-acc : BT Nat -> Boolean
          ; determines whether or not the value is greater than the last value.
          ; ACCUMULATOR : lastval keeps track of the last value.
          (define (right-acc bt lastval)
            (cond [(leaf? bt) (> (leaf-value bt) lastval)]
                  [(node? bt) (and (> (node-value bt) lastval)
                                   (left-acc (node-left bt) (node-value bt))
                                   (right-acc (node-right bt) (node-value bt)))]))]
    (cond [(leaf? bt) #t]
          [(node? bt) (and (left-acc (node-left bt) (node-value bt))
                           (right-acc (node-right bt) (node-value bt)))])))