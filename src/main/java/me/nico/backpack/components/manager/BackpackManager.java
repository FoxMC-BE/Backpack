package me.nico.backpack.components.manager;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import com.nukkitx.fakeinventories.inventory.ChestFakeInventory;
import com.nukkitx.fakeinventories.inventory.FakeSlotChangeEvent;
import me.nico.backpack.Backpack;
import me.nico.backpack.components.api.ItemAPI;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BackpackManager {
    private final Backpack plugin;
    private final ItemAPI api;

    public BackpackManager(final Backpack plugin, final ItemAPI api) {
        this.plugin = plugin;
        this.api = api;
    }

    public void showBackpack(@NotNull Player player) {
        ChestFakeInventory chestFakeInventory = new ChestFakeInventory();

        if(!this.plugin.getBackpacks().containsKey(player.getName())) {
            final Map<Integer, Item> items = new HashMap<>();
            this.api.inventoryFromString((String) this.plugin.getBackpackscfg().get(player.getName())).forEach(items::put);

            chestFakeInventory.setContents(items);
            this.plugin.getBackpacks().put(player.getName(), chestFakeInventory);
        } else if(this.plugin.getBackpacks().containsKey(player.getName())) {
            chestFakeInventory.setContents(this.plugin.getBackpacks().get(player.getName()).getContents());
        }

        chestFakeInventory.addListener(this::onFakeSlotChange);
        chestFakeInventory.setName("Backpack");

        player.addWindow(chestFakeInventory);
    }

    public void saveBackpacks(boolean async, Inventory inventory) {
        this.plugin.getBackpacks().forEach((p, __) -> this.plugin.getBackpackscfg().set(p, api.inventoryToString(inventory)));

        this.plugin.getBackpackscfg().save(async);
    }

    private void onFakeSlotChange(final FakeSlotChangeEvent event) {
        if(!(this.plugin.getBackpacks().containsKey(event.getPlayer().getName()))) {
            this.plugin.getBackpacks().put(event.getPlayer().getName(), event.getInventory());
        } else {
            this.plugin.getBackpacks().replace(event.getPlayer().getName(),
                    this.plugin.getBackpacks().get(event.getPlayer().getName()),
                    event.getInventory());
        }
    }
}
