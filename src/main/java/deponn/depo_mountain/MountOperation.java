package deponn.depo_mountain;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.world.block.*;
import com.sk89q.worldedit.function.mask.AbstractExtentMask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

/**
 * 山を作る際の実際の処理
 * Operationを返す様になっているのは、ティック分散処理ができるようにするため
 * 関数から処理が返った段階ではまだ処理は終わっていないため、Operationの完了を待つ必要がある
 */
public class MountOperation {
    /**
     * ラピスラズリの位置を集める
     *
     * @param wWorld              WorldEdit版ワールド
     * @param region              範囲
     * @param bCollectBorder      縁をなめらかにつなぐかどうか
     * @param heightmapArray      ハイトマップ、高さを保持する
     * @param heightControlPoints ラピスラズリの位置 (出力)
     * @return 操作オブジェクト
     */
    public static Operation collectSurfacePoints(World wWorld, CuboidRegion region, boolean bCollectBorder, int[][] heightmapArray, ArrayList<ControlPointData> heightControlPoints) {
        // 座標
        int x1 = region.getMinimumPoint().getBlockX();
        int y1 = region.getMinimumPoint().getBlockY();
        int z1 = region.getMinimumPoint().getBlockZ();
        int x2 = region.getMaximumPoint().getBlockX();
        int y2 = region.getMaximumPoint().getBlockY();
        int z2 = region.getMaximumPoint().getBlockZ();
        // 操作
        return new RegionOperation(region, heightmapArray, (xPoint, zPoint, top, context) -> {
            // (x,z)におけるラピスラズリブロックのうち最高を記録。なければ-1を代入
            top = -1;
            // y座標方向のループ
            for (int yPoint = y2; yPoint >= y1; yPoint--) {
                // ループで処理する座標のブロックを取得します。
                BlockState currentBlock = wWorld.getBlock(BlockVector3.at(xPoint, yPoint, zPoint));
                if (currentBlock.getBlockType() == BlockTypes.LAPIS_BLOCK) {
                    top = yPoint;
                    break;
                }
                // ボーダーモードがONかつ、座標が縁で空気以外のブロックがあれば、それをラピスブロックとして扱う
                else if (bCollectBorder
                        && (xPoint == x1 || xPoint == x2 || zPoint == z1 || zPoint == z2)
                        && (currentBlock.getBlockType() != BlockTypes.AIR)) {
                    top = yPoint;
                    break;
                }
            }
            // ラピスラズリブロックがあった場合にリストに記録
            if (top != -1) {
                heightControlPoints.add(new ControlPointData(xPoint, zPoint, top));
            }
            return top;
        });
    }

    /**
     * ラピスラズリの位置から面を補間する計算をする
     *
     * @param maxi                最大何個のラピスラズリを加味するか
     * @param region              範囲
     * @param heightmapArray      ハイトマップ、高さを保持する
     * @param heightControlPoints ラピスラズリの位置 (出力)
     * @return 操作オブジェクト
     */
    public static Operation interpolateSurface(int maxi, CuboidRegion region, int[][] heightmapArray, ArrayList<ControlPointData> heightControlPoints,boolean b_round) {
        // 操作
        return new RegionOperation(region, heightmapArray, (xPoint, zPoint, top, context) -> {
            if (b_round) {
                // ラピスラズリブロックがなかった場合、k近傍法を参考にし、y=sum(yn/((x-xn)^2+(z-zn)^2))/sum(1/((x-xn)^2+(z-zn)^2))で標高計算。あった場合そのy座標が標高

                // 距離のリストに変換。
                ArrayList<DistanceHeightData> trainingFixedList = new ArrayList<DistanceHeightData>();
                for (ControlPointData line : heightControlPoints) {
                    trainingFixedList.add(new DistanceHeightData(
                            Math.pow(xPoint - line.xPoint, 2) + Math.pow(zPoint - line.zPoint, 2),
                            line.yHeight
                    ));
                }
                // 距離順にする
                trainingFixedList.sort(Comparator.comparingDouble(a -> a.xzDistance));

                // 計算
                double numerator = 0;
                double denominator = 0;
                for (int i = 0; i < maxi; i++) {
                    numerator += trainingFixedList.get(i).yHeight * Math.exp(-trainingFixedList.get(i).xzDistance);
                    denominator += 1 * Math.exp(-trainingFixedList.get(i).xzDistance);
                }
                // ハイトマップを補間
                top = (int) Math.round(numerator / denominator);

            }else {
                // ラピスラズリブロックがなかった場合、k近傍法を参考にし、y=sum(yn/((x-xn)^2+(z-zn)^2))/sum(1/((x-xn)^2+(z-zn)^2))で標高計算。あった場合そのy座標が標高
                if (top == -1) {
                    // 距離のリストに変換。
                    ArrayList<DistanceHeightData> trainingFixedList = new ArrayList<DistanceHeightData>();
                    for (ControlPointData line : heightControlPoints) {
                        trainingFixedList.add(new DistanceHeightData(
                                Math.pow(xPoint - line.xPoint, 2) + Math.pow(zPoint - line.zPoint, 2),
                                line.yHeight
                        ));
                    }
                    // 距離順にする
                    trainingFixedList.sort(Comparator.comparingDouble(a -> a.xzDistance));

                    // 計算
                    double numerator = 0;
                    double denominator = 0;
                    for (int i = 0; i < maxi; i++) {
                        numerator += trainingFixedList.get(i).yHeight / trainingFixedList.get(i).xzDistance;
                        denominator += 1 / trainingFixedList.get(i).xzDistance;
                    }
                    // ハイトマップを補間
                    top = (int) Math.round(numerator / denominator);
                }
            }
            return top;
        });
    }

    /**
     * 面を実際に地形に反映する
     *
     * @param editSession    セッション情報、これを通してワールドを改変することで「//undo」ができるようになる(+軽い)
     * @param wWorld         WorldEdit版ワールド
     * @param bReplaceAll    空気ブロック以外も書き換えるかどうか
     * @param region         範囲
     * @param heightmapArray ハイトマップ、高さを保持する
     * @return 操作オブジェクト
     */
    public static Operation applySurface(EditSession editSession, World wWorld, boolean bReplaceAll, CuboidRegion region, int[][] heightmapArray) {
        // ブロックを予め定義
        BlockType lapis0 = BlockTypes.LAPIS_BLOCK;
        BlockState lapis1 = Objects.requireNonNull(lapis0).getDefaultState();
        BaseBlock lapis = lapis1.toBaseBlock();
        BlockType air0 = BlockTypes.AIR;
        BlockState air1 = Objects.requireNonNull(air0).getDefaultState();
        BaseBlock air = air1.toBaseBlock();
        BlockType grass0 = BlockTypes.GRASS_BLOCK;
        BlockState grass1 = Objects.requireNonNull(grass0).getDefaultState();
        BaseBlock grass = grass1.toBaseBlock();
        BlockType dirt0 = BlockTypes.DIRT;
        BlockState dirt1 = Objects.requireNonNull(dirt0).getDefaultState();
        BaseBlock dirt = dirt1.toBaseBlock();
        BlockType stone0 = BlockTypes.STONE;
        BlockState stone1 = Objects.requireNonNull(stone0).getDefaultState();
        BaseBlock stone = stone1.toBaseBlock();
        // 座標
        int y1 = region.getMinimumPoint().getBlockY();
        int y2 = region.getMaximumPoint().getBlockY();
        // 操作
        return new RegionOperation(region, heightmapArray, (xPoint, zPoint, top, context) -> {
            // 改変後の地形のパターン
            Pattern pattern = new Pattern() {

                @Override
                public BaseBlock applyBlock (BlockVector3 position){
                    // 表面からの距離に応じて違うブロックにする
                    int yPoint = position.getBlockY();
                    if (top - yPoint < 0) {
                        return air;
                    } else if (top - yPoint < 1) {
                        return grass;
                    } else if (top - yPoint < 5) {
                        return dirt;
                    } else {
                        return stone;
                    }
                }
            };
            // 縦長の範囲を置き換えする
            // ラピスラズリブロックを消去したうえで、標高の地点まで土を盛っていく
            editSession.replaceBlocks(
                    // 範囲、幅と奥行が1マスの縦長の範囲
                    new CuboidRegion(
                            wWorld,
                            BlockVector3.at(xPoint, y1, zPoint),
                            BlockVector3.at(xPoint, bReplaceAll ? y2 : top, zPoint)
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
                            if (compare.equalsFuzzy(lapis)) {
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
            return top;
        });
    }
}
