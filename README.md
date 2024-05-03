# RVElastic
## Runtime Verification Framework for  Microservice Systems
>This project proposes RVElastic, a Runtime Verification prototype framework for monitoring microservice systems. We present its general architecture and report a possible instantiation wherein Apache Kafka is utilised to create the system and its instrumentation, while OpenSearch is employed as a means to analyse and visualise the verification results. We experiment with RVElastic on a simple case study as a proof of concept and report the results in terms of the overhead introduced by the addition of monitors in the microservice system.

## Getting Started
This guide was created to give the possibility of running RVElastic locally on any machine.
## Prerequisites

 - [Docker](https://www.docker.com/products/docker-desktop/)
 - [Maven 3.6+](https://maven.apache.org/)
 - [Amazon Corretto Java 11+](https://aws.amazon.com/it/corretto/)
 
 ## Create docker images on local docker registry
 
### Compile project:
    mvn clean packages
### Compile docker images:

    mvn docker:build
This command creates 4 images on the local docker registry

 - stefno83/rvelastic/verifier
 - stefno83/rvelastic/worker
 - stefno83/rvelastic/writer
 - stefno83/rvelastic/producer

## Execute project

First of all third-party software used by RV Elastic must be started.
The software are:

 - Apache Kafka (with Apache Zookeeper)
 - Opensearch
 - Opensearch Dashboards

To start them there is a **docker-compose** file in the folder **docker-compose/environment**

    cd docker-compose/environment
    docker-compose up

Wait for the software to start and the containers to be up.

Then it is necessary start the microservices of RVElastic. To start them there is a **docker-compose** file in the folder **docker-compose/rvelastc**

     cd docker-compose/rvelastc
     docker-compose up