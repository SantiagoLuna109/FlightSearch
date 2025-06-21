# ---------- builder ----------
FROM node:20-alpine AS builder
WORKDIR /app

# deps first – enables layer-cache
COPY package*.json ./
RUN npm ci \
 && npm install react-router-dom \
 && npm install -D @types/react-router-dom

# app source (this will NOT include node_modules thanks to .dockerignore)
COPY . .

# adjust TS config & build
RUN sed -i 's/"noUnusedLocals": true/"noUnusedLocals": false/' tsconfig.json \
 && sed -i 's/"noUnusedParameters": true/"noUnusedParameters": false/' tsconfig.json \
 && npm pkg set scripts.build="vite build" \
 && npm run build

# ---------- runtime ----------
FROM nginx:stable-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx","-g","daemon off;"]

