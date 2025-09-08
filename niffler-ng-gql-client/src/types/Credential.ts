import {AuthenticatorAttestationResponse} from "./AuthenticatorAttestationResponse.ts";

export interface Credential {
  id: string;
  rawId: string;
  type: string;
  response: AuthenticatorAttestationResponse;
}
