package deponn.depo_mountain;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * コマンドを処理するパーサークラス
 */
public class CommandParser {
    /**
     * パース成功したか
     */
    public final boolean isSuccess;
    /**
     * 全置き換えモード、trueの場合空気ブロック以外も置き換える
     */
    public final boolean bReplaceAll;
    /**
     * 境界にラピスラズリブロック配置モード、trueの場合境界をなめらかにする
     */
    public final boolean bCollectBorder;

    /**
     * y軸の範囲を無視する
     */
    public final boolean b_Y_Limited;
    /**
     * 補間する頂点(ラピスラズリブロック)の数
     */
    public final int numInterpolationPoints;

    public final double b_degree;

    // パース成功
    private CommandParser(boolean isSuccess, boolean bReplaceAll, boolean bCollectBorder,boolean b_Y_Limited, int numInterpolationPoints,double b_degree) {
        this.isSuccess = isSuccess;
        this.bReplaceAll = bReplaceAll;
        this.bCollectBorder = bCollectBorder;
        this.numInterpolationPoints = numInterpolationPoints;
        this.b_Y_Limited = b_Y_Limited;
        this.b_degree = b_degree;

    }

    // パース失敗
    private CommandParser() {
        this(false, false, false , false,0,0);
    }

    /**
     * コマンドのTAB補完候補を返す
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    public static List<String> suggestCommand(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        if (argsList.size() > 1 && "-k".equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList("0", "5", "20","100");
        } else if (argsList.size() > 1 && "-degree".equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList("2", "3", "4");
        } else {
            return Stream.of("-OnlyAir", "-NoBorder","-NoLimited","-k","-degree")
                    .filter(s -> !argsList.contains(s))
                    .collect(Collectors.toList());
        }
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
        boolean bReplaceAll = true;
        boolean bCollectBorder = true;
        boolean b_Y_Limited = true;

        int numInterpolationPoints = 0;
        double b_degree = 2;

        if (argsList.contains("-OnlyAir")) {
            // 全置き換えモード、trueの場合空気ブロック以外も置き換える
            bReplaceAll = false;
        }
        if (argsList.contains("-NoBorder")) {
            // 境界にラピスラズリブロック配置モード、trueの場合境界をなめらかにする
            bCollectBorder = false;
        }
        if (argsList.contains("-NoLimited")) {
            // y軸の範囲を無視する
            b_Y_Limited = false;
        }
        if (argsList.contains("-k")) {
            // 引数が何番目か取得し、若い番号を採用する
            int index = argsList.indexOf("-k");
            if (index + 1 >= argsList.size()) {
                // 引数の次がなかった場合、エラー
                sender.sendMessage(ChatColor.RED + "数値が必要です。 -k <数字>");
                return new CommandParser();
            }
            try {
                // 補間する頂点(ラピスラズリブロック)の数
                numInterpolationPoints = Integer.parseInt(argsList.get(index + 1));
                if (numInterpolationPoints < 0) {
                    sender.sendMessage(ChatColor.RED + "数値は正の数である必要があります。 -k <数字>");
                    return new CommandParser();
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。 -k <数字>");
                return new CommandParser();
            }
        }
        if (argsList.contains("-degree")) {
            // 引数が何番目か取得し、若い番号を採用する
            int index = argsList.indexOf("-degree");
            if (index + 1 >= argsList.size()) {
                // 引数の次がなかった場合、エラー
                sender.sendMessage(ChatColor.RED + "数値が必要です。 -degree <数字>");
                return new CommandParser();
            }
            try {
                // 補間する頂点(ラピスラズリブロック)の数
                b_degree= Integer.parseInt(argsList.get(index + 1));
                if (b_degree < 0) {
                    sender.sendMessage(ChatColor.RED + "数値は正の数である必要があります。 -degree <数字>");
                    return new CommandParser();
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。 -degree <数字>");
                return new CommandParser();
            }
        }
        return new CommandParser(true, bReplaceAll, bCollectBorder, b_Y_Limited, numInterpolationPoints,b_degree);
    }
}
