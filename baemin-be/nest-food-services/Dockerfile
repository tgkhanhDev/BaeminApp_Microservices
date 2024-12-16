FROM node:18

WORKDIR /root/nest-food-services

COPY package.json .

RUN yarn install

COPY ./src/prisma ./src/prisma

RUN yarn prisma generate --schema ./src/prisma/schema.prisma

COPY . .

RUN yarn build

CMD ["yarn", "start:prod"]