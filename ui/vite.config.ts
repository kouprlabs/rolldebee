import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'
import svgr from 'vite-plugin-svgr'
import tsconfigPaths from 'vite-tsconfig-paths'

const config = defineConfig({
  plugins: [react(), tsconfigPaths(), svgr()],
})

export default config
