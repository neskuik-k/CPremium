package fr.neskuik.cpremium.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

@CommandAlias("cpremium")
public class ConnectionsCommands extends BaseCommand {

    private final File usersFile;
    private final FileConfiguration usersConfig;

    public ConnectionsCommands(File dataFolder) {
        usersFile = new File(dataFolder, "users.yml");
        usersConfig = YamlConfiguration.loadConfiguration(usersFile);
    }

    @Subcommand("register")
    @Description("Register a new player")
    public void onRegister(Player player, String password) {
        usersConfig.set(player.getUniqueId().toString(), password);
        saveConfig();
        player.sendMessage("§cTu as bien été enregistré !");
    }

    @Subcommand("unregister")
    @Description("Unregister yourself or another player")
    public void onUnregister(Player player, @Optional String targetPlayerName) {
        if (targetPlayerName == null) {
            usersConfig.set(player.getUniqueId().toString(), null);
            saveConfig();
            player.sendMessage("You have been unregistered.");
        } else {
            Player targetPlayer = player.getServer().getPlayer(targetPlayerName);
            if (targetPlayer != null) {
                usersConfig.set(targetPlayer.getUniqueId().toString(), null);
                saveConfig();
                player.sendMessage("Player " + targetPlayerName + " has been unregistered.");
            } else {
                player.sendMessage("Player not found.");
            }
        }
    }

    private void saveConfig() {
        try {
            usersConfig.save(usersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}