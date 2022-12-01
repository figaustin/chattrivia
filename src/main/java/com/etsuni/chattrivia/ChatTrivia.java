package com.etsuni.chattrivia;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class ChatTrivia extends JavaPlugin implements Listener {

    private File customConfigFile;
    private FileConfiguration customConfig;

    private File triviaJSON;
    public static String VERSION;
    protected static ChatTrivia plugin;

    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;

    @Override
    public void onEnable() {
        plugin = this;
        VERSION = this.getServer().getVersion();

        createCustomConfig();
        createJson();
        this.getCommand("trivia").setExecutor(new Commands());
        this.getServer().getPluginManager().registerEvents(new Question(), this);
        this.getServer().getPluginManager().registerEvents(this, this);

        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Trivia trivia = new Trivia();
        trivia.triviaLoop();

    }

    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    public File getCustomConfigFile() {
        return this.customConfigFile;
    }

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    public void setCustomConfig(FileConfiguration fileConfiguration) {
        this.customConfig = fileConfiguration;
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

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }


}
