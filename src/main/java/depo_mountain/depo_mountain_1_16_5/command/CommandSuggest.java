package depo_mountain.depo_mountain_1_16_5.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandSuggest implements TabCompleter{
    /**
     * コマンドのTAB補完候補を返す
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    // コマンドのTAB補完の実装
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase(CmdName.Mountain.getCmd())) {
            return suggestCommand_Mountain(sender, args);
        } else if (command.getName().equalsIgnoreCase(CmdName.Ground.getCmd())) {
            return suggestCommand_Ground(sender, args);
        }else if (command.getName().equalsIgnoreCase(CmdName.GrassHill.getCmd())) {
            return suggestCommand_Mountain(sender, args);
        }else if (command.getName().equalsIgnoreCase(CmdName.SandHill.getCmd())) {
            return suggestCommand_Mountain(sender, args);
        }else if (command.getName().equalsIgnoreCase(CmdName.StoneHill.getCmd())) {
            return suggestCommand_Mountain(sender, args);
        }else if (command.getName().equalsIgnoreCase(CmdName.StoneCeiling.getCmd())) {
            return suggestCommand_Mountain(sender, args);
        }
        return new ArrayList<>();
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
}
