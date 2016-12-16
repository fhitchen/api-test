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
  (value @stored-values))


;(def test-values (lazy-seq '({:content "$BANID", :tag "createBan>createBanRespInfo>banId"} {:content "1142664520", :tag "NXHeader>rReplyCompCode"})))

(defn replace-variables [values]
  (map #(if-let [v (re-find  #"^\$.*" (:content %))]
          (do
            (println (str "found var " (:content %)))
            (println (str "replace with " %))
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

(defn -main
  ""
  [& args]
  (println (x/print-values (first args))))




