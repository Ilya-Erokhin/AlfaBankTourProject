# Order of running autotests

***

#### Before running auto-tests, you must:
* Install "Intellij IDEA Ultimate", "Docker", "Docker-compose" programs to work with "MySQL" containers,
   "PostgreSQL", "Node-app"

* Check for installed versions of libraries in "build.gradle" file,
   required to run auto-tests

* Run containers "MySQL", "PostgreSQL", "Node-app" in "Docker-compose"

* Run SUT for "MySQL" or "PostgreSQL"

1. To launch containers "MySQL", "PostgreSQL", "Node-app", enter in
    terminal the following command: ```docker-compose up -d --force-recreate```
2. Run SUT command
* For MySQL: ```java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar artifacts/aqa-shop.jar```
* For PostgreSQL: ```java -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app -jar artifacts/aqa-shop.jar```
3. Run the tests with the command:
* For MySQL: `./gradlew clean test -Ddb.url=jdbc:mysql://localhost:3306/app -Dlogin=app -Dpassword=pass -Dapp.url=http://localhost:8080`
* For PostgreSQL: `./gradlew clean test -Ddb.url=jdbc:postgresql://localhost:5432/postgres -Dlogin=app -Dpassword=pass -Dapp.url=http://localhost:8080`

### Environment:
- Windows 10 Home
- Asus VivoBook 14 i5-1135G7 @ 2.40GHz
- Intellij IDEA 2022.3.2 Community
- Chrome Version 110.0.5481.178
