(defproject clj-bosonnlp "0.1.1"
  :description "A clojure wrapper of Boson NLP restful API."
  :url "http://github.com/m00nlight/clj-bosonnlp"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.5"]
                 [clj-http "1.0.1"]]
  :scm {:name "git"
        :url "http://www.eclipse.org/legal/epl-v10.html"}
  :signing {:gpg-key "dot_wangyushi@yeah.net"}
  :deploy-repositories [["clojars" {:creds :gpg}]])
