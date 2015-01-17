(ns clj-bosonnlp.core-test
  (:require [clojure.test :refer :all]
            [clj-bosonnlp.core :refer :all]
            [clj-bosonnlp.test-utils :as tu]))


(tu/with-private-fns [clj-bosonnlp.core [combine-arguments]]
  (deftest test-private-fn-combine-argument
    (testing "Testing combine argument"
      (is (= "?auto"
             (combine-arguments ["auto"])))
      (is (= "?top_k=10&segmented"
             (combine-arguments [{"top_k" 10} "segmented"])))
      (is (= "?top_k=10"
             (combine-arguments [{"top_k" 10}])))
      (is (= "?alpha=0.8&beta=0.4"
             (combine-arguments [{"alpha" 0.8 "beta" 0.4}]))))))


(deftest test-invalid-token
  (testing "Testing invalid token should raise Exception."
    (initialize "invalid")
    (is (thrown? Exception (sentiment ["hello world"])))))
