# ---- Stage 1: Build the React Vite App ----
FROM node:20 AS builder

WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps
# COPY . .
RUN npm run build

# ---- Stage 2: Serve with Nginx ----
FROM nginx:1.23.2

# Remove default config and add custom one
RUN rm /etc/nginx/conf.d/default.conf
COPY default.conf /etc/nginx/conf.d/

# Copy built Vite assets
COPY --from=builder /app/dist /usr/share/nginx/html

# Optional: fix permissions
RUN chown -R nginx:nginx /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
