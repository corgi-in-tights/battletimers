package dev.reyaan.utils;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class QueryUtils {
    public static Optional<PokemonBattle> getBattle(ServerPlayerEntity player) {
        return Optional.ofNullable(Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player));
    }

    public static <T> List<T> iterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    public static Optional<BattleSide> getPlayerSide(ServerPlayerEntity player, PokemonBattle battle) {
        for (var side : iterableToList(battle.getSides())) {
            for (var actor : side.getActors()) {
                var uuids = iterableToList(actor.getPlayerUUIDs());
                if (uuids.contains(player.getUuid())) {
                    return Optional.of(side);
                }
            }
        }
        return Optional.empty();
    }

    public static List<PlayerBattleActor> actorsToPlayerActors(List<BattleActor> actors) {
        return actors.stream().filter(a -> a instanceof PlayerBattleActor).map(a -> (PlayerBattleActor) a).toList();
    }


    public static List<ServerPlayerEntity> getPlayersFromSide(BattleSide side) {
        List<ServerPlayerEntity> l = new ArrayList<>();

        for (var player : side.getBattle().getPlayers()) {
            for (var actor : side.getActors()) {
                if (iterableToList(actor.getPlayerUUIDs()).contains(player.getUuid())) {
                    l.add(player);
                }
            }
        }

        return l;
    }
}
