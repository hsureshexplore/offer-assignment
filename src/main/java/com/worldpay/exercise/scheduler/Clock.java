package com.worldpay.exercise.scheduler;

public interface Clock {
    void register(Listener listener);
    void start();
    void stop();

    interface Listener {
        void timeElapsed();
    }
}