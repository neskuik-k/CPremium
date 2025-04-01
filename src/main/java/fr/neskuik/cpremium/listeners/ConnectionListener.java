package fr.neskuik.cpremium.listeners;

import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConnectionListener implements Listener {

    private final Set<Player> frozenPlayers = new HashSet<>();
    private final FileConfiguration usersConfig;

    public ConnectionListener(File dataFolder) {
        File usersFile = new File(dataFolder, "users.yml");
        usersConfig = YamlConfiguration.loadConfiguration(usersFile);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (isPremium(player)) {
            player.sendMessage("Welcome back, premium player!");
        } else if (!usersConfig.contains(player.getUniqueId().toString())) {
            frozenPlayers.add(player);
            player.sendMessage("Please register or login.");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (frozenPlayers.contains(player)) {
            event.setCancelled(true);
            player.sendMessage("You must register or login to move.");
        }
    }

    public void unfreezePlayer(Player player) {
        frozenPlayers.remove(player);
    }

    private boolean isPremium(Player player) {
        return AuthMeApi.getInstance().isPremium(player.getName());
    }
}