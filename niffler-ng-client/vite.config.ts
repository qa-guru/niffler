import {defineConfig, loadEnv} from 'vite'
import react from '@vitejs/plugin-react'
import svgr from "vite-plugin-svgr";
import devtoolsJson from 'vite-plugin-devtools-json';


export default defineConfig(({mode}) => {
    process.env = {...process.env, ...loadEnv(mode, process.cwd())};

    return defineConfig({
        plugins: [svgr(), react(), devtoolsJson()],
        server: {
            host: process.env.VITE_FRONT_HOST,
            port: 3000,
        },
        preview: {
            host: process.env.VITE_FRONT_HOST,
            port: 3000,
            strictPort: true,
        },
    });
});
