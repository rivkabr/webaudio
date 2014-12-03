(ns webaudio.aurora
  (:use-macros [purnam.core :only [? ! def.n obj !>]])
  (:require [cljs.core.async :refer [<! put! close! chan timeout]]))

(defn to-regular-array[arr]
    (IndexedSeq. arr 0))

(defn decode-data [data]
  (let [c (chan)]
    (try
      (let  [asset (!> js/AV.Asset.fromBuffer data)]
        (!> asset.on "error" (fn [e]
                              (put! c [:error e])))
        (!> asset.on "format"
         (fn [the-format]
            (!> asset.decodeToBuffer (fn [data]
                                       (put! c [:ok [(? the-format.sampleRate) (to-regular-array data) data]])))))
        (!> asset.start))
      (catch js/Object e
        (put! c [:error (str "Error in decoding data: " e)])))
    c))
