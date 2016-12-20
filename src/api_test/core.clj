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

(defn gen
  [input]
  (with-open [rdr (clojure.java.io/reader (str "resources/" input))]
    (doseq [line (line-seq rdr)]
      (let [m (re-find #": (<\?xml .*$)" line)
            service (x/get-value "NXHeader>ServiceName" (second m))]
        (if (= nil service)
          (println "nil message " line))
        (if (= nil ((keyword service) @gen-msgs))
          (do
            (reset! gen-msgs (conj @gen-msgs {(keyword service) m}))
            (println "Found " service))
                                        ;(println "Skipping service " service)
          )))))
            



(defn -main
  ""
  [& args]
  (cond
   (= "flatten" (first args)) (println (x/print-values (second args)))
   (= "gen" (first args)) (gen "msgs.txt")
   ))




