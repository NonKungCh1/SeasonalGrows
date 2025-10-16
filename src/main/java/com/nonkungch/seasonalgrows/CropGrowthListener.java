// /src/main/java/com/nonkungch/seasonalgrows/CropGrowthListener.java (ฉบับแก้ไข)

package com.nonkungch.seasonalgrows;

import com.nonkungch.dynamicsurvival.DynamicSurvivalAPI;
import com.nonkungch.dynamicsurvival.Season;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class CropGrowthListener implements Listener {

    private final Main plugin;

    public CropGrowthListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCropGrow(BlockGrowEvent event) {
        Block block = event.getBlock();
        Material cropType = event.getNewState().getType();

        DynamicSurvivalAPI dsAPI = plugin.getDsAPI();
        ConfigManager configManager = plugin.getConfigManager();

        if (dsAPI == null || configManager == null) {
            return; // ป้องกัน Error หาก Plugin หลักยังไม่พร้อม
        }

        // ✅ [แก้ไขแล้ว] เรียกใช้เมธอดโดยไม่มี argument
        Season currentSeason = dsAPI.getCurrentSeason();

        // ตรวจสอบกับ Config Manager ว่าพืชชนิดนี้โตได้หรือไม่
        if (!configManager.canCropGrow(cropType, currentSeason)) {
            event.setCancelled(true);
        }
    }
}
