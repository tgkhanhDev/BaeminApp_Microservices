# How to start BaeminApp_Microservices
## 1 Services:
Go to each services, add prodived .env (.env only for service)
```bash
nvm use 20
yarn
yarn prisma generate --schema src/prisma/schema-postgres.prisma 
```

run source:
```bash
yarn start:dev
```

## 2 Api gateway:
This file does not need a .env file, so adding this in unnecessary
```bash
nvm use 20
yarn
```
Run source local:
```bash
yarn dev
```
