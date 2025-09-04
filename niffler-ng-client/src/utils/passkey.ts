export function b64urlToBuf(b64: string): ArrayBuffer {
    const pad = "=".repeat((4 - (b64.length % 4)) % 4);
    const bin = atob(b64.replace(/-/g, "+").replace(/_/g, "/") + pad);
    const buf = new ArrayBuffer(bin.length);
    const view = new Uint8Array(buf);
    for (let i = 0; i < bin.length; i++) view[i] = bin.charCodeAt(i);
    return buf;
}
export function bufToB64url(buf: ArrayBuffer): string {
    const b = String.fromCharCode(...new Uint8Array(buf));
    return btoa(b)
        .replace(/\+/g, "-")
        .replace(/\//g, "_")
        .replace(/=+$/, "");
}
