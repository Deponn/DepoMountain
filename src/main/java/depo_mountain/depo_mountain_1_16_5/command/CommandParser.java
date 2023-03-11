package depo_mountain.depo_mountain_1_16_5.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;


/**
 * コマンドを処理するパーサークラス
 */
public class CommandParser  implements CmdParser {
    /**
     * パース成功したか
     */
    private final boolean isSuccess;
    /**
     * 境界にラピスラズリブロック配置モード、trueの場合境界をなめらかにする
     */
    public final Boolean bCollectBorder;

    /**
     * y軸の範囲を無視しない、falseの場合高度上限から岩盤までやる
     */
    public final Boolean b_Y_Limited;
    /**
     * 補間する頂点(ラピスラズリブロック)の数
     */
    public final Integer kNum;
    /**
     * 補間するときの傾き(距離の乗算を決める)
     */
    public final Double b_degree;
    /**
     * 資源量の倍率
     */
    public final Float resource;
    /**
     * 資源量の倍率
     */
    public final Boolean Box;


    // パース成功
    private CommandParser(boolean isSuccess, Boolean bCollectBorder, Boolean b_Y_Limited,Integer kNum,Double b_degree,Float resource,Boolean box) {
        this.isSuccess = isSuccess;
        this.bCollectBorder = bCollectBorder;
        this.b_Y_Limited = b_Y_Limited;
        this.kNum = kNum;
        this.b_degree = b_degree;
        this.resource = resource;
        this.Box = box;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * コマンドをパースする
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    public static CommandParser parseCommand(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        boolean bCollectBorder = true;
        boolean b_Y_Limited = true;
        float resource = 1;
        int kNum = 0;
        double b_degree = 2;
        boolean box = false;

        if (argsList.contains("-NoBorder")) {
            // 境界にラピスラズリブロック配置モード、trueの場合境界をなめらかにする
            bCollectBorder = false;
        }
        if (argsList.contains("-NoLimited")) {
            // y軸の範囲を無視する
            b_Y_Limited = false;
        }
        if (argsList.contains("-Box")) {
            // 箱型にする
            box = true;
        }
        if (argsList.contains("-k")) {
            // 引数が何番目か取得し、若い番号を採用する
            int index = argsList.indexOf("-k");
            if (index + 1 >= argsList.size()) {
                // 引数の次がなかった場合、エラー
                sender.sendMessage(ChatColor.RED + "数値が必要です。 -k <数字>");
                return new CommandParser(false,null,null,null,null,null,null);
            }
            try {
                // 補間する頂点(ラピスラズリブロック)の数
                kNum = Integer.parseInt(argsList.get(index + 1));
                if (kNum < 0) {
                    sender.sendMessage(ChatColor.RED + "数値は正の数である必要があります。 -k <数字>");
                    return new CommandParser(false,null,null,null,null,null,null);
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。 -k <数字>");
                return new CommandParser(false,null,null,null,null,null,null);
            }
        }
        if (argsList.contains("-degree")) {
            // 引数が何番目か取得し、若い番号を採用する
            int index = argsList.indexOf("-degree");
            if (index + 1 >= argsList.size()) {
                // 引数の次がなかった場合、エラー
                sender.sendMessage(ChatColor.RED + "数値が必要です。 -degree <数字>");
                return new CommandParser(false,null,null,null,null,null,null);
            }
            try {
                // 補間する頂点(ラピスラズリブロック)の数
                b_degree = Integer.parseInt(argsList.get(index + 1));
                if (b_degree < 0) {
                    sender.sendMessage(ChatColor.RED + "数値は正の数である必要があります。 -degree <数字>");
                    return new CommandParser(false,null,null,null,null,null,null);
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。 -degree <数字>");
                return new CommandParser(false,null,null,null,null,null,null);
            }
        }
        if (argsList.contains("-resource")) {
            // 引数が何番目か取得し、若い番号を採用する
            int index = argsList.indexOf("-resource");
            if (index + 1 >= argsList.size()) {
                // 引数の次がなかった場合、エラー
                sender.sendMessage(ChatColor.RED + "数値が必要です。 -resource <数字>");
                return new CommandParser(false,null,null,null,null,null,null);
            }
            try {
                // 補間する頂点(ラピスラズリブロック)の数
                resource = Float.parseFloat(argsList.get(index + 1));
                if (resource <= 0) {
                    sender.sendMessage(ChatColor.RED + "数値は正の数である必要があります。 -resource <数字>");
                    return new CommandParser(false,null,null,null,null,null,null);
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。 -resource <数字>");
                return new CommandParser(false,null,null,null,null,null,null);
            }
        }
        return new CommandParser(true,bCollectBorder,b_Y_Limited,kNum,b_degree,resource,box);
    }
}
