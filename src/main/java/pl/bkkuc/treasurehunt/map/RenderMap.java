package pl.bkkuc.treasurehunt.map;

import org.bukkit.entity.Player;
import org.bukkit.map.*;
import org.jetbrains.annotations.NotNull;
import pl.bkkuc.treasurehunt.Plugin;

public class RenderMap extends MapRenderer {

    private final int x,z;

    public RenderMap(int x, int z){
        this.x = x;
        this.z = z;
    }

    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {

        MapCursorCollection cursors = new MapCursorCollection();
        cursors.addCursor(x, z, (byte) 0, Plugin.getInstance().getConfigData().getCursorType().getValue(), true);
        mapCanvas.setCursors(cursors);
    }
}
