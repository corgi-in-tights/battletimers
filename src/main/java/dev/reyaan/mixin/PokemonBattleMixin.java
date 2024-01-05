package dev.reyaan.mixin;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleSide;
import dev.reyaan.BattleTimers;
import dev.reyaan.accessor.EnableAccess;
import dev.reyaan.accessor.TimerAccess;
import dev.reyaan.utils.TimerUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;


@Mixin(PokemonBattle.class)
public abstract class PokemonBattleMixin implements EnableAccess {
    @Unique
    private boolean timerEnabled = false;

    @Unique
    private UUID originPlayer = null;

    @Shadow(remap = false) public abstract Iterable<BattleSide> getSides();

    @Inject(method="<init>", at=@At(value="TAIL"), remap = false)
    private void battleTimers$setInitialTimer(CallbackInfo ci) {
        getSides().forEach((s) -> ((TimerAccess) (Object) s).battleTimers$setTimer(BattleTimers.config.default_timer_ticks));
    }

    @Inject(method="tick", at=@At(value="TAIL"), remap = false)
    private void battleTimers$checkTimerEveryTick(CallbackInfo ci) {
        if (battleTimers$isTimerEnabled()) {
            getSides().forEach(TimerUtils::tickBattle);
        }
    }

    @Inject(method="turn", at=@At(value="TAIL"), remap = false)
    private void battleTimers$newTurnMessages(CallbackInfo ci) {
        if (battleTimers$isTimerEnabled()) {
            getSides().forEach(TimerUtils::newTurnStart);
        }
    }

    @Override
    public boolean battleTimers$isTimerEnabled() {
        return timerEnabled;
    }

    @Override
    public void battleTimers$setTimerEnabled(boolean bl) {
        timerEnabled = bl;
        originPlayer = null;
    }

    @Override
    public void battleTimers$startTimerByPlayer(UUID uuid) {
        if (!timerEnabled && originPlayer == null) {
            timerEnabled = true;
            originPlayer = uuid;
        }
    }

    @Override
    public void battleTimers$stopTimerByPlayer(UUID uuid) {
        if (originPlayer == uuid) {
            timerEnabled = false;
            originPlayer = null;
        }
    }

    @Override
    public UUID battleTimers$getCurrentOriginPlayer() {
        return originPlayer;
    }
}
