package com.james.breadplugin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BreadPlugin extends JavaPlugin implements Listener {

    private final Set<UUID> recentlyDead = new HashSet<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("BreadPlugin enabled. Bread awaits the fallen.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BreadPlugin disabled. The ovens cool.");
        recentlyDead.clear();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // Track victim for post-respawn bread
        recentlyDead.add(victim.getUniqueId());

        // Give bread to killer
        if (killer != null && killer != victim) {
            ItemStack bread = new ItemStack(Material.BREAD, 5);
            killer.getInventory().addItem(bread);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();

        if (recentlyDead.contains(id)) {
            ItemStack bread = new ItemStack(Material.BREAD, 5);
            player.getInventory().addItem(bread);
            recentlyDead.remove(id);
        }
    }
}