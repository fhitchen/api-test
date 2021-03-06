Feature: Create Prepaid BAN followed by Subscriber

  Background:   
     Given the message "create_ban.xml" with the following values:
     
     | tag                                                                   | content                   |
     | "NXHeader>ServiceName"                                                | "AMD.ENSEMBLE.CREATE_BAN" |
     | "NXHeader>ApplRef"                                                    | "$ApplRef"                |
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
     | "createBan>banValueObjInfo>billingAddressInfo>number"                 | "2109"                    |
     | "createBan>banValueObjInfo>billingAddressInfo>streetName"             | "fox drive"               |
     | "createBan>banValueObjInfo>billingAddressInfo>city"                   | "Champaign"               |
     | "createBan>banValueObjInfo>billingAddressInfo>state"                  | "IL"                      |
     | "createBan>banValueObjInfo>billingAddressInfo>country"                | "USA"                     |
     | "createBan>banValueObjInfo>billingAddressInfo>zipCode"                | "61820"                   |
     | "createBan>banValueObjInfo>contactNameInfo>first"                     | "Boost"                   |
     | "createBan>banValueObjInfo>contactNameInfo>last"                      | "Apiauto"                 |
     | "createBan>banValueObjInfo>billingNameInfo>first"                     | "Boost"                   |
     | "createBan>banValueObjInfo>billingNameInfo>last"                      | "Apiauto"                 |
     | "createBan>banValueObjInfo>banBillInfo>billFormatType"                | "C"                       |
     | "createBan>banValueObjInfo>securityInfo>pin"                          | "1234"                    |
     | "createBan>applicationDataInfo>applicationID"                         | "API"                     |

   Scenario:
     Given we send the message 
     When we receive the good response
     Then the message header response value named "NXHeader>ReplyCompCode" must equal "0" 
     And the message header response value named "NXHeader>ServiceName" must equal "AMD.ENSEMBLE.CREATE_BAN"
     And the message body response value named "createBan>CreateBanRespInfo>banId" should be 9 characters long
     Then store the "createBan>CreateBanRespInfo>banId" value in variable "$StoredBoostBan"

   Scenario:
     Given we send the message with the following modified values:

     | tag                                                                   | content                   |
     | "createBan>banValueObjInfo>banDetailsInfo>ssn"                        | "AAAAAAAAA"               |
     
     When we receive the bad response
     Then the message header response value named "NXHeader>ReplyCompCode" must equal "8"
     And the message header response value named "NXHeader>ServiceName" must equal "AMD.ENSEMBLE.CREATE_BAN"
     And the response message tag named "exception" contains the text "XML Parsing error"
     
   Scenario:
     Given we send the message with the following modified values:

     | tag                                                                   | content                   |
     | "createBan>banValueObjInfo>banDetailsInfo>ssn"                        | "999999998"               |
     
     When we receive the bad response
     Then the message header response value named "NXHeader>ReplyCompCode" must equal "8" 
     And the message header response value named "NXHeader>ServiceName" must equal "AMD.ENSEMBLE.CREATE_BAN"
     And the response message tag named "createBan>message" contains the text "An individual account cannot share an SSN with another BAN."

    Scenario:
     Given the message "9034_CrReservedSub_boost_PAYGO.xml" with the following values:

     | tag                                                                   | content                   |
        | "NXHeader>Version" | "Hilik CR566 Step1" |
        | "NXHeader>ServiceName" | "AMD.ENSEMBLE.ADD_SUB_INFO" |
        | "NXHeader>ApplRef" | "$ApplRef" |
        | "createSubscriber>banInfo>banId" | "$StoredBoostBan" |
        | "createSubscriber>subscriberApiInfo>networkInfo>networkInd" | "C" |
        | "createSubscriber>subscriberApiInfo>resourceApiInfo>ptnInfo>numberLocation" | "REG" |
        | "createSubscriber>subscriberApiInfo>resourceApiInfo>ptnInfo>ptnType" | "RGL" |
        | "createSubscriber>subscriberApiInfo>resourceApiInfo>ptnInfo>method" | "R" |
        | "createSubscriber>subscriberApiInfo>resourceApiInfo>npaNxxInfo>npa" | "303" |
        | "createSubscriber>subscriberApiInfo>resourceApiInfo>npaNxxInfo>nxx" | "330" |
        | "createSubscriber>subscriberApiInfo>resourceApiInfo>naiInfo>userName" | "BSTPAYGO" |
        | "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>pricePlanSocCode" | "PGCDM1090" |
        | "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "CHSDA>CHSDA" |
        | "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "CLIP>CLIP" |
        | "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "HLT>HLT" |
        | "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "INT>INT" |
        | "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "SMS>SMS" |
        | "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "STD>STD" |
        | "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "TWAYR>TWAYR" |
        | "createSubscriber>subscriberApiInfo>newPricePlanInfo>newPpSocInfo>featureApiInfoList>FeatureApiInfo>featureCode" | "UNTETH>UNTETH" |
        | "createSubscriber>subscriberApiInfo>equipmentApiInfoList>EquipmentApiInfo>serial" | "$ESN" |
        | "createSubscriber>subscriberApiInfo>equipmentApiInfoList>EquipmentApiInfo>serialType" | "E" |
        | "createSubscriber>subscriberApiInfo>equipmentApiInfoList>EquipmentApiInfo>activateInd" | "Y" |
        | "createSubscriber>subscriberApiInfo>memoInfo>memoSource" | "C" |
        | "createSubscriber>subscriberApiInfo>activationPINInfo>reactivationInd" | "Y" |
        | "createSubscriber>subscriberApiInfo>prePaidPlanPolicyInfo>policyInd" | "Y" |
        | "createSubscriber>subscriberApiInfo>prePaidPlanPolicyInfo>overrideInd" | "Y" |
        | "createSubscriber>applicationDataInfo>applicationID" | "API" |

     Given we send the message
     When we receive the good response
     Then the message header response value named "NXHeader>ReplyCompCode" must equal "0" 


     


# ApplRef must match
