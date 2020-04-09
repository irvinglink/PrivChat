package com.github.irvinglink.PrivChat;

import com.github.irvinglink.PrivChat.Commands.PrivChat;
import com.github.irvinglink.PrivChat.Handlers.Chat;
import com.github.irvinglink.PrivChat.Handlers.StringReplace;
import com.github.irvinglink.PrivChat.Listeners.AsyncChat;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MClass extends JavaPlugin {

    public static MClass plugin;
    private File configFile;
    private File langFile;
    private FileConfiguration config;
    private FileConfiguration lang;
    private Chat chat;

    @Override
    public void onEnable() {

        plugin = this;
        this.chat = new Chat();
        createFiles();

        StringReplace replace = new StringReplace();
        replace.register();

        getServer().getPluginCommand("privchat").setExecutor(new PrivChat(this));

        getServer().getPluginManager().registerEvents(new AsyncChat(this), this);

    }

    public void createFiles() {

        if (!(getDataFolder().exists())) {
            getDataFolder().mkdirs();
        }

        this.configFile = new File(getDataFolder(), "config.yml");

        if (!(this.configFile.exists())) {
            try {
                Files.copy(getResource(this.configFile.getName()), this.configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.langFile = new File(getDataFolder(), "lang.yml");

        if (!(this.langFile.exists())) {
            try {
                Files.copy(getResource(this.langFile.getName()), this.langFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void reloadConfig(){

        if (this.config != null) this.config = YamlConfiguration.loadConfiguration(this.configFile);

        if (this.lang != null) this.lang = YamlConfiguration.loadConfiguration(this.langFile);

    }

    @Override
    public void saveConfig() {

        if (this.config != null) {
            try {
                this.config.save(this.configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (this.lang != null) {
            try {
                this.lang.save(this.langFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public FileConfiguration getLang() {

        if (this.lang != null) return lang;

        this.lang = new YamlConfiguration();

        try {

            assert false;
            this.lang.load(langFile);

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return lang;
    }

    @Override
    public FileConfiguration getConfig() {

        if (this.config != null) return config;

        this.config = new YamlConfiguration();

        try {

            assert false;
            this.config.load(configFile);

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return config;
    }

    public Chat getChat(){
        return this.chat;
    }

    public String getPrefix(){
        return chat.getPrefix();
    }
    
}
