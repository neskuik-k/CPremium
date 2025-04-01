package fr.neskuik.cpremium.listeners;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
            player.sendMessage("Rebienvenue sur le serveur !");
        } else if (!usersConfig.contains(player.getUniqueId().toString())) {
            frozenPlayers.add(player);
            player.sendMessage("§cMerci de te connecter avec /login ou /register.");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (frozenPlayers.contains(player)) {
            event.setCancelled(true);
        }
    }

    public void unfreezePlayer(Player player) {
        frozenPlayers.remove(player);
    }


    private boolean isPremium(Player player) {
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/" + player.getName();
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
                String uuid = response.get("id").getAsString();
                return uuid != null && !uuid.isEmpty();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}