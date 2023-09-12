;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname |Homework 5|) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
; Exercise 1

; part a

(define-struct bracelet [charm material entire-bracelet])
; A CharmBracelet is one of:
; - "clasp"
; - (make-bracelet String Material CharmBracelet)
; - A (make-bracelet s m c) represents a charm bracelet where _s_ is a String
; that describes the shape of a charm on the bracelet, _m_ is the material of that charm, and
; _c_ is the rest of the charm bracelet.
; A "clasp" represents the end of the bracelet (where the clasp is located).

; A Description is a string that descriped the charm's shape.

(define charmbracelet-ex-1 "clasp")
(define charmbracelet-ex-2 (make-bracelet "Skull-n-Bones" "silver" charmbracelet-ex-1))
(define charmbracelet-ex-3 (make-bracelet "Unicorn" "pewter"charmbracelet-ex-2))
(define charmbracelet-ex-4 (make-bracelet "Heart" "gold" charmbracelet-ex-3))

; charmbracelet-temp : CharmBracelet -> ???
(define (bracelet-temp cb)
  (cond
    [(string? cb) ...]
    [(bracelet-charm cb) (... (material-temp (bracelet-material cb))
                              (bracelet-temp (bracelet-enitre-bracelet cb) ...))]))

; A Material is one of:
; - "silver"
; - "gold"
; - "pewter"

(define material-ex-1 "silver")
(define material-ex-2 "gold")
(define material-ex-3 "pewter")

; material-temp : Material -> ???
(define (material-temp m)
  (cond [(string=? m "silver") ...]
        [(string=? m "gold") ...]
        [(string=? m "pewter") ...]))

; part b

; bracelet-cost : CharmBracelet -> Number
; returns the cost of the CharmBracelet

(check-expect (bracelet-cost charmbracelet-ex-1) 0)
(check-expect (bracelet-cost charmbracelet-ex-2) 12)
(check-expect (bracelet-cost charmbracelet-ex-3) 22)
(check-expect (bracelet-cost charmbracelet-ex-4) 37)

(define (bracelet-cost c)
  (cond [(string? c) 0]
        [(bracelet? c) (+ (charmcost (bracelet-material c))
                          (bracelet-cost (bracelet-entire-bracelet c)))]))

; charmcost : Material -> Number
; returns the cost of the material of the charm.

(check-expect (charmcost "gold") 15)
(check-expect (charmcost "silver") 12)
(check-expect (charmcost "pewter") 10)

(define (charmcost m)
  (cond [(string=? m "silver") 12]
        [(string=? m "gold") 15]
        [(string=? m "pewter") 10]))

; Exercise 2

; part a

(define-struct bead [color size entire-bracelet])
; A FancyBracelet is one of:
; - "clasp"
; - (make-bead String Number FancyBracelet)
; - (make-charmbracelet String Material FancyBracelet)
; A FancyBracelet is a (make-bead c s fb) which is a bead on a bracelet, where _c_
; the color of the bead represented as a string, _s_ is the diameter
; of the bead in millimeters, and _fb_ is a different FancyBracelet.
; A FancyBracelet can also be a (make-bracelet s m fb) represents a charm bracelet
; where _s_ is a String that describes the shape of a charm on the bracelet,
; _m_ is the material of that charm, and _fb_ is the FancyBracelet.

(define fancybracelet-ex-1 "clasp")
(define fancybracelet-ex-2 (make-bead "magenta" 12 fancybracelet-ex-1))
(define fancybracelet-ex-3 (make-bracelet "Turtle" "silver" fancybracelet-ex-2))
(define fancybracelet-ex-4 (make-bead "green" 10 fancybracelet-ex-3))
(define fancybracelet-ex-5 (make-bracelet "Sun" "gold" fancybracelet-ex-4))
(define fancybracelet-ex-6 (make-bead "blue" 10 fancybracelet-ex-5))

; fancybracelet-temp : FancyBracelet -> ???
(define (fancybracelet-temp fb)
  (cond [(string? fb) ...]
        [(bead? fb) (...(bead-color fb) ... (bead-size fb) ...
                        (fancybracelet-temp (bead-rest-of-bracelet fb)) ...)]
        [(bracelet? fb) (...(bracelet-charm fb)...
                            (material-temp (bracelet-material fb))...
                            (fancybracelet-temp (bracelet-entire-bracelet fb)) ...)]))

; part b

; count-charms : FancyBracelet -> PositiveInteger
; returns the total number of charms on a FancyBracelet

(check-expect (count-charms fancybracelet-ex-1) 0)
(check-expect (count-charms fancybracelet-ex-2) 0)
(check-expect (count-charms fancybracelet-ex-3) 1)
(check-expect (count-charms fancybracelet-ex-4) 1)
(check-expect (count-charms fancybracelet-ex-5) 2)
(check-expect (count-charms fancybracelet-ex-6) 2)

(define (count-charms fb)
  (cond [(string? fb) 0]
        [(bead? fb)
         (count-charms (bead-entire-bracelet fb))]
        [(bracelet? fb)
         (+ 1 (count-charms (bracelet-entire-bracelet fb)))]))

; part c

; upgrade-bracelet : FancyBracelet Color String -> FancyBracelet
; inputs a FancyBracelet and replaces all beads in the given Color with a
; silver charm with the requested shape.

(check-expect (upgrade-bracelet fancybracelet-ex-1 "yellow" "Sun") "clasp")
(check-expect (upgrade-bracelet fancybracelet-ex-2 "magenta" "Moon")
              (make-bracelet "Moon" "silver" "clasp"))
(check-expect (upgrade-bracelet fancybracelet-ex-3 "blue" "Shell")
              (make-bracelet
               "Turtle"
               "silver"
               (make-bead "magenta" 12 "clasp")))
(check-expect (upgrade-bracelet fancybracelet-ex-4 "green" "Basketball")
              (make-bracelet
               "Basketball"
               "silver"
               (make-bracelet
                "Turtle"
                "silver"
                (make-bead "magenta" 12 "clasp"))))
(check-expect
 (upgrade-bracelet fancybracelet-ex-5 "black" "Diamond") (make-bracelet
                                                          "Sun"
                                                          "gold"
                                                          (make-bead
                                                           "green"
                                                           10
                                                           (make-bracelet
                                                            "Turtle"
                                                            "silver"
                                                            (make-bead "magenta" 12 "clasp")))))

(define (upgrade-bracelet fb c f)
  (cond [(string? fb) "clasp"]
        [(bead? fb) (if (string=? (bead-color fb) c)
                        (make-bracelet f "silver" (upgrade-bracelet
                                                   (bead-entire-bracelet fb) c f))
                        (make-bead (bead-color fb)
                                   (bead-size fb)
                                   (upgrade-bracelet (bead-entire-bracelet fb) c f)))]
        [(bracelet? fb) (make-bracelet (bracelet-charm fb)
                                       (bracelet-material fb)
                                       (upgrade-bracelet
                                        (bracelet-entire-bracelet fb) c f))]))

; Exercise 3

(define-struct student [firstname lastname gpa on-coop])
; A Student is a (make-student String String Number Boolean)
; Interpretation: A (make-student fn ln g c) represents a
; Northeastern student whose first name is fn and last name is ln, with 
; cumulative grade point average g, and for whom c is #true if they are
; currently doing a coop experience this term and #false otherwise.
(define student1 (make-student "Jane" "Smith" 4.0 #true))
(define student2 (make-student "Ashok" "Singhal" 0.0 #false))

; student-templ : Student -> ???
(define (student-templ st)
  (... (student-firstname st) ...
       (student-lastname st) ...
       (student-gpa st) ...
       (student-on-coop st) ...))

; part a

; A LOS (ListOfStudents) is one of:
; - '()
; - (cons Student LOS)
; represents a list of NEU Students.

(define los1 (cons student2 (cons student1 '())))
(define los2 '())
(define los3 (cons student1 '()))

; los-temp : LOS -> ???
(define (los-temp los)
  (cond
    [(empty? los) ...]
    [(cons? los) (... (student-templ (first los)) ... (los-temp (rest los)) ...)]))

; part b

; count-coop-students : LOS -> PositiveInteger
; returns the number of students on a co-op in the list.

(check-expect (count-coop-students los1) 1)
(check-expect (count-coop-students los2) 0)
(check-expect (count-coop-students los3) 1)

(define (count-coop-students los)
  (cond
    [(empty? los) 0]
    [(cons? los) (if (coop? (first los))
                     (+ 1 (count-coop-students (rest los)))
                     (count-coop-students (rest los)))]))

; coop? : Student -> Boolean
; returns whether a student is on co-op or not.

(check-expect (coop? student1) #true)
(check-expect (coop? student2) #false)

(define (coop? student)
  (cond
    [(student-on-coop student) #true]
    [(not (student-on-coop student)) #false]))

; part c

; exchange-coop-students : LOS -> LOS
; switches each student's co-op status in the list of students.

(check-expect (exchange-coop-students los1) (cons (switch-coop student2)
                                                  (cons (switch-coop student1) '())))
(check-expect (exchange-coop-students los2) '())
(check-expect (exchange-coop-students los3) (cons (switch-coop student1) '()))

(define (exchange-coop-students los)
  (cond
    [(empty? los) '()]
    [(cons? los) (cons (switch-coop (first los)) (exchange-coop-students (rest los)))]))

; switch-coop : Student -> Student
; inputs a student on co-op and returns a student not on co-op and vice versa.

(check-expect (switch-coop (make-student "Anjali" "Tanna" 4.0 #false))
              (make-student "Anjali" "Tanna" 4.0 #true))
(check-expect (switch-coop student1) (make-student  "Jane" "Smith" 4.0 #false))
(check-expect (switch-coop student2) (make-student  "Ashok" "Singhal" 0.0 #true))

(define (switch-coop student)
  (make-student
   (student-firstname student)
   (student-lastname student)
   (student-gpa student)
   (not (student-on-coop student))))

