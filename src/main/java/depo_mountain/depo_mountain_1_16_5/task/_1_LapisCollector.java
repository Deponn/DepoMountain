package depo_mountain.depo_mountain_1_16_5.task;


import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
import depo_mountain.depo_mountain_1_16_5.MyProperties;

public class _1_LapisCollector extends _0_taskTemplate {
    public _1_LapisCollector(MyProperties parent) {
        super(parent);
    }

    @Override
    protected void miniTask(int xPoint, int zPoint,int x,int z) {
        // (x,z)におけるラピスラズリブロックのうち最高を記録。なければ-1を代入
        int top = -1;
        // y座標方向のループ
        for (int yPoint = maxY; yPoint >= minY; yPoint--) {
            // ループで処理する座標のブロックを取得します。
            BlockState currentBlock = prop.wWorld.getBlock(BlockVector3.at(xPoint, yPoint, zPoint));
            if (currentBlock.getBlockType() == BlockTypes.LAPIS_BLOCK) {
                top = yPoint;
                break;
            }
            // ボーダーモードがONかつ、座標が縁で空気以外のブロックがあれば、それをラピスブロックとして扱う
            else if (prop.commandParser.bCollectBorder
                    && (xPoint == minX || xPoint == maxX || zPoint == minZ || zPoint == maxZ)
                    && (prop.material_judge.GroundDirtOrStoneJudge(currentBlock))){
                top = yPoint;
                break;
            }
        }
        prop.heightmapArray[xPoint - minX][zPoint - minZ] = top;
        // ラピスラズリブロックがあった場合にリストに記録
        if (top != -1) {
            prop.heightControlPoints.add(new Data_ControlPoint(xPoint, zPoint, top));
        }

        int oldTop = -1;
        // y座標方向のループ
        for (int yPoint = maxY; yPoint >= minY; yPoint--) {
            // ループで処理する座標のブロックを取得します。
            if (prop.material_judge.GroundStoneJudge(prop.wWorld,BlockVector3.at(xPoint, yPoint, zPoint) )) {
                oldTop = yPoint;
                break;
            }

        }
        prop.oldHeightmapArray[xPoint - minX][zPoint - minZ] = oldTop;
    }
}


