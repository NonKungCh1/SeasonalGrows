package com.nonkungch.seasonalgrows;

import com.dynamicsurvival.api.DynamicSurvivalAPI;
import com.dynamicsurvival.api.Season;
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
        // ใช้ getNewState().getType() จะแม่นยำกว่าสำหรับพืชบางชนิด เช่น ฟักทอง แตงโม
        Material cropType = event.getNewState().getType();

        var dsAPI = plugin.getDsAPI();
        var configManager = plugin.getConfigManager();

        if (dsAPI == null || configManager == null) {
            return; // ป้องกัน Error หาก Plugin หลักยังไม่พร้อม
        }

        // ดึงฤดูปัจจุบันจาก API
        Season currentSeason = dsAPI.getCurrentSeason(block.getWorld());

        // ตรวจสอบกับ Config Manager ว่าพืชชนิดนี้โตได้หรือไม่
        if (!configManager.canCropGrow(cropType, currentSeason)) {
            event.setCancelled(true);
        }
    }
}
