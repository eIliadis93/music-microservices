import { AuthConfig } from "angular-oauth2-oidc";
import { environment } from "src/environments/environment";

export const authCodeFlowConfig: AuthConfig = {
    issuer: environment.keycloak.issuer,
    // redirectUri: window.location.origin + '/index.html',
    redirectUri: environment.keycloak.redirectUri,
    clientId: environment.keycloak.clientId,
    responseType: 'code',
    scope: environment.keycloak.scope,
    showDebugInformation: true,
    requireHttps: false
};