(defproject api-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
		 [info.cukes/cucumber-clojure "1.2.4"]
                 [org.clojure/data.zip "0.1.2"]
                 [org.clojure/data.xml "0.1.0-beta3"]]
  :main ^:skip-aot api-test.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :plugins [[com.siili/lein-cucumber "1.0.7"]]
  :resource-paths ["resources/com.ibm.mqjms.jar"
                   "resources/com.ibm.mq.jar"
                   "resources/dhbcore.jar"
                   "resources/connector.jar"
                   "resources/jta.jar"]
)

