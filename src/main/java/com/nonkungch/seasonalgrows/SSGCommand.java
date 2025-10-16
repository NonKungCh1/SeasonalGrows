// /src/main/java/com/nonkungch/seasonalgrows/SSGCommand.java

package com.nonkungch.seasonalgrows;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSGCommand implements CommandExecutor {

    private final Main plugin;
    private final CropGUI cropGUI;

    public SSGCommand(Main plugin, CropGUI cropGUI) {
        this.plugin = plugin;
        this.cropGUI = cropGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            // แสดงหน้าต่างช่วยเหลือ
            sender.sendMessage("§a--- Seasonal Grows Help ---");
            sender.sendMessage("§e/ssg gui §7- เปิดหน้าต่างข้อมูลพืชตามฤดูกาล");
            if (sender.hasPermission("seasonalgrows.reload")) {
                sender.sendMessage("§c/ssg reload §7- โหลดไฟล์ตั้งค่าใหม่");
            }
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("gui")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cคำสั่งนี้สามารถใช้ได้โดยผู้เล่นเท่านั้น");
                return true;
            }
            // ผู้เล่นทุกคนใช้ได้ ไม่ต้องเช็ค permission
            cropGUI.openGUI((Player) sender);
            return true;
        }

        if (subCommand.equals("reload")) {
            if (!sender.hasPermission("seasonalgrows.reload")) {
                sender.sendMessage("§cคุณไม่มีสิทธิ์ใช้คำสั่งนี้");
                return true;
            }
            plugin.getConfigManager().loadConfigValues();
            sender.sendMessage("§a[SeasonalGrows] โหลดไฟล์ตั้งค่าใหม่เรียบร้อยแล้ว!");
            return true;
        }

        sender.sendMessage("§cคำสั่งไม่ถูกต้อง. ใช้ /ssg help เพื่อดูคำสั่งทั้งหมด");
        return true;
    }
}
