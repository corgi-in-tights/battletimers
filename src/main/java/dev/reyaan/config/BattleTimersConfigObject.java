package dev.reyaan.config;

import java.util.List;

public class BattleTimersConfigObject {
    public String command_prefix = "battletimer";
    public int default_timer_ticks = 2400;
    public int ticks_returned_on_turn_end = 0;
    public int command_default_permission_level = 1;
    public boolean command_use_permissions_api = false;
    public String command_permission = "";
    public boolean declare_forfeit_on_timer_end = true;
    public List<Integer> reminder_timestamps = List.of(2000, 1600, 1200, 1000, 8000, 6000, 4000, 2000, 1000, 500, 400, 300, 200, 100, 80, 60, 40, 20);
    public String timer_start_message = "Battle timer was started by %player:displayname%! <white>You have %battletimers:seconds% seconds remaining.";
    public String timer_stop_message = "Battle timer was stopped by %player:displayname%";
    public String turn_start_message = "<gray>Turn %battletimers:turn% started. <white>You have %battletimers:seconds% seconds remaining.";
    public String timer_reminder_message = "<white>You have <blue>%battletimers:seconds%<reset> <white>seconds remaining.";
    public String time_query_message = "<white>You have <blue>%battletimers:seconds%<reset> <white>seconds remaining.";
    public String timer_end_message = "<red><bold>Your timer ran out and therefore you lost.";
    public String not_in_battle_message = "<red>You are not currently in a battle?";
}
