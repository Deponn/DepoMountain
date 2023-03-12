package depo_mountain.depo_mountain_1_16_5;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import depo_mountain.depo_mountain_1_16_5.command.CmdName;
import depo_mountain.depo_mountain_1_16_5.command.CommandParser;
import depo_mountain.depo_mountain_1_16_5.task.TaskRun;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Operator_CreateMountain {

    private final boolean isEnabled;
    public final MyProperties prop;

    public Operator_CreateMountain(CommandParser parser, Player player){
        this.prop = new MyProperties();
        // WorldEditを取得
        prop.worldEditPlugin = JavaPlugin.getPlugin(WorldEditPlugin.class);
        prop.player = player;
        prop.wPlayer = prop.worldEditPlugin.wrapPlayer(player);
        prop.wWorld = prop.wPlayer.getWorld();
        prop.commandParser = parser;
        // プレイヤーセッション
        prop.session = WorldEdit.getInstance().getSessionManager().get(prop.wPlayer);

        this.isEnabled = setRegion(parser, player);
    }
    private boolean setRegion(CommandParser parser, Player player){

        if (!prop.session.isSelectionDefined(prop.wWorld)) {
            // 範囲が選択されていない場合
            player.sendMessage(ChatColor.RED + "WorldEditの範囲が選択されていません。");
            return false;
        }

        Region selectRegion;
        try {
            selectRegion = prop.session.getSelection(prop.wWorld);
        } catch (WorldEditException e) {
            // 範囲が不完全です
            player.sendMessage(ChatColor.RED + "WorldEditの範囲が不完全です。");
            return false;
        }

        BlockVector3 minimum_pos = selectRegion.getMinimumPoint();
        BlockVector3 maximum_pos = selectRegion.getMaximumPoint();
        if(parser.b_Y_Limited) {
            minimum_pos = BlockVector3.at(minimum_pos.getBlockX(), minimum_pos.getBlockY(), minimum_pos.getBlockZ());
            maximum_pos = BlockVector3.at(maximum_pos.getBlockX(), maximum_pos.getBlockY(), maximum_pos.getBlockZ());
        }else {
            minimum_pos = BlockVector3.at(minimum_pos.getBlockX(), 0, minimum_pos.getBlockZ());
            maximum_pos = BlockVector3.at(maximum_pos.getBlockX(), 255, maximum_pos.getBlockZ());
        }

        // 範囲を設定
        prop.region = new CuboidRegion(selectRegion.getWorld(), minimum_pos, maximum_pos);
        // 範囲中のラピスラズリブロックの位置を座標指定型で記録
        prop.heightmapArray = new int[prop.region.getWidth()][prop.region.getLength()];
        // 範囲中のラピスラズリブロックの位置をリストとして記録
        prop.heightControlPoints = new ArrayList<>();
        prop.oldHeightmapArray = new int[prop.region.getWidth()][prop.region.getLength()];
        return true;
    }

    public void CreateMountain(CmdName mode){
        if (isEnabled) {
            prop.mode = mode;
            TaskRun taskRun = new TaskRun(prop);
            taskRun.run();
        }
    }
}
