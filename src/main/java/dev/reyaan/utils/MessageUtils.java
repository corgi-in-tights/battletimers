package dev.reyaan.utils;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.BattleSide;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.node.TextNode;
import eu.pb4.placeholders.api.parsers.NodeParser;
import eu.pb4.placeholders.api.parsers.TextParserV1;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static dev.reyaan.utils.QueryUtils.getPlayersFromSide;
import static dev.reyaan.utils.QueryUtils.iterableToList;

public class MessageUtils {
    public static void sendMessage(ServerPlayerEntity player, String message) {
        NodeParser parser = NodeParser.merge(TextParserV1.DEFAULT, Placeholders.DEFAULT_PLACEHOLDER_PARSER);
        TextNode output = parser.parseNode(message);
        Text text = output.toText(PlaceholderContext.of(player));

        player.sendMessage(text);
    }

    public static void sendMessage(BattleSide side, String message) {
        getPlayersFromSide(side).forEach(p -> sendMessage(p, message));
    }

    public static void sendMessage(BattleActor actor, String message) {
        for (var player : actor.getBattle().getPlayers()) {
            if (iterableToList(actor.getPlayerUUIDs()).contains(player.getUuid())) {
                sendMessage(player, message);
            }
        }
    }
}
