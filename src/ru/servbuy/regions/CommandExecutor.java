package ru.servbuy.regions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    private static Plugin plugin;
    private String prefix;

    public CommandExecutor(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        prefix = Main.translateColor(Main.locale.getString("prefix") + "&f");
        if (sender instanceof Player)
            player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("rginfo") && args.length == 0){
            PrintColorMessage(sender, "&6&lRegionsInfo v" + plugin.getDescription().getVersion() +
                    "\n&fMade by &b&nvk.com/servbuy\n&fFor help type &a&l/ri help");
                    //"\n&fПлагин создан группой &b&nvk.com/servbuy \n&fАвтор &e&lFlamesYT: &b&nvk.com/dimonl2\n&fДля помощи пиши &a&l/ri help");
            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("rginfo")){
                switch(args[0].toLowerCase()){
                    case "help":
                    case "?":
                        PrintColorMessage(sender, Main.locale.getStringList("help"));
                        return true;
                    case "reload":
                        if (!sender.hasPermission("rginfo.reload"))
                            PrintColorMessage(sender, prefix + Main.locale.getString("noPerm"));
                        else{
                        plugin.reloadConfig();
                        Main.localeFile = new File(plugin.getDataFolder(),"locale.yml");
                        Main.locale = new YamlConfiguration();
                            try {
                            Main.locale.load(Main.localeFile);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        PrintColorMessage(sender, prefix + Main.locale.getString("reloadConfig"));
                        }
                        return true;
                    case "toggle":
                        if (!sender.hasPermission("rginfo.toggle"))
                            PrintColorMessage(sender, prefix + Main.locale.getString("noPerm"));
                        if (player != null && (boolean) plugin.getConfig().get("showBar"))
                            ToggleBar(player, false, prefix + Main.locale.getString("barOff"));
                        else if (player != null)
                            ToggleBar(player, true, prefix + Main.locale.getString("barOn"));
                        return true;
                    default:
                        return false;
                }
        }
        return false;
    }

    private static void ToggleBar(CommandSender sender, boolean b, String s) {
        plugin.getConfig().set("showBar", b);
        PrintColorMessage(sender, s);
        plugin.saveConfig();
    }

    private static void PrintColorMessage(CommandSender sender, String s){
        sender.sendMessage(Main.translateColor(s));
    }

    private static void PrintColorMessage(CommandSender sender, List<String> strings){
        for (String s : strings)
            sender.sendMessage(Main.translateColor(s));
    }
}
