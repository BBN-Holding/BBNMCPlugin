package com.hax.bbnmcplugin;

import com.hax.bbnmcplugin.commands.MLGCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.HashMap;

public class BBNPlugin extends JavaPlugin {

    HashMap<String, String> objectivesMap = new HashMap<>();
    JDA jda = null;

    @Override
    public void onEnable() {
        getLogger().info("BBNPlugin is starting up!");

        this.saveDefaultConfig();

        String token = this.getConfig().getString("token");

        if (token!=null) {
            this.getLogger().warning("Bot starting!");
            try {
                jda = JDABuilder.createDefault(token, Arrays.asList(GatewayIntent.values())).build();
                this.getLogger().warning("Bot started!");
            } catch (LoginException e) {
                e.printStackTrace();
            }
        } else {
            this.getLogger().warning("No Token specified! Bot not starting!");
        }

        Listener listener = new Listener(this, jda);
        listener.setup();
        listener.registerObjectives(this);

        getServer().getPluginManager().registerEvents(listener, this);
        getCommand("MLG").setExecutor(new MLGCommand(this));

    }

    @Override
    public void onDisable() {
        jda.shutdownNow();
        getLogger().info("BBNPlugin is shutting down!");
    }
}
