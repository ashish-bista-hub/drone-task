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
#### To register drone
```http://localhost:8080/api/v1/drone/register```
```json
{
  "droneModel": "LIGHT_WEIGHT",
  "serialNo": "Drone-MKYU-0L-UTRE-987",
  "weight": 500
}
```
![#1589F0](https://via.placeholder.com/15/1589F0/1589F0.png) `Note - battery full capacity & initial state for drone 99D`
#### To load medication
```http://localhost:8080/api/v1/drone/17/load?medication```
1. Get the drone id which you have just registered or look up in the db table drone to get id
2. use the same drone id as path variable
3. Then select an image file from the file uploader
4. Then place the following medication json
```json
{"code":"TEST-MED-1098-OPY","name":"Test-med-para-cetamol-098","weight":200}
```
#### To check the audit logs
```http://localhost:8080/api/v1/drone/1/audits```

where 1 is the existing drone id which is created on app load.

Similarly, try out other end-points also.

## Unit Test
The tests are run when you start the app by any of the methods mentioned above either maven or jar
1. Private method tests are covered from public method tests are they used in that context and scope limited to class.
2. For dependency calls, like call to service/repository methods are mocked
3. Minimum of 80% code coverage is done

## Validations
```javax.validations and custom validations are performed at```
1. Entity levels
2. Service method levels
3. Rest controller level

## Periodic task
For periodic task spring boot feature scheduler is used which is scheduled to run at 10 seconds interval and which will update drone battery usage.