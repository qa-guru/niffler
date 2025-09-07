self.addEventListener("push", function (event) {
    const data = event.data.json();
    const {title, message, interaction} = data;

    const options = {
        body: message,
        icon: '/src/assets/icons/pwa/launchericon-512x512.png',
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