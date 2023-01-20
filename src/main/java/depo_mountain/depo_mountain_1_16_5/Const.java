package depo_mountain.depo_mountain_1_16_5;

import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockTypes;

import java.util.ArrayList;
import java.util.List;

public class Const {
    public static final String DpMountain = "/DpMountain";
    public static final String DpGround = "/DpGround";
    public static final String DpGrassHill = "/DpGrassHill";
    public static final String DpSandHill = "/DpSandHill";
    public static final String DpStoneHill = "/DpStoneHill";
    public static final String DpStoneCeiling = "/DpStoneCeiling";
    public static final String DpLake = "/DpLake";
    public static final String MountainMode = "MountainMode";
    public static final String GroundMode = "GroundMode";
    public static final String GrassHillMode = "GrassHillMode";
    public static final String SandHillMode = "SandHillMode";
    public static final String StoneHillMode = "StoneHillMode";
    public static final String StoneCeilingMode = "StoneCeilingMode";
    public static final String LakeMode = "LakeMode";
    public static final BaseBlock lapisblock = BlockTypes.LAPIS_BLOCK.getDefaultState().toBaseBlock();
    public static final BaseBlock air = BlockTypes.AIR.getDefaultState().toBaseBlock();
    public static final BaseBlock grass = BlockTypes.GRASS_BLOCK.getDefaultState().toBaseBlock();
    public static final BaseBlock dirt = BlockTypes.DIRT.getDefaultState().toBaseBlock();
    public static final BaseBlock bedrock = BlockTypes.BEDROCK.getDefaultState().toBaseBlock();
    public static final BaseBlock STONE = BlockTypes.STONE.getDefaultState().toBaseBlock();
    public static final BaseBlock ANDESITE = BlockTypes.ANDESITE.getDefaultState().toBaseBlock();
    public static final BaseBlock GRANITE = BlockTypes.GRANITE.getDefaultState().toBaseBlock();
    public static final BaseBlock DIORITE = BlockTypes.DIORITE.getDefaultState().toBaseBlock();
    public static final BaseBlock SAND = BlockTypes.SAND.getDefaultState().toBaseBlock();
    public static final BaseBlock SANDSTONE = BlockTypes.SANDSTONE.getDefaultState().toBaseBlock();
    public static final BaseBlock COAL_ORE = BlockTypes.COAL_ORE.getDefaultState().toBaseBlock();
    public static final BaseBlock LAPIS_ORE = BlockTypes.LAPIS_ORE.getDefaultState().toBaseBlock();
    public static final BaseBlock REDSTONE_ORE = BlockTypes.REDSTONE_ORE.getDefaultState().toBaseBlock();
    public static final BaseBlock DIAMOND_ORE = BlockTypes.DIAMOND_ORE.getDefaultState().toBaseBlock();
    public static final BaseBlock IRON_ORE = BlockTypes.IRON_ORE.getDefaultState().toBaseBlock();
    public static final BaseBlock GOLD_ORE = BlockTypes.GOLD_ORE.getDefaultState().toBaseBlock();
    public static final BaseBlock EMERALD = BlockTypes.EMERALD_ORE.getDefaultState().toBaseBlock();

    private Const(){
    }
    public static List<String> getCommandList(){
        List<String> commandList = new ArrayList<>();
        commandList.add(DpMountain);
        commandList.add(DpGround);
        commandList.add(DpGrassHill);
        commandList.add(DpSandHill);
        commandList.add(DpStoneHill);
        commandList.add(DpStoneCeiling);
        commandList.add(DpLake);
        return commandList;
    }
    public static List<String> getModeList(){
        List<String> modeList = new ArrayList<>();
        modeList.add(MountainMode);
        modeList.add(GroundMode);
        modeList.add(GrassHillMode);
        modeList.add(SandHillMode);
        modeList.add(StoneHillMode);
        modeList.add(StoneCeilingMode);
        modeList.add(LakeMode);
        return modeList;
    }
}
