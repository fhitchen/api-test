(ns api-test.xml-test
  (:require [clojure.test :refer :all]
            [api-test.core :refer :all]
            [api-test.mq-send :refer :all]
            [api-test.xml :refer :all]))

(def test-response "<?xml version='1.0'?><response><NXHeader><Version></Version><ServiceName>AMD.ENSEMBLE.CREATE_BAN</ServiceName><ApplRef>1477923078_BOOST_BAN</ApplRef><ReplyCompCode>0</ReplyCompCode></NXHeader><header></header><createBan><CreateBanRespInfo><banId>754776797</banId></CreateBanRespInfo></createBan></response>")

(def test-values (lazy-seq '({:content "888888888", :tag "createBan>CreateBanRespInfo>banId"} {:content "1142664520", :tag "NXHeader>ReplyCompCode"})))

(def bad-test-values (lazy-seq '({:content "888888888", :tag "CreateBan>CreateBanRespInfo>banId"} {:content "1142664520", :tag "NXHeader>ReplyCompCode"})))

(def two-bad-test-values (lazy-seq '({:content "888888888", :tag "CreateBan>CreateBanRespInfo>banId"} {:content "1142664520", :tag "NXHeader>rReplyCompCode"})))


(deftest get-header
  (testing "getting a value out of the NXHeader"
    (is (= "0" (get-value "NXHeader>ReplyCompCode" test-response)))))

(deftest get-value-1
  (testing "getting a value"
    (is (= "AMD.ENSEMBLE.CREATE_BAN"  (get-value "NXHeader>ServiceName" test-response)))))

(deftest get-value-2
  (testing "getting a value"
    (is (= "754776797"  (get-value "createBan>CreateBanRespInfo>banId" test-response)))))

(deftest get-value-3
  (testing "getting a value thats not there"
    (is (not= "754776797"  (get-value "CreateBan>CreateBanRespInfo>banId" test-response)))))

(deftest set-value
  (testing "setting a value in a message"
    (let [message (set-values test-values test-response)]
      (is (and
           (= "888888888" (get-value "createBan>CreateBanRespInfo>banId" message))
           (= "1142664520" (get-value "NXHeader>ReplyCompCode" message)))))))

(deftest set-value-1
  (testing "setting a value in a message with one bad line"
    (let [message (set-values bad-test-values test-response)]
      (is (and
           (not= "888888888" (get-value "createBan>CreateBanRespInfo>banId" message))
           (= "1142664520" (get-value "NXHeader>ReplyCompCode" message)))))))


(deftest set-value-2
  (testing "setting a value in a message with two bad lines"
    (let [message (set-values two-bad-test-values test-response)]
      (is (and
           (not= "888888888" (get-value "createBan>CreateBanRespInfo>banId" message))
           (not= "1142664520" (get-value "NXHeader>ReplyCompCode" message)))))))

