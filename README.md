# Infernobuster

## About
Infernobuster is a tool that can analyze and isolate conflicting firewall rules in a firewall rule configuration file. The tool can be used to help modify existing rules and create non-conflicting rules. Currently, the supported firewall types are UFW and IPtables. The tool does not modify the original configuration file. Instead, the user may export a new configuration file which they can choose to deploy.

Infernobuster is built with Java only. A screenshot of Infernobuster is shown below:
![Infernobuster](./docs/images/infernobuster.png)

## Supervisor
Jason Jaskolka

## Authors
- Hoang Bui
- Michael Fan
- Tamer Ibrahim
- Mrunal Patel
- Souheil Yazji

## About our Project
A Youtube video about our project can be found [here](https://www.youtube.com/watch?v=WFze8DN9dI4).


## Tutorial video
A Youtube video about how to use infernobuster can be found [here](https://youtu.be/flbTEQttjD4).

## Dependencies
The only dependency that Infernobuster has is JUnit. 

## Building and Running
First clone the project like  
`git clone https://github.com/mpfan/InfernoBuster.git` 

Then navigate to the project folder like  
`cd ./InfernoBuster/infer`  

From here you can either use Intellij or the command line to run the project.
### Intellij
To use Intellij simply open the project using Intellij and go from there.

### Command line
To use the command line first navigate to the source code directory like  
`cd ./infernobuster`  

The project is a maven project so just use the maven commands to build the project. For exmaple, 
To build the project run  
`mvn package`  

Then to run the project just do  
`java -jar ./target/infernobuster-1.0-SNAPSHOT`  
or just click on the jar file. 

## Testing
All tests can be found in `infernobuster/src/test/java`. To run the tests do  
`mvn test`

## Contributions
Use the following link for [Commit Convention](https://www.conventionalcommits.org/en/v1.0.0/). All contributions to the project should pretain to an [Issue](https://github.com/mpfan/InfernoBuster/issues). Individual contributions to the project should be made through Pull Requests into the dev branch.
