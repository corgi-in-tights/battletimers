package dev.reyaan;

import dev.reyaan.api.BattleTimersAPI;
import dev.reyaan.config.BattleTimersConfigObject;
import dev.reyaan.config.ConfigHandler;
import dev.reyaan.commands.BattleTimerCommand;
import dev.reyaan.utils.QueryUtils;
import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import net.fabricmc.api.DedicatedServerModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleTimers implements DedicatedServerModInitializer {
	public static String MOD_ID = "battletimers";
	public static String MOD_NAME = "Cobblemon Battle Timers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static ConfigHandler<BattleTimersConfigObject> handler;
	public static BattleTimersConfigObject config;
	
	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}



	@Override
	public void onInitializeServer() {
		LOGGER.info("Initializing " + MOD_ID);
		var configPath = FabricLoader.getInstance().getConfigDir();
		handler = new ConfigHandler<>(configPath.resolve(MOD_ID + ".json").toFile());
		refreshConfig();
		registerCommands();
		registerPlaceholders();

		LOGGER.info("Successfully initialized " + MOD_ID);
	}

	public static void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			new BattleTimerCommand(config.command_prefix).register(dispatcher);
		});
	}

	public static void registerPlaceholders() {
		Placeholders.register(new Identifier(MOD_ID, "turn"), (ctx, arg) -> {
			if (!ctx.hasPlayer())
				return PlaceholderResult.invalid("No player!");
			var b = QueryUtils.getBattle(ctx.player());
            return b.map(battle ->
					PlaceholderResult.value(String.valueOf(battle.getTurn())))
					.orElseGet(() ->
							PlaceholderResult.invalid("Player is not in battle?"));
        });

		Placeholders.register(new Identifier(MOD_ID, "seconds"), (ctx, arg) -> {
			if (!ctx.hasPlayer())
				return PlaceholderResult.invalid("No player!");

			var b = QueryUtils.getBattle(ctx.player());
			if (b.isPresent()) {
				var s = QueryUtils.getPlayerSide(ctx.player(), b.get());
                return s.map(battleSide -> PlaceholderResult.value(String.valueOf(BattleTimersAPI.getSideTimer(battleSide) / 20))).orElseGet(() -> PlaceholderResult.invalid("No side found!"));
            }
			return PlaceholderResult.invalid("No battle found!");
		});

		Placeholders.register(new Identifier(MOD_ID, "ticks"), (ctx, arg) -> {
			if (!ctx.hasPlayer())
				return PlaceholderResult.invalid("No player!");

			var b = QueryUtils.getBattle(ctx.player());
			if (b.isPresent()) {
				var s = QueryUtils.getPlayerSide(ctx.player(), b.get());
                return s.map(battleSide -> PlaceholderResult.value(String.valueOf(BattleTimersAPI.getSideTimer(battleSide)))).orElseGet(() -> PlaceholderResult.invalid("No side found!"));
            }
			return PlaceholderResult.invalid("No battle found!");
		});
	}


	public static void refreshConfig() {
		config = handler.init(BattleTimersConfigObject.class);
	}
}