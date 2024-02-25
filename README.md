This repository is artifact archive for PROM06 Submission.

## Architecture Overview
![Architecture Overview](/design/architecture-overview.jpg)

## System Requirement for Development
### Browser Extension

- Node (v16.15.9)
- NPM (8.5.5)
- Google Chrome (for testing)

### Front Facing API

- Java 17
- Gradle 8.6
- Docker, Docker Compose (for MySQL and Redis development dependency)

### Cyberbully Identification System

- Python 3.7
- pip
- Poetry

### Other

- flyway (only for migration on real server)

## How To Run (for Development)

1. Open new terminal session
2. Move to cyberbully identification system working directory `cd cyberbullying-detection-api`
3. Install all dependency `poetry install`
4. Run service `poetry run uvicorn main:app --reload`
5. Open new terminal session
6. Move to front facing api working directory `cd cyberbullying-ff-api`
7. Run service `./gradlew bootRun`
8. Open new terminal session
9. Move to browser extension working directory `cd browser-extension`
10. Run the transpiler `npm run dev:chrome`
11. Go to chrome://extensions
12. Turn on Developer mode in the top right corner 
13. Click Load unpacked 
14. Find and select the app or extension folder 
15. Click Select Folder `browser-extension/extension`


## Design Docs
1. [API Spec](/docs/api_spec)
2. [Data Model](/docs/data_model)
   ![Data Model](/docs/data_model/db_design.png)
3. [Sequence](/docs/sequence)
   ![Sequence](/docs/sequence/screen_cyberbully_twitter.png)

## Other
- Performance Test (see `load-test` directory)
- Http Test (see `http-test` directory)
- Migration (for production/performance test, please use `flyway` to run the migration inside `migration` directory)
