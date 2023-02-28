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
Обнаружено багов - 8 (7 багов в ходе прогона сценариев и 1 баг орфографическая ошибка в названии)

Отчеты о тестировании были сформированы с помощью инструмента "Allure"
<img width="921" alt="Allure report" src="https://user-images.githubusercontent.com/79021474/146738163-630e4c76-7cd1-4462-9a5c-a28d922c22b4.png">

____

Рекомендации:

* Необходимо исправление выявленных багов (см. Issue)
* Необходимо повысить testability приложения путем присвоения уникальных test_id для элементов графического интерфейса
* Заменить предупреждения "Неверный формат" на более понятные пользователю
____
