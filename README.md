# Yoga App !

## Start the project

Git clone:

> git clone https://github.com/Asediab/Testez-une-application-full-stack-master

MySQL (MySQL Community Server - GPL):

`Port: 3306`

`Create the username and the password `

`Create a new schema named: test`

`Give rights for this username on the test schema`

`Modify` `back/src/main/resources/application.properties`

`Create the schema with SQL script` `ressources/sql/script.sql`

Go inside folder:

> cd Testez-une-application-full-stack-master

### Install Front-End
Go inside folder:

> cd front

Install dependencies:

> npm install

Launch Front-end:

> npm run start;

### Install Back-End
Go inside folder:

> cd back

Install dependencies:

> mvn install

Launch Back-end:

> mvn spring-boot:run

## Ressources

### Postman collection

For Postman import the collection

> ressources/postman/yoga.postman_collection.json

### Database default content
By default, the admin account is:
- login: `yoga@studio.com`
- password: `test!1234`


### Test

#### Front-End
Go inside folder:

> cd front

Launching e2e test:

> npm run e2e

Generate coverage report (you should launch e2e test before):

> npm run e2e:coverage

Report is available here:

> front/coverage/lcov-report/index.html

#### Unitary test

Launching test:

> npm run test

Generate coverage report:

> npm test -- --coverage

Report is available here:

> front/coverage/jest/lcov-report/index.html

#### Back-End
Go inside folder:

> cd back

Launching test:

> mvn clean test

Report is available here:

> /back/target/site/jacoco/index.html
