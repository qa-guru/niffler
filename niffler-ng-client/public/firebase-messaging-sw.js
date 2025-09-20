importScripts('https://www.gstatic.com/firebasejs/12.2.1/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/12.2.1/firebase-messaging-compat.js');

const CASHE_NAME = "pwa-cache-v1";
const OFFLINE_URL = "/offline.html";

self.skipWaiting();
self.addEventListener("activate", event => {
    event.waitUntil(clients.claim());
});

firebase.initializeApp({
    apiKey: "AIzaSyDEtu5oECQq5s8PU--l22YZbt8ck-fB9sI",
    authDomain: "niffler-ea54f.firebaseapp.com",
    projectId: "niffler-ea54f",
    storageBucket: "niffler-ea54f.firebasestorage.app",
    messagingSenderId: "641444169058",
    appId: "1:641444169058:web:045b5f13587012db84983b"
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
  const { title, body, icon, click_action } = payload.notification ?? {};
  self.registration.showNotification(title || 'Notification', {
    body: body || '',
    icon: icon || '/icon.png',
    data: { click_action: click_action || '/' }
  });
});

self.addEventListener('notificationclick', (event) => {
  event.notification.close();
  const url = event.notification?.data?.click_action || '/';
  event.waitUntil(
    clients.matchAll({ type: 'window', includeUncontrolled: true }).then((clientList) => {
      for (const client of clientList) {
        if ('focus' in client && client.url.includes(self.origin)) return client.focus();
      }
      return clients.openWindow(url);
    })
  );
});

self.addEventListener("install", event => {
    event.waitUntil(
        caches.open(CASHE_NAME).then(cache => {
            return cache.addAll([OFFLINE_URL]);
        })
    )
});

self.addEventListener("fetch", (event) => {
    if (event.request.mode === "navigate") {
        event.respondWith((async () => {
            if (!navigator.onLine) {
                const cache = await caches.open("pwa-cache-v1");
                return cache.match("/offline.html");
            }
            return fetch(event.request);
        })());
    }
});

self.addEventListener("push", function (event) {
    const data = event.data.json();
    const {title, message, interaction} = data;

    const options = {
        body: message,
        icon: '/pwa/launchericon-512x512.png',
        vibrate: [100, 50, 100],
        data: {
            dateOfArrival: Date.now()
        },
        actions: [
            {
                action: 'confirm',
                title: 'OK'
            },
            {
                action: 'close',
                title: 'Close notification'
            },
        ],
        requireInteraction: interaction
    };

    event.waitUntil(
        self.registration.showNotification(title, options)
    );
});
