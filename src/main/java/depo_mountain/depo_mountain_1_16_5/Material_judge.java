package depo_mountain.depo_mountain_1_16_5;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;

import java.util.ArrayList;
import java.util.List;

public class Material_judge {
    private final List<BlockState> StoneMaterial = new ArrayList<>();
    private final List<BlockState> DirtAndStoneMaterial = new ArrayList<>();
    public Material_judge(){

        StoneMaterial.add(BlockTypes.STONE.getDefaultState());
        StoneMaterial.add(BlockTypes.ANDESITE.getDefaultState());
        StoneMaterial.add(BlockTypes.GRANITE.getDefaultState());
        StoneMaterial.add(BlockTypes.DIORITE.getDefaultState());
        StoneMaterial.add(BlockTypes.COAL_ORE.getDefaultState());
        StoneMaterial.add(BlockTypes.LAPIS_ORE.getDefaultState());
        StoneMaterial.add(BlockTypes.REDSTONE_ORE.getDefaultState());
        StoneMaterial.add(BlockTypes.DIAMOND_ORE.getDefaultState());
        StoneMaterial.add(BlockTypes.IRON_ORE.getDefaultState());
        StoneMaterial.add(BlockTypes.GOLD_ORE.getDefaultState());
        StoneMaterial.add(BlockTypes.EMERALD_ORE.getDefaultState());
        StoneMaterial.add(BlockTypes.SANDSTONE.getDefaultState());
        StoneMaterial.add(BlockTypes.CLAY.getDefaultState());
        StoneMaterial.add(BlockTypes.GRAVEL.getDefaultState());

        DirtAndStoneMaterial.addAll(StoneMaterial);
        DirtAndStoneMaterial.add(BlockTypes.GRASS_BLOCK.getDefaultState());
        DirtAndStoneMaterial.add(BlockTypes.DIRT.getDefaultState());
        DirtAndStoneMaterial.add(BlockTypes.SAND.getDefaultState());
    }
    public boolean GroundStoneJudge(BlockState targetBlockState){

        boolean flag = false;
        for(BlockState blockState : StoneMaterial){
            if (targetBlockState == blockState) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    public boolean GroundStoneJudge(World world, BlockVector3 position){

        boolean flag = false;
        for(BlockState blockState : StoneMaterial){
            if(world.getBlock(position)==blockState){
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean GroundDirtOrStoneJudge(BlockState targetBlockState){

        boolean flag = false;
        for(BlockState blockState : DirtAndStoneMaterial){
            if (targetBlockState == blockState) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    public boolean GroundDirtOrStoneJudge(World world, BlockVector3 position){
        boolean flag = false;
        for(BlockState blockState : DirtAndStoneMaterial){
            if(world.getBlock(position)==blockState){
                flag = true;
                break;
            }
        }
        return flag;
    }
}
