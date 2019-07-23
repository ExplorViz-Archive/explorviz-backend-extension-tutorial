[//]: # (This readme is partly copied from other extension readmes to ensure consistency in the ExplorViz project)
# ExplorViz-Backend-Extension-Tutorial

This extension adds features to the backend of ExplorViz to enable communication with the frontend extension is [explorviz-frontend-extension-tutorial](https://github.com/ExplorViz/explorviz-frontend-extension-tutorial).

## Requirements
- MongoDB, `/explorviz-backend-extension-tutorial/src/main/resources/explorviz.properties` contains host and port. (Docker-Compose with docker-compose-tutorial.yml)

## Installation
1. Follow the [Eclipse Setup](https://github.com/ExplorViz/explorviz-backend#eclipse-setup) of the [ExplorViz Backend](https://github.com/ExplorViz/explorviz-backend)
2. Clone this repository
3. Import project into eclipse: via `Import -> Gradle -> Existing Gradle project -> path/to/explorviz-backend-extension-tutorial`
4. Start **explorviz-backend-extension-tutorial** via Eclipse Tab: Run `src/main/java/net/explorviz/extension/tutorial/main/Main.java` as java-application.
5. Setup and start [explorviz-frontend](https://github.com/ExplorViz/explorviz-frontend) with the installed [explorviz-frontend-extension-tutorial](https://github.com/ExplorViz/explorviz-frontend-extension-tutorial)
