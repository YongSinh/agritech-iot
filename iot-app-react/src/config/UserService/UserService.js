import Keycloak from 'keycloak-js';
const keycloakConfig = {
    url: import.meta.env.VITE_KEYCLOAK_URL,
    realm: import.meta.env.VITE_KEYCLOAK_REALM,
    clientId: import.meta.env.VITE_KEYCLOAK_CLIENT,
};

const _kc = new Keycloak(keycloakConfig);
const doLogin = _kc.login;

//  const doLogout = _kc.logout;


const doLogout = async () => {
  try {
    await _kc.logout({ redirectUri: window.location.origin });
  } catch (error) {
    console.error('Logout failed:', error);
  }
};

const account = _kc.accountManagement;


const getToken = () => _kc.token;

const getrole = () => JSON.stringify(_kc.tokenParsed?.realm_access.roles);

const refreshToken = () => _kc.refreshToken;

const isLoggedIn = () => !!_kc.token;
const LoggedIn = () => _kc.token;
const updateToken = (successCallback) =>
  _kc.updateToken(60)
    .then(successCallback)
    .catch(doLogin());


const getUsername = () => _kc.tokenParsed?.preferred_username;
const getLastname = () => _kc.tokenParsed?.family_name;
const getFirstname = () => _kc.tokenParsed?.given_name;
const getEmail = () => _kc.tokenParsed?.email;
const authenticated = () => _kc.authenticated;
const hasRole = (roles) => roles.some((role) => _kc.hasRealmRole(role));

// const hasRole = (roles) => {
//   // Ensure roles is an array, even if it's undefined or another type
//   const roleArray = Array.isArray(roles) ? roles : [];
//   return roleArray.some((role) => _kc.hasRealmRole(role));
// };

const UserService = {
  doLogin,
  doLogout,
  isLoggedIn,
  getToken,
  updateToken,
  getUsername,
  hasRole,
  refreshToken,
  getLastname,
  LoggedIn,
  getEmail,
  authenticated,
  account,
  getrole,
  getFirstname
};

export default UserService;