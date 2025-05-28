import React from 'react';
import ReactDOM from 'react-dom/client';
import AppRouter from './Router';
import './index.css';
import { KeycloakProvider } from './config/UserService/keycloak'

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <KeycloakProvider>
            <AppRouter />
        </KeycloakProvider>
    </React.StrictMode>
);