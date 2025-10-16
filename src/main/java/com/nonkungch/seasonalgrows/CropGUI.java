// /src/main/java/com/nonkungch/seasonalgrows/CropGUI.java

package com.nonkungch.seasonalgrows;

import com.nonkungch.dynamicsurvival.Season;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CropGUI implements Listener {

    private static final String GUI_TITLE = "§2§lพืชที่สามารถปลูกได้";
    private final Main plugin;

    public CropGUI(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openGUI(Player player) {
        Inventory gui = Bukkit.createInventory(player, 45, GUI_TITLE);
        ConfigManager config = plugin.getConfigManager();

        gui.setItem(11, createSeasonItem(Season.SPRING, "§aฤดูใบไม้ผลิ", config.getAllowedCrops(Season.SPRING)));
        gui.setItem(13, createSeasonItem(Season.SUMMER, "§6ฤดูร้อน", config.getAllowedCrops(Season.SUMMER)));
        gui.setItem(15, createSeasonItem(Season.AUTUMN, "§cฤดูใบไม้ร่วง", config.getAllowedCrops(Season.AUTUMN)));
        gui.setItem(31, createSeasonItem(Season.WINTER, "§bฤดูหนาว", config.getAllowedCrops(Season.WINTER)));

        player.openInventory(gui);
    }

    private ItemStack createSeasonItem(Season season, String name, Set<Material> crops) {
        Material icon = getIconForSeason(season);
        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        List<String> lore = new ArrayList<>();
        lore.add("§7พืชที่สามารถปลูกได้ในฤดูนี้:");
        if (crops == null || crops.isEmpty()) {
            lore.add("§8- ไม่มี -");
        } else {
            for (Material crop : crops) {
                lore.add("§a- " + getThaiCropName(crop));
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private Material getIconForSeason(Season season) {
        switch (season) {
            case SPRING: return Material.OAK_SAPLING;
            case SUMMER: return Material.SUNFLOWER;
            case AUTUMN: return Material.PUMPKIN;
            case WINTER: return Material.SNOWBALL;
            default: return Material.GRASS_BLOCK;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(GUI_TITLE)) {
            event.setCancelled(true);
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
