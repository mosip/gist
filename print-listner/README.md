<!--[![Build Status](https://travis-ci.org/mosip/admin-services.svg?branch=1.0.9)](https://travis-ci.org/mosip/admin-services) -->

# mosip-platform
This repository contains the source code of the Modular Open Source Identity Platform. To know more about MOSIP, its architecture, external integrations, releases, etc., please check the [Platform Documentation](https://github.com/mosip/mosip-docs/wiki)

## Print-Listener
### Introduction
1. Print-Listener is a service which is run in user system (or) local server. This listener service will listen to ActiveMQ queue ('print-to-printlistener') and read the messages when available in queue.
2. Also it will send the print status to ActiveMQ queue ('printlistener-to-print') which is received by print service.
### Key Points -
1. `ActiveMQ (print-to-printlistener)` - Contains messages with JSON format. This message contains following parameters
   1. id - Default value (`mosip.print.pdf.data`).
   2. refId - Contains Reference id (RID) of the demographic details.
   3. printId - Contains reference id for print request. Based on printId listener will send back print status to print service.
   4. data - Contains Datashare URI which contain encrypted pdf data.
```json
{
  "id":"mosip.print.pdf.data",
  "refId":"10001100010000120211217031008",
  "printId":"d5baddc4-e644-4d7f-b8a2-328c54f5b71d",
  "data":"http://{mosip.hostname}/v1/datashare/get/mpolicy-default-resident/PART3720/PART3720mpolicy-default-resident20211217031348rDrvgzwy"
}
```
2. `ActiveMQ (printlistener-to-print)` - Contains messages with JSON format. This message contains following parameters
    1. id - Default value (`mosip.print.pdf.response`).
    2. data:id - Contains reference id for print request. Based on printId listener will send back print status to print service.
    3. data:printStatus - Printing Status (QUEUED, SENT_FOR_PRINTING, PRINTED, ERROR).
    4. data:statusComments - Contains Error Message.
    5. data:processedTime - Pdf processed Timestamp.
```json
{
  "id":"mosip.print.pdf.response",
  "data": {
    "id":"d5baddc4-e644-4d7f-b8a2-328c54f5b71d",
    "printStatus":"PRINTED",
    "statusComments":"",
    "processedTime":"2021-12-17T05:08:58.691Z"
  }
}
```
2. `*.p12 File` - Partner private key file which is used to decrypt the pdf data.
3. partner.properties - contain property of partner private key information.
4. Swagger URI - `http://{mosip.hostname}/v1/printListener/swagger-ui.html`

### Pre-Request
1. MOSIP hostname should be configured in application.properties.
2. partner `*.p12` file should be placed in resources package.
3. Partner credentials information like key.filename, key.password & key.alias to be configured in `partner.properties`.
4. ActiveMQ queue information should be configured in `print-activemq-listener.json`.
### Build
The following commands should be run in the parent project to build all the modules -

`mvn clean install`

The above command can be used to build individual modules when run in their respective folders

### Deploy
The following command should be executed to run any service locally in specific profile and local configurations -
`java -Dspring.profiles.active=<profile> -jar <jar-name>.jar`

The following command should be executed to run any service locally in specific profile and `remote` configurations -
`java -Dspring.profiles.active=<profile> -Dspring.cloud.config.uri=<config-url> -Dspring.cloud.config.label=<config-label> -jar <jar-name>.jar`

The following command should be executed to run a docker image -
`docker run -it -p <host-port>:<container-port> -e active_profile_env={profile} -e spring_config_label_env= {branch} -e spring_config_url_env={config_server_url} <docker-registry-IP:docker-registry-port/<dcker-image>`

### Dependencies

* Print-Listener
    * Kernel-auth-service in Kernel module.

### Run as Developer
For running services in a native environment developer has to run some core components
[Instruction to follow for running Print Listener](./StartPrintListener_instructions.md)

### Configurations
All the configurations used by the codebase is present in `resources` path.