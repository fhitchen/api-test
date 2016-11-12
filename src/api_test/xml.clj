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

(def test-values (lazy-seq '({:content "888888888", :tag "createBan>createBanRespInfo>banId"} {:content "1142664520", :tag "NXHeader>rReplyCompCode"})))

;(println (pr-str test-values))


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


;(print-values "create-subscriber.xml")










