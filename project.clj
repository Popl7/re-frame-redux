(defproject org.clojars.unrealistic/re-frame-redux "0.1.0-SNAPSHOT"
  :description "Small library to couple the re-frame database to the redux devtools. Works in 2 directions, e.g. with time travelling."
  :url "https://gitlab.com/StevenT/redux-frame-devtools"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"
            :key "mit"
            :year 2019}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.520"]
                 [re-frame "0.10.6"]]
  :plugins [[lein-cljsbuild "1.1.7"]]
  :cljsbuild
  {:builds {:minify {:source-paths ["src"]
                     :compiler {:optimizations :advanced
                                :pretty-print false}}
            :dev {:source-paths ["src"]
                  :compiler {:optimizations :whitespace}}}})
