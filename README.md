# Kaiburr Java Backend - REST API

## Applications/Tools

- **Java JDK 11 or Higher** - For compiling and running the Spring Boot application. [Download](https://www.oracle.com/java/technologies/javase-downloads.html)
- **Maven** - For building and managing project dependencies. [Download](https://maven.apache.org/download.cgi)
- **Docker** - To build, run, and push the container image. [Download](https://www.docker.com/get-started)
- **Kubernetes Cluster**
  - Docker Desktop (with Kubernetes enabled), or
  - Minikube/Kind for local testing. [Download Minikube](https://minikube.sigs.k8s.io/docs/start/), [Download Kind](https://kind.sigs.k8s.io/docs/user/quick-start/)
- **MongoDB** - Either run locally for testing or deploy via Kubernetes. [Download](https://www.mongodb.com/try/download/community)
- **IDE (Optional)** - IntelliJ IDEA, VS Code, or Eclipse for development.
  - [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
  - [VS Code](https://code.visualstudio.com/download)
  - [Eclipse](https://www.eclipse.org/downloads/)
- **Postman** - API testing. [Download](https://www.postman.com/downloads/)

## Libraries/Dependencies (Maven)

- **Spring Boot Starter Web** - For building REST APIs.
- **Spring Boot Starter Data MongoDB** - For MongoDB integration.
- **Kubernetes Java Client** - To programmatically create Kubernetes pods.
- **Lombok (Optional)** - For reducing boilerplate code.

## Clone the Repository

Clone the repository to your local machine to work with the project files.

```bash
git clone https://github.com/Leonallr10/kaiburr_Java_backend-REST_API.git
cd kaiburr_Java_backend-REST_API
```

## Project Setup

Set up the project in your preferred IDE.

### IntelliJ IDEA

1. **Mark Source Root:**
   - This ensures that IntelliJ recognizes `src/main/java` as the main source folder.
   - Right-click on the `src/main/java` folder.
   - Choose **Mark Directory as → Sources Root**.
2. **Rebuild the Project:**
   - This updates the package paths and ensures proper compilation.

### Eclipse

1. **Configure Build Path:**
   - Ensures Eclipse correctly recognizes the source folder.
   - Right-click on your project and select **Build Path → Configure Build Path**.
   - Under the **Source** tab, ensure that `src/main/java` is added as a source folder.
2. **Apply Changes:**
   - Click **Apply and Close** and refresh your project.

### VS Code

1. **Update `.vscode/settings.json`**:
   - Ensures VS Code recognizes the Java source and output paths.
   - In your project’s root, create a folder named `.vscode` (if it doesn’t exist).
   - Create or update the `settings.json` file with the following:
   ```json
   {
       "java.project.sourcePaths": [
           "src/main/java"
       ],
       "java.project.outputPath": "target/classes"
   }
   ```

## Build and Run the Application

### Maven Build

Maven is used to manage dependencies and build the project.

```bash
mvn clean package
```

### Run the Application

After building, run the application using the generated JAR file.

```bash
java -jar target/task-api-0.0.1-SNAPSHOT.jar
```

## Test Your Endpoints

Use **Postman**, `curl`, or a browser to test the REST API.

### Create a Task (PUT `/tasks`)

Creates a new task in the database.

```bash
curl -X PUT http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{
        "id": "123",
        "name": "Print Hello",
        "owner": "John Smith",
        "command": "echo Hello World!"
      }'
```

### Get All Tasks (GET `/tasks`)

Retrieves all stored tasks.

```bash
curl http://localhost:8080/tasks
```

### Get a Specific Task (GET `/tasks?id=123`)

Fetches a specific task by ID.

```bash
curl http://localhost:8080/tasks?id=123
```

### Search Tasks by Name (GET `/tasks/search?name=Hello`)

Finds tasks that match a given name.

```bash
curl http://localhost:8080/tasks/search?name=Hello
```

### Execute a Task (PUT `/tasks/{id}/executions`)

Runs a task with the given ID.

```bash
curl -X PUT http://localhost:8080/tasks/123/executions
```

### Delete a Task (DELETE `/tasks/123`)

Removes a task from the database.

```bash
curl -X DELETE http://localhost:8080/tasks/123
```

## Kubernetes Configuration

### Environment Variables for MongoDB

Defines database connection details using environment variables.

```yaml
env:
  - name: MONGO_HOST
    value: "mongodb-service"
  - name: MONGO_PORT
    value: "27017"
  - name: MONGO_DB
    value: "yourdbname"
```

### Enable Kubernetes in Docker Desktop

1. Go to **Settings** → Enable **Kubernetes**.
2. Verify Kubernetes is running:
   ```bash
   kubectl cluster-info
   ```

### Deploy MongoDB

Deploys a MongoDB instance in the Kubernetes cluster.

```bash
kubectl apply -f mongodb.yaml --validate=false
```

### Deploy the Application

Deploys the REST API application in Kubernetes.

```bash
kubectl apply -f app.yaml
```

### Verify Deployments

Check if pods and services are running.

```bash
kubectl get pods
kubectl get svc
```

## Build and Publish Docker Image

### Build the Docker Image

Creates a Docker image for the application.

```bash
docker build -t yourusername/task-api:latest .
```

(Replace `yourusername` with your Docker Hub username.)

### Push the Image to Docker Hub

Uploads the Docker image to Docker Hub.

```bash
docker login
docker push yourusername/task-api:latest
```

### Update Kubernetes Deployment

Modify `app.yaml` to use the new Docker image.

```yaml
containers:
  - name: task-api
    image: yourusername/task-api:latest
    ports:
    - containerPort: 8080
    env:
      - name: MONGO_HOST
        value: "mongodb-service"
      - name: MONGO_PORT
        value: "27017"
      - name: MONGO_DB
        value: "yourdbname"
```

Apply changes:

```bash
kubectl apply -f app.yaml
```

### Check Pod Status

```bash
kubectl get pods
```

## image for docker image :
![Image](https://github.com/user-attachments/assets/802ca390-4ed6-4465-91ca-ca98fe4e3aa4)
## GET : http://localhost:30080/tasks?id=123
![Image](https://github.com/user-attachments/assets/25a8179f-10ce-4c0a-9385-938fce998d77)
## PUT: http://localhost:30080/tasks
![Image](https://github.com/user-attachments/assets/883afbb0-4267-45a5-ad35-6f37ff5487c5)
## GET: http://localhost:30080/tasks?id=123
![Image](https://github.com/user-attachments/assets/31d24549-b11c-4491-9e36-6fa81d0f4a98)
## DELETE: http://localhost:30080/tasks/123
![Image](https://github.com/user-attachments/assets/d3de9252-be5a-4dfd-b6f9-72da3a06edc9)


