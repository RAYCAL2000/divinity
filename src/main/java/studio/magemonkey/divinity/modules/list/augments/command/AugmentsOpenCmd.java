package studio.magemonkey.divinity.modules.list.augments.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import studio.magemonkey.codex.util.PlayerUT;
import studio.magemonkey.divinity.Perms;
import studio.magemonkey.divinity.modules.command.MCmd;
import studio.magemonkey.divinity.modules.list.augments.AugmentsManager;

public class AugmentsOpenCmd extends MCmd<AugmentsManager> {

    public AugmentsOpenCmd(@NotNull AugmentsManager module) {
        super(module, new String[]{"open"}, Perms.AUGMENTS_CMD_OPEN);
    }

    @NotNull
    @Override
    public String description() {
        return plugin.lang().Augments_Cmd_Info_Desc.getMsg();
    }

    @NotNull
	@Override
	public String usage() {
        return "";
	}

	@Override
	public boolean playersOnly() {
        return true;
	}

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
        if (i == 1) {
            return PlayerUT.getPlayerNames();
        }
        if (i == 2) {
            return Arrays.asList("true", "false");
        }
        return super.getTab(player, i, args);
    }

	@Override
	protected void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2 && !(sender instanceof Player)) {
            this.errSender(sender);
            return;
        }

        String pName = sender.getName();
        if (args.length >= 2) {
            pName = args[1];
        }

        Player player = plugin.getServer().getPlayer(pName);
        if (player == null) {
            this.errPlayer(sender);
            return;
        }

        boolean force = args.length >= 3 ? Boolean.parseBoolean(args[2]) : (args.length < 2 ? true : false);

        this.module.openAugmentsGUI(player, force);

        if (!sender.equals(player)) {
            plugin.lang().Sell_Cmd_Open_Done_Others
                    .replace("%player%", player.getName())
                    .send(sender);
        }
    }

}
