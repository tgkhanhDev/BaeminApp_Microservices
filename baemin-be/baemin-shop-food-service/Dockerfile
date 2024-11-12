# Use the official Node.js image as the base image
FROM node:20

# Create and set the working directory
WORKDIR /app

# Copy package.json and yarn.lock to install dependencies
COPY package.json yarn.lock ./

# Install dependencies
RUN yarn install
RUN yarn prisma generate --schema src/prisma/schema-postgres.prisma 

# Copy the rest of the application code to the working directory
COPY . .

# Build the project (optional if the `dist` directory is already built)
RUN yarn build

# The microservice might run a script or background process, so there's no need to expose a port

# Command to run the application
CMD ["node", "dist/index.js"]
