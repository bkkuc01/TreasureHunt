package pl.bkkuc.treasurehunt.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import pl.bkkuc.purutils.ColorUtility;
import pl.bkkuc.purutils.inventory.InventoryUtils;
import pl.bkkuc.purutils.inventory.item.ItemBuilder;
import pl.bkkuc.treasurehunt.Plugin;
import pl.bkkuc.treasurehunt.config.ConfigData;
import pl.bkkuc.treasurehunt.map.RenderMap;
import pl.bkkuc.treasurehunt.utilities.Utility;

import java.util.concurrent.ThreadLocalRandom;

public class TreasureBuilder {

    public static Treasure build(boolean debug) throws IllegalAccessException {
        if(Plugin.getInstance().getConfigData().getWorld() == null)
            throw new IllegalArgumentException("World to spawn treasure is not found!");
        ItemStack item = new ItemStack(Material.FILLED_MAP);
        MapMeta meta = (MapMeta) item.getItemMeta();

        MapView mapView = Bukkit.createMap(Plugin.getInstance().getConfigData().getWorld());

        ConfigData configData = Plugin.getInstance().getConfigData();
        Location location = LocationUtils.findRandomLocation(configData.getWorld(), Integer.parseInt(configData.getCoordsMin().split(";")[0]), Integer.parseInt(configData.getCoordsMax().split(";")[0]), Integer.parseInt(configData.getCoordsMin().split(";")[1]), Integer.parseInt(configData.getCoordsMax().split(";")[1]));

        if(location == null){
            throw new IllegalAccessException("Location is null");
        }

        int randX = Utility.randomInt(-30, 124);
        int randZ = Utility.randomInt(-30, 124);

        mapView.setCenterX(location.getBlockX() + randX);
        mapView.setCenterZ(location.getBlockZ() + randZ);

        mapView.addRenderer(new RenderMap(-randX, -randZ));

        mapView.setTrackingPosition(true);
        mapView.setUnlimitedTracking(true);
        mapView.setScale(configData.getMapScale());

        meta.setMapView(mapView);
        item.setItemMeta(meta);

        ItemMeta itemMeta = item.getItemMeta();
        if(Plugin.getInstance().getConfig().get("map.display-name") != null){
            itemMeta.setDisplayName(ColorUtility.colorize(Plugin.getInstance().getConfig().getString("map.display-name")));
        }
        item.setItemMeta(itemMeta);

        Block block = location.getBlock();
        block.setType(Material.CHEST);

        Chest chest = (Chest) block.getState();

        ConfigurationSection itemsSection = Plugin.getInstance().getItems().getConfigurationSection("items");
        ConfigurationSection sitemsSection = Plugin.getInstance().getItems().getConfigurationSection("special-items");

        if(itemsSection != null && !itemsSection.getKeys(false).isEmpty()) {
            for(String name: itemsSection.getKeys(false)){
                int chance = itemsSection.getInt(name + ".chance", 100);
                if(chance >= ThreadLocalRandom.current().nextInt(100)){
                    try {
                        chest.getBlockInventory().setItem(InventoryUtils.getRandomEmptySlot(chest.getBlockInventory()), ItemBuilder.fromConfiguration(itemsSection.getConfigurationSection(name)));
                    } catch (Exception ignored){}
                }
            }
        }
        if(sitemsSection != null && !sitemsSection.getKeys(false).isEmpty()) {
            for(String name: sitemsSection.getKeys(false)){
                int chance = sitemsSection.getInt(name + ".chance", 100);
                if(chance >= ThreadLocalRandom.current().nextInt(100)){
                    try {
                        chest.getBlockInventory().setItem(InventoryUtils.getRandomEmptySlot(chest.getBlockInventory()), sitemsSection.getItemStack(name + ".item"));
                    } catch (Exception ignored){}
                }
            }
        }

        if(debug){
            Plugin.getInstance().getLogger().info("Treasure spawned in location (" + configData.getWorld().getName() + "): x;" + location.getBlockX() + ", y;"+ location.getBlockY() +" z;" + location.getBlockZ());
        }

        return new Treasure(item, location.getBlockX(), location.getBlockZ());
    }
}
