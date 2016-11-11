(ns api-test.core
  (:gen-class))

(def x-values (atom []))
(def response (atom ""))
(def stored-values (atom (hash-map)))

(defn update-stored-value
  [value]
  (reset! stored-values (conj @stored-values value)))
(defn get-stored-value
  [value]
  (value @stored-values))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))




