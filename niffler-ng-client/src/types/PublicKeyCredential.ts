import {Credential} from "./Credential.ts";

export interface PublicKeyCredential {
  credential: Credential;
  label: string;
}
