(ns api-test.core
  (:require [api-test.xml :as x])
  (:gen-class))

(def x-values (atom []))
(def response (atom ""))
(def stored-values (atom (hash-map)))
(def message (atom ""))


(defn update-stored-value
  [value]
  (reset! stored-values (conj @stored-values value)))
(defn get-stored-value
  [value]
  (if (string? value)
    ((keyword value) @stored-values)
    (value @stored-values)))


;(def test-values (lazy-seq '({:content "$BANID", :tag "createBan>createBanRespInfo>banId"} {:content "1142664520", :tag "NXHeader>rReplyCompCode"})))

(defn replace-variables [values]
  (map #(if-let [v (re-find  #"^\$.*" (:content %))]
          (do
            ;(println (str "found var " (:content %)))
            ;(println (str "replace with " %))
            (assoc-in % [:content] (get-stored-value (keyword v) )))
          %) values))

(defn generate-esn
  "Generate a random esn"
  []
  (format "26840000000%07d" (rand-int 9999999)))

(defn generate-applref
  "Generate a ramdon ApplRef"
  [] (str (java.util.UUID/randomUUID)))

;(class (update-stored-value {:$BANID "nine" }))
;(get-stored-value :$BANID)
;(replace-variables test-values)

                                        ;(assoc-in {:content "$BANID" :tag "xxx" } [:content] "9999")



(def gen-msgs (atom (hash-map)))

(def feature-template "Feature: Test %s message

  Rules:
  - the ApplRef in the request must match the ApplRef in the response.

  Background:   
     Given the message \"%s.xml\" with the following values:

     | tag                                                                   | content                   |
")

(defn create-feature-files
  [m]
  (doseq [[n message] m]
    (println "Found Name" n)
    (let [m-name (name n)
          feature-file (str "generated/features/" m-name ".feature")
          message-file (str m-name ".xml")]
      (spit feature-file
            (format feature-template m-name m-name))
      (println (class message))
      (spit (str "generated/messages/" message-file) message)
      (spit feature-file
            (x/gen-values "generated/messages/" message-file) :append true))))
    

(defn gen
  [input]
  (with-open [rdr (clojure.java.io/reader (str "resources/" input))]
    (doseq [line (line-seq rdr)]
      (let [m (re-find #": (<\?xml .*$)" line)
            service (x/get-value "NXHeader>ServiceName" (second m))]
        (if (not= nil service)
          (if (= nil ((keyword service) @gen-msgs))
            (do
              (reset! gen-msgs (conj @gen-msgs {(keyword service) (second m)}))
              (println "Found " service)))))))
  (create-feature-files @gen-msgs))
            



(defn -main
  ""
  [& args]
  (cond
   (= "flatten" (first args)) (println (x/print-values (second args)))
   (= "gen" (first args)) (gen (second args))
   ))




