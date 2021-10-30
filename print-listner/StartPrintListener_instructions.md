### Introduction

Please follow the below steps to run Print Listner in Development mode.

### Prerequisite:

Before you start any of the steps, you should be aware of the following technical stuff, 

1. Kernel architecture

2. Print Listener architecture

3. Springboot services

4. Postman tool or any such similar tools to test the web service

5. Basic knowledge about PostgreSQL server 

6. Basic knowledge about pgadmin4. This is a client tool to connect to PostgreSQL server

7. Java 8 should have been installed in your development machine

8. Maven build tool should be installed in your development machine

9. Clone git@github.com:mosip/gist.git
  

#### a. Print-Listener
1. MOSIP hostname should be configured in application.properties.
2. partner `*.p12` file should be placed in resources package.
3. Partner credentials information like key.filename, key.password, key.alias & pdf.download.path to be configured in `partner.properties`
    ```
   partner.private.keyfilename=Client.p12
   partner.private.key.password=password
   partner.private.key.alias=Client
   partner.pdf.download.path = C:/Users/Downloads/
   ```
   
4. ActiveMQ queue information should be configured in `print-activemq-listener.json`.
```
{
    "printMQ": [{
        "name": "PRINT LISTENER",
        "brokerUrl": "tcp://sandbox.mosip.net:30616",
        "inboundQueueName": "print-to-listener",
        "outboundQueueName": "",
        "userName": "userId",
        "password": "password",
        "typeOfQueue": "ACTIVEMQ"
    }]
}
```

5. Modify bootstrap.properties

```
spring.cloud.config.uri=localhost
spring.cloud.config.label=develop
spring.profiles.active=local
spring.cloud.config.name=printListener
spring.application.name=printListener
management.endpoint.health.show-details=always
management.endpoints.web.exposure.include=info,health,refresh
listener.service = printListener
config.server.file.storage.uri=${spring.cloud.config.uri}/${listener.service}/${spring.profiles.active}/${spring.cloud.config.label}/
server.port=9092
server.servlet.path=/v1/printListener/
health.config.enabled=false
```

4. Build and start admin service

```
java -jar {printlistenerjarname}.jar
```

5. Verify print listener service

```
1. Open http://localhost:9092/v1/printListener/swagger-ui.html

2. Go to /decryptCredentials

3. Try it out

4. sample Request

{
  "event": {
    "dataShareUri": "string",
    "id": "string"
  },
  "requestedOn": "string"
}
```