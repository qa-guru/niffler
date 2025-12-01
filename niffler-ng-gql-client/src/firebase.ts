import { initializeApp } from 'firebase/app';
import { getMessaging, getToken, onMessage, isSupported, Messaging } from 'firebase/messaging';

const firebaseConfig = {
    apiKey: `${import.meta.env.VITE_FIREBASE_API_KEY}`,
    authDomain: `${import.meta.env.VITE_FIREBASE_AUTH_DOMAIN}`,
    projectId: `${import.meta.env.VITE_FIREBASE_PROJECT_ID}`,
    storageBucket: `${import.meta.env.VITE_FIREBASE_STORAGE_BUCKET}`,
    messagingSenderId: `${import.meta.env.VITE_FIREBASE_MESSAGING_SENDER_ID}`,
    appId: `${import.meta.env.VITE_FIREBASE_APP_ID}`
};

const firebaseApp = initializeApp(firebaseConfig);

export async function getMessagingIfSupported(): Promise<Messaging | null> {
  try {
    return (await isSupported()) ? getMessaging(firebaseApp) : null;
  } catch {
    return null;
  }
}

export async function ensureServiceWorkerReady(): Promise<ServiceWorkerRegistration> {
  if (!('serviceWorker' in navigator)) throw new Error('ServiceWorker unsupported');
  return navigator.serviceWorker.register('/firebase-messaging-sw.js', { scope: '/' });
}

export async function requestNotificationPermission(): Promise<NotificationPermission> {
  if (!('Notification' in window)) throw new Error('Notification API unsupported');
  if (Notification.permission === 'granted') return 'granted';
  if (Notification.permission === 'denied') return 'denied';
  return await Notification.requestPermission();
}

export async function fetchFcmToken(
  swReg: ServiceWorkerRegistration
): Promise<string | null> {
  const messaging = await getMessagingIfSupported();
  if (!messaging) return null;
  const vapidKey = `${import.meta.env.VITE_FIREBASE_VAPID_KEY}`
  try {
    return await getToken(messaging, { vapidKey, serviceWorkerRegistration: swReg });
  } catch {
    return null;
  }
}

const STORAGE_KEY = 'fcm_registration_token';

export function readStoredFcmToken(): string | null {
  try { return localStorage.getItem(STORAGE_KEY); } catch { return null; }
}
export function writeStoredFcmToken(token: string): void {
  try { localStorage.setItem(STORAGE_KEY, token); } catch {}
}

export async function syncFcmTokenIfChanged(
  swReg: ServiceWorkerRegistration,
  onChanged: (token: string) => Promise<void>
): Promise<string | null> {
  const current = await fetchFcmToken(swReg);
  if (!current) return null;
  const stored = readStoredFcmToken();
  if (stored !== current) {
    await onChanged(current);
    writeStoredFcmToken(current);
  }
  return current;
}

export async function subscribeForegroundMessages(cb: (payload: any) => void) {
  const messaging = await getMessagingIfSupported();
  if (!messaging) return () => {};
  const unsub = onMessage(messaging, cb);
  return unsub;
}
