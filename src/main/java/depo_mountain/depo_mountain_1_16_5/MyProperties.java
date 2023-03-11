package depo_mountain.depo_mountain_1_16_5;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extension.platform.AbstractPlayerActor;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import depo_mountain.depo_mountain_1_16_5.Constant.Material_judge;
import depo_mountain.depo_mountain_1_16_5.command.CmdName;
import depo_mountain.depo_mountain_1_16_5.command.CommandParser;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MyProperties {

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
    public AbstractPlayerActor wPlayer;
    public CmdName mode;
    public final Material_judge material_judge;

    public MyProperties(){
        material_judge = new Material_judge();
    }

}
