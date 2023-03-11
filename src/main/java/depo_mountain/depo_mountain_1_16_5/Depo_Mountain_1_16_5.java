package depo_mountain.depo_mountain_1_16_5;

import depo_mountain.depo_mountain_1_16_5.command.CmdName;
import depo_mountain.depo_mountain_1_16_5.command.CommandParser;
import depo_mountain.depo_mountain_1_16_5.command.CommandSuggest;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
        for (CmdName cmdName : CmdName.values()) {
            Objects.requireNonNull(this.getCommand(cmdName.getCmd())).setTabCompleter(new CommandSuggest());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("DepoMountainが無効化されました。");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        // プレイヤーがコマンドを投入した際の処理...

        // プレイヤーチェック
        if (!(sender instanceof Player)) {
            // コマブロやコンソールからの実行の場合
            sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみが使えます。");
            return true;
        }
        Player player = (Player) sender;
        //コマンド引数を処理
        CommandParser parser = CommandParser.parseCommand(sender, args);
        if (!parser.isSuccess()) {
            // パース失敗
            return true;
        }
        Operator_CreateMountain operatorCreateMountain = new Operator_CreateMountain(parser, player);

        for (CmdName cmdName : CmdName.values()) {
            if (cmd.getName().equalsIgnoreCase(cmdName.getCmd())) {
                operatorCreateMountain.CreateMountain(cmdName);
            }
        }

        // コマンドが実行されなかった場合は、falseを返して当メソッドを抜ける。
        return false;
    }

}