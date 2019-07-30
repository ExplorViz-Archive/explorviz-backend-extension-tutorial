[//]: # (This readme is partly copied from other extension readmes to ensure consistency in the ExplorViz project)
# ExplorViz-Backend-Extension-Tutorial

This extension adds features to the backend of ExplorViz to enable communication with the frontend extension is [explorviz-frontend-extension-tutorial](https://github.com/ExplorViz/explorviz-frontend-extension-tutorial).

## Installation
1. Follow the [Eclipse Setup](https://github.com/ExplorViz/explorviz-backend#eclipse-setup) of the [ExplorViz Backend](https://github.com/ExplorViz/explorviz-backend)
2. Clone this repository
3. Follow the instructions inside of the [docker-compose](docker-compose) folder to start mandatory docker-based services (e.g. MongoDB)
4. Import project into eclipse: via `Import -> Gradle -> Existing Gradle project -> path/to/explorviz-backend-extension-tutorial`
5. Start **explorviz-backend-extension-tutorial** via Eclipse Tab: Run `src/main/java/net/explorviz/extension/tutorial/main/Main.java` as java-application.
6. Setup and start [explorviz-frontend](https://github.com/ExplorViz/explorviz-frontend) with the installed [explorviz-frontend-extension-tutorial](https://github.com/ExplorViz/explorviz-frontend-extension-tutorial)
