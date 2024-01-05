package dev.reyaan.api;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleSide;
import dev.reyaan.accessor.EnableAccess;
import dev.reyaan.accessor.TimerAccess;

public class BattleTimersAPI {
    public static boolean isBattleTimed(PokemonBattle battle) {
        return ((EnableAccess) battle).battleTimers$isTimerEnabled();
    }

    public static void setBattleTimed(PokemonBattle battle, boolean bl) {
        ((EnableAccess) battle).battleTimers$setTimerEnabled(bl);
    }

    public static int getSideTimer(BattleSide side) {
        return ((TimerAccess)(Object)side).battleTimers$getTimer();
    }

    public static void setSideTimer(BattleSide side, int ticks) {
        ((TimerAccess)(Object)side).battleTimers$setTimer(ticks);
    }
}
