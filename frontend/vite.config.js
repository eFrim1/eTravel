import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      //'/users': 'http://localhost:4000',
      //
      // → fetch("/users/register") will be proxied to http://localhost:4000/users/register
      '/users/register':        {
        target: 'http://localhost:4000',
        changeOrigin: true,
        secure: false,
        ws: true,
        rewrite: (path) => path.replace(/^\/users/, ''),
        configure: (proxy, _options) => {
          proxy.on('error', (err, _req, _res) => {
            console.log('proxy error', err);
          });
          proxy.on('proxyReq', (proxyReq, req, _res) => {
            console.log('Sending Request to the Target:', req.method, req.url);
          });
          proxy.on('proxyRes', (proxyRes, req, _res) => {
            console.log('Received Response from the Target:', proxyRes.statusCode, req.url);
          });
        },
      },
      // → fetch("/catalog") …
      '/catalog':      {
        target: 'http://localhost:4000',
        changeOrigin: true
      },
      '/reservations': {
        target: 'http://localhost:4000',
        changeOrigin: true
      },
      '/reviews':      {
        target: 'http://localhost:4000',
        changeOrigin: true
      },
      '/statistics':   {
        target: 'http://localhost:4000',
        changeOrigin: true
      }
    }
  }
})