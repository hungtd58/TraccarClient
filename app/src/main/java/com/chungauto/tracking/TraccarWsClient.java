package com.chungauto.tracking;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.util.Map;

public class TraccarWsClient extends WebSocketClient {
    public TraccarWsClient(URI serverUri) {
        super(serverUri);
    }

    public TraccarWsClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    public TraccarWsClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    public TraccarWsClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders) {
        super(serverUri, protocolDraft, httpHeaders);
    }

    public TraccarWsClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e("@@@@@@", "opened connection");
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage(String message) {
        Log.e("@@@@@@", "received: " + message);
        try {
            JSONObject data = new JSONObject(message);
            String imei = data.getString("imei");
            int seconds = data.getInt("data");
            if (onUpdateIntervalCallback != null) {
                onUpdateIntervalCallback.update(imei, seconds);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame
        Log.e("@@@@@@",
                "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                        + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        Log.e("@@@@@@", "OnError");
        // if the error is fatal then onClose will be called additionally
    }

    private OnUpdateIntervalCallback onUpdateIntervalCallback;

    public TraccarWsClient setOnUpdateIntervalCallback(OnUpdateIntervalCallback onUpdateIntervalCallback) {
        this.onUpdateIntervalCallback = onUpdateIntervalCallback;
        return this;
    }

    public interface OnUpdateIntervalCallback {
        void update(String imei, int seconds);
    }
}
