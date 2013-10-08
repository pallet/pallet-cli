(defproject com.palletops/pallet-cli "0.9.0-SNAPSHOT"
  :description "CLI for pallet"
  :url "https://github.com/pallet/pallet-cli"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[com.palletops/pallet "0.9.0-SNAPSHOT"]
                 [nrepl-main "0.1.1"
                  :exclusions [org.clojure/clojure]]
                 [alembic "0.2.0"]
                 [org.clojure/tools.nrepl "0.2.3"]]
  :plugins [[lein-package "2.1.1"]]
  :hooks [leiningen.package.hooks.deploy
          leiningen.package.hooks.install]
  :package {:skipjar true
            :autobuild true
            :reuse false
            :artifacts [{:build "uberjar"
                         :extension "jar"
                         :classifier "standalone"}]})
