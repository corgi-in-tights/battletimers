package dev.reyaan.accessor;


import java.util.UUID;

public interface EnableAccess {
    boolean battleTimers$isTimerEnabled();
    void battleTimers$setTimerEnabled(boolean bl);
    void battleTimers$startTimerByPlayer(UUID uuid);
    void battleTimers$stopTimerByPlayer(UUID uuid);
    UUID battleTimers$getCurrentOriginPlayer();
}
