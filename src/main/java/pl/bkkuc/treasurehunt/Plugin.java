package pl.bkkuc.treasurehunt;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bkkuc.treasurehunt.commands.MainCommand;
import pl.bkkuc.treasurehunt.config.ConfigData;
import pl.bkkuc.treasurehunt.listeners.EventListener;
import pl.bkkuc.treasurehunt.utilities.FileUtility;

@Getter
public final class Plugin extends JavaPlugin {

    @Getter
    private static Plugin instance;

    private ConfigData configData;
    private FileConfiguration items;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        configData = new ConfigData(getConfig());
        items = FileUtility.get("items.yml");

        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        new MainCommand(this, "treasurehunt");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
