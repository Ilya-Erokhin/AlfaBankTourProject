# Report on the results of automation

Automated testing of a web service for buying a trip by card or on credit. According to test plan
Everything that was required according to the Technical Task has been done.

___

Service has been tested:

* Application launch correctness (SUT), with two integrations
* Support for two DBMS ("MySQL" and "PostgreSQL") and correct connection to them
* Correct display of the start page of the offer and forms of payment
* Correctness of filling in the fields of the forms "Payment by card" and "Credit according to the card data"
* The correctness of the display and content of pop-up windows with notifications of a successful operation or an operation with an error
* Correctness of saved information about payments in the DBMS
___

Unaccounted risks:

* lack of knowledge and experience in automation
* difficulties with connecting the environment due to the change of obsolete equipment (additional time was required)
* the need for additional information on working with docker containers (the project was renamed after the previous container was launched)

___

Scheduled time: 50 hours

Total elapsed time: 100 hours (net time)