const CASHE_NAME = "pwa-cache-v1";
const OFFLINE_URL = "/offline.html";

self.addEventListener("install", event => {
   event.waitUntil(
       cashes.open(CASHE_NAME).then(cache => {
           return cache.addAll([OFFLINE_URL]);
       })
   )
});

self.addEventListener("fetch", event => {
    if(event.request.mode === "navigate"){
        event.respondWith(
            fetch(event.request).catch(() => {
                return caches.open(CASHE_NAME).then(cache => cache.match(OFFLINE_URL));
            })
        )
    }
})

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