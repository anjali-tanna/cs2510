;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname |Homework 1|) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))

; QUESTION 8

; multiply a number, x, by 9
; nineply : Number -> Number

(define (nineply x)
  (* x 9))

(check-expect (nineply -2) -18)
(check-expect (nineply 0) 0)
(check-expect (nineply 3) 27)


; QUESTION 9

; t = 0          1    2      3   4   5  6  7 
; d = FUNDIES FUNDIE FUNDI FUND FUN FU  F

; As the number, t, increases, the string "FUNDIES" subtracts a character.
; string-minus : Number -> String

(define (string-minus t)
  (substring "FUNDIES" 0 (- (string-length "FUNDIES") t)))


; QUESTION 10

; String input is animated to spell out given string in all uppercase, one letter at
; a time in size 40 blue font on a white background.
; String animation restarts when end of given string is reached.
; cycle-spelling : String -> String

(require 2htdp/universe)
(require 2htdp/image)

(define LONG-WORD "diScomBoBUlaTeDiScomBoBulaTeDboBUlaTeD")

(string-length "diScomBoBUlaTeDiScomBoBulaTeDboBUlaTeD")

(define (cycle-spelling word)
  (overlay (text (string-upcase
                  (substring LONG-WORD 0 (remainder word (string-length LONG-WORD)))) 40 "blue")
           (rectangle 1000 80 "solid" "white")))


; QUESTION 11

; When inputing any width and height into (draw-kite w h), a kite with four colors,
; blue, yellow, green, red (going clockwise starting in the top left corner), is produced.
; The width of the kite is at a point that separates the length of the kite into 1/3 and 2/3,
; top and bottom respectively.
; draw-kite : Number -> Image

(require 2htdp/image)

(define (draw-kite w h)
  (above (beside/align "baseline" (flip-horizontal (right-triangle (/ w 2) (/ h 3) "solid" "blue"))
                       (right-triangle (/ w 2) (/ h 3) "solid" "yellow"))
         (beside/align "baseline"
                       (flip-vertical (flip-horizontal (right-triangle (/ w 2)
                                                                       (/ (* 2 h) 3) "solid" "red")))
                       (flip-vertical (right-triangle (/ w 2) (/ (* 2 h) 3) "solid" "green")))))


; QUESTION 12

; Converts all inputs to inches from the given values of yards, feet, and inches.
; Calculates the sum, in inches, from the input values given in yards, feet, and inches.
; convert-to-inches : Number -> Number

(define (convert-to-inches a b c)
  (+ (* 36 a) (* 12 b) c))

(check-expect (convert-to-inches 4 1 6) 162)
(check-expect (convert-to-inches 1 3 4) 76)


; QUESTION 13

; Calculates whether the driver is driving at an "okay" speed, a "bit fast", or a
; certain mph over the limit.
; Under the limit is "okay", at most 9mph over limit is "bit fast", and 10+mph over limit is speeding.
; Inputs should be only natural numbers.
; speed-check : Number -> String

(define (speed-check x y)
  (cond
    [(< x y) "okay"]
    [(<= (- x 9) y) "bit fast"]
    [(> x y) (string-append "speeding: " (number->string (- x y)) "mph over limit")]))
    
(check-expect (speed-check 50 60) "okay" )
(check-expect (speed-check 65 60) "bit fast" )
(check-expect (speed-check 70 60) "speeding: 10mph over limit")
(check-expect (speed-check 44 40) "bit fast" )
(check-expect (speed-check 30 45) "okay" )


