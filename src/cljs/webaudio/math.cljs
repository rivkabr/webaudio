(ns webaudio.math)

(defn log[x]
         (.log js/Math x)
                    )

(defn pow[a b]
         (.pow js/Math a b)
                      )

(defn sqrt [x]
  (pow x 0.5))

(def log10-const
         (.-LN10 js/Math)
                )

(defn log10 [x]
  (/ (log x) log10-const))

(defn db-to-gain[db]
    (pow 10 (/ db 20)))

(defn gain-to-db[gain]
  (* 20 (log10 gain)))

(defn to-sec [x] (/ x 1000))
(defn to-msec [x] (* x 1000))

(defn volume-splitted [volume num-of-speakers]
  (- volume (* 20 (log10 (sqrt num-of-speakers)))))

(defn fft-frequencies[sampling-rate fftSize]
  (range 0 (/ sampling-rate 2) (/ sampling-rate fftSize)))

(defn freq-min [sampling-rate fftSize]
  (/ sampling-rate fftSize))
