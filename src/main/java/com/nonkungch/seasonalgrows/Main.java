package com.nonkungch.seasonalgrows;

import com.dynamicsurvival.api.DynamicSurvivalAPI;
import com.dynamicsurvival.api.Season;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private DynamicSurvivalAPI dsAPI;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // 1. โหลดและจัดการไฟล์ config.yml
        this.configManager = new ConfigManager(this);
        getLogger().info("Configuration loaded.");

        // 2. เชื่อมต่อกับ API ของ Dynamic Survival
        if (!setupAPI()) {
            getLogger().severe("Failed to hook into Dynamic Survival API. This addon will be disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Successfully hooked into Dynamic Survival API!");

        // 3. ลงทะเบียน Event Listener
        getServer().getPluginManager().registerEvents(new CropGrowthListener(this), this);

        getLogger().info("Seasonal Grows Addon has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Seasonal Grows Addon has been disabled.");
    }

    private boolean setupAPI() {
        if (getServer().getPluginManager().getPlugin("DynamicSurvival") == null) {
            return false;
        }
        // **สำคัญ: แก้ไขวิธีเรียกใช้ API ให้ถูกต้องตาม Document ของ Dynamic Survival**
        try {
            this.dsAPI = DynamicSurvivalAPI.getInstance();
            return this.dsAPI != null;
        } catch (Exception e) {
            getLogger().severe("An error occurred while hooking into the Dynamic Survival API.");
            e.printStackTrace();
            return false;
        }
    }

    // Getter เพื่อให้ Class อื่นเรียกใช้ API และ Config ได้
    public DynamicSurvivalAPI getDsAPI() {
        return dsAPI;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
                                                      }
          
