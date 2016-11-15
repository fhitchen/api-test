(ns api-test.mq-send)
;; Simple example of sending a message to Websphere MQ with Clojure
;;
;; Uses the Websphere MQ JMS classes (i.e. JMS-like in API, but
;; submits to a MQ queue)
;; 
;; Author:   Frederico Munoz <frederico.munoz@pt.ibm.com>
;; Date:     20-Jun-2013
;; Keywords: mq, websphere, messaging, jms
;;
;; Copying and distribution of this file, with or without modification,
;; are permitted in any medium without royalty provided the copyright
;; notice and this notice are preserved.  This file is offered as-is,
;; without any warranty.
;;
;; See http://www.ibm.com/developerworks/downloads/ws/wmq/ for all the
;; client downloads available; the following jar files should be in
;; the classpath: 
;; - com.ibm.mqjms.jar 
;; - jms.jar
;;
;; This is just a quick example that tries to keep it as simple as
;; possible; this means no error checking, catch/try, or even any
;; degree of optimisation that could make the example less obvious, no
;; checking return codes.
;;
;; Use of leiningen is recommended for any more advanced use, but
;; assuming all the jars are in the current directory the following
;; should work
;;
;; $ java -cp clojure.jar:com.ibm.mqjms.jar:jms.jar clojure.main mqm.clj
;; Sat Jun 22 17:53:49 WEST 2013 : Sent 
;;   JMSMessage class: jms_text
;;   JMSType:          null
;;   JMSDeliveryMode:  2
;;   JMSExpiration:    0
;;   JMSPriority:      4
;;   JMSMessageID:     ID:414d5120494f432e4d422e514d202020ad7cc45102c30f20
;;   JMSTimestamp:     1371920029641
;;   JMSCorrelationID: null
;;   JMSDestination:   queue:///PT.TEST.IN
;;   JMSReplyTo:       null
;;   JMSRedelivered:   false
;;     JMSXAppID: WebSphere MQ Client for Java
;;     JMSXDeliveryCount: 0
;;     JMSXUserID: mqmconn     
;;     JMS_IBM_PutApplType: 28
;;     JMS_IBM_PutDate: 20130622
;;     JMS_IBM_PutTime: 16534966
;; My soul is like a shepherd. It knows wind and sun, walking hand in hand with the Seasons
;; Delivered to queue:///PT.TEST.IN
;; $

(import 'com.ibm.jms.JMSMessage)
(import 'com.ibm.jms.JMSTextMessage)
(import 'com.ibm.mq.jms.JMSC)
(import 'com.ibm.mq.jms.MQQueue)
(import 'com.ibm.mq.jms.MQQueueConnection)
(import 'com.ibm.mq.jms.MQQueueConnectionFactory)
(import 'com.ibm.mq.jms.MQQueueReceiver)
(import 'com.ibm.mq.jms.MQQueueSender)
(import 'com.ibm.mq.jms.MQQueueSession)
(import 'javax.jms.Session)


;; Main variables, most likely candidates for additional command line arguments
;; Change them according to your environment (the message can be supplied in the cli)
(def mq-qm "QM1")             ; MQ queue manager name
(def mq-channel "SYSTEM.ADMIN.SVRCONN")  ; MQ channel name
(def mq-port 32768)                  ; MQ port
(def mq-queue "REQUEST_QUEUE")       ; MQ queue name
(def mq-host "cmildev136")          ; MQ server
(def mq-message (if (seq (first *command-line-args*)) ; Message, either the first 
                  (first *command-line-args*)         ; command line argument of a snippet from Pessoa's
                  "My soul is like a shepherd. It knows wind and sun, walking hand in hand with the Seasons"))

(defn- mq-connection-factory
  "Sets and returns a CONNECTION-FACTORY using the provided options"
  [{:keys [host port transport qm channel]}]
  (let [connection-factory (MQQueueConnectionFactory.)]
    (doto connection-factory
      (.setHostName host)
      (.setPort port)
      (.setTransportType transport)
      (.setQueueManager qm)
      (.setChannel channel))
    connection-factory))

(defn- mq-send-message [connection-factory queue message]
  "Sends MESSAGE, a string, to the QUEUE using CONNECTION-FACTORY"
  (let [conn (.createQueueConnection connection-factory)
        session (.createQueueSession conn false Session/AUTO_ACKNOWLEDGE)
        queue (.createQueue session (str "queue:///" queue))
        sender (.createSender session queue)
        message (.createTextMessage session message)]
    (.start conn)
    (.send sender message)
    (.close sender)
    (.close conn)
    ;(println (str (java.util.Date.) " : Sent message follows" message
    ;"\nDelivered to " queue))
    ))

(defn- mq-receive-message [connection-factory queue]
  "Sends MESSAGE, a string, to the QUEUE using CONNECTION-FACTORY"
  (let [conn (.createQueueConnection connection-factory)
        session (.createQueueSession conn false Session/AUTO_ACKNOWLEDGE)
        queue (.createQueue session (str "queue:///" queue))
        receiver (.createReceiver session queue) 
        message (.createTextMessage session "dummy")]
    (.start conn)
    (let [message (.receive receiver 5000)]
      (.close receiver)
      (.close conn)
      ;(println (str (java.util.Date.) " : Reveived message " message "\nfrom " queue))
      message)))

(defn mq-send
  "Function to send message"
  [message & args]
  (let [mq-connection (mq-connection-factory {:host mq-host
                                              :port mq-port
                                              ;:transport JMSC/MQJMS_TP_CLIENT_MQ_TCPIP
                                              :transport JMSC/MQJMS_TP_CLIENT_MQ_TCPIP
                                              :qm mq-qm
                                              :channel mq-channel})]
    (mq-send-message mq-connection mq-queue message)))

(defn mq-receive
  "Function to receive message"
  [& args]
  (let [mq-connection (mq-connection-factory {:host mq-host
                                              :port mq-port
                                              ;:transport JMSC/MQJMS_TP_CLIENT_MQ_TCPIP
                                              :transport JMSC/MQJMS_TP_CLIENT_MQ_TCPIP
                                              :qm mq-qm
                                              :channel mq-channel})]
    (mq-receive-message mq-connection "RESPONSE_QUEUE")))


;; Just send it already
;(mq-example)
;(mq-receive)





;;;; End of file
