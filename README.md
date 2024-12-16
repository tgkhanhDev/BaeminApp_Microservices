# How to start BaeminApp_Microservices

## 1. Third-party setup:
####    RabbitMQ
Install RabbitMQ in your Docker container (use the password: 170504 for syncing with the project below):

```bash
docker run -d --hostname <name-host> --name <name> -p 5672:5672 -p 15672:15672 -p 15692:15692 -
e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=<password> rabbitmq:3-management
```

####    Redis
Install Redis in your Docker container (use the password: 170504 for syncing with the project below):
```bash
docker run --name <name> -d -p 6379:6379 redis redis-server --requirepass <password>
```

####    Postgres Database
-   create Database: **baemin_micro_db**
-   run DDL and DML files.


## 2. Backend setup
Clone this project into your directory:
```
git clone https://github.com/tgkhanhDev/BaeminApp_Microservices.git
```

###  **nest-food-service**:

####    Run Project:

*   Install node_modules:
```bash
nvm use 18
yarn
```

*   Add .env file:

```bash
echo 'DATABASE_POSTGRES_URL="postgresql://postgres:170504@localhost:5432/baemin_micro_db"' > .env
```

*   Generate prisma:

```bash
yarn prisma generate --schema ./src/prisma/schema.prisma 
```

*   Start source:
```bash
yarn start:dev
```

###  **authen-service**:
**NOTE**: You can edit env variables in *[/src/main/resources/application.properties]*

####    Pre-setup:
Make sure you have installed **Java 17+** and **Maven**.

-   Install Java 17 (Ubuntu):
```bash
sudo apt update
sudo apt install openjdk-17-jdk -y
```
-   Install Maven (Ubuntu):
```bash
sudo apt install maven -y
```

####    Run Project:
-   Build package:
```bash
cd ./baemin-be/authen-services
mvn package
```
-   Run source:
```bash
java -jar ./target/authen-service-3.3.5.jar
```


###  **java-gateway**:
**NOTE**: You can edit env variables in *[/src/main/resources/application.properties]*
####    Pre-setup:
Make sure you have installed **Java 17+** and **Maven**.

-   Install Java 17 (Ubuntu):
```bash
sudo apt update
sudo apt install openjdk-17-jdk -y
```
-   Install Maven (Ubuntu):
```bash
sudo apt install maven -y
```

####    Run Project:
-   Build package:
```bash
cd ./baemin-be/java-gateway
mvn package
```
-   Run source:
```bash
java -jar ./target/java-gateway-0.0.1-SNAPSHOT.jar
```



Now, your source is running on port: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/api-baemin)



## 3. Front-end setup
####    Install node_modules:
```bash
yarn
```

####    Start source:
```bash
yarn start:dev
```

Now, your source is running on port: [http://localhost:3000](http://localhost:3000/)
