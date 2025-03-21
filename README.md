# Development

This repository is one big repo with both the frontend and backend code for the application in one and same repository.
The folder names should be self explanitory.

## Run the backend

Assuming your terminal is in the root directory of the project:
```bash
cd ./backend/Gallery    # Be in the right directory

# On Linux/MacOS
./mvnw spring-boot:run  # This will download and run the right version of maven before running the spring boot development server.

# On Windows
.\mvnw spring-boot:run  # This will download and run the right version of maven before running the spring boot development server.

```

If using the maven wrapper (mvnw), that is the command that should with subsequent commands.

## Run a development server (with Hot Module Reload) for the frontend

**If you do not have pnpm installed, but do have node and npm, just substitute pnpm with npm**

Assuming your terminal is in the root directory of the project:
```bash
cd ./Frontend/DAT251-frontend/
pnpm install
pnpm run dev
```