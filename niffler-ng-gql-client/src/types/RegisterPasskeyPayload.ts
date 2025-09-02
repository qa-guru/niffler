import {PublicKeyCredential} from "./PublicKeyCredential.ts";

export interface RegisterPasskeyPayload {
  publicKey: PublicKeyCredential;
}
