import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import fs from 'fs';
import path from 'path';

// https://vitejs.dev/config/

const websocketUrl = "https://localhost:8085";

export default defineConfig({
  plugins: [react()],
  server: {
    https: {
      key: fs.readFileSync(path.resolve(__dirname, './ssl/key.pem')),
      cert: fs.readFileSync(path.resolve(__dirname, './ssl/cert.pem')),
  },
  proxy: {
      '/iot/ws': {
        target: websocketUrl,
        changeOrigin: true,
        secure: false,
        ws: true, // <-- IMPORTANT: proxy WebSocket
      }
  },
},
  
  define: {
    global: 'window', // 👈 define global for browser
  }
})
