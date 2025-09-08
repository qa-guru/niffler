export function normalizePasskeyRegistrationError(e: unknown): string {
  if (e instanceof DOMException) {
    switch (e.name) {
      case 'NotAllowedError':   return 'The operation was cancelled by the user or timed out';
      case 'InvalidStateError': return 'This passkey is already registered on this device';
      case 'NotSupportedError': return 'WebAuthn is not supported in this environment';
      case 'SecurityError':     return 'RP ID / origin mismatch (check your domain and HTTPS)';
      case 'AbortError':        return 'The operation was aborted';
      default:                  return `${e.name}: ${e.message}`;
    }
  }
  return (e as any)?.message ?? 'Unknown error';
}
