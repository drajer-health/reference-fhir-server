# Refernce FHIR Server
### Prerequisites:

    1. Java 8
    2. Apache Tomcat 9 and above
    3. PostgresSql Database 10.x
    4. Maven 3.3.x
    5. GIT
    6. MSSQL Server
    7. NodeJS


Clone the Repository Clone the respository using the below command in command prompt

    git clone https://github.com/drajer-health/reference-fhir-server.git

### Build and Deploy Reference FHIR Server:
Create Build: Build Reference FHIR Server: Change the database configurations in the file application.properties located under src/main/resources

    jdbc.url=dbc:sqlserver://<SQL_INSTANCE>;database=<DATABASE_NAME>;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
    jdbc.username=<username>
    jdbc.password=<password>

Create the tables in the database by executing the SQL script 'Reference_FHIR_Server.sql' file present in the base directory.

Then navigate to reference-fhir-server directory and run Maven build to build application war file.

```
$ mvn clean install
```
Note: Skip the tests if the build is getting failed.

This will generate a war file under target/fhir.war. Copy this to your tomcat webapp directory for deployment.

### Build and Deploy Reference Authorization Server

Navigate to the directory `/reference-fhir-server/auth-server/` and follow the below instructions

Create Build:
Build Reference Authorization Service:
Change the database configurations in the file application.properties located under src/main/resources 

```
jdbc.url=jdbc:postgresql://localhost:5432/<database_name>
jdbc.username=<username>
jdbc.password=<password>
```

Navigate to the directory `/reference-fhir-server/auth-server/frontend` and execute the below command
```
npm install -g @angular/cli
npm install --force
```
Then run Maven build to build application war file.

```
$ mvn clean install
```

This will generate a war file under target/auth-server.war. Copy this to your tomcat webapp directory for deployment.


Start Tomcat Service 
If the tomcat is started successfully then you should be able to access below endpoints
ENDPOINTS:

```
1.http://localhost:<tomcatport>/<Service_name>/fhir/metadata

Request Method: GET
Request Headers:

1.	Content-Type: application/json

```







