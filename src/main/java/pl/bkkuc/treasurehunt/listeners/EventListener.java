package pl.bkkuc.treasurehunt.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.loot.LootTable;
import pl.bkkuc.treasurehunt.Plugin;
import pl.bkkuc.treasurehunt.config.ConfigData;
import pl.bkkuc.treasurehunt.manager.Treasure;
import pl.bkkuc.treasurehunt.manager.TreasureBuilder;

import java.util.concurrent.ThreadLocalRandom;

public class EventListener implements Listener {

    @EventHandler
    public void onLootGenerate(LootGenerateEvent e) throws IllegalAccessException {
        ConfigData data = Plugin.getInstance().getConfigData();

        LootTable lootTable = e.getLootTable();
        if(data.getBlacklistLootTables().contains(lootTable)) {
            return;
        }

        int chance = data.getChanceSpawnMap();
        int randomInt = ThreadLocalRandom.current().nextInt(100);
        if(chance < randomInt) {
            return;
        }

        boolean logging = data.isDebugMode();
        Treasure treasure = TreasureBuilder.build(logging);

        e.getLoot().add(treasure.getMap());
        if(logging){
            Plugin.getInstance().getLogger().info("Founded treasure map! X: " + e.getLootContext().getLocation().getBlockX() + ", Z: " + e.getLootContext().getLocation().getBlockZ());
            Plugin.getInstance().getLogger().info("Structure: " + lootTable.getKey().getKey());
        }
    }
}
