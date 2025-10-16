// /src/main/java/com/nonkungch/seasonalgrows/CropPlantListener.java

package com.nonkungch.seasonalgrows;

import com.nonkungch.dynamicsurvival.DynamicSurvivalAPI;
import com.nonkungch.dynamicsurvival.Season;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CropPlantListener implements Listener {

    private final Main plugin;
    private final Map<Material, Material> seedToCropMap = new HashMap<>();

    public CropPlantListener(Main plugin) {
        this.plugin = plugin;
        // จับคู่เมล็ดกับต้นพืชที่โตแล้ว
        seedToCropMap.put(Material.WHEAT_SEEDS, Material.WHEAT);
        seedToCropMap.put(Material.CARROT, Material.CARROTS);
        seedToCropMap.put(Material.POTATO, Material.POTATOES);
        seedToCropMap.put(Material.BEETROOT_SEEDS, Material.BEETROOTS);
        seedToCropMap.put(Material.MELON_SEEDS, Material.MELON_STEM);
        seedToCropMap.put(Material.PUMPKIN_SEEDS, Material.PUMPKIN_STEM);
        seedToCropMap.put(Material.NETHER_WART, Material.NETHER_WART);
        seedToCropMap.put(Material.SWEET_BERRIES, Material.SWEET_BERRY_BUSH);
    }

    @EventHandler
    public void onPlayerPlant(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItem();
        Block clickedBlock = event.getClickedBlock();

        if (itemInHand == null || clickedBlock == null || !seedToCropMap.containsKey(itemInHand.getType()) || clickedBlock.getType() != Material.FARMLAND) {
            return;
        }

        DynamicSurvivalAPI dsAPI = plugin.getDsAPI();
        ConfigManager configManager = plugin.getConfigManager();
        if (dsAPI == null || configManager == null) return;

        Season currentSeason = dsAPI.getCurrentSeason();
        Material cropType = seedToCropMap.get(itemInHand.getType());

        if (!configManager.canCropGrow(cropType, currentSeason)) {
            event.setCancelled(true);
            String message = configManager.getCannotPlantMessage()
                    .replace("%crop%", getThaiCropName(cropType))
                    .replace("%season%", currentSeason.getThaiName());
            player.sendMessage(message);
        }
    }

    private String getThaiCropName(Material crop) {
        switch (crop) {
            case WHEAT: return "ข้าวสาลี";
            case CARROTS: return "แครอท";
            case POTATOES: return "มันฝรั่ง";
            case BEETROOTS: return "บีทรูท";
            case MELON_STEM: return "แตงโม";
            case PUMPKIN_STEM: return "ฟักทอง";
            case NETHER_WART: return "เนเธอร์วอร์ท";
            case SWEET_BERRY_BUSH: return "เบอร์รี่";
            default: return crop.name().toLowerCase().replace("_", " ");
        }
    }
}
