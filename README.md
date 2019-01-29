# Authentication

## MongoDB setup

1. Download and install [docker](https://www.docker.com/)
2. Run `docker run -d --name explorviz-auth-mongo -p 27017:27017 mongo`.
3. Start the application

### Development

Update the contents of `src/main/resources/explorviz.properties` by setting `mongo.ip` and `mongo.port` to the ip and port of your running docker instance. By default the ip should be `127.0.0.1`. If you are using docker-machine, you can obtain the ip by using `docker-machine ip`.

