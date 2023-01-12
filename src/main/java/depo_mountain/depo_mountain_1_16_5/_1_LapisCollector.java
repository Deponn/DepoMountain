package depo_mountain.depo_mountain_1_16_5;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;

import java.util.ArrayList;

public class _1_LapisCollector {

    public static void CollectLapis(World wWorld, CuboidRegion region, boolean bCollectBorder, int[][] heightmapArray, ArrayList<Data_ControlPoint> heightControlPoints) {
        // 座標
        int minX = region.getMinimumPoint().getBlockX();
        int minY = region.getMinimumPoint().getBlockY();
        int minZ = region.getMinimumPoint().getBlockZ();
        int maxX = region.getMaximumPoint().getBlockX();
        int maxY = region.getMaximumPoint().getBlockY();
        int maxZ = region.getMaximumPoint().getBlockZ();

        for (int xPoint = minX; xPoint <= maxX; xPoint++) {
            for (int zPoint = minZ; zPoint <= maxZ; zPoint++) {
                // (x,z)におけるラピスラズリブロックのうち最高を記録。なければ-1を代入
                int top = -1;
                // y座標方向のループ
                for (int yPoint = maxY; yPoint >= minY; yPoint--) {
                    // ループで処理する座標のブロックを取得します。
                    BlockState currentBlock = wWorld.getBlock(BlockVector3.at(xPoint, yPoint, zPoint));
                    if (currentBlock.getBlockType() == BlockTypes.LAPIS_BLOCK) {
                        top = yPoint;
                        break;
                    }
                    // ボーダーモードがONかつ、座標が縁で空気以外のブロックがあれば、それをラピスブロックとして扱う
                    else if (bCollectBorder
                            && (xPoint == minX || xPoint == maxX || zPoint == minZ || zPoint == maxZ)
                            && (currentBlock.getBlockType() != BlockTypes.AIR)) {
                        top = yPoint;
                        break;
                    }
                }
                heightmapArray[xPoint - minX][zPoint -minZ] = top;
                // ラピスラズリブロックがあった場合にリストに記録
                if (top != -1) {
                    heightControlPoints.add(new Data_ControlPoint(xPoint, zPoint, top));
                }
            }
        }
    }
}
