package studio.magemonkey.divinity.modules.list.augments.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.magemonkey.divinity.Perms;
import studio.magemonkey.divinity.modules.command.MCmd;
import studio.magemonkey.divinity.modules.list.augments.AugmentsManager;

public class AugmentsInfoCmd extends MCmd<AugmentsManager> {

    public AugmentsInfoCmd(@NotNull AugmentsManager module) {
        super(module, new String[]{"info"}, Perms.AUGMENTS_CMD_INFO);
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
	protected void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        player.sendMessage("Augments Module!");
    }

}
