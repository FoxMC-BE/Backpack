package me.nico.backpack;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.plugin.PluginBase;
import com.nukkitx.fakeinventories.inventory.ChestFakeInventory;
import lombok.Getter;
import me.nico.backpack.commands.BackpackCommand;
import me.nico.backpack.components.api.ItemAPI;
import me.nico.backpack.components.manager.BackpackManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class Backpack extends PluginBase implements Listener {

    private ItemAPI api;
    public static Backpack GET;
    String database;
    String collection;
    public static final Map<String, Inventory> CACHE = new ConcurrentHashMap<>();

    @Getter
    private BackpackManager manager;

    @Override
    public void onEnable() {
        GET = this;
        this.saveDefaultConfig();
        this.database = getConfig().getString("database");
        this.collection = getConfig().getString("collection");
        this.api = new ItemAPI();
        this.manager = new BackpackManager();
        this.getServer().getPluginManager().registerEvents(this, this);
		this.getServer().getCommandMap().register("bp", new BackpackCommand(this));
    }

    @EventHandler
    public void onJoin(PlayerPreLoginEvent e) {
        CompletableFuture.runAsync(() -> this.loadFromDB(e.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        this.save(e.getPlayer(), this.getServer().isRunning());
    }

    public void save(Player player, boolean async) {
        if (async) {
            CompletableFuture.runAsync(() -> this.saveInternal(player));
        } else {
            this.saveInternal(player);
        }
    }

    private void saveInternal(Player player) {
        if (player.getUniqueId() == null) {
            this.getLogger().warning("Unable to save " + player.getName() + ": uuid == null");
            return;
        }
        String uuid = player.getUniqueId().toString();
        Inventory save = CACHE.remove(uuid);
        if (save == null) {
            this.getLogger().warning("Unable to save " + player.getName() + ": data does not exist");
            return;
        }
        DatabaseHandler.update(uuid, this.api.inventoryToString(save));
    }

    private void loadFromDB(Player player) {
        if (player.getUniqueId() == null) {
            this.getLogger().warning("Unable to load " + player.getName() + ": uuid == null");
            return;
        }
        String uuid = player.getUniqueId().toString();
        Inventory inv = new ChestFakeInventory();
        Map map = DatabaseHandler.query(uuid);
        if (map == null || !map.containsKey("backpack")) {
            this.getLogger().debug("Creating player data for " + player.getName());
            Map<String, Object> data = new HashMap<>();
            data.put("uuid", uuid);
            data.put("backpack", "");
            DatabaseHandler.create(data);
        } else {
            inv.setContents(this.api.inventoryFromString((String) map.get("backpack")));
        }
        CACHE.put(uuid, inv);
    }
}
