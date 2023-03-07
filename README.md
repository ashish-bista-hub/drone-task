# Drone Interview Task

## Introduction
There is a major new technology that is destined to be a disruptive force in the field of transportation: the
drone. Just as the mobile phone allowed developing countries to leapfrog older technologies for personal
communication, the drone has the potential to leapfrog traditional transportation infrastructure.
## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)
- [Git version control (preferred latest version)](https://git-scm.com/downloads) 
## Running the application locally
#### If you are using Git then 
1. Open your OS terminal or Git CMD
2. Go to directory where you want to clone the repository
3. Type the command or copy ```git clone https://github.com/ashish-bista-hub/drone-task.git```
4. and hit enter

#### Without using Git
1. Open the ```https://github.com/ashish-bista-hub/drone-task.git``` in your browser
2. Click on the code dropdown
3. Click on ```Download ZIP``` option

![#1589F0](https://via.placeholder.com/15/1589F0/1589F0.png) Note :- you could also try direct link to download zip ```https://github.com/ashish-bista-hub/drone-task/archive/refs/heads/main.zip```

#### After you are done with repository download/cloning
1. Open the OS terminal
2. Go to the location where you have cloned/placed extracted folder
3. If you directly want to run the app then execute following command
```mvn spring-boot:run```
4. Or, you could build the app first by executing ```mvn clean compile install```
5. You could see this command will create a new folder target inside which you will have a ```drone-task-1.0.0-SNAPSHOT.jar```
6. Now execute this command ```java -jar target/drone-task-1.0.0-SNAPSHOT.jar```
7. Once you see the server has started message in your terminal then you are all set to use the rest api

## Access the Rest APIs
This app comes with the embedded rest client [swagger](https://swagger.io/)
1. Open the web browser and hit the following URL ```http://localhost:8080/swagger-ui.html```
2. Then you could access the rest APIs
Or you could try rest APIs with any other rest client such as [postman](https://www.postman.com/downloads/)

## Access the database
This app uses in-memory [H2](http://www.h2database.com/html/main.html) 
1. Open the web browser and hit the following URL ```http://localhost:8080/h2-ui```
2. Change the JDBC URL to ```jdbc:h2:mem:drone```
3. Enter the password ```password```
4. Click on Test Connection
5. After successful message click on connect
6. There you could see drone, medication and audit table
   1. Drone table - capture and store drone records (Note:- Only drone table is pre-populated with sample data)
   2. Medication table - capture and store medication records
   3. Audit table - capture and store activity logs respect to drone battery percentage
4. Try out SQL statements on editor

![#1589F0](https://via.placeholder.com/15/1589F0/1589F0.png) `Note - Rest APIs are available for drone & medication only, audit table is populated from scheduler`

## Rest APIs end-point & JSON
The rest APIs documentation is also available in swagger, just for the FYI

