(ns webaudio.utils)

(defn fill-js-array
  ([js-a js-b] (fill-js-array js-a js-b 0 (alength js-b)))
  ([js-a js-b start stop]
   (loop [i start]
     (when (< i stop)
       (aset js-a (- i start) (aget js-b i))
       (recur (inc i))))))
