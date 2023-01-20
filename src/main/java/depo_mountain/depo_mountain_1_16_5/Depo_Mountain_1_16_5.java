package depo_mountain.depo_mountain_1_16_5;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Depo_Mountain_1_16_5 extends JavaPlugin {
    @Override
    public void onEnable() {
        // WorldEditがない場合はプラグインを無効化
        if (!getServer().getPluginManager().isPluginEnabled("WorldEdit")) {
            // Plugin startup logic
            getLogger().info("DepoMountainを動作させるためにはWorldEditが必要です。");
            setEnabled(false);
            return;
        }

        // Plugin startup logic
        getLogger().info("DepoMountainが有効化されました。");

        // コマンドを登録
        for(String command : Const.getCommandList()) {
            Objects.requireNonNull(getServer().getPluginCommand(command)).setExecutor(new CommandListener());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("DepoMountainが無効化されました。");
    }
}