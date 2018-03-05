package com.worldpay.exercise.scheduler;

import java.util.ArrayList;
import java.util.List;

public class ManualClock implements Clock {
    private final List<Listener> listeners = new ArrayList<>();

    @Override
    public void register(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void start() {
        // Ignored
    }

    @Override
    public void stop() {
        // Ignored
    }

    public void elapseTime(){
        listeners.forEach(Listener::timeElapsed);
    }
}