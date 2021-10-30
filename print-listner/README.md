<!--[![Build Status](https://travis-ci.org/mosip/admin-services.svg?branch=1.0.9)](https://travis-ci.org/mosip/admin-services) -->

# mosip-platform
This repository contains the source code of the Modular Open Source Identity Platform. To know more about MOSIP, its architecture, external integrations, releases, etc., please check the [Platform Documentation](https://github.com/mosip/mosip-docs/wiki)

## Print-Listener
### Introduction
Print-Listener is a service which is run in user system (or) local server. This listener service will listen to ActiveMQ queue ('print-to-listener') and read the messages when available in queue.

### Key Points -
1. `ActiveMQ (print-to-listener)` - Contains messages with JSON format. This message contain following parameters
   1. id - Default value (`mosip.print.pdf.data`).
   2. refId - Contains Reference id (RID) of the demographic details.
   3. data - Contains Datashare URI which contain encrypted pdf data.
2. `*.p12 File` - Partner private key file which is used to decrypt the pdf data.
3. Swagger URI - `http://{mosip.hostname}/v1/printListener/swagger-ui.html`

### Pre-Request
1. MOSIP hostname should be configured in application.properties.
2. partner `*.p12` file should be placed in resources package.
3. Partner credentials information like key.filename, key.password, key.alias & pdf.download.path to be configured in `partner.properties`.
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