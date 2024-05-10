package pl.bkkuc.treasurehunt.manager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.treasurehunt.Plugin;

import java.util.concurrent.ThreadLocalRandom;

public class LocationUtils {

    public static @Nullable Location findRandomLocation(World world, int minX, int maxX, int minZ, int maxZ) {
        Location randomLocation = null;

        int attempts = 0;
        while (attempts < 10) {
            int randomX = getRandomInt(minX, maxX);
            int randomZ = getRandomInt(minZ, maxZ);
            int y = world.getHighestBlockYAt(randomX, randomZ) - Plugin.getInstance().getConfigData().getYDeep();
            randomLocation = new Location(world, randomX, y, randomZ);

            if (Plugin.getInstance().getConfigData().isCanSpawnInRegion() && isInRegion(randomLocation)) {
                break;
            }

            if(!Plugin.getInstance().getConfigData().getBlackListBiomes().contains(world.getBiome(randomX, y, randomZ))){
                break;
            }

            attempts++;
        }

        return randomLocation;
    }

    private static boolean isInRegion(Location location) {
        ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(BukkitAdapter.adapt(location)).getRegions().stream().findFirst().orElse(null);
        return region != null;
    }

    private static int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max + 1 - min) + min;
    }
}
