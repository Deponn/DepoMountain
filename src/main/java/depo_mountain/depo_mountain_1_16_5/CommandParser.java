package depo_mountain.depo_mountain_1_16_5;

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
    public boolean isSuccess;
    /**
     * 境界にラピスラズリブロック配置モード、trueの場合境界をなめらかにする
     */
    public boolean bCollectBorder;

    /**
     * y軸の範囲を無視しない、falseの場合高度上限から岩盤までやる
     */
    public boolean b_Y_Limited;
    /**
     * 補間する頂点(ラピスラズリブロック)の数
     */
    public int kNum;
    /**
     * 補間するときの傾き(距離の乗算を決める)
     */
    public double b_degree;
    /**
     * 資源量の倍率
     */
    public float resource;
    /**
     * 資源量の倍率
     */
    public boolean Box;


    // パース成功
    private CommandParser(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    // パース失敗
    private CommandParser() {
        this(false);
    }

    /**
     * コマンドのTAB補完候補を返す
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    public static List<String> suggestCommand_Mountain(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        if (argsList.size() > 1 && "-k".equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList("0", "5", "20", "100");
        } else if (argsList.size() > 1 && "-degree".equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList("2", "3", "4");
        } else if (argsList.size() > 1 && "-resource".equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList("1", "2", "3");
        } else {
            return Stream.of("-NoBorder", "-NoLimited", "-k", "-degree", "-resource")
                    .filter(s -> !argsList.contains(s))
                    .collect(Collectors.toList());
        }
    }
    public static List<String> suggestCommand_Ground(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        if (argsList.size() > 1 && "-resource".equals(argsList.get(argsList.size() - 2))) {
            return Arrays.asList("1", "2", "3");
        } else {
            return Stream.of("-Box", "-NoLimited", "-resource")
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
        boolean bCollectBorder = true;
        boolean b_Y_Limited = true;
        float resource = 1;
        int numInterpolationPoints = 0;
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
                b_degree = Integer.parseInt(argsList.get(index + 1));
                if (b_degree < 0) {
                    sender.sendMessage(ChatColor.RED + "数値は正の数である必要があります。 -degree <数字>");
                    return new CommandParser();
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。 -degree <数字>");
                return new CommandParser();
            }
        }
        if (argsList.contains("-resource")) {
            // 引数が何番目か取得し、若い番号を採用する
            int index = argsList.indexOf("-resource");
            if (index + 1 >= argsList.size()) {
                // 引数の次がなかった場合、エラー
                sender.sendMessage(ChatColor.RED + "数値が必要です。 -resource <数字>");
                return new CommandParser();
            }
            try {
                // 補間する頂点(ラピスラズリブロック)の数
                resource = Float.parseFloat(argsList.get(index + 1));
                if (resource <= 0) {
                    sender.sendMessage(ChatColor.RED + "数値は正の数である必要があります。 -resource <数字>");
                    return new CommandParser();
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値が不正です。 -resource <数字>");
                return new CommandParser();
            }
        }
        CommandParser Me = new CommandParser(true);
        Me.bCollectBorder = bCollectBorder;
        Me.b_Y_Limited = b_Y_Limited;
        Me.resource = resource;
        Me.kNum = numInterpolationPoints;
        Me.b_degree = b_degree;
        Me.Box = box;
        return Me;
    }
}
