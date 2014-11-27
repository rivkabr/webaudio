(ns webaudio.utils)

(defmacro doseq-indexed "https://gist.github.com/halgari/4136116" [index-sym [item-sym coll] & body]
    `(doseq [[~item-sym ~index-sym]
                        (map vector ~coll (range))]
              ~@body))
