package depo_mountain.depo_mountain_1_16_5;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extension.platform.AbstractPlayerActor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Operator_CreateMountain {

    private boolean isEnabled;
    // 範囲中のラピスラズリブロックの位置を座標指定型で記録、その後に産出した標高を記録
    public int[][] heightmapArray;
    // 範囲中のラピスラズリブロックの位置をリストとして記録
    public ArrayList<Data_ControlPoint> heightControlPoints;
    public int[][] oldHeightmapArray;
    public CuboidRegion region;
    public World wWorld;
    public CommandParser commandParser;
    public Player player;
    public WorldEditPlugin worldEditPlugin;
    public LocalSession session;
    public AbstractPlayerActor wPlayer;
    public String mode;
    public final Material_judge material_judge;

    public Operator_CreateMountain(CommandParser parser, Player player){
        this.material_judge = new Material_judge();
        // WorldEditを取得
        this.worldEditPlugin = JavaPlugin.getPlugin(WorldEditPlugin.class);
        this.player = player;
        this.wPlayer = worldEditPlugin.wrapPlayer(player);
        this.wWorld = wPlayer.getWorld();
        this.commandParser = parser;
        // プレイヤーセッション
        this.session = WorldEdit.getInstance().getSessionManager().get(wPlayer);

        this.isEnabled = setRegion(parser, player);
    }
    private boolean setRegion(CommandParser parser, Player player){

        if (!session.isSelectionDefined(wWorld)) {
            // 範囲が選択されていない場合
            player.sendMessage(ChatColor.RED + "WorldEditの範囲が選択されていません。");
            return false;
        }

        Region selectRegion;
        try {
            selectRegion = session.getSelection(wWorld);
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
        region = new CuboidRegion(selectRegion.getWorld(), minimum_pos, maximum_pos);
        // 範囲中のラピスラズリブロックの位置を座標指定型で記録
        heightmapArray = new int[region.getWidth()][region.getLength()];
        // 範囲中のラピスラズリブロックの位置をリストとして記録
        heightControlPoints = new ArrayList<>();
        oldHeightmapArray = new int[region.getWidth()][region.getLength()];
        return true;
    }

    public void CreateMountain(String mode){
        if (isEnabled) {
            this.mode = mode;
            Operator_TaskRun taskRun = new Operator_TaskRun(this);
            taskRun.runTaskTimer(JavaPlugin.getPlugin(Depo_Mountain_1_16_5.class), 1, 1);
            isEnabled = false;
        }
    }
}
