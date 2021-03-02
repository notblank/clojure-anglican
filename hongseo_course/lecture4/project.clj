(defproject lecture4 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [anglican "1.1.0"]
                 [gorilla-renderable "2.0.0"]
                 ;; lecture 4:
                 [nstools "0.2.4"]
                 [clojure-csv/clojure-csv "2.0.1"]
                 [org.nfrac/cljbox2d.testbed "0.5.0"]
                 [org.nfrac/cljbox2d "0.5.0"]
                 [org.clojure/data.priority-map "0.0.7"]
                 [net.mikera/core.matrix "0.33.2"]
                 [net.mikera/core.matrix.stats "0.5.0"]
                 [net.mikera/vectorz-clj "0.29.0"]
                 ]
  :main ^:skip-aot lecture4.core
  :plugins [[org.clojars.benfb/lein-gorilla "0.6.0"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
