package pl.bkkuc.treasurehunt.manager;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Treasure {

    ItemStack map;
    int x, z;

    public Treasure(ItemStack map, int x, int z) {
        this.map = map;
        this.x = x;
        this.z = z;
    }
}
