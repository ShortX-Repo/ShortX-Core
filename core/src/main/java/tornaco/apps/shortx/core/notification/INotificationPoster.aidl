package tornaco.apps.shortx.core.notification;

interface INotificationPoster {
    // PostNotification
    oneway void post(in ByteArrayWrapper data);
}