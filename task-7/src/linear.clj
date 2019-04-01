(defn =count?
  ([xs] (apply = (mapv count xs)))
  ([count xs] (apply #(= count %) (mapv count xs))))
(defn cyclic-shift [v shift] (subvec (vec (reduce concat (repeat 2 v)))
                                     (mod shift (count v))
                                     (+ (mod shift (count v)) (count v))))

(defn vv [f] (fn [& xs] {:pre [(=count? xs)]}
               (apply mapv f xs)))
(defn mm [f] (fn [& xs] {:pre [(=count? xs) (every? =count? xs)]}
               (apply mapv (vv f) xs)))

(def v+ (vv +))
(def v- (vv -))
(def v* (vv *))
(defn scalar [a b] (reduce + (v* a b)))
(defn vect
  ([a] (vec a))
  ([a b] {:pre [(= 3 (count a) (count b))]} (cyclic-shift (v- (v* a (cyclic-shift b 1)) (v* (cyclic-shift a 1) b)) 1))
  ([a b & xs] (reduce vect (vect a b) xs)))
(defn v*s [v & xs] (mapv #(* % (reduce * xs)) v))

(def m+ (mm +))
(def m- (mm -))
(def m* (mm *))
(defn m*s [m & xs] (mapv #(apply v*s % xs) m))
(defn m*v [m v] (mapv #(scalar v %) m))
(defn transpose [m] (apply mapv vector m))
(defn m*m
  ([a] a)
  ([a b] (mapv #(m*v (transpose b) %) a))
  ([a b & xs] (reduce m*m (m*m a b) xs)))