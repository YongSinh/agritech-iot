import React, {
  createContext,
  useEffect,
  useState,
  useRef,
} from 'react';
import Keycloak from 'keycloak-js';
import LoadingPage from '../../components/LoadingPage';

const KeycloakContext = createContext(undefined);

const KeycloakProvider = ({ children }) => {
  const isRun = useRef(false);
  const [keycloak, setKeycloak] = useState(null);
  const [authenticated, setAuthenticated] = useState(false);
  const [token, setToken] = useState("")
  const [refreshToken, setRefreshToken] = useState("")

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
          onLoad: "login-required", // check-sso | login-required
          KeycloakResponseType: "code",
          silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
          pkceMethod: 'S256',
          checkLoginIframe: false,
          refreshToken : refreshToken,
          token: token
        })
        .then((authenticated) => {
          setAuthenticated(authenticated);
          setRefreshToken(keycloakInstance.refreshToken);
          setToken(keycloakInstance.token)
        })
        .catch((error) => {
          console.error('Keycloak initialization failed:', error);
          setAuthenticated(false);
        })
        .finally(() => {
          setKeycloak(keycloakInstance);
        });
    };

    initKeycloak();
  }, []);

  return (
    <KeycloakContext.Provider value={{ keycloak, authenticated }}>
      {authenticated ? children : <LoadingPage/>}
    </KeycloakContext.Provider>
  );
};

export { KeycloakProvider, KeycloakContext };
