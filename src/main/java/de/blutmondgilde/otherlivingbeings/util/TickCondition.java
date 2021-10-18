package de.blutmondgilde.otherlivingbeings.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TickCondition {
    EVERY_TICK(1), EVERY_SECOND_TICK(2), EVERY_FOURTH_TICK(4), EVERY_FIFTH_TICK(5), SKIP_TENTH_TICK(10), ONCE_A_SECOND(20);

    private final int triggerTick;

    public boolean check(int tick) {
        return tick % this.triggerTick == 0;
    }
}
