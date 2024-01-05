package dev.reyaan.utils;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.ForcePassActionResponse;
import dev.reyaan.api.BattleTimersAPI;

import java.util.Arrays;
import java.util.List;

import static dev.reyaan.BattleTimers.config;
import static dev.reyaan.utils.MessageUtils.sendMessage;
import static dev.reyaan.utils.QueryUtils.actorsToPlayerActors;

public class TimerUtils {

    public static boolean isUnchosenPlayerSide(BattleSide side) {
        return Arrays.stream(side.getActors()).anyMatch(a -> a.getPlayerUUIDs().iterator().hasNext() && a.getMustChoose());
    }

    public static void tickBattle(BattleSide side) {
        if (isUnchosenPlayerSide(side)) {
            var ticks = BattleTimersAPI.getSideTimer(side);
            if (checkForReminder(ticks)) {
                sendMessage(side, config.timer_reminder_message);
            }

            if (ticks == 0) {
                timerEnd(side);
            } else {
                BattleTimersAPI.setSideTimer(side,ticks-1);
            }
        }
    }

    public static boolean checkForReminder(int currentTick) {
        return config.reminder_timestamps.contains(currentTick);
    }


    public static void newTurnStart(BattleSide side) {
        if (config.ticks_returned_on_turn_end > 0) {
            BattleTimersAPI.setSideTimer(
                    side,
                    BattleTimersAPI.getSideTimer(side)+config.ticks_returned_on_turn_end
            );
        }

        sendMessage(side, config.turn_start_message);
    }

    public static void timerEnd(BattleSide side) {
        var battle = side.getBattle();

        Arrays.stream(side.getActors()).toList().forEach((actor) -> {
            if (config.declare_forfeit_on_timer_end) {
                battle.stop();
                var losingPlayerActors = actorsToPlayerActors(Arrays.stream(side.getActors()).toList());
                var winningPlayerActors = actorsToPlayerActors(Arrays.stream(side.getOppositeSide().getActors()).toList());
                CobblemonEvents.BATTLE_VICTORY.emit(new BattleVictoryEvent(battle, winningPlayerActors, losingPlayerActors, false));
            } else {
                actor.setMustChoose(false);
                actor.setActionResponses(List.of(new ForcePassActionResponse()));
            }
            sendMessage(actor, config.timer_end_message);
        });
    }
}
