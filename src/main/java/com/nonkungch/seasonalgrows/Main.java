// /src/main/java/com/nonkungch/seasonalgrows/Main.java

package com.nonkungch.seasonalgrows;

import com.nonkungch.dynamicsurvival.DynamicSurvivalAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private DynamicSurvivalAPI dsAPI;
    private ConfigManager configManager;
    private CropGUI cropGUI;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        getLogger().info("Configuration loaded.");

        if (!setupAPI()) {
            getLogger().severe("Failed to hook into Dynamic Survival API. This addon will be disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Successfully hooked into Dynamic Survival API!");

        // สร้าง Instance ของ CropGUI
        this.cropGUI = new CropGUI(this);
        
        // ลงทะเบียน Listener ทั้งหมด
        getServer().getPluginManager().registerEvents(new CropGrowthListener(this), this);
        getServer().getPluginManager().registerEvents(new CropPlantListener(this), this);
        
        // ลงทะเบียน Command Executor
        this.getCommand("ssg").setExecutor(new SSGCommand(this, cropGUI));

        getLogger().info("Seasonal Grows Addon has been enabled with new features!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Seasonal Grows Addon has been disabled.");
    }

    private boolean setupAPI() {
        try {
            // ใช้ Class.forName เพื่อให้แน่ใจว่า Plugin หลักโหลดแล้ว
            Class.forName("com.nonkungch.dynamicsurvival.DynamicSurvivalAPI");
            this.dsAPI = DynamicSurvivalAPI.getInstance();
            return this.dsAPI != null;
        } catch (ClassNotFoundException | IllegalStateException e) {
            return false;
        }
    }

    public DynamicSurvivalAPI getDsAPI() {
        return dsAPI;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
