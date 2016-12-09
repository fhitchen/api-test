(use 'api-test.core)
(use 'clojure.test)
(use 'api-test.mq-send)
(use 'api-test.xml)


(Before []
        (update-stored-value {(keyword "$ESN") (generate-esn)}))

(Given #"^the message \"([^\"]*)\" with the following values:$" [message-file data]
       (reset! x-values (table->rows data))
       ;(println (pr-str "x-values= " (table->rows data)))
       (reset! message (set-values (replace-variables (table->rows data)) (slurp (str "resources/messages/" message-file)))))

(Given #"^we send the message$" []
       (mq-send @message))

(Given #"^we send the message with the following modified values:$" [data]
       (let [m (set-values (replace-variables (table->rows data)) @message)]
         (mq-send m)))

(When #"^we receive the bad response$" []
      (reset! response (mq-receive))
      ;(println (.getText @response))
      (assert (not= nil @response)))

(When #"^we receive the good response$" []
      (reset! response (mq-receive))
      (assert (not= nil @response) "We didn't get a response")
      (let [m (get-value "createBan>message" (.getText @response))]
        (assert (= nil m) (str "Unexpected error message returned: " m))))



(Then #"^the message header response value named \"([^\"]*)\" must equal \"([^\"]*)\"$" [arg1 arg2]
      (let [value (get-value arg1 (.getText @response))]
        (assert (= (str arg2) value))))

(Then #"^the message body response value named \"([^\"]*)\" should be (\d+) characters long$" [arg1 length]
      (let [value (get-value arg1 (.getText @response))]
        (assert (= (Integer/parseInt length) (count value)))))

(Then #"^the response message tag named \"([^\"]*)\" contains the text \"([^\"]*)\"$" [tag value]
      (assert (not= nil (re-find (re-pattern value) (get-value tag (.getText @response))))))

(Then #"^store the \"([^\"]*)\" value in variable \"([^\"]*)\"$" [tag variable-name]
      (update-stored-value {(keyword variable-name) (get-value tag (.getText @response))}))

(Given #"^message add-subscriber\.xml$" []
  (comment  Write code here that turns the phrase above into concrete actions  )
  (throw (cucumber.api.PendingException.)))
