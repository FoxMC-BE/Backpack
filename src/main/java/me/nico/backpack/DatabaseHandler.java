package me.nico.backpack;

import NukkitDB.CustomNukkitDB;

import java.util.Map;

public class DatabaseHandler {

    public static Map<String, Object> query(String playerUuid) {
        return CustomNukkitDB.query(playerUuid, "uuid", Backpack.GET.database, Backpack.GET.collection);
    }

    public static void create(Map<String, Object> objectMap) {
        CustomNukkitDB.insertDocument(objectMap, Backpack.GET.database, Backpack.GET.collection);
    }

    public static void update(String playerUuid, String value) {
        CustomNukkitDB.updateDocument(playerUuid, "uuid", "backpack", value, Backpack.GET.database, Backpack.GET.collection);
    }
}
