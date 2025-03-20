// SignalRService.java
package com.example.pixelmediaplayer;

import android.util.Log;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import io.reactivex.Single;
import io.reactivex.functions.Action1;

public class SignalRService {
    private static final String TAG = "SignalRService";
    private HubConnection hubConnection;

    public SignalRService(String serverUrl) {
        hubConnection = HubConnectionBuilder.create(serverUrl).build();
    }

    public void start() {
        if (hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
            hubConnection.start().blockingAwait();
            Log.d(TAG, "SignalR connection started");
        }
    }

    public void stop() {
        if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
            hubConnection.stop().blockingAwait();
            Log.d(TAG, "SignalR connection stopped");
        }
    }

    public void sendMessage(String target, Object... args) {
        if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
            hubConnection.send(target, args);
            Log.d(TAG, "Message sent: " + target);
        }
    }

    public void on(String target, Action1<Object[]> handler) {
        hubConnection.on(target, handler, Object[].class);
    }

    public Single<String> handshake() {
        return hubConnection.invoke(String.class, "Handshake", "json", 1);
    }

    public Single<String> registerScreen(String screenId) {
        return hubConnection.invoke(String.class, "RegisterScreen", screenId);
    }

    public Single<String> sendScheduleUpdate(String screenId) {
        return hubConnection.invoke(String.class, "SendScheduleUpdate", screenId);
    }

    public void onScheduleVideo(Action1<Object[]> handler) {
        hubConnection.on("ScheduleVideo", handler, Object[].class);
    }
}