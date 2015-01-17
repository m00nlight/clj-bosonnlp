(ns clj-bosonnlp.core
  "Clojure wrapper for the boson NLP restful API. "
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as s]))

(defonce ^:private token (atom nil))
(defonce ^:private endpoint "http://api.bosonnlp.com/")


(defn initialize
  "Initialize the API token."
  [^String t]
  (reset! token t))


(defn- combine-arguments
  "Combine the arguments to form http request arguments."
  [args]
  (letfn [(f2 [hash] (s/join "&" (map #(s/join "=" %) hash)))]
    (str "?" (s/join "&" (map (fn [x] (if (map? x) (f2 x) x)) args)))))

(defn single-api
  "Common API function for single document Boson NLP. api-name is
one of [:sentiment, :ner :depparser, :keywords :classify :suggest :tag]"
  [api-name input & more-args]
  {:pre [(some #(= api-name %) [:sentiment :ner :depparser :keywords
                                :classify :suggest :tag])]}
  (let [res (client/post
             (str endpoint (name api-name) "/" "analysis"
                  (if (not (empty? more-args))
                    (combine-arguments more-args)))
             {:headers {"Content-Type" "application/json"
                        "Accept" "application/json"
                        "X-Token" @token}
              :body (json/write-str input)})]
    (if (= (:status res) 200)
      (json/read-str (:body res))
      (throw (Exception. (:message res))))))

(defn sentiment
  "Sentiment API for sentiment analysis, input is an collection of 
text, return is a json array with each is a pair of score of the 
input text. The first is postitive score, and the second is negative,
both normalize into [0,1], and they add up to 1.
type is one of [\"auto\" \"kitchen\" \"food\" \"news\" \"weibo\"],
please see <http://docs.bosonnlp.com/sentiment.html> for details."
  ([coll] (single-api :sentiment coll))
  ([coll type] (single-api :sentiment coll type)))


(defn ner
  "Name entity recognition. Please see <http://docs.bosonnlp.com/ner.html>
for details."
  ([coll] (ner coll {"sensitivity" 3}))
  ([coll v] (single-api :ner coll {"sentivity" v})))

(defn depparser
  "Dependent parser API. Please see <http://docs.bosonnlp.com/depparser.html>
for details."
  [coll] (single-api :depparser coll))

(defn keywords
  "Keywords API. Please see <http://docs.bosonnlp.com/keywords.html> for 
details."
  ([coll] (single-api :keywords coll))
  ([coll top_k] (single-api :keywords coll {"top_k" top_k})))


(defn classify
  "News classify API. Please see <http://docs.bosonnlp.com/classify.html>
for details."
  [coll] (single-api :classify coll))


(defn suggest
  "Semantic words suggestion API. See <http://docs.bosonnlp.com/suggest.html>
for details."
  ([^String word] (single-api :suggest word))
  ([^String word top_k] (single-api :suggest word {"top_k" top_k})))

(defn tag
  "Segmentation and postag API. See <http://docs.bosonnlp.com/tag.html> for
details. Coll can be a text list or a string of text."
  [coll] (single-api :tag coll))


(defn multiple-api
  "Multiple documents asynchronous API wrapper. Multiple documents API offer
three step to get analysis done. First push contents to the Boson server, 
then start the analysis on the server, wait for the analysis finish, and 
get the result from the server. The argument more is the parameters to 
balance the analysis precision and result. It should be of a map, which 
of the type {\"alpha\" 0.7, \"beta\" 0.40}"
  [api-name step-type task-id input & more-args]
  {:pre [(some #(= api-name %) [:cluster :comments])
         (some #(= step-type %) [:push :analysis :status :result :clear])]}
  (let [res (if (= step-type :push)
              (client/post
               (str endpoint (name api-name) "/" (name step-type) task-id
                    (if (not (empty? more-args))
                      (combine-arguments more-args)))
               {:headers {"Content-Type" "application/json"
                          "Accept" "application/json"
                          "X-Token" @token}
                :body (json/write-str input)})
              (client/get
               (str endpoint (name api-name) "/" (name step-type) task-id
                    (if (not (empty? more-args))
                      (combine-arguments more-args)))
               {:headers {"Content-Type" "application/json"
                          "Accept" "application/json"
                          "X-Token" @token}}))]
    (if (= (:status res) 200)
      (json/read-str (:body res))
      (throw (Exception. (:message res))))))

(defn cluster-push
  "Push content to server for cluster analysis. Please see 
<http://docs.bosonnlp.com/cluster.html> for details. Input
coll should of the form ({'_id' 1, 'text' 'hello'}, ... ). 
User should make sure that the _id push to the same task-id 
should be different for different text."
  [coll task-id] (multiple-api :cluster :push task-id coll))

(defn cluster-analysis
  "Start cluster analysis on the server."
  ([task-id] (multiple-api :cluster :analysis task-id nil))
  ([task-id alpha beta]
     (multiple-api :cluster :analysis task-id nil
                   {"alpha" alpha, "beta" beta})))

(defn cluster-status
  "Check the status of cluster task."
  [task-id] (multiple-api :cluster :status task-id nil))

(defn cluster-result
  "Get the result of cluster result."
  [task-id] (multiple-api :cluster :result task-id nil))

(defn cluster-clear
  "Clear task result on the server."
  [task-id] (multiple-api :cluster :clear task-id nil))


(defn comments-push
  "Push contents to server for comments analysis. Please see
<http://docs.bosonnlp.com/comments.html> for details. The format
of coll is the same as cluster-push, refer to the above url for 
details."
  [coll task-id] (multiple-api :comments :push task-id coll))

(defn comments-analysis
  "Start comments analysis on the server."
  ([task-id] (multiple-api :comments :analysis task-id nil))
  ([task-id alpha beta]
     (multiple-api :comments :analysis task-id nil
                   {"alpha" alpha, "beta" beta})))

(defn comments-status
  "Check for comments analysis status."
  [task-id] (multiple-api :comments :status task-id nil))

(defn comments-result
  "Get the comments analysis result."
  [task-id] (multiple-api :comments :result task-id nil))

(defn comments-clear
  "Clear the comments analysis result on the server."
  [task-id] (multiple-api :comments :clear task-id nil))
