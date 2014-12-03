(ns webaudio.audio-api
  (:use-macros [webaudio.utils :only [doseq-indexed]]
               [purnam.core :only [? ! obj !>]])
  (:require [webaudio.math :as audio]
            [webaudio.utils :refer [fill-js-array]]
            [cljs.core.async :refer [<! put! close! chan timeout]]))

(defn create-context []
  (let [context (js/AudioContext.)]
      (! context.destination.channelCount (? context.destination.maxChannelCount))
    context))

(def speakers ["L" "R" "C" "LFE" "SL" "SR"])

(defn sample-rate [context]
    (? context.sampleRate))

(defn create-gain [context]
  (!> context.createGain))

(defn create-gains [context n]
    (repeatedly 2 (partial create-gain context)))

(defn num-of-speakers [context]
  (? context.destination.maxChannelCount))

(defn name-of-speakers [context]
  (take (num-of-speakers context) speakers))

(defn set-gain 
  ([node value] (! node.gain.value value))
  ([node value rampup-duration-in-sec]
   (!> node.gain.setValueAtTime (? node.gain.value) (? node.context.currentTime))
   (!> node.gain.linearRampToValueAtTime value (+ rampup-duration-in-sec (? node.context.currentTime)))
   node))

(defn set-volume 
  ([node volume] (set-volume node volume 0))
  ([node volume delay-in-sec]
   (set-gain node (audio/db-to-gain volume) delay-in-sec)))

(defn set-volume-speakers 
  ([node volume num-speakers rampup-duration-in-sec] (set-volume node (audio/volume-splitted volume num-speakers) rampup-duration-in-sec))
  ([node volume num-speakers] (set-volume-speakers node volume num-speakers 0)))

(defn pcm->buffer [context pcm-js-array [start stop] sample-rate]
  (let [buffer (!> context.createBuffer 1 (- stop start) sample-rate)]
    (fill-js-array (!> buffer.getChannelData 0) pcm-js-array start stop)
    buffer))

(defn stereo->4chan [context buff-stereo]
  (let [sample-rate (? buff-stereo.sampleRate)
        pcm-left (!> buff-stereo.getChannelData 0)
        pcm-right (!> buff-stereo.getChannelData 1)
        buffer (!> context.createBuffer 4 (alength pcm-left) sample-rate)]
    (fill-js-array (!> buffer.getChannelData 0) pcm-left)
    (fill-js-array (!> buffer.getChannelData 2) pcm-left)
    (fill-js-array (!> buffer.getChannelData 1) pcm-right)
    (fill-js-array (!> buffer.getChannelData 3) pcm-right)
    buffer))

(defn create-node-with-buffer [context buffer]
  (let [node (!> context.createBufferSource)]
    (! node.buffer buffer)))

(defn decode-data [context data]
  (let [c (chan)]
    (try
      (!> context.decodeAudioData data
          #(put! c [:ok %])
          #(put! c [:error %]))
      (catch js/Object e
        (put! c [:error e])))
      c))

(defn stop [node
            & {:keys [delay-in-sec] :or {delay-in-sec 0}}]
  (!> node.stop (+ delay-in-sec (? node.context.currentTime)))
  node)

(defn start [node 
             & {:keys [offset-in-sec delay-in-sec] :or {offset-in-sec 0 delay-in-sec 0}}]
  (!> node.start (+ delay-in-sec (? node.context.currentTime)) offset-in-sec)
  node)

(defn on-ended [node f]
  (! node.onended f)
  node)

(defn connect 
  ([a b] (!> a.connect b) a)
  ([a b c d] (!> a.connect b c d) a))

(defn connect-to-destination [node]
  (connect node (? node.context.destination)))

(defn connect-node-to-nodes [node nodes]
  (doseq-indexed i [n nodes]
    (connect node n i 0)))


(defn disconnect 
  ([a] (!> a.disconnect))
  ([a i] (!> a.disconnect i)))

(defn disconnect-several [node n]
  (doseq [i (range n)]
    (disconnect node i)))
