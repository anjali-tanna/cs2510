;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-reader.ss" "lang")((modname |Homework 7|) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
; Exercise 2

; Data Definitions

; A [List-of SocialMediaItem] is one of:
; - '()
; - (cons SocialMediaItem [List-of SocialMediaItem])
; 
; A list of items, where every item is an SocialMediaItem.

; A SocialMediaItem is one of:
; - Tweet
; - FacebookPost
; - MediumStory
; A SocialMediaItem represents either a Tweet with text, a certain
; number of likes, and certain number of retweets, or
; a FacebookPost with text and a certain number of likes, or a MediumStory
; with text and a certain number of page views.

(define-struct tweet [text likes retweets])
; A Tweet is a (make-tweet String[0,280] PostiveInteger PostiveInteger)
; A (make-tweet t l r) represents a the text of a
; Tweet _t_ with a maximum of 280 characters, the
; number of likes _l_, and the number of retweets _r_.

(define-struct facebookpost [text likes])
; A FacebookPost is a (make-facebookpost String NNN)
; a (make-facebook t l) represents the text in a
; FacebookPost _t_, and the number of likes _l_.

(define-struct medium [text pageviews])
; A MediumStory is a (make-medium String NNN)
; a (make-medium t p) represents the text of a
; MediumStory _t_ and the number of page views _p_.

; Examples and Templates

; Tweet Examples
(define tweet1 (make-tweet "dont ghost healthy habits this halloween, huskies #protectthepack"
                           203 44))
(define tweet2 (make-tweet "varsity sports are back. @GoNUathletics teams are making safe returns"
                           397 67))
(define tweet3 (make-tweet "remember to keep on track with your covid-19 testing! #protectthepack"
                           156 23))
(define tweet4 (make-tweet "happy tuesday!!!"
                           3 0))
(define tweet5 (make-tweet "hi"
                           0 0))

; Template
; tweet-temp : Tweet -> ???
(define (tweet-temp tw)
  (... (tweet-text tw) ...
       (tweet-likes tw) ...
       (tweet-retweets tw) ...))

; FacebookPost Examples
(define fb1 (make-facebookpost "hi everyone, today is a great day! i went hiking
with my family and now i am on my way to grab ice cream." 55))
(define fb2 (make-facebookpost "tonight i watched the movie 'halloweentown' to get
into the spooky spirit. stay safe during this pandemic!" 78))
(define fb3 (make-facebookpost "the election is coming up, and i hope all of you are
exercising your right to vote! #voteblue" 104))
(define fb4 (make-facebookpost "hey how is everyone" 0))

; Template
; facebookpost-temp : FacebookPost -> ???
(define (facebookpost-temp fb)
  (... (facebookpost-text fb) ...
       (facebookpost-likes fb) ...))

; MediumStory Examples
(define medium1 (make-medium (string-append "We hold these truths to be self-evident, that all men"
                                            "are created equal, that they are endowed by their "
                                            "Creator with certain unalienable Rights, that among "
                                            "these are Life, Liberty and the pursuit of Happiness."
                                            "--That to secure these rights, Governments are "
                                            "instituted among Men, deriving their just powers from "
                                            "the consent of the governed,--That whenever any Form "
                                            "of Government becomes destructive of these ends, it "
                                            "is the Right of the People to alter or to abolish it, "
                                            "and to institute new Government, laying its "
                                            "foundation on such principles and organizing its "
                                            "powers in such form, as to them shall seem most "
                                            "likely to effect their Safety and Happiness.") 452))

(define medium2 (make-medium (string-append "Exercise 2 Design a data definition that holds "
                                            "information about a single social media item, which "
                                            "may be a Twitter tweet, Facebook post, or Medium "
                                            "story. For a tweet, the data definition should hold "
                                            "the text of the tweet (at most 280 characters), the "
                                            "number of likes, and the number of retweets. For a "
                                            "Facebook post, the data definition should hold the "
                                            "text of the post, and the number of likes. For a "
                                            "Medium story, the data definition should hold the "
                                            "text of the story and the number of page views.") 0))

(define medium3 (make-medium (string-append "Northeastern University (NU or NEU) is a private "
                                            "research university in Boston, Massachusetts, "
                                            "established in 1898. The university offers "
                                            "undergraduate and graduate programs on its main "
                                            "campus in Boston, as well as regional campuses in "
                                            "Charlotte, North Carolina; Seattle, Washington; San "
                                            "Jose, California; San Francisco, California; Toronto, "
                                            "Vancouver, and Portland, Maine. In 2019, Northeastern "
                                            "purchased the New College of the Humanities in "
                                            "London, England. The university's enrollment is "
                                            "approximately 18,000 undergraduate students and 8,000 "
                                            "graduate students.") 2033))

; Template
; medium-temp : Medium -> ???
(define (medium-temp md)
  (... (medium-text md) ...
       (medium-pageviews md) ...))

; SocialMediaItem Examples
(define socialmedia1 tweet1)
(define socialmedia2 tweet4)
(define socialmedia3 tweet5)
(define socialmedia4 fb1)
(define socialmedia5 fb4)
(define socialmedia6 medium1)
(define socialmedia7 medium2)

; Template
; socialmedia-temp : SocialMediaItem -> ???
(define (socialmedia-temp sm)
  (cond
    [(tweet? sm) (... (tweet-temp sm) ...)]
    [(facebookpost? sm) (... (facebookpost-temp sm) ...)]
    [(medium? sm) (... (medium-temp sm) ...)]))

; [List-of SocialMediaItem] Examples
(define socialmedialist-ex-1 (list socialmedia1 socialmedia2 socialmedia3
                                   socialmedia4 socialmedia5 socialmedia6
                                   socialmedia7))
(define socialmedialist-ex-2 (list socialmedia1 socialmedia3
                                   socialmedia4 socialmedia6))
(define socialmedialist-ex-3 (list socialmedia1 socialmedia6))

; Template
; socialmedialist-template : [List-of SocialMediaItem] -> ???
(define (socialmedialist-template sml)
  (cond
    [(empty? sml) ...]
    [(cons? sml) (... (first sml) ...
                      (socialmedialist-template (rest sml)) ...)]))

; Exercise 3

; tweets-w-retweets : {SocialMediaItem} (SocialMediaItem -> Boolean)
; [List-of SocialMediaItem] -> [List-of SocialMediaItem]
; Produces only the tweets in a SocialMediaList that have at least one retweet.
(check-expect (tweets-w-retweets socialmedialist-ex-1) (list socialmedia1))
(check-expect (tweets-w-retweets socialmedialist-ex-2) (list socialmedia1))
(check-expect (tweets-w-retweets socialmedialist-ex-3) (list socialmedia1))

(define (tweets-w-retweets sml)
  (filter retweeted? (filter tweet? sml)))

; retweeted? : Tweet -> Boolean
; Checks if a tweet has retweets
(check-expect (retweeted? tweet5) #false)
(check-expect (retweeted? tweet1) #true)
(check-expect (retweeted? tweet2) #true)

(define (retweeted? tw)
  (> (tweet-retweets tw) 0))

; Exercise 4

; posts-without-interactions : {SocialMediaItem} (SocialMediaItem -> Boolean)
; [List-of SocialMediaItem] -> [List-of SocialMediaItem]
; Produces only the posts in a SocialMediaList that no retweets, likes, or views.
(check-expect (posts-without-interactions socialmedialist-ex-1) (list socialmedia3
                                                                      socialmedia5
                                                                      socialmedia7))
(check-expect (posts-without-interactions socialmedialist-ex-2) (list socialmedia3))
(check-expect (posts-without-interactions socialmedialist-ex-3) '())

(define (posts-without-interactions sml)
  (append
   (filter (compose not tweet-liked?) (filter (compose not retweeted?) (filter tweet? sml)))
   (filter (compose not fb-liked?) (filter facebookpost? sml))
   (filter (compose not viewed?) (filter medium? sml))))

; tweet-liked? : Tweet -> Boolean
; Checks if a tweet has likes
(check-expect (tweet-liked? tweet5) #false)
(check-expect (tweet-liked? tweet1) #true)
(check-expect (tweet-liked? tweet2) #true)

(define (tweet-liked? tw)
  (> (tweet-likes tw) 0))

; fb-liked? : FacebookPost -> Boolean
; Checks if a FacebookPost has likes
(check-expect (fb-liked? fb4) #false)
(check-expect (fb-liked? fb1) #true)
(check-expect (fb-liked? fb3) #true)

(define (fb-liked? fb)
  (> (facebookpost-likes fb) 0))

; viewed? : MediumStory -> Boolean
; Checks if a MediumStory has views
(check-expect (viewed? medium2) #false)
(check-expect (viewed? medium1) #true)
(check-expect (viewed? medium3) #true)

(define (viewed? md)
  (> (medium-pageviews md) 0))

; Exercise 5

; total-engagement : {SocialMediaItem} (SocialMediaItem -> PositiveInteger)
; [List-of SocialMediaItem] -> PositiveInteger
; Produces the sum of retweets, likes, and views of items within a SocialMediaList
(check-expect (total-engagement socialmedialist-ex-1) 757)
(check-expect (total-engagement socialmedialist-ex-2) 754)
(check-expect (total-engagement socialmedialist-ex-3) 699)

(define (total-engagement sml)
  (apply + (map post-engagement sml)))

; post-engagement : SocialMediaItem -> PositiveInteger
; Produces post-engagement sum of retweets, likes, and views of items within a SocialMediaList
(check-expect (post-engagement socialmedia1) 247)
(check-expect (post-engagement socialmedia4) 55)
(check-expect (post-engagement socialmedia6) 452)

(define (post-engagement sm)
  (cond
    [(tweet? sm) (+ (tweet-likes sm) (tweet-retweets sm))]
    [(facebookpost? sm) (facebookpost-likes sm)]
    [(medium? sm) (medium-pageviews sm)]))

; Exercise 6

; crosspost : [List-of SocialMediaItem] -> [List-of SocialMediaItem]
; Adds two SocialMediaItem as different platforms for every SocialMediaItem given.
(check-expect (crosspost socialmedialist-ex-1)
              (list
               (make-facebookpost
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-medium
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-facebookpost "happy tuesday!!!" 0)
               (make-medium "happy tuesday!!!" 0)
               (make-facebookpost "hi" 0)
               (make-medium "hi" 0)
               (make-tweet (string-append "hi everyone, today is a great day! i went hiking\nwith "
                                          "my family and now i am on my way to grab ice cream.")
                           0 0)
               (make-medium (string-append "hi everyone, today is a great day! i went hiking\nwith "
                                           "my family and now i am on my way to grab ice cream.") 0)
               (make-tweet "hey how is everyone" 0 0)
               (make-medium "hey how is everyone" 0)
               (make-facebookpost
                (string-append "We hold these truths to be self-evident, that all menare created "
                               "equal, that they are endowed by their Creator with certain "
                               "unalienable Rights, that among these are Life, Liberty and the "
                               "pursuit of Happiness.--That to secure these rights, Governments "
                               "are instituted among Men, deriving their just powers from the "
                               "consent of the governed,--That whenever any Form of Government "
                               "becomes destructive of these ends, it is the Right of the People "
                               "to alter or to abolish it, and to institute new Government, "
                               "laying its foundation on such principles and organizing its "
                               "powers in such form, as to them shall seem most likely to effect "
                               "their Safety and Happiness.") 0)
               (make-tweet (string-append "We hold these truths to be self-evident, that all "
                                          "menare created equal, that they are endowed by their "
                                          "Creator with certain unalienable Rights, that among "
                                          "these are Life, Liberty and the pursuit of Happiness."
                                          "--That to secure these rights, Governments are "
                                          "instituted among Men, der") 0 0)
               (make-facebookpost
                (string-append "Exercise 2 Design a data definition that holds information about a "
                               "single social media item, which may be a Twitter tweet, Facebook "
                               "post, or Medium story. For a tweet, the data definition should "
                               "hold the text of the tweet (at most 280 characters), the number of "
                               "likes, and the number of retweets. For a Facebook post, the data "
                               "definition should hold the text of the post, and the number of "
                               "likes. For a Medium story, the data definition should hold the "
                               "text of the story and the number of page views.") 0)
               (make-tweet (string-append "Exercise 2 Design a data definition that holds "
                                          "information about a single social media item, which may "
                                          "be a Twitter tweet, Facebook post, or Medium story. For "
                                          "a tweet, the data definition should hold the text of "
                                          "the tweet (at most 280 characters), the number of "
                                          "likes, and the num") 0 0)))
(check-expect (crosspost socialmedialist-ex-2)
              (list
               (make-facebookpost
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-medium
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-facebookpost "hi" 0)
               (make-medium "hi" 0)
               (make-tweet (string-append "hi everyone, today is a great day! i went hiking\nwith "
                                          "my family and now i am on my way to grab ice cream.")
                           0 0)
               (make-medium (string-append "hi everyone, today is a great day! i went hiking\nwith "
                                           "my family and now i am on my way to grab ice cream.") 0)
               (make-facebookpost
                (string-append "We hold these truths to be self-evident, that all menare created "
                               "equal, that they are endowed by their Creator with certain "
                               "unalienable Rights, that among these are Life, Liberty and the "
                               "pursuit of Happiness.--That to secure these rights, Governments "
                               "are instituted among Men, deriving their just powers from the "
                               "consent of the governed,--That whenever any Form of Government "
                               "becomes destructive of these ends, it is the Right of the People "
                               "to alter or to abolish it, and to institute new Government, "
                               "laying its foundation on such principles and organizing its "
                               "powers in such form, as to them shall seem most likely to effect "
                               "their Safety and Happiness.") 0)
               (make-tweet (string-append "We hold these truths to be self-evident, that all "
                                          "menare created equal, that they are endowed by their "
                                          "Creator with certain unalienable Rights, that among "
                                          "these are Life, Liberty and the pursuit of Happiness."
                                          "--That to secure these rights, Governments are "
                                          "instituted among Men, der") 0 0)))
(check-expect (crosspost socialmedialist-ex-3)
              (list
               (make-facebookpost
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-medium
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-facebookpost
                (string-append "We hold these truths to be self-evident, that all menare created "
                               "equal, that they are endowed by their Creator with certain "
                               "unalienable Rights, that among these are Life, Liberty and the "
                               "pursuit of Happiness.--That to secure these rights, Governments "
                               "are instituted among Men, deriving their just powers from the "
                               "consent of the governed,--That whenever any Form of Government "
                               "becomes destructive of these ends, it is the Right of the People "
                               "to alter or to abolish it, and to institute new Government, "
                               "laying its foundation on such principles and organizing its "
                               "powers in such form, as to them shall seem most likely to effect "
                               "their Safety and Happiness.") 0)
               (make-tweet (string-append "We hold these truths to be self-evident, that all "
                                          "menare created equal, that they are endowed by their "
                                          "Creator with certain unalienable Rights, that among "
                                          "these are Life, Liberty and the pursuit of Happiness."
                                          "--That to secure these rights, Governments are "
                                          "instituted among Men, der") 0 0)))

(define (crosspost sml)
  (cond
    [(empty? sml) '()]
    [(cons? sml) (append (crosspost-helper (first sml)) (crosspost (rest sml)))]))

; crosspost-helper : SocialMediaItem -> [List-of SocialMediaItem]
; Creates a list of two SocialMediaItem as different platform.
(check-expect (crosspost-helper tweet1)
              (list
               (make-facebookpost
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-medium
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)))
(check-expect (crosspost-helper fb1)
              (list
               (make-tweet (string-append "hi everyone, today is a great day! i went hiking\nwith "
                                          "my family and now i am on my way to grab ice cream.")
                           0 0)
               (make-medium (string-append "hi everyone, today is a great day! i went hiking\nwith "
                                           "my family and now i am on my way to grab ice cream.")
                            0)))
(check-expect (crosspost-helper medium1)
              (list
               (make-facebookpost
                (string-append "We hold these truths to be self-evident, that all menare created "
                               "equal, that they are endowed by their Creator with certain "
                               "unalienable Rights, that among these are Life, Liberty and the "
                               "pursuit of Happiness.--That to secure these rights, Governments "
                               "are instituted among Men, deriving their just powers from the "
                               "consent of the governed,--That whenever any Form of Government "
                               "becomes destructive of these ends, it is the Right of the People "
                               "to alter or to abolish it, and to institute new Government, "
                               "laying its foundation on such principles and organizing its "
                               "powers in such form, as to them shall seem most likely to effect "
                               "their Safety and Happiness.") 0)
               (make-tweet (string-append "We hold these truths to be self-evident, that all "
                                          "menare created equal, that they are endowed by their "
                                          "Creator with certain unalienable Rights, that among "
                                          "these are Life, Liberty and the pursuit of Happiness."
                                          "--That to secure these rights, Governments are "
                                          "instituted among Men, der") 0 0)))

(define (crosspost-helper smi)
  (cond
    [(tweet? smi) (list
                   (make-facebookpost (tweet-text smi) 0)
                   (make-medium (tweet-text smi) 0))]
    [(facebookpost? smi) (list
                          (make-tweet (tweet-truncate (facebookpost-text smi)) 0 0)
                          (make-medium (facebookpost-text smi) 0))]
    [(medium? smi) (list
                    (make-facebookpost (medium-text smi) 0)
                    (make-tweet (tweet-truncate (medium-text smi)) 0 0))]))

; tweet-truncate : String -> String
; Truncates a string to 280 characers
(check-expect (tweet-truncate (medium-text medium1))
              (string-append "We hold these truths to be self-evident, that all menare created "
                             "equal, that they are endowed by their Creator with certain "
                             "unalienable Rights, that among these are Life, Liberty and the "
                             "pursuit of Happiness.--That to secure these rights, Governments are "
                             "instituted among Men, der"))
(check-expect (tweet-truncate (medium-text medium2))
              (string-append "Exercise 2 Design a data definition that holds information about a "
                             "single social media item, which may be a Twitter tweet, Facebook "
                             "post, or Medium story. For a tweet, the data definition should hold "
                             "the text of the tweet (at most 280 characters), the number of "
                             "likes, and the num"))
(check-expect (tweet-truncate (medium-text medium3))
              (string-append "Northeastern University (NU or NEU) is a private research university "
                             "in Boston, Massachusetts, established in 1898. The university "
                             "offers undergraduate and graduate programs on its main campus in "
                             "Boston, as well as regional campuses in Charlotte, North Carolina; "
                             "Seattle, Washingt"))

(define (tweet-truncate str)
  (cond
    [(< 280 (string-length str)) (substring str 0 280)]
    [else str]))

; Exercise 7

; append-apply-to-all : Function(that produces a list) List -> List
; Makes a List that has Function applied to each item, then appends that to the original list.
(check-expect (append-apply-to-all string->list (list "hi" "what" "yo"))
              (list #\h #\i #\w #\h #\a #\t #\y #\o))
(check-expect (append-apply-to-all crosspost-helper socialmedialist-ex-3)
              (list
               (make-facebookpost
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-medium
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-facebookpost
                (string-append "We hold these truths to be self-evident, that all menare created "
                               "equal, that they are endowed by their Creator with certain "
                               "unalienable Rights, that among these are Life, Liberty and the "
                               "pursuit of Happiness.--That to secure these rights, Governments "
                               "are instituted among Men, deriving their just powers from the "
                               "consent of the governed,--That whenever any Form of Government "
                               "becomes destructive of these ends, it is the Right of the People "
                               "to alter or to abolish it, and to institute new Government, "
                               "laying its foundation on such principles and organizing its "
                               "powers in such form, as to them shall seem most likely to effect "
                               "their Safety and Happiness.") 0)
               (make-tweet (string-append "We hold these truths to be self-evident, that all "
                                          "menare created equal, that they are endowed by their "
                                          "Creator with certain unalienable Rights, that among "
                                          "these are Life, Liberty and the pursuit of Happiness."
                                          "--That to secure these rights, Governments are "
                                          "instituted among Men, der") 0 0)))
(check-expect (append-apply-to-all string->list (list "testing" "this" "ok?"))
              (list #\t #\e #\s #\t #\i #\n #\g #\t #\h #\i #\s #\o #\k #\?))

(define (append-apply-to-all fun lst)
  (cond
    [(empty? lst) '()]
    [(cons? lst) (append (fun (first lst)) (append-apply-to-all fun (rest lst)))]))

; Exercise 8

; crosspost/v2 : [List-of SocialMediaItem] -> [List-of SocialMediaItem]
; Adds two SocialMediaItem as different platforms for every SocialMediaItem given.
(check-expect (crosspost/v2 socialmedialist-ex-1)
              (list
               (make-facebookpost
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-medium
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-facebookpost "happy tuesday!!!" 0)
               (make-medium "happy tuesday!!!" 0)
               (make-facebookpost "hi" 0)
               (make-medium "hi" 0)
               (make-tweet (string-append "hi everyone, today is a great day! i went hiking\nwith "
                                          "my family and now i am on my way to grab ice cream.")
                           0 0)
               (make-medium (string-append "hi everyone, today is a great day! i went hiking\nwith "
                                           "my family and now i am on my way to grab ice cream.") 0)
               (make-tweet "hey how is everyone" 0 0)
               (make-medium "hey how is everyone" 0)
               (make-facebookpost
                (string-append "We hold these truths to be self-evident, that all menare created "
                               "equal, that they are endowed by their Creator with certain "
                               "unalienable Rights, that among these are Life, Liberty and the "
                               "pursuit of Happiness.--That to secure these rights, Governments "
                               "are instituted among Men, deriving their just powers from the "
                               "consent of the governed,--That whenever any Form of Government "
                               "becomes destructive of these ends, it is the Right of the People "
                               "to alter or to abolish it, and to institute new Government, "
                               "laying its foundation on such principles and organizing its "
                               "powers in such form, as to them shall seem most likely to effect "
                               "their Safety and Happiness.") 0)
               (make-tweet (string-append "We hold these truths to be self-evident, that all "
                                          "menare created equal, that they are endowed by their "
                                          "Creator with certain unalienable Rights, that among "
                                          "these are Life, Liberty and the pursuit of Happiness."
                                          "--That to secure these rights, Governments are "
                                          "instituted among Men, der") 0 0)
               (make-facebookpost
                (string-append "Exercise 2 Design a data definition that holds information about a "
                               "single social media item, which may be a Twitter tweet, Facebook "
                               "post, or Medium story. For a tweet, the data definition should "
                               "hold the text of the tweet (at most 280 characters), the number of "
                               "likes, and the number of retweets. For a Facebook post, the data "
                               "definition should hold the text of the post, and the number of "
                               "likes. For a Medium story, the data definition should hold the "
                               "text of the story and the number of page views.") 0)
               (make-tweet (string-append "Exercise 2 Design a data definition that holds "
                                          "information about a single social media item, which may "
                                          "be a Twitter tweet, Facebook post, or Medium story. For "
                                          "a tweet, the data definition should hold the text of "
                                          "the tweet (at most 280 characters), the number of "
                                          "likes, and the num") 0 0)))
(check-expect (crosspost/v2 socialmedialist-ex-2)
              (list
               (make-facebookpost
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-medium
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-facebookpost "hi" 0)
               (make-medium "hi" 0)
               (make-tweet (string-append "hi everyone, today is a great day! i went hiking\nwith "
                                          "my family and now i am on my way to grab ice cream.")
                           0 0)
               (make-medium (string-append "hi everyone, today is a great day! i went hiking\nwith "
                                           "my family and now i am on my way to grab ice cream.") 0)
               (make-facebookpost
                (string-append "We hold these truths to be self-evident, that all menare created "
                               "equal, that they are endowed by their Creator with certain "
                               "unalienable Rights, that among these are Life, Liberty and the "
                               "pursuit of Happiness.--That to secure these rights, Governments "
                               "are instituted among Men, deriving their just powers from the "
                               "consent of the governed,--That whenever any Form of Government "
                               "becomes destructive of these ends, it is the Right of the People "
                               "to alter or to abolish it, and to institute new Government, "
                               "laying its foundation on such principles and organizing its "
                               "powers in such form, as to them shall seem most likely to effect "
                               "their Safety and Happiness.") 0)
               (make-tweet (string-append "We hold these truths to be self-evident, that all "
                                          "menare created equal, that they are endowed by their "
                                          "Creator with certain unalienable Rights, that among "
                                          "these are Life, Liberty and the pursuit of Happiness."
                                          "--That to secure these rights, Governments are "
                                          "instituted among Men, der") 0 0)))
(check-expect (crosspost/v2 socialmedialist-ex-3)
              (list
               (make-facebookpost
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-medium
                "dont ghost healthy habits this halloween, huskies #protectthepack" 0)
               (make-facebookpost
                (string-append "We hold these truths to be self-evident, that all menare created "
                               "equal, that they are endowed by their Creator with certain "
                               "unalienable Rights, that among these are Life, Liberty and the "
                               "pursuit of Happiness.--That to secure these rights, Governments "
                               "are instituted among Men, deriving their just powers from the "
                               "consent of the governed,--That whenever any Form of Government "
                               "becomes destructive of these ends, it is the Right of the People "
                               "to alter or to abolish it, and to institute new Government, "
                               "laying its foundation on such principles and organizing its "
                               "powers in such form, as to them shall seem most likely to effect "
                               "their Safety and Happiness.") 0)
               (make-tweet (string-append "We hold these truths to be self-evident, that all "
                                          "menare created equal, that they are endowed by their "
                                          "Creator with certain unalienable Rights, that among "
                                          "these are Life, Liberty and the pursuit of Happiness."
                                          "--That to secure these rights, Governments are "
                                          "instituted among Men, der") 0 0)))

(define (crosspost/v2 sml)
  (append-apply-to-all crosspost-helper sml))

; Exercise 9

; items-since-tweet : [List-of SocialMediaItem] String -> [List-of SocialMediaItem]
; Produces a [List-of SocialMediaItem] of all the social media items that
; were made since a given Tweet
(check-expect (items-since-tweet (list medium1 tweet1 fb4) (tweet-text tweet1))
              (list tweet1 fb4))
(check-expect (items-since-tweet (list fb1 fb4 tweet4 fb2 fb3) "happy tuesday!!!")
              (list tweet4 fb2 fb3))
(check-expect (items-since-tweet (list medium1 tweet3 fb4 fb3 fb2 tweet5 medium2 fb1) "hi")
              (list tweet5 medium2 fb1))

(define (items-since-tweet sml str)
  (cond
    [(empty? sml) '()]
    [(and (cons? sml) (tweet-and-text-equals? (first sml) str)) sml]
    [(cons? sml) (items-since-tweet (rest sml) str)]))

; tweet-and-text-equals? : SocialMediaItem String -> Boolean
; Checks if a SocialMediaItem is a Tweet, and if the string is equal to the text.
(check-expect (tweet-and-text-equals? fb2 "hello") #false)
(check-expect (tweet-and-text-equals? tweet4 "hello") #false)
(check-expect (tweet-and-text-equals? tweet5 "hi") #true)

(define (tweet-and-text-equals? smi str)
  (cond
    [(tweet? smi) (string=? (tweet-text smi) str)]
    [else false]))

; Exercise 10

; items-since-10-likes : [List-of SocialMediaItem] String -> [List-of SocialMediaItem]
; Produces a [List-of SocialMediaItem] of all the social media items that
; were made since the first FacebookPost to reach 10 likes.
(check-expect (items-since-10-likes (list medium1 fb3 tweet1 fb4))
              (list fb3 tweet1 fb4))
(check-expect (items-since-10-likes (list tweet3 fb4 tweet4 fb2 fb3))
              (list fb2 fb3))
(check-expect (items-since-10-likes (list medium1 tweet3 fb4 fb3 fb2 tweet5 medium2 fb1))
              (list fb3 fb2 tweet5 medium2 fb1))

(define (items-since-10-likes sml)
  (cond
    [(empty? sml) '()]
    [(and (cons? sml) (fb-at-least-ten-likes? (first sml))) sml]
    [(cons? sml) (items-since-10-likes (rest sml))]))

; fb-at-least-ten-likes? : SocialMediaItem -> Boolean
; Checks if a SocialMediaItem is a FacebookPost, and it has at least 10 likes.
(check-expect (fb-at-least-ten-likes? fb1) #true)
(check-expect (fb-at-least-ten-likes? fb4) #false)
(check-expect (fb-at-least-ten-likes? fb2) #true)

(define (fb-at-least-ten-likes? smi)
  (cond
    [(facebookpost? smi) (> (facebookpost-likes smi) 9)]
    [else false]))

; Exercise 11

; suffix-from-2500 : [List-of Numbers] -> [List-of Numbers]
; Produces the suffix of a [List-of Numbers] that begins from the first 2500
; that occurs in the given [List-of Numbers].
(check-expect (suffix-from-2500 (list 2 424 43 2500 1 2 3 4)) (list 2500 1 2 3 4))
(check-expect (suffix-from-2500 (list 5 3 2 2500 9 8 77)) (list 2500 9 8 77))
(check-expect (suffix-from-2500 (list 2500 5 3 223 321)) (list 2500 5 3 223 321))

(define (suffix-from-2500 lon)
  (cond
    [(empty? lon) '()]
    [(and (cons? lon) (= 2500 (first lon))) lon]
    [(cons? lon) (suffix-from-2500 (rest lon))]))

; Exercise 12

; suffix-from-y : [List-of X] Function -> [List-of X]
; Produces the suffix of a [List-of X] that begins from the first item
; that function Y returns true in the given [List-of X].
(check-expect (suffix-from-y (list medium1 fb1 tweet2 tweet3 medium3) tweet?)
              (list tweet2 tweet3 medium3))
(check-expect (suffix-from-y (list medium1 fb1 tweet2 tweet3 medium1) facebookpost?)
              (list fb1 tweet2 tweet3 medium1))
(check-expect (suffix-from-y (list fb1 tweet2 medium3 tweet3 medium1) medium?)
              (list medium3 tweet3 medium1))

(define (suffix-from-y lst y)
  (cond
    [(empty? lst) '()]
    [(and (cons? lst) (y (first lst))) lst]
    [(cons? lst) (suffix-from-y (rest lst) y)]))

; Exercise 13

; items-since-tweet/v2 : [List-of SocialMediaItem] String -> [List-of SocialMediaItem]
; Produces a [List-of SocialMediaItem] of all the social media items that
; were made since a given tweet 
(check-expect (items-since-tweet/v2 (list medium1 tweet1 fb4) (tweet-text tweet1))
              (list tweet1 fb4))
(check-expect (items-since-tweet/v2 (list fb1 fb4 tweet4 fb2 fb3) "happy tuesday!!!")
              (list tweet4 fb2 fb3))
(check-expect (items-since-tweet/v2 (list medium1 tweet3 fb4 fb3 fb2 tweet5 medium2 fb1) "hi")
              (list tweet5 medium2 fb1))

(define (items-since-tweet/v2 sml str)
  (local (; items-since-helper : SocialMediaItem -> Boolean
          ; determines whether a tweet and given text are matching.
          [define (items-since-helper smi) (tweet-and-text-equals? smi str)])
    (suffix-from-y sml items-since-helper)))

; items-since-10-likes/v2 : [List-of SocialMediaItem] String -> [List-of SocialMediaItem]
; Produces a [List-of SocialMediaItem] of all the social media items that
; were made since the first FacebookPost to reach 10 likes.
(check-expect (items-since-10-likes/v2 (list medium1 fb3 tweet1 fb4))
              (list fb3 tweet1 fb4))
(check-expect (items-since-10-likes/v2 (list tweet3 fb4 tweet4 fb2 fb3))
              (list fb2 fb3))
(check-expect (items-since-10-likes/v2 (list medium1 tweet3 fb4 fb3 fb2 tweet5 medium2 fb1))
              (list fb3 fb2 tweet5 medium2 fb1))

(define (items-since-10-likes/v2 sml)
  (suffix-from-y sml fb-at-least-ten-likes?))

; suffix-from-2500/v2 : [List-of Numbers] -> [List-of Numbers]
; Produces the suffix of a [List-of Numbers] that begins from the first 2500
; that occurs in the given [List-of Numbers].
(check-expect (suffix-from-2500/v2 (list 2 424 43 2500 1 2 3 4)) (list 2500 1 2 3 4))
(check-expect (suffix-from-2500/v2 (list 5 3 2 2500 9 8 77)) (list 2500 9 8 77))
(check-expect (suffix-from-2500/v2 (list 2500 5 3 223 321)) (list 2500 5 3 223 321))

(define (suffix-from-2500/v2 sml)
  (suffix-from-y sml =2500))

; =2500 : Number -> Boolean
; Checks if a number is equal to 2500
(check-expect (=2500 5) #false)
(check-expect (=2500 2500.5) #false)
(check-expect (=2500 2500) #true)

(define (=2500 num) (= 2500 num))
