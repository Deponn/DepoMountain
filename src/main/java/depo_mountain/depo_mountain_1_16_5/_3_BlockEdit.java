package depo_mountain.depo_mountain_1_16_5;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.function.mask.AbstractExtentMask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;

public class _3_BlockEdit extends BukkitRunnable {
    private final EditSession editSession;
    private final World wWorld;
    private final boolean bReplaceAll;
    private final CuboidRegion region;
    private final int[][] heightmapArray;
    private final Object_GroundPattern objectGroundPattern;

    public _3_BlockEdit(EditSession editSession, World wWorld, boolean bReplaceAll, CuboidRegion region, int[][] heightmapArray) {
        this.editSession = editSession;
        this.wWorld = wWorld;
        this.bReplaceAll = bReplaceAll;
        this.region = region;
        this.heightmapArray = heightmapArray;
        this.objectGroundPattern = new Object_GroundPattern(region.getMaximumPoint(), region.getMinimumPoint());
    }

    @Override
    public void run() {
        // ブロックを予め定義
        BaseBlock lapisblock = BlockTypes.LAPIS_BLOCK.getDefaultState().toBaseBlock();
        BaseBlock air = BlockTypes.AIR.getDefaultState().toBaseBlock();
        BaseBlock grass = BlockTypes.GRASS_BLOCK.getDefaultState().toBaseBlock();
        BaseBlock dirt = BlockTypes.DIRT.getDefaultState().toBaseBlock();
        BaseBlock bedrock = BlockTypes.BEDROCK.getDefaultState().toBaseBlock();
        // 座標
        int minY = region.getMinimumPoint().getBlockY();
        int maxY = region.getMaximumPoint().getBlockY();
        // 操作
        int minX = region.getMinimumPoint().getBlockX();
        int minZ = region.getMinimumPoint().getBlockZ();
        int widthX = region.getWidth();
        int widthZ = region.getLength();
        for (int i = 0; i < widthX * widthZ; i++) {
            int x = i % widthX;
            int z = i / widthX;
            // x座標方向の座標
            int xPoint = x + minX;
            // z座標方向の座標
            int zPoint = z + minZ;
            int top = heightmapArray[x][z];
            // 改変後の地形のパターン
            Pattern pattern = new Pattern() {
                @Override
                public BaseBlock applyBlock(BlockVector3 position) {
                    // 表面からの距離に応じて違うブロックにする
                    int yPoint = position.getBlockY();
                    if (top - yPoint < 0) {
                        return air;
                    } else if (top - yPoint < 1) {
                        return grass;
                    } else if (top - yPoint < 6) {
                        return dirt;
                    } else if (yPoint < 2) {
                        return bedrock;
                    } else {
                        return objectGroundPattern.getBlock(position);
                    }
                }
            };
            // 縦長の範囲を置き換えする
            // ラピスラズリブロックを消去したうえで、標高の地点まで土を盛っていく
            try {
                editSession.replaceBlocks(
                        // 範囲、幅と奥行が1マスの縦長の範囲
                        new CuboidRegion(
                                wWorld,
                                BlockVector3.at(xPoint, minY, zPoint),
                                BlockVector3.at(xPoint, maxY, zPoint)
                        ),
                        // bReplaceAllがtrueのとき全て書き換え、falseのとき空気のみ書き換える。
                        new AbstractExtentMask(editSession) {
                            @Override
                            public boolean test(BlockVector3 vector) {
                                // ブロックを取得、getBlockと比べ、getLazyBlockはブロックの種類だけを取得できるため軽い
                                BlockState currentBlock = wWorld.getBlock(vector);
                                BaseBlock compare = currentBlock.toBaseBlock();
                                // 変更がなければ更新しない (差分が増えることでメモリを圧迫するため)
                                if (compare.equalsFuzzy(pattern.applyBlock(vector))) {
                                    return false;
                                }
                                // ラピスラズリブロックは置き換え
                                if (compare.equalsFuzzy(lapisblock)) {
                                    return true;
                                }
                                // bReplaceAllがfalseの場合は空気しか置き換えない
                                if (!bReplaceAll && !compare.equalsFuzzy(air)) {
                                    return false;
                                }
                                return true;
                            }

                            @Nullable
                            @Override
                            public Mask2D toMask2D() {
                                return null;
                            }
                        }, pattern);
                editSession.close();
            } catch (MaxChangedBlocksException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
