package me.nico.backpack.components.manager;

import cn.nukkit.Player;
import com.nukkitx.fakeinventories.inventory.ChestFakeInventory;
import com.nukkitx.fakeinventories.inventory.FakeSlotChangeEvent;
import me.nico.backpack.Backpack;

public class BackpackManager {

    public void showBackpack(Player player) {
        ChestFakeInventory chestFakeInventory = new ChestFakeInventory();
        chestFakeInventory.setContents(Backpack.CACHE.get(player.getUniqueId().toString()).getContents());
        chestFakeInventory.addListener(this::onFakeSlotChange);
        chestFakeInventory.setName(player.getName() + "'s Backpack");
        player.addWindow(chestFakeInventory);
    }

    private void onFakeSlotChange(FakeSlotChangeEvent event) {
        Backpack.CACHE.put(event.getPlayer().getUniqueId().toString(), event.getInventory());
    }
}
