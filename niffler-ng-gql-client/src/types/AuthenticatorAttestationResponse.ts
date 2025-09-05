export interface AuthenticatorAttestationResponse {
  attestationObject: string;
  clientDataJSON: string;
  transports?: string[];
}
