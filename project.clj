(defproject contacts "0.1.0-SNAPSHOT"
  :description "Application to add and display contacts"
  :url "http://localhost"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.json "1.0.0"]
                 [compojure "1.6.2"]
                 [ring/ring-core "1.9.0"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-devel "1.9.0"]
                 [ring/ring-jetty-adapter "1.9.0"]
                 [ring-middleware-format "0.7.4"]
                 [ring/ring-mock "0.4.0"]]
  :main ^:skip-aot main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
