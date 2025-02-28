# Kaiburr Java Backend - REST API with React Frontend

This project provides a full-stack application with a Spring Boot backend and a React/TypeScript frontend. The backend exposes a REST API to create, fetch, update, execute, and delete "task" objects that represent shell commands. Data is stored in MongoDB, and tasks can be executed inside Kubernetes pods. The CI/CD pipeline (using GitHub Actions and KIND) automates the build, test, and deployment processes.

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [Cloning the Repository](#cloning-the-repository)
- [Project Structure](#project-structure)
- [Local Setup and Running the Application](#local-setup-and-running-the-application)
  - [Backend (task-api)](#backend-task-api)
  - [Frontend (task-ui)](#frontend-task-ui)
- [Docker and Kubernetes Deployment](#docker-and-kubernetes-deployment)
- [CI/CD Pipeline Advantages](#cicd-pipeline-advantages)
- [API Endpoints Test Cases](#api-endpoints-test-cases)
- [Additional Resources](#additional-resources)

---

## Prerequisites

- **Java JDK 11 or Higher**  
  [Download](https://www.oracle.com/java/technologies/javase-downloads.html)

- **Maven**  
  [Download](https://maven.apache.org/download.cgi)

- **Docker**  
  [Download](https://www.docker.com/get-started)

- **Kubernetes Cluster**  
  - Docker Desktop (with Kubernetes enabled)  
  - Or use [Minikube](https://minikube.sigs.k8s.io/docs/start/) / [Kind](https://kind.sigs.k8s.io/docs/user/quick-start/) for local testing

- **MongoDB**  
  Run locally for testing or deploy via Kubernetes  
  [Download](https://www.mongodb.com/try/download/community)

- **Node.js (v18)**  
  For building and running the frontend  
  [Download](https://nodejs.org/en/)

- **Postman**  
  For API testing  
  [Download](https://www.postman.com/downloads/)

- **An IDE (Optional)**  
  IntelliJ IDEA, VS Code, or Eclipse

---

## Cloning the Repository

Clone the repository to your local machine:

```bash
git clone https://github.com/Leonallr10/task-api.git
cd task-api
```

This repository contains both the backend (`task-api/`) and frontend (`task-ui/`) projects.

---

## Project Structure

```
kaiburr/
├── task-api/         # Backend Spring Boot application
│   ├── src/          # Java source code, resources, and tests
│   ├── pom.xml       # Maven build file
│   ├── Dockerfile    # Dockerfile for backend image
│   └── kubernetes/   # Kubernetes manifests (app.yaml and mongodb.yaml)
├── task-ui/          # Frontend React/TypeScript application
│   ├── src/          # React source code
│   ├── package.json  # Node.js project file
│   └── Dockerfile    # Dockerfile for frontend image
└── .github/
    └── workflows/
        └── ci-cd.yml # GitHub Actions CI/CD pipeline configuration
```

---

## Local Setup and Running the Application

### Backend (task-api)

1. **Set Up Your IDE (Optional)**  
   - **IntelliJ IDEA:** Mark `src/main/java` as the source root.  
   - **VS Code:** Ensure your `.vscode/settings.json` includes:
     ```json
     {
       "java.project.sourcePaths": ["src/main/java"],
       "java.project.outputPath": "target/classes"
     }
     ```

2. **Build the Project**  
   Navigate to the `task-api` folder and run:
   ```bash
   mvn clean package
   ```

3. **Run the Application**  
   After building, start the backend:
   ```bash
   java -jar target/task-api-0.0.1-SNAPSHOT.jar
   ```
   The application will start on port **8080**.

4. **Test the API**  
   Use Postman or curl to test endpoints (see [API Endpoints Test Cases](#api-endpoints-test-cases)).

---

### Frontend (task-ui)

1. **Install Dependencies**  
   Navigate to the `task-ui` folder:
   ```bash
   cd task-ui
   npm install
   ```

2. **Run the Frontend Development Server**  
   Start the development server:
   ```bash
   npm run dev
   ```
   The UI will be available at **http://localhost:5174** (or another port if 5174 is busy).

3. **Configure API URL**  
   Ensure the frontend’s service file (`task-ui/src/services/taskService.ts`) uses the correct API URL (e.g., `http://localhost:30080` if deployed via Kubernetes or as per your setup).

---

## Docker and Kubernetes Deployment

### Docker

- **Build Backend Image:**  
  From the `task-api` folder:
  ```bash
  docker build -t leolr10/task-api:latest .
  ```
- **Build Frontend Image:**  
  From the `task-ui` folder:
  ```bash
  docker build -t leolr10/task-ui:latest .
  ```

### Kubernetes

- **Deploy MongoDB and Backend:**  
  In the `task-api/kubernetes` folder, apply the manifests:
  ```bash
  kubectl apply -f mongodb.yaml --validate=false
  kubectl apply -f app.yaml
  ```
- **Service Exposure:**  
  The backend is exposed via a NodePort at **30080**, routing traffic to container port **8080**.

---

## CI/CD Pipeline Advantages

This project includes a complete GitHub Actions CI/CD pipeline which:

- **Automates Builds:**  
  On every push or pull request, the backend and frontend are built automatically using Maven and npm.

- **Containerization:**  
  Docker images are built for both backend and frontend, ensuring consistency across different environments.

- **Automated Deployment:**  
  The pipeline deploys the backend (and optionally the frontend) to a local Kubernetes cluster using KIND, demonstrating how to integrate Kubernetes in your workflow.

- **Faster Onboarding:**  
  New contributors can simply clone the repo, see the CI/CD pipeline in action, and know that their changes will be automatically built, tested, and deployed.

- **Consistency and Quality:**  
  Automated tests and deployments help maintain a high quality of code and reduce manual steps in the deployment process.

---

## API Endpoints Test Cases

Below are some sample test cases you can use with Postman to verify the API:

1. **Create a Task (PUT `/tasks`):**
   ```json
   {
     "id": "123",
     "name": "Print Hello",
     "owner": "John Smith",
     "command": "echo Hello World!"
   }
   ```
2. **Get All Tasks (GET `/tasks`):**  
   Should return a JSON array of tasks.

3. **Get a Specific Task (GET `/tasks?id=123`):**  
   Should return the task with ID "123".

4. **Search Tasks (GET `/tasks/search?name=Hello`):**  
   Should return tasks matching the search query.

5. **Execute a Task (POST `/tasks/123/execute`):**  
   Should execute the command and return the output.

6. **Record Task Execution (PUT `/tasks/123/executions`):**  
   Should create a new execution record with start/end times and command output.

7. **Delete a Task (DELETE `/tasks/123`):**  
   Should remove the task and return a confirmation message.

---

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Docker Documentation](https://docs.docker.com/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [React Documentation](https://reactjs.org/docs/getting-started.html)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)

---

By following these instructions, users can clone the repository, run the project locally, deploy via Docker/Kubernetes, and benefit from an automated CI/CD pipeline that ensures consistent builds, testing, and deployments. This streamlines the development process and makes it easier for new contributors to get started with your project.

---
