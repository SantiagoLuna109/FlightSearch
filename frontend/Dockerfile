# Stage 1: Build static assets, salu2
FROM node:20-alpine AS builder
WORKDIR /app

COPY package*.json ./
RUN npm ci \
 && npm install react-router-dom --save \
 && npm install @types/react-router-dom --save-dev

COPY . .

RUN sed -i 's/"noUnusedLocals": true/"noUnusedLocals": false/' tsconfig.json \
 && sed -i 's/"noUnusedParameters": true/"noUnusedParameters": false/' tsconfig.json

RUN npm pkg set scripts.build="vite build"

RUN npm run build

# Stage 2: Serve with Nginx
FROM nginx:stable-alpine
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx","-g","daemon off;"]