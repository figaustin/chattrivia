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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
        updateConfig();
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

    public void updateConfig() {
        File config = new File(getDataFolder(), "config.yml");
        YamlConfiguration externalYamlConfig = YamlConfiguration.loadConfiguration(config);
        InputStreamReader defConfigStream = new InputStreamReader(getResource("config.yml"), StandardCharsets.UTF_8);
        YamlConfiguration internalYamlConfig = YamlConfiguration.loadConfiguration(defConfigStream);

        for(String str : internalYamlConfig.getKeys(true)) {
            if(!externalYamlConfig.contains(str)) {
                externalYamlConfig.set(str, internalYamlConfig.get(str));
            }
        }
        try {
            externalYamlConfig.save(config);
        } catch (IOException io) {
            io.printStackTrace();
        }


    }


    private void createJson() {
        triviaJSON = new File(ChatTrivia.plugin.getDataFolder(), "trivia.json");
        if(!triviaJSON.exists()) {
            triviaJSON.getParentFile().mkdirs();
            ChatTrivia.plugin.saveResource("trivia.json", false);
        }

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
