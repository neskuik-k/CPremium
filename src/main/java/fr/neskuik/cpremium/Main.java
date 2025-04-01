package fr.neskuik.cpremium;

import co.aikar.commands.PaperCommandManager;
import fr.neskuik.cpremium.commands.ConnectionsCommands;
import fr.neskuik.cpremium.listeners.ConnectionListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new ConnectionsCommands());

        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
    }
}