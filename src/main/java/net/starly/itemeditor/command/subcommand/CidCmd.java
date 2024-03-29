package net.starly.itemeditor.command.subcommand;

import net.starly.core.data.Config;
import net.starly.itemeditor.command.SubCommand;
import net.starly.itemeditor.context.MessageContent;
import net.starly.itemeditor.util.ItemEditUtil;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CidCmd implements SubCommand {
    private static CidCmd instance;

    private CidCmd() {}

    public static CidCmd getInstance() {
        if (instance == null) {
            synchronized (CidCmd.class) {
                instance = new CidCmd();
            }
        }

        return instance;
    }

    @Override
    public boolean executeCommand(Player player, Command cmd, String label, String[] args) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Config msgConfig = MessageContent.getInstance().getConfig();
        if (!player.hasPermission("starly.itemeditor.command.cid")) {
            player.sendMessage(msgConfig.getMessage("errorMessages.noPermission"));
            return true;
        } else if (args.length == 1) {
            player.sendMessage(msgConfig.getMessage("errorMessages.noCustomModelData"));
            return true;
        }

        try {
            ItemMeta.class.getDeclaredMethod("setCustomModelData", Integer.class);
        } catch (NoSuchMethodException ex) {
            player.sendMessage(msgConfig.getMessage("errorMessages.cannotUseCustomModelData"));
            return true;
        }

        int cid;
        try {
            cid = Integer.parseInt(args[1]);
            player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().setCustomMolelData(itemStack, cid));
        } catch (NumberFormatException e) {
            player.sendMessage(msgConfig.getMessage("errorMessages.customModelDataMustBeNumber"));
            return true;
        }

        player.sendMessage(msgConfig.getMessage("messages.customModelDataChanged").replace("{customModelData}", cid + ""));
        return true;
    }
}
