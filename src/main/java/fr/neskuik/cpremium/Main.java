package fr.neskuik.cpremium;

import co.aikar.commands.PaperCommandManager;
import fr.neskuik.cpremium.commands.ConnectionsCommands;
import fr.neskuik.cpremium.listeners.ConnectionListener;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new ConnectionsCommands(getDataFolder()));

        getServer().getPluginManager().registerEvents(new ConnectionListener(getDataFolder()), this);
    }
}