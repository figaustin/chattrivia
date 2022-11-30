package com.etsuni.chattrivia;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ChatTrivia extends JavaPlugin implements Listener {

    private File customConfigFile;
    private FileConfiguration customConfig;

    public static Question currentQuestion = null;

    private File triviaJSON;
    public static String VERSION;
    protected static ChatTrivia plugin;

    @Override
    public void onEnable() {
        plugin = this;
        VERSION = this.getServer().getVersion();
        createCustomConfig();
        createJson();
        this.getCommand("trivia").setExecutor(new Commands());
        this.getServer().getPluginManager().registerEvents(new Question(), this);
        this.getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if(!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        customConfig = new YamlConfiguration();

        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    private void createJson() {
        triviaJSON = new File(ChatTrivia.plugin.getDataFolder(), "trivia.json");
        if(!triviaJSON.exists()) {
            triviaJSON.getParentFile().mkdirs();
            ChatTrivia.plugin.saveResource("trivia.json", false);
        }

    }


}
