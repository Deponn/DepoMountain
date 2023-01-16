package depo_mountain.depo_mountain_1_16_5;


import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;

public class _1_LapisCollector extends _0_taskTemplate {
    public _1_LapisCollector(Operator_CreateMountain parent) {
        super(parent);
    }

    @Override
    protected void miniTask(int xPoint, int zPoint,int x,int z) {
        // (x,z)におけるラピスラズリブロックのうち最高を記録。なければ-1を代入
        int top = -1;
        // y座標方向のループ
        for (int yPoint = maxY; yPoint >= minY; yPoint--) {
            // ループで処理する座標のブロックを取得します。
            BlockState currentBlock = parent.wWorld.getBlock(BlockVector3.at(xPoint, yPoint, zPoint));
            if (currentBlock.getBlockType() == BlockTypes.LAPIS_BLOCK) {
                top = yPoint;
                break;
            }
            // ボーダーモードがONかつ、座標が縁で空気以外のブロックがあれば、それをラピスブロックとして扱う
            else if (parent.commandParser.bCollectBorder
                    && (xPoint == minX || xPoint == maxX || zPoint == minZ || zPoint == maxZ)
                    && (currentBlock.getBlockType() != BlockTypes.AIR)) {
                top = yPoint;
                break;
            }
        }
        parent.heightmapArray[xPoint - minX][zPoint - minZ] = top;
        // ラピスラズリブロックがあった場合にリストに記録
        if (top != -1) {
            parent.heightControlPoints.add(new Data_ControlPoint(xPoint, zPoint, top));
        }
    }
}


