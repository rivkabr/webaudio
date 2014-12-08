(ns webaudio.noise
  (:require [webaudio.audio-api :as api]))

(defn create-white-noise-node 
  "Generate a 5 second white noise buffer. 
  http://chimera.labs.oreilly.com/books/1234000001552/ch06.html#s06_3"
  [context &{:keys [start?] :or {start? false}}] 
  (let [sample-rate (api/sample-rate context)
        lengthInSamples (* 5 sample-rate)
        buffer (api/create-buffer context 1 lengthInSamples sample-rate)
        node (api/create-buffer-source context)
        data (api/get-channel-data buffer 0)]
    (dotimes [i lengthInSamples]
      (aset data i (- (rand  2) 1)))
    (api/set-buffer node buffer)
    (api/set-loop node true)
    (when start?
      (api/start node))
    node))


(defn create-mult-noise-node 
  "Generate a 5 second white noise buffer. 
  http://chimera.labs.oreilly.com/books/1234000001552/ch06.html#s06_3"
  [context num-channels &{:keys [start?] :or {start? false}}] 
  (let [sample-rate (api/sample-rate context)
        lengthInSamples (* 5 sample-rate)
        buffer (api/create-buffer context 4 lengthInSamples sample-rate)
        node (api/create-buffer-source context)]
    (dotimes [j num-channels]
      (let [data (api/get-channel-data buffer j)]
        (dotimes [i lengthInSamples]
          (aset data i (- (rand  2) 1)))))
    (api/set-buffer node buffer)
    (api/set-loop node true)
    (when start?
      (api/start node))
    node))
