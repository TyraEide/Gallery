# Development

This repository is one big repo with both the frontend and backend code for the application in one and same repository.
The folder names should be self explanitory.

## prerequisites for working on the project
You need to have installed:
#### For backend
1. Java JDK (v.21 atleast I think). [Recommend using SDKMan on Linux/Mac](https://sdkman.io/) install the JDK or if on Windows [downloading from Adoptium](https://adoptium.net/)

#### For frontend
1. [Recommend using pnpm](https://sdkman.io/) for installing node and replacing the standard npm.


## Run the backend

Assuming your terminal is in the root directory of the project:
```bash
cd ./backend/Gallery    # Be in the right directory
# Depending on the os:

## On Linux/MacOS
./mvnw spring-boot:run  # This will download and run the right version of maven before running the spring boot development server.

## On Windows
.\mvnw spring-boot:run  # This will download and run the right version of maven before running the spring boot development server.

```

If using the maven wrapper (mvnw), that is the command that should with subsequent commands.

## Run a development server (with Hot Module Reload) for the frontend

**If you do not have pnpm installed, but do have node and npm, just substitute pnpm with npm**

Assuming your terminal is in the root directory of the project:
```bash
cd ./Frontend/DAT251-frontend/
pnpm install  # Installs the dependencies 
pnpm run dev  # Runs the frontend dev server
```