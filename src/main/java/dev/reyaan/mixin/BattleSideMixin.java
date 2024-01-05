package dev.reyaan.mixin;

import com.cobblemon.mod.common.battles.BattleSide;
import dev.reyaan.accessor.TimerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BattleSide.class)
public class BattleSideMixin implements TimerAccess {
    @Unique
    private int timer = 0;

    @Override
    public int battleTimers$getTimer() {
        return timer;
    }

    @Override
    public void battleTimers$setTimer(int time) {
        timer = Math.max(0, time);
    }
}
