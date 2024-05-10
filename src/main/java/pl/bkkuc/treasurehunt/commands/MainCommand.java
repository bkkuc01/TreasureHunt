package pl.bkkuc.treasurehunt.commands;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bkkuc.purutils.ColorUtility;
import pl.bkkuc.purutils.commands.AbstractCommand;
import pl.bkkuc.treasurehunt.Plugin;
import pl.bkkuc.treasurehunt.utilities.FileUtility;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainCommand extends AbstractCommand {


    public MainCommand(JavaPlugin javaPlugin, String commandName) {
        super(javaPlugin, commandName);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.isOp()) {
            sender.sendMessage(ColorUtility.colorize("&cYou don't have permission."));
            return;
        }

        if(args.length == 0 || args[0].equalsIgnoreCase("help")){
            sender.sendMessage(ColorUtility.colorize("&6[!] &fUsage: &7/th add [chance]"));
            return;
        }

        if(args[0].equalsIgnoreCase("add")){
            if(!(sender instanceof Player)) {
                sender.sendMessage(ColorUtility.colorize("&c[!] &fThis command available only ingame!"));
                return;
            }

            ItemStack item = ((Player)sender).getInventory().getItemInMainHand();
            if(item.getType() == Material.AIR){
                sender.sendMessage(ColorUtility.colorize("&c[!] &fPlease hold item in your &7main hand&f!"));
                return;
            }

            int chance;

            try {
                chance = Integer.parseInt(args[1]);
                if(chance <= 0) chance = 0;
                else if(chance >= 100) chance = 100;
            } catch (NumberFormatException e){
                sender.sendMessage(ColorUtility.colorize("&c[!] &7" + args[1] + " &fis not number!"));
                return;
            }

            String uuid = UUID.randomUUID().toString();
            Plugin.getInstance().getItems().set("special-items." + uuid + ".chance", chance);
            Plugin.getInstance().getItems().set("special-items." + uuid + ".item", item);
            FileUtility.save(Plugin.getInstance().getItems(), "items.yml");

            sender.sendMessage(ColorUtility.colorize("&eSuccessfully saved item &6(&7" + uuid + "&6) &ewith chance &6" + chance + "%&e."));
            ((Player)sender).playSound(((Player)sender).getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            return;
        }
        sender.sendMessage(ColorUtility.colorize("&6[!] &fInvalid &7subcommand&f. Use &6/th help&f."));
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        if(!sender.isOp()) return Collections.emptyList();
        if(args.length == 1) return Stream.of("add").collect(Collectors.toList());
        if(args.length == 2) return Stream.of("<chance>", "1", "30", "50", "80", "100").collect(Collectors.toList());
        return Collections.emptyList();
    }
}
