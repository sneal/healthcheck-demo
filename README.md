# CF Health Check Demo
Demo showing custom liveness and readiness health checks in a Spring Boot application.

## Overview
In CF health checks are used to monitor if an application instance is working and able to serve traffic.

- **Liveness Check**: Determines if the application is alive and running. This check ensures that the application process is still running and has not crashed.
- **Readiness Check**: Determines if the application is ready to accept traffic. This check ensures that the application is fully initialized and can handle incoming requests.

This application simulates an app that requires the following before it can serve traffic enforced via the readiness check:
- Fully populated in-memory cache (takes 30sec to populate)
- H2 DB connectivity
- GitHub API connectivity

### Endpoints
- /hello
- /actuator/health/liveness
- /actuator/health/readiness

### Run in CF
```sh
./gradlew build && cf push
```

### Run Locally
```sh
./gradlew bootRun
```
