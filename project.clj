(defproject viebel/webaudio "0.0.3"
  :description "Web Audio API (and more) in cljs"
  :url "https://github.com/viebel/webaudio"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2202"]
                 [im.chit/purnam "0.4.3"]
                 [org.clojure/core.async "0.1.278.0-76b25b-alpha"]]
  :plugins [[lein-cljsbuild "1.0.3"]]
  :source-paths ["src/cljs"]
  :cljsbuild {
              :builds
              {:dev {:source-paths ["src/cljs"]
                     :compiler
                     {:output-to "resources/public/js/main.js"
                      :optimizations :simple
                      :pretty-print true}}}})
