(ns webaudio.aurora
  (:use-macros [purnam.core :only [? ! def.n obj !>]])
  (:require [cljs.core.async :refer [<! put! close! chan timeout]]))

(defn to-regular-array[arr]
    (IndexedSeq. arr 0))

(defn decode-data [data]
  (let [c (chan)
        asset (!> js/AV.Asset.fromBuffer data)]
    (!> asset.on "error" (fn [e]
                           (print "error:" e)
                           (close! c)))
    (!> asset.on "format"
        (fn [the-format]
          (!> asset.decodeToBuffer (fn [data]
                                     (put! c [(? the-format.sampleRate) (to-regular-array data) data])))))
    (!> asset.start)
    c))
