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
* For MySQL: ```java -jar ./artifacts/aqa-shop.jar -Dspring.datasource.url=jdbc:mysql://localhost:3306/app```
* For PostgreSQL: ```java -jar ./artifacts/aqa-shop.jar -Dspring.datasource.url=jdbc:postgresql://localhost:3306/app```
3. Run the tests with the command:
* For MySQL: `./gradlew clean test -Ddb.url=jdbc:mysql://localhost:3306/app -Dlogin=app -Dpassword=pass -Dapp.url=http://localhost:8080`
* For PostgreSQL: `./gradlew clean test -Ddb.url=jdbc:postgresql://localhost:5432/postgres -Dlogin=app -Dpassword=pass -Dapp.url=http://localhost:8080`
