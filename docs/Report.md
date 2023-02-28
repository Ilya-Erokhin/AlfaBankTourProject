# Report on the results of automation

Automated testing of a web service for buying a trip by card or on credit:
* implemented positive and negative scenarios
* implemented support for two databases - MySQL and PostgreSQL
* the correctness of the data stored in the DBMS

Number of test scenarios:
* A total of 44 test cases were tested (according to the plan, 22 cases for each form of payment), of which:
* Testing the form of purchase by card - 20
* Testing the form of purchase on credit - 20
* Testing entering information into databases: 4/4
___
Bugs found - 8 (7 bugs during script run and 1 spelling bug in title)
____

Recommendations:

* It is necessary to fix the identified bugs (see Issue)
* It is necessary to increase the testability of the application by assigning unique test_id to GUI elements
* Replace "Invalid Format" warnings with more user-friendly ones
____
