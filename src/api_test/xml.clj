(ns api-test.xml
  (:require
   [clojure.zip :as zip]
   [clojure.data.zip :as zf]
   [clojure.data.xml :as xml]
   [clojure.data.zip.xml :as zx]
   [clojure.string :as str]
   [clojure.pprint :as p])
  (:import [java.io ByteArrayInputStream]))

(defn parse [string]
  (xml/parse
   (ByteArrayInputStream.
    (.getBytes (.trim string)))))

(defn uuid [] (str (java.util.UUID/randomUUID)))

;(def test-response "<?xml version='1.0'?><response><NXHeader><Version></Version><ServiceName>AMD.ENSEMBLE.CREATE_BAN</ServiceName><ApplRef>1477923078_BOOST_BAN</ApplRef><ReplyCompCode>0</ReplyCompCode></NXHeader><header></header><createBan><CreateBanRespInfo><banId>754776797</banId></CreateBanRespInfo></createBan></response>")

(;def test-values (lazy-seq '({:content "888888888", :tag
 ;"createBan>createBanRespInfo>banId"} {:content "1142664520", :tag
 ;"NXHeader>rReplyCompCode"}))
 )

;(println (pr-str test-values))

(def test-request "<?xml version='1.0'?><newPpSocInfo>
          <pricePlanSocCode>PGCDM1090</pricePlanSocCode>
          <featureApiInfoList>
            <FeatureApiInfo>
              <featureCode>CHSDA</featureCode>
            </FeatureApiInfo>
            <FeatureApiInfo>
              <featureCode>CLIP</featureCode>
            </FeatureApiInfo>
            <!--FeatureApiInfo>
              <featureCode>HLT</featureCode>
            </FeatureApiInfo>
            <FeatureApiInfo>
              <featureCode>INT</featureCode>
            </FeatureApiInfo>
            <FeatureApiInfo>
              <featureCode>SMS</featureCode>
            </FeatureApiInfo>
            <FeatureApiInfo>
              <featureCode>STD</featureCode>
            </FeatureApiInfo>
            <FeatureApiInfo>
              <featureCode>TWAYR</featureCode>
            </FeatureApiInfo>
            <FeatureApiInfo>
              <featureCode>UNTETH</featureCode>
            </FeatureApiInfo-->
          </featureApiInfoList>
        </newPpSocInfo>
")


(def test-request "<?xml version='1.0'?><e>
          <a>PGCDM1090</a>
          <b>
            <c>
              <d>CHSDA</d>
            </c>
            <c>
              <d>CLIP</d>
            </c>
            <!--FeatureApiInfo>
              <featureCode>HLT</featureCode>
            </FeatureApiInfo>
            <FeatureApiInfo>
              <featureCode>INT</featureCode>
            </FeatureApiInfo>
            <FeatureApiInfo>
              <featureCode>SMS</featureCode>
            </FeatureApiInfo>
            <FeatureApiInfo>
              <featureCode>STD</featureCode>
            </FeatureApiInfo>
            <FeatureApiInfo>
              <featureCode>TWAYR</featureCode>
            </FeatureApiInfo>
            <FeatureApiInfo>
              <featureCode>UNTETH</featureCode>
            </FeatureApiInfo-->
          </b>
        </e>
")


(def zipper (zip/xml-zip (parse test-request)))

(def match (map #(zx/tag= %) '(:b :c :d)))
(pr match)
(pr (class match))

(def content '("CLIP" "foo"))

(pr (first content))

(def big-match (concat match (map #(zx/text= %) (list (first content)))))
(pr (count big-match))
(pr big-match)
(println (apply zx/xml-> zipper
           big-match))



(defn test-set-values
  "given a lazy sequence of cucumber table values of :tag path and :content reset the :content of the matching paths in the message"
  [values message]
  (loop [zipper (zip/xml-zip (parse message)) line values]
    (if (= () line)

      (xml/indent-str (zip/root zipper))
      (do
        (println "line=" line)
        (let [paths (str/split (:tag (first line)) #">")
              m (map #(zx/tag= (keyword %)) paths)
              c (str/split (:content (first line)) #">")
              content (if (= (count c) 2)
                        (last c)
                        (first c))
              _ (println (str paths " C=" c "Content=" content "M=" (first c) ":" (count (first c))))
              matches (if (= (count c) 2)
                        (concat m (map #(zx/text= %) (list (first c))))
                        m)
              _ (println "COUNT OF MATCHES = " (count matches))
              edit { :tag (keyword (last paths)) :content content }
              new-loc (apply zx/xml-> zipper matches)]
          (if (= 0 (count new-loc))
            (println "WARNING: " (first line) " NOT FOUND"))
          (let [loc (if (not= () new-loc)
                      (do
                        (println "edits="  edit)
                        (zip/root (zip/replace new-loc edit)))
                      (zip/root zipper))]
            (recur (zip/xml-zip loc) (rest line))))))))

(pr (test-set-values  (lazy-seq '({:tag "a" :content "arglebargle" }
                              {:tag "b>c>d":content "CHSDA>foo" }
                              {:tag "b>c>d":content "CLIP>bar" })) test-request))



(def myvals (lazy-seq '({:tag "a" :content "arglebargle" }
                              {:tag "b>c>d" :content "foo" }
                              {:tag "b>c>d" :content "bar" })))




(defn reduce-by [key-fn f init coll]
  (reduce (fn [summaries x]
            (let [k (key-fn x)]
              (assoc summaries k (f (summaries k init ) x))))
          {} coll))

(def rdcd (reduce-by :tag #(conj %1 (:content %2)) [] myvals))

(pr rdcd)
(pr  (second rdcd))
`
(for [line rdcd]
  (do
    (pr (key line))
    (pr (val line))))


(defn set-values
  "given a lazy sequence of cucumber table values of :tag path and :content reset the :content of the matching paths in the message"
  [values message]
  (loop [zipper (zip/xml-zip (parse message)) line values]
    (if (= () line)
      (xml/indent-str (zip/root zipper))
      (do
        (let [paths (str/split (:tag (first line)) #">")
              matches (map #(zx/tag= (keyword %)) paths)
              edit { :tag (keyword (last paths)) :content (:content (first line)) }
              new-loc (apply zx/xml1-> zipper matches)]
          (let [loc (if (not= nil new-loc)
                      (zip/root (zip/replace new-loc edit))
                      (zip/root zipper))]
            (recur (zip/xml-zip loc) (rest line))))))))


;(println (xset-values test-values test-response))

(defn get-value
  [path message]
  (let [paths (str/split path #">")
        matches (map #(zx/tag= (keyword %)) paths)
        zipper (zip/xml-zip (parse message))]
    (apply zx/xml1-> zipper (conj (into [] matches) zx/text))))

(defn print-values
  "print set values out of input message"
  [message-file]
  (let [message (slurp (str "resources/messages/" message-file))
        zipper (zip/xml-zip (parse message))]
    (loop [z zipper]
      (if (zip/end? z)
        true
        (do
          (if (string? (zip/node z))
            (do
              (loop [zz z path (str " \"" (zip/node z) "\"")]
                (if-not (zip/up zz)
                  (println (str/replace path #"request>(.*)> (\".*\")" "\t| \"$1\" | $2 |"))
                  (do
                    (recur (zip/up zz) (str (name (:tag (zip/node (zip/up zz)))) ">" path)))))))
          (recur (zip/next z)))))))


;(print-values "bottom.xml")











