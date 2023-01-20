package depo_mountain.depo_mountain_1_16_5;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * コマンド処理クラス
 * WorldEditがない環境でWorldEditのクラスを読み込まないよう、別クラスに移動
 */
public class CommandListener implements CommandExecutor, TabCompleter {
    // コマンドを実際に処理
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
        if (!parser.isSuccess) {
            // パース失敗
            return true;
        }
        Operator_CreateMountain operatorCreateMountain = new Operator_CreateMountain(parser, player);

        for (int i = 0; i < Const.getCommandList().size(); i++) {
            if (cmd.getName().equalsIgnoreCase(Const.getCommandList().get(i))) {
                operatorCreateMountain.CreateMountain(Const.getModeList().get(i));
            }
        }


        // コマンドが実行されなかった場合は、falseを返して当メソッドを抜ける。
        return false;
    }

    // コマンドのTAB補完の実装
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase(Const.DpMountain)) {
            return CommandParser.suggestCommand_Mountain(sender, args);
        } else if (command.getName().equalsIgnoreCase(Const.DpGround)) {
            return CommandParser.suggestCommand_Ground(sender, args);
        }else if (command.getName().equalsIgnoreCase(Const.DpGrassHill)) {
            return CommandParser.suggestCommand_Mountain(sender, args);
        }else if (command.getName().equalsIgnoreCase(Const.DpSandHill)) {
            return CommandParser.suggestCommand_Mountain(sender, args);
        }else if (command.getName().equalsIgnoreCase(Const.DpStoneHill)) {
            return CommandParser.suggestCommand_Mountain(sender, args);
        }else if (command.getName().equalsIgnoreCase(Const.DpStoneCeiling)) {
            return CommandParser.suggestCommand_Mountain(sender, args);
        }
        return new ArrayList<>();
    }
}
