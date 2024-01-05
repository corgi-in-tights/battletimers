package dev.reyaan.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.reyaan.accessor.EnableAccess;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static dev.reyaan.BattleTimers.config;
import static dev.reyaan.utils.MessageUtils.sendMessage;
import static dev.reyaan.utils.QueryUtils.getBattle;
import static net.minecraft.server.command.CommandManager.literal;

public class BattleTimerCommand {
    public String commandPrefix;


    public BattleTimerCommand(String commandPrefix) {
        this.commandPrefix = commandPrefix;
    }

    public static int startTimer(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null || player.getServer() == null) return 0;

        var b = getBattle(player);
        if (b.isPresent()) {
            var battle = b.get();
            ((EnableAccess) battle).battleTimers$startTimerByPlayer(player.getUuid());
            battle.getPlayers().forEach(p -> sendMessage(p, config.timer_start_message));
            return 1;
        }
        sendMessage(player, config.not_in_battle_message);
        return 0;
    }

    public static int stopTimer(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null || player.getServer() == null) return 0;

        var b = getBattle(player);
        if (b.isPresent()) {
            var battle = b.get();
            ((EnableAccess) battle).battleTimers$stopTimerByPlayer(player.getUuid());
            battle.getPlayers().forEach(p -> sendMessage(p, config.timer_stop_message));
            return 1;
        }
        sendMessage(player, config.not_in_battle_message);
        return 0;
    }

    public static int getTimer(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null || player.getServer() == null) return 0;

        var b = getBattle(player);
        if (b.isPresent()) {
            sendMessage(player, config.time_query_message);
            return 1;
        }
        sendMessage(player, config.not_in_battle_message);
        return 0;
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        var main = literal(this.commandPrefix)
                .requires(source -> {
                    if (config.command_use_permissions_api) {
                        return Permissions.check(source, config.command_permission, config.command_default_permission_level);
                    }
                    return source.hasPermissionLevel(config.command_default_permission_level);
                })
                .build();

        var start = literal("start")
                .executes(BattleTimerCommand::startTimer)
                .build();

        var stop = literal("stop")
                .executes(BattleTimerCommand::stopTimer)
                .build();

        var query = literal("query")
                .executes(BattleTimerCommand::getTimer)
                .build();

        dispatcher.getRoot().addChild(main);
        main.addChild(start);
        main.addChild(stop);
        main.addChild(query);
    }
}
