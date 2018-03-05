package com.worldpay.exercise.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RealClock implements Clock {
    private final long period;
    private final TimeUnit periodTimeUnit;
    private final List<Listener> listeners = Collections.synchronizedList(new ArrayList<>());
    private final ScheduledExecutorService timerService = Executors.newSingleThreadScheduledExecutor();

    public RealClock(long period, TimeUnit periodTimeUnit) {
        this.period = period;
        this.periodTimeUnit = periodTimeUnit;
    }

    @Override
    public void register(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void start() {
        timerService.scheduleAtFixedRate(this::reportTimeElapse, period, period, periodTimeUnit);
    }

    private void reportTimeElapse() {
        listeners.forEach(Listener::timeElapsed);
    }

    @Override
    public void stop() {
        timerService.shutdown();
    }
}