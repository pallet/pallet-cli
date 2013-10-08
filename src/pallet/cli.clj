(ns pallet.cli
  "An extensible cli for pallet.")

(defn -main
  "Pallet command line interface"
  [& args]
  (apply println "Hello, World!" args))
