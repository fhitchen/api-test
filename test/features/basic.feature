Feature: Create Boost BAN followed by Subscriber

  Background:   
     Given the message "create_ban.xml" with the following values:
     
     | tag                                                                   | content                   |
     | "NXHeader>ServiceName"                                                | "AMD.ENSEMBLE.CREATE_BAN" |
     | "NXHeader>ApplRef"                                                    | "1477923078_BOOST_BAN"    |
     | "createBan>banValueObjInfo>banTypeInfo>banType"                       | "I"                       |
     | "createBan>banValueObjInfo>banTypeInfo>banSubType"                    | "C"                       |
     | "createBan>banValueObjInfo>banTypeInfo>serviceZipCode"                | "61820"                   |
     | "createBan>banValueObjInfo>banTypeInfo>equipmentBanInd"               | "false"                   |
     | "createBan>banValueObjInfo>subMarketInfo>defaultSubMarket"            | "NOH"                     |
     | "createBan>banValueObjInfo>dealerAgentInfo>dealerAgent"               | "ACDADQ9X"                |
     | "createBan>banValueObjInfo>banDetailsInfo>businessEntity"             | "NTH"                     |
     | "createBan>banValueObjInfo>banDetailsInfo>homeNumberInfo>phoneNumber" | "4568564589"              |
     | "createBan>banValueObjInfo>banDetailsInfo>prefLangInd"                | "SPA"                     |
     | "createBan>banValueObjInfo>banDetailsInfo>communicationMethod"        | "E"                       |
     | "createBan>banValueObjInfo>banDetailsInfo>communicationMethodValue"   | "apiBoost@amdocs.com"     |
     | "createBan>banValueObjInfo>banDetailsInfo>ssn"                        | "999999999"               |
     | "createBan>banValueObjInfo>invoiceDetailsInfo>deliveryMethod"         | "P"                       |
     | "createBan>banValueObjInfo>invoiceDetailsInfo>emailRelatedCode"       | "N"                       |
     | "createBan>banValueObjInfo>banIndividualInfo>driverLicenseNumber"     | "1477923078"              |
     | "createBan>banValueObjInfo>banIndividualInfo>driverLicenseState"      | "AL"                      |
     | "createBan>banValueObjInfo>banIndividualInfo>driverLicenseExpires"    | "10/09/2026"              |
     | "createBan>banValueObjInfo>banIndividualInfo>dateOfBirth"             | "03/10/1985"              |
     | "createBan>banValueObjInfo>banIndividualInfo>employer"                | "NXTL"                    |
     | "createBan>banValueObjInfo>banIndividualInfo>employeePosition"        | "programmer"              |
     | "createBan>banValueObjInfo>banIndividualInfo>employeeHireDate"        | "09/10/2006"              |
     | "createBan>banValueObjInfo>billingAddressInfo>applyAddress"           | "S"                       |
     | "createBan>banValueObjInfo>billingAddressInfo>type"                   | "S"                       |
     | "createBan>banValueObjInfo>billingAddressInfo>streetName"             | "fox drive"               |
     | "createBan>banValueObjInfo>billingAddressInfo>city"                   | "Champaign"               |
     | "createBan>banValueObjInfo>billingAddressInfo>state"                  | "IL"                      |
     | "createBan>banValueObjInfo>billingAddressInfo>country"                | "USA"                     |
     | "createBan>banValueObjInfo>billingAddressInfo>zipCode"                | "61820"                   |
     | "createBan>banValueObjInfo>billingAddressInfo>number"                 | "2109"                    |
     | "createBan>banValueObjInfo>contactNameInfo>first"                     | "Boost"                   |
     | "createBan>banValueObjInfo>contactNameInfo>last"                      | "Apiauto"                 |
     | "createBan>banValueObjInfo>billingNameInfo>first"                     | "Boost"                   |
     | "createBan>banValueObjInfo>billingNameInfo>last"                      | "Apiauto"                 |
     | "createBan>banValueObjInfo>banBillInfo>billFormatType"                | "C"                       |
     | "createBan>banValueObjInfo>securityInfo>pin"                          | "1234"                    |
     | "createBan>applicationDataInfo>applicationID"                         | "API"                     |

   Scenario:
     Given we send the message 
     When we receive the response
     Then the message header response value named "NXHeader>ReplyCompCode" must equal "0" 
     And the message header response value named "NXHeader>ServiceName" must equal "AMD.ENSEMBLE.CREATE_BAN"
     And the message body response value named "createBan>CreateBanRespInfo>banId" should be 9 characters long
     Then store the "createBan>CreateBanRespInfo>banId" value in variable "$StoredBoostBan"

   Scenario:
     Given we send the message with the following modified values:

     | tag                                                                   | content                   |
     | "createBan>banValueObjInfo>banDetailsInfo>ssn"                        | "AAAAAAAAA"               |
     
     When we receive the response
     Then the message header response value named "NXHeader>ReplyCompCode" must equal "8"
     And the message header response value named "NXHeader>ServiceName" must equal "AMD.ENSEMBLE.CREATE_BAN"
     And the response message tag named "exception" contains the text "XML Parsing error"
     
   Scenario:
     Given we send the message with the following modified values:

     | tag                                                                   | content                   |
     | "createBan>banValueObjInfo>banDetailsInfo>ssn"                        | "999999998"               |
     
     When we receive the response
     Then the message header response value named "NXHeader>ReplyCompCode" must equal "8" 
     And the message header response value named "NXHeader>ServiceName" must equal "AMD.ENSEMBLE.CREATE_BAN"
     And the response message tag named "createBan>message" contains the text "An individual account cannot share an SSN with another BAN."
     
     Given the message "create-subscriber.xml" with the following values:

     | tag                                                                   | content                   |
     | "NXHeader>Version" | "0102" |
     | "NXHeader>ServiceName" | "AMD.ENSEMBLE.ADD_SUB_INFO" |
     | "NXHeader>ServiceVer" | "0102" |
     | "NXHeader>DialogType" | "2" |
     | "NXHeader>DialogRef" | "4BN" |
     | "NXHeader>ApplRef" | "4BN" |
     | "NXHeader>ApplGroup" | "4BN" |
	| "NXHeader>ContentType" | "XML" |
	| "NXHeader>ReqSentTime" | "2016/09/09 00:58:52" |
	| "createSubscriber>banInfo>banId" | "$StoredBoostBan" |
	| "createSubscriber>subscriberApiInfo>networkInfo>networkInd" | "C" |
	| "createSubscriber>subscriberApiInfo>resourceApiInfo>ptnInfo>numberLocation" | "REG" |
	| "createSubscriber>subscriberApiInfo>resourceApiInfo>ptnInfo>ptnType" | "RGL" |
	| "createSubscriber>subscriberApiInfo>resourceApiInfo>ptnInfo>method" | "R" |
	| "createSubscriber>subscriberApiInfo>resourceApiInfo>npaNxxInfo>npa" | "303" |
	| "createSubscriber>subscriberApiInfo>resourceApiInfo>npaNxxInfo>ngp" | "SFRCBL408" |
	| "createSubscriber>subscriberApiInfo>resourceApiInfo>naiInfo>naiInd" | "Y" |
	| "createSubscriber>subscriberApiInfo>subAdditionalInfo>dealerAgent" | "ZEABV00X" |
	| "createSubscriber>subscriberApiInfo>subAdditionalInfo>oarssInd" | "false" |
	| "createSubscriber>subscriberApiInfo>ldcInfo>ldcCode" | "NXTL" |
	| "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>pricePlanSocCode" | "MUCX1350" |
	| "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "CHSDA" |
	| "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "CLIP" |
	| "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "HLT" |
	| "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "INT" |
	| "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "PM" |
	| "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "SMS" |
	| "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "STD" |
	| "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "UNTETH" |
	| "createSubscriber>subscriberApiInfo>newPricePlanInfo>otherSocInfoList>SocInfo>pricePlanSocCode" | "VMENGLISH" |
	| "createSubscriber>subscriberApiInfo>newPricePlanInfo>otherSocInfoList>SocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "VMC9" |
	| "createSubscriber>subscriberApiInfo>equipmentApiInfoList>EquipmentApiInfo>itemId" | "AL2017BAVB" |
	| "createSubscriber>subscriberApiInfo>equipmentApiInfoList>EquipmentApiInfo>serial" | "270113185012770329" |
	| "createSubscriber>subscriberApiInfo>equipmentApiInfoList>EquipmentApiInfo>serialType" | "E" |
	| "createSubscriber>subscriberApiInfo>equipmentApiInfoList>EquipmentApiInfo>activateInd" | "Y" |
	| "createSubscriber>subscriberApiInfo>memoInfo>memoSource" | "F" |
	| "createSubscriber>applicationDataInfo>applicationID" | "FAST" |

     Given we send the message
     When we receive the response
     Then the message header response value named "NXHeader>ReplyCompCode" must equal "8" 


     


# ApplRef must match
