package pl.bkkuc.treasurehunt.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import pl.bkkuc.treasurehunt.Plugin;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigData {

    List<Biome> blackListBiomes;
    List<LootTable> blacklistLootTables;

    boolean canSpawnInRegion;

    World world;

    String coordsMin, coordsMax;

    int yDeep;
    int chanceSpawnMap;

    @NonFinal
    MapView.Scale mapScale;
    @NonFinal
    MapCursor.Type cursorType;

    boolean debugMode;

    public ConfigData(FileConfiguration configuration){
        this.blackListBiomes = new ArrayList<>();
        this.blacklistLootTables = new ArrayList<>();

        List<String> biomes = configuration.getStringList("blacklist-biomes");
        if(!biomes.isEmpty()) {
            for(String biomeName: biomes){
                biomeName = biomeName.toUpperCase();
                try {
                    Biome biome = Biome.valueOf(biomeName);
                    blackListBiomes.add(biome);
                } catch (IllegalArgumentException e){
                    Plugin.getInstance().getLogger().warning("Biome '" + biomeName + "' is not found.");
                }
            }
        }

        List<String> loottables = configuration.getStringList("blacklist-loottables");
        if(!loottables.isEmpty()) {
            for(String lootTableName: loottables){
                lootTableName = lootTableName.toUpperCase();
                try {
                    LootTables lootTable = LootTables.valueOf(lootTableName);
                    blacklistLootTables.add(lootTable.getLootTable());
                } catch (IllegalArgumentException e){
                    Plugin.getInstance().getLogger().warning("LootTable '" + lootTableName + "' is not found.");
                }
            }
            for(LootTables lootTables: LootTables.values()){
                for(EntityType entityType: EntityType.values()){
                    if(entityType.name().contains(lootTables.name())){
                        blacklistLootTables.add(lootTables.getLootTable());
                    }
                }
            }
        }

        this.canSpawnInRegion = configuration.getBoolean("can-spawn-in-region", false);
        this.world = Bukkit.getWorld(configuration.getString("world", null));

        this.coordsMin = configuration.getString("coords.min", "1500;1500");
        this.coordsMax = configuration.getString("coords.max", "1500;1500");

        this.yDeep = configuration.getInt("coords.y-deep", 15);
        this.chanceSpawnMap = configuration.getInt("chance-spawn-map", 50);

        try {
            mapScale = MapView.Scale.valueOf(configuration.getString("map.scale", "NORMAL"));
        } catch (IllegalArgumentException e){
            Plugin.getInstance().getLogger().warning("Map Scale '" + configuration.getString("map.scale") + "' is not found!");
            mapScale = MapView.Scale.NORMAL;
        }

        try {
            cursorType = MapCursor.Type.valueOf(configuration.getString("map.cursor", "RED_X"));
        } catch (IllegalArgumentException e){
            Plugin.getInstance().getLogger().warning("Map cursor '" + configuration.getString("map.cursor", "RED_X") + "' is not found.");
            cursorType = MapCursor.Type.RED_X;
        }

        this.debugMode = configuration.getBoolean("debug-mode", false);
    }
}
