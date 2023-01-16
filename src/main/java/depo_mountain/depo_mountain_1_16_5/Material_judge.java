package depo_mountain.depo_mountain_1_16_5;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;

import java.util.ArrayList;
import java.util.List;

public class Material_judge {

    public static boolean GroundJudge(World world, BlockVector3 position){
        List<BlockState> GroundMaterial = new ArrayList<>();
        GroundMaterial.add(BlockTypes.STONE.getDefaultState());
        GroundMaterial.add(BlockTypes.ANDESITE.getDefaultState());
        GroundMaterial.add(BlockTypes.GRANITE.getDefaultState());
        GroundMaterial.add(BlockTypes.DIORITE.getDefaultState());
        GroundMaterial.add(BlockTypes.COAL_ORE.getDefaultState());
        GroundMaterial.add(BlockTypes.LAPIS_ORE.getDefaultState());
        GroundMaterial.add(BlockTypes.REDSTONE_ORE.getDefaultState());
        GroundMaterial.add(BlockTypes.DIAMOND_ORE.getDefaultState());
        GroundMaterial.add(BlockTypes.IRON_ORE.getDefaultState());
        GroundMaterial.add(BlockTypes.GOLD_ORE.getDefaultState());
        GroundMaterial.add(BlockTypes.GRASS_BLOCK.getDefaultState());
        GroundMaterial.add(BlockTypes.DIRT.getDefaultState());
        GroundMaterial.add(BlockTypes.SAND.getDefaultState());
        GroundMaterial.add(BlockTypes.SANDSTONE.getDefaultState());
        GroundMaterial.add(BlockTypes.CLAY.getDefaultState());
        GroundMaterial.add(BlockTypes.GRAVEL.getDefaultState());
        boolean flag = false;
        for(BlockState blockState : GroundMaterial){
            if(world.getBlock(position)==blockState){
                flag = true;
            }
        }
        return flag;
    }

    public static boolean AirJudge(World world, BlockVector3 position){
        List<BlockState> AirMaterial = new ArrayList<>();
        AirMaterial.add(BlockTypes.AIR.getDefaultState());
        AirMaterial.add(BlockTypes.GRASS.getDefaultState());
        AirMaterial.add(BlockTypes.WATER.getDefaultState());
        AirMaterial.add(BlockTypes.ACACIA_LEAVES.getDefaultState());
        AirMaterial.add(BlockTypes.AZALEA_LEAVES.getDefaultState());
        AirMaterial.add(BlockTypes.BIRCH_LEAVES.getDefaultState());
        AirMaterial.add(BlockTypes.DARK_OAK_LEAVES.getDefaultState());
        AirMaterial.add(BlockTypes.OAK_LEAVES.getDefaultState());
        AirMaterial.add(BlockTypes.JUNGLE_LEAVES.getDefaultState());
        AirMaterial.add(BlockTypes.MANGROVE_LEAVES.getDefaultState());
        AirMaterial.add(BlockTypes.SPRUCE_LEAVES.getDefaultState());
        boolean flag = false;
        for(BlockState blockState : AirMaterial){
            if(world.getBlock(position)==blockState){
                flag = true;
            }
        }
        return flag;
    }
}
