(defn =count? [xs] (apply = (mapv count xs)))

(defn vv [f] (fn [& xs] {:pre [(=count? xs)]}
               (apply mapv f xs)))
(defn mm [f] (fn [& xs] {:pre [(=count? xs) (every? =count? xs)]}
               (apply mapv (vv f) xs)))

(def v+ (vv +))
(def v- (vv -))
(def v* (vv *))
(defn scalar [a b] (reduce + (v* a b)))
(defn vect [a b] (vector (- (* (get a 1) (get b 2)) (* (get a 2) (get b 1)))
                         (- (* (get a 2) (get b 0)) (* (get a 0) (get b 2)))
                         (- (* (get a 0) (get b 1)) (* (get a 1) (get b 0)))))
(defn v*s [v & xs] (mapv #(* % (reduce * xs)) v))

(def m+ (mm +))
(def m- (mm -))
(def m* (mm *))
(defn m*s [m & xs] (mapv #(apply v*s % xs) m))
(defn m*v [m v] (mapv #(scalar v %) m))
(defn transpose [m] (apply mapv vector m))
(defn m*m [a b] (mapv #(m*v (transpose b) %) a))