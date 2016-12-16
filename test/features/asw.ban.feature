Feature: Create ASW BAN

  Background:  
     Given the message "ASW_Create_ban_9020.xml" with the following values:
     
     | tag                                                                   | content                   |
     | "NXHeader>ServiceName"                                                | "AMD.ENSEMBLE.CREATE_BAN" |
     | "NXHeader>ApplRef"                                                    | "First ASW Message"       |
     | "createBan>banValueObjInfo>banTypeInfo>banType"                       | "I"                       |
     | "createBan>banValueObjInfo>banTypeInfo>banSubType"                    | "K"                       |
     | "createBan>banValueObjInfo>banTypeInfo>serviceZipCode"                | "61820"                   |
     | "createBan>banValueObjInfo>banTypeInfo>equipmentBanInd"               | "false"                   |
     | "createBan>banValueObjInfo>subMarketInfo>defaultSubMarket"            | "NOH"                     |
     | "createBan>banValueObjInfo>dealerAgentInfo>dealerAgent"               | "ACDADQ9X"                |
     | "createBan>banValueObjInfo>banDetailsInfo>businessEntity"             | "WST"                     |
     | "createBan>banValueObjInfo>banDetailsInfo>prefLangInd"                | "ENG"                     |
     | "createBan>banValueObjInfo>banDetailsInfo>communicationMethod"        | "L"                       |
     | "createBan>banValueObjInfo>banDetailsInfo>longDistanceCarrier"        | "NXTL"                    |
     | "createBan>banValueObjInfo>banDetailsInfo>ssn"                        | "999999999"               |
     | "createBan>banValueObjInfo>invoiceDetailsInfo>deliveryMethod"         | "E"                       |
     | "createBan>banValueObjInfo>invoiceDetailsInfo>emailAddress"           | "sb@amdocs.com"           |
     | "createBan>banValueObjInfo>extraBanInfo>contactPhoneInfo>phoneNumber" | "4588456914"              |
     | "createBan>banValueObjInfo>extraBanInfo>contactPhoneInfo>ext"         | "1234"                    |
     | "createBan>banValueObjInfo>banIndividualInfo>driverLicenseNumber"     | "9876543211"              |
     | "createBan>banValueObjInfo>banIndividualInfo>driverLicenseState"      | "OH"                      |
     | "createBan>banValueObjInfo>banIndividualInfo>driverLicenseExpires"    | "04/02/2099"              |
     | "createBan>banValueObjInfo>banIndividualInfo>dateOfBirth"             | "03/10/1985"              |
     | "createBan>banValueObjInfo>banIndividualInfo>employer"                | "Ilan AB"                 |
     | "createBan>banValueObjInfo>banIndividualInfo>employeePosition"        | "lala"                    |
     | "createBan>banValueObjInfo>banIndividualInfo>employeeHireDate"        | "02/01/1999"              |
     | "createBan>banValueObjInfo>billingAddressInfo>applyAddress"           | "I"                       |
     | "createBan>banValueObjInfo>billingAddressInfo>type"                   | "F"                       |
     | "createBan>banValueObjInfo>billingAddressInfo>line1"                  | "97284112211"             |
     | "createBan>banValueObjInfo>billingAddressInfo>line2"                  | "97284112212"             |
     | "createBan>banValueObjInfo>billingAddressInfo>streetName"             | "Vernon"                  |
     | "createBan>banValueObjInfo>billingAddressInfo>city"                   | "PROVIDENCE"              |
     | "createBan>banValueObjInfo>billingAddressInfo>country"                | "IL"                      |
     | "createBan>banValueObjInfo>billingAddressInfo>zipCode"                | "88888"                   |
     | "createBan>banValueObjInfo>contactNameInfo>first"                     | "CFK"                     |
     | "createBan>banValueObjInfo>contactNameInfo>last"                      | "CFK"                     |
     | "createBan>banValueObjInfo>billingNameInfo>first"                     | "222"                     |
     | "createBan>banValueObjInfo>billingNameInfo>last"                      | "22"                      |
     | "createBan>banValueObjInfo>banBillInfo>billLanguage"                  | "ENG"                     |
     | "createBan>banValueObjInfo>securityInfo>pin"                          | "111111"                  |
     | "createBan>applicationDataInfo>applicationID"                         | "API"                     |


   Scenario: Do somethings with ASW BAN's whatever they are
     Given we send the message
     When we receive the good response
     Then the message header response value named "NXHeader>ReplyCompCode" must equal "0" 
     And the message header response value named "NXHeader>ServiceName" must equal "AMD.ENSEMBLE.CREATE_BAN"
     And the message body response value named "createBan>CreateBanRespInfo>banId" should be 9 characters long

   Scenario: Negative test for market code.
     Given we send the message with the following modified values:
     
     | tag                                                                   | content                   |
     | "NXHeader>ApplRef"                                                    | "Secod ASW Message"       |
     | "createBan>banValueObjInfo>billingAddressInfo>zipCode"                | "88888"                   |

     When we receive the good response
     Then the message header response value named "NXHeader>ReplyCompCode" must equal "0" 
     And the message header response value named "NXHeader>ServiceName" must equal "AMD.ENSEMBLE.CREATE_BAN"
     And the response message tag named "createBan>message" contains the text "The system was unable to determine the market based on the zip code supplied"

     


# ApplRef must match
