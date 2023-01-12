package depo_mountain.depo_mountain_1_16_5;

import com.sk89q.worldedit.EditSession;
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

    // 範囲中のラピスラズリブロックの位置を座標指定型で記録
    private int[][] heightmapArray;
    // 範囲中のラピスラズリブロックの位置をリストとして記録
    private ArrayList<Data_ControlPoint> heightControlPoints;
    private CuboidRegion region;
    private boolean hasRegion;
    private World wWorld;
    private CommandParser commandParser;
    private Player player;
    private WorldEditPlugin worldEditPlugin;

    public Operator_CreateMountain(){
        hasRegion = false;
    }
    public boolean setRegion(CommandParser parser, Player player){
        // WorldEditを取得
        this.worldEditPlugin = JavaPlugin.getPlugin(WorldEditPlugin.class);
        this.player = player;
        AbstractPlayerActor wPlayer = worldEditPlugin.wrapPlayer(player);
        this.wWorld = wPlayer.getWorld();
        this.commandParser = parser;

        // プレイヤーセッション
        LocalSession session = WorldEdit.getInstance()
                .getSessionManager()
                .get(wPlayer);

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
            minimum_pos = BlockVector3.at(minimum_pos.getBlockX() + 1, minimum_pos.getBlockY(), minimum_pos.getBlockZ() + 1);
            maximum_pos = BlockVector3.at(maximum_pos.getBlockX() - 1, maximum_pos.getBlockY(), maximum_pos.getBlockZ() - 1);
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
        hasRegion = true;
        return true;
    }

    public void CreateHill(){
        if (hasRegion) {
            _1_LapisCollector.CollectLapis(wWorld, region,commandParser.bCollectBorder,heightmapArray,heightControlPoints);

            EditSession editSession = worldEditPlugin.createEditSession(player);
            _3_BlockEdit a3BlockEdit = new _3_BlockEdit(editSession,wWorld,commandParser.bReplaceAll,region,heightmapArray);
            _2_CalculationSurface a2CalculationSurface = new _2_CalculationSurface(commandParser.kNum,region,heightmapArray,heightControlPoints,commandParser.b_degree, a3BlockEdit);
            a2CalculationSurface.runTaskLater(JavaPlugin.getPlugin(Depo_Mountain_1_16_5.class),10);

        }
    }



}
