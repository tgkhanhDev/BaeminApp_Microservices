# How to start BaeminApp_Microservices

## 1. Services:
Go to each services, add prodived .env (.env only for service)
```bash
nvm use 20
yarn
yarn prisma generate --schema src/prisma/schema-postgres.prisma 
```

Run source local:
```bash
yarn start:dev
```

## 2. Api gateway:
This file does not need a .env file, so adding this in unnecessary
```bash
nvm use 20
yarn
```
Run source local:
```bash
yarn start:dev
```

Now, your source is running on port: [8080/api-baemin](http://localhost:8080/api-baemin)



# Front-end
Start source:
```bash
yarn dev
```

Now, your source is running on port: [3000](http://localhost:3000/)

