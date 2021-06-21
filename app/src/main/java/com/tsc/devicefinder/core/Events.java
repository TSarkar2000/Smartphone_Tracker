package com.tsc.devicefinder.core;

public class Events {

    private Events() {}

    private static Events events;

    public static Events getInstance() {
        if(events == null)
            events = new Events();
        return events;
    }

    private AuthMessageListener authMessageListener;

    public void registerAuthMessageListener(AuthMessageListener listener) {
        authMessageListener = listener;
    }

    public void fireAuthMessageEvent(String message, int extra) {
        if(authMessageListener != null)
            authMessageListener.onAuthMessageReceived(message, extra);
    }

    public interface AuthMessageListener {
        void onAuthMessageReceived(String message, int extra);
    }
}
