package de.blutmondgilde.otherlivingbeings.util;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;

public class TickCountDown {
    private int tick = 20;
    private HashMap<TickCondition, ArrayList<Runnable>> tickListener = new HashMap<>();


    public void tick() {
        this.tickListener.keySet().stream()
                .filter(tickCondition -> tickCondition.check(this.tick))
                .map(tickCondition -> tickListener.get(tickCondition))
                .forEach(runnables -> runnables.forEach(Runnable::run));

        if (tick == 0) {
            tick = 20;
        } else {
            tick--;
        }
    }

    public int getCurrentTick() {
        return this.tick;
    }

    public void addListener(TickCondition condition, Runnable runnable) {
        if (tickListener.containsKey(condition)) {
            ArrayList<Runnable> actions = tickListener.get(condition);
            actions.add(runnable);
            tickListener.put(condition, actions);
        } else {
            tickListener.put(condition, Lists.newArrayList(runnable));
        }
    }
}
