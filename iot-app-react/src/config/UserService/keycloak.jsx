import React, {
  createContext,
  useEffect,
  useState,
  useRef,
} from 'react';
import Keycloak from 'keycloak-js';

const KeycloakContext = createContext(undefined);

const KeycloakProvider = ({ children }) => {
  const isRun = useRef(false);
  const [keycloak, setKeycloak] = useState(null);
  const [authenticated, setAuthenticated] = useState(false);

  useEffect(() => {
    if (isRun.current) return;

    isRun.current = true;

    const initKeycloak = async () => {
      const keycloakConfig = {
        url: import.meta.env.VITE_KEYCLOAK_URL,
        realm: import.meta.env.VITE_KEYCLOAK_REALM,
        clientId: import.meta.env.VITE_KEYCLOAK_CLIENT,
      };

      const keycloakInstance = new Keycloak(keycloakConfig);

      keycloakInstance
        .init({
           onLoad: "check-sso", // check-sso | login-required
          KeycloakResponseType: "code",
          silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html'
        })
        .then((authenticated) => {
          setAuthenticated(authenticated);
        })
        .catch((error) => {
          console.error('Keycloak initialization failed:', error);
          setAuthenticated(false);
        })
        .finally(() => {
          setKeycloak(keycloakInstance);
          console.log('keycloak', keycloakInstance);
        });
    };

    initKeycloak();
  }, []);

  return (
    <KeycloakContext.Provider value={{ keycloak, authenticated }}>
      {children}
    </KeycloakContext.Provider>
  );
};

export { KeycloakProvider, KeycloakContext };
    