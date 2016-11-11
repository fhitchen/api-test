(ns api-test.core-test
  (:require [clojure.test :refer :all]
            [api-test.core :refer :all]
            [api-test.mq-send :refer :all]
            [api-test.xml :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))


(deftest run-cukes
  ;(. cucumber.api.cli.Main (main (into-array ["--plugin" "pretty" "--glue" "/Users/fhitchen/git/api-test/api-test/test/features/step_definitions" "/Users/fhitchen/git/api-test/api-test/test/features"])))
  )
