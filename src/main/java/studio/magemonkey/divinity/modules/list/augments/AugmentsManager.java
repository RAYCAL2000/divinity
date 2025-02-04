package studio.magemonkey.divinity.modules.list.augments;

import studio.magemonkey.divinity.modules.EModule;
import studio.magemonkey.divinity.modules.api.QModule;
import studio.magemonkey.divinity.modules.list.augments.command.*;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import studio.magemonkey.divinity.Divinity;
import studio.magemonkey.divinity.Perms;

public class AugmentsManager extends QModule {

    private Boolean useGUI = false;
    private AugmentsGUI gui = null;

    public AugmentsManager(@NotNull Divinity plugin) {
        super(plugin);
    }

    @Override
    @NotNull
    public String getId() {
        return EModule.AUGMENTS;
    }

    @Override
    @NotNull
    public String version() {
        return "1.0.0";
    }

    @Override
    public void setup() {

        String path = "general.";

        this.useGUI = this.cfg.getString(path+"mode", "INVENTORY").equalsIgnoreCase("GUI");

        this.moduleCommand.addSubCommand(new AugmentsInfoCmd(this));
        if (useGUI) {
            this.gui = new AugmentsGUI(this);
            this.moduleCommand.addSubCommand(new AugmentsOpenCmd(this));
        }
    }

    @Override
    public void shutdown() {
    }

    public void openAugmentsGUI(@NotNull Player player, boolean isForce) {
        this.gui.open(player, 1);
    }

}
