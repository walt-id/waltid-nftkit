FROM node

# Create app directory
WORKDIR /usr/app

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied
# where available (npm@5+)
COPY js/package*.json ./

RUN npm install
RUN npm install @types/bn.js



# If you are building your code for production
# RUN npm install --only=production

# Bundle app source
COPY js/. .

# for typescript
RUN npm run build
COPY js/.env ./dist/
COPY js/resources/ dist/
WORKDIR ./dist

EXPOSE 3000
CMD node app.js
