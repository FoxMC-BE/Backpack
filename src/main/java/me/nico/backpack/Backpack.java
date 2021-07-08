package me.nico.backpack;

import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import com.nukkitx.fakeinventories.inventory.ChestFakeInventory;
import lombok.Getter;
import me.nico.backpack.components.api.ItemAPI;
import me.nico.backpack.components.manager.BackpackManager;
import me.nico.backpack.commands.BackpackCommand;

import java.util.HashMap;
import java.util.Map;

public class Backpack extends PluginBase {

    @Getter
    private Config backpackscfg;

    @Getter
    private Map<String, Inventory> backpacks = new HashMap<>();

    @Getter
    private BackpackManager manager;

    @Override
    public void onLoad() {
		this.backpackscfg = new Config(this.getDataFolder() + "/backpacks.yml", Config.YAML);
		this.manager = new BackpackManager(this, new ItemAPI());
    }

    @Override
    public void onEnable() {
		this.getServer().getCommandMap().register("bp", new BackpackCommand(this));
    }

    @Override
    public void onDisable() {
        this.backpacks.forEach((p, inv) -> this.manager.saveBackpacks(false, inv));
    }
}
