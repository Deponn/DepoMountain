package depo_mountain.depo_mountain_1_16_5;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockTypes;

import java.util.Random;
import java.util.Vector;

public class GroundPattern {


    private final BaseBlock[][][] Ground;
    private final int MinX;
    private final int MinY;
    private final int MinZ;
    private final int SizeX;
    private final int SizeY;
    private final int SizeZ;

    public GroundPattern(BlockVector3 MaxPos, BlockVector3 MinPos) {
        BaseBlock STONE = BlockTypes.STONE.getDefaultState().toBaseBlock();
        BaseBlock ANDESITE = BlockTypes.ANDESITE.getDefaultState().toBaseBlock();
        BaseBlock GRANITE = BlockTypes.GRANITE.getDefaultState().toBaseBlock();
        BaseBlock DIORITE = BlockTypes.DIORITE.getDefaultState().toBaseBlock();
        BaseBlock COAL_ORE = BlockTypes.COAL_ORE.getDefaultState().toBaseBlock();
        BaseBlock LAPIS_ORE = BlockTypes.LAPIS_ORE.getDefaultState().toBaseBlock();
        BaseBlock REDSTONE_ORE = BlockTypes.REDSTONE_ORE.getDefaultState().toBaseBlock();
        BaseBlock DIAMOND_ORE = BlockTypes.DIAMOND_ORE.getDefaultState().toBaseBlock();
        BaseBlock IRON_ORE = BlockTypes.IRON_ORE.getDefaultState().toBaseBlock();
        BaseBlock GOLD_ORE = BlockTypes.GOLD_ORE.getDefaultState().toBaseBlock();

        this.SizeX = Math.abs(MaxPos.getX() - MinPos.getX()) + 1;
        this.SizeY = Math.abs(MaxPos.getY() - MinPos.getY()) + 1;
        this.SizeZ = Math.abs(MaxPos.getZ() - MinPos.getZ()) + 1;
        this.MinX = MinPos.getX();
        this.MinY = MinPos.getY();
        this.MinZ = MinPos.getZ();
        this.Ground = new BaseBlock[SizeX][SizeY][SizeZ];
        for (int i = 0; i < SizeX; i++) {
            for (int j = 0; j < SizeY; j++) {
                for (int k = 0; k < SizeZ; k++) {

                    Ground[i][j][k] = STONE;
                }
            }
        }
        int Times = Math.round(SizeX * SizeY * SizeZ / 1000f);

        setRandomBlocks(ANDESITE,Times,2);

        setRandomBlocks(GRANITE,Times,2);

        setRandomBlocks(DIORITE,Times,2);
        Times = Math.round(SizeX * SizeY * SizeZ / 500f);
        setRandomBlocks(COAL_ORE,Times,0);
        Times = Math.round(SizeX * SizeY * SizeZ / 200f);
        setRandomBlocks(IRON_ORE,Times,0);
        Times = Math.round(SizeX * SizeY * SizeZ / 800f);
        setRandomBlocks(REDSTONE_ORE,Times,0);
        Times = Math.round(SizeX * SizeY * SizeZ / 1600f);
        setRandomBlocks(DIAMOND_ORE,Times,0);
        setRandomBlocks(GOLD_ORE,Times,0);
        setRandomBlocks(LAPIS_ORE,Times,0);
    }

    public BaseBlock getBlock(BlockVector3 position) {
        int X = position.getX() - MinX;
        int Y = position.getY() - MinY;
        int Z = position.getZ() - MinZ;
        return Ground[X][Y][Z];
    }

    private void setRandomBlocks(BaseBlock Block,int Times,int size){
        for (int i = 0; i < Times; i++) {
            Random rand = new Random();
            int randX = rand.nextInt(SizeX);
            int randY = rand.nextInt(SizeY);
            int randZ = rand.nextInt(SizeZ);
            setBlocks(Block,size,randX,randY,randZ);
        }
    }

    private void setBlocks(BaseBlock Block,int size,int X, int Y, int Z) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < size + 2 - i; j++) {
                for (int k = 0; k < size + 2 - i - j; k++) {
                    setBlock(Block,X + j, Y + i,Z + k);
                    setBlock(Block,X + j, Y + i,Z - k);
                    setBlock(Block,X + j, Y - i,Z + k);
                    setBlock(Block,X + j, Y - i,Z - k);
                    setBlock(Block,X - j, Y + i,Z + k);
                    setBlock(Block,X - j, Y + i,Z - k);
                    setBlock(Block,X - j, Y - i,Z + k);
                    setBlock(Block,X - j, Y - i,Z - k);
                }
            }
        }
    }

    private void setBlock(BaseBlock Block,int X, int Y, int Z){
        if(inBound(X,Y,Z)){
            Ground[X][Y][Z] = Block;
        }
    }

    private boolean inBound(int X, int Y, int Z) {
        if (X >= 0 & X < SizeX) {
            if (Y >= 0 & Y < SizeY) {
                if (Z >= 0 & Z < SizeZ) {
                    return true;
                }
            }
        }
        return false;
    }
}
