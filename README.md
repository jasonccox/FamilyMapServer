# Family Map Server

The Family Map Server is a Java server I created to be used as the server for the [Family Map Android app](https://github.com/jasonccox/FamilyMap). I created this server for a university course project.

Check out the [specification](https://github.com/jasonccox/FamilyMapServer/blob/master/FamilyMapServerSpecification.pdf) for more information about how the server works.

**Note to future students:** Don't look through, or especially copy, my code if you're working on the same assignment. That would be cheating. I've made this code public in order to include it in a portfolio, not to provide you with a way to cheat.

## Installation

1. Make sure you have the Java Runtime Environment installed. I created the application with `openjdk-12`, so I can only be sure that it works with that version.
2. Download and extract the `release.tar.gz` file.

## Usage

From within the directory containing the `FamilyMapServer.jar` file, run the command `java -jar FamilyMapServer.jar [port]`, where the port is the port number on which the server should run.

You can interact with the server via the Family Map Android app or via a web portal included with the server that can be accessed at http://localhost:[port].
