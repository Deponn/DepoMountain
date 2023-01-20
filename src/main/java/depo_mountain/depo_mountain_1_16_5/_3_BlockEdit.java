package depo_mountain.depo_mountain_1_16_5;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.function.mask.AbstractExtentMask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;

import javax.annotation.Nullable;
import java.util.Objects;

public class _3_BlockEdit extends _0_taskTemplate {

    private final Object_GroundPattern groundPattern;
    private final Material_judge material_judge;
    private EditSession editSession;
    private LocalSession session;

    public _3_BlockEdit(Operator_CreateMountain parent) {
        super(parent);
        // ブロックを予め定義
        this.groundPattern = new Object_GroundPattern(parent.region.getMaximumPoint(), parent.region.getMinimumPoint(), parent.commandParser.resource);
        this.material_judge = new Material_judge();
    }

    public void start() {
        this.editSession = parent.worldEditPlugin.createEditSession(parent.player);
        this.session = WorldEdit.getInstance().getSessionManager().get(parent.wPlayer);
    }

    public void end() {
        editSession.close();
        session.remember(editSession);
    }

    @Override
    protected void miniTask(int xPoint, int zPoint, int x, int z) {
        if (Objects.equals(parent.mode, Const.MountainMode)) {
            Mountain(xPoint, zPoint, x, z);
        } else if (Objects.equals(parent.mode, Const.GroundMode)) {
            Ground(xPoint, zPoint, x, z);
        }else if (Objects.equals(parent.mode, Const.GrassHillMode)) {
            GrassHill(xPoint, zPoint, x, z);
        }else if (Objects.equals(parent.mode, Const.SandHillMode)) {
            SandHill(xPoint, zPoint, x, z);
        }else if (Objects.equals(parent.mode, Const.StoneHillMode)) {
            StoneHill(xPoint, zPoint, x, z);
        }else if (Objects.equals(parent.mode, Const.StoneCeilingMode)) {
            StoneCeiling(xPoint, zPoint, x, z);
        }
    }



    private void Mountain(int xPoint, int zPoint, int x, int z) {
        int top = parent.heightmapArray[x][z];
        // 範囲、幅と奥行が1マスの縦長の範囲
        CuboidRegion miniRegion = new CuboidRegion(parent.wWorld,
                BlockVector3.at(xPoint, minY, zPoint),
                BlockVector3.at(xPoint, maxY, zPoint));
        // 改変後の地形のパターン
        Pattern pattern = new Pattern() {
            @Override
            public BaseBlock applyBlock(BlockVector3 position) {
                // 表面からの距離に応じて違うブロックにする
                int yPoint = position.getBlockY();
                if (top - yPoint < 0) {
                    return Const.air;
                }else if (yPoint < 2) {
                    return Const.bedrock;
                } else {
                    return groundPattern.getBlock(position);
                }
            }
        };
        // bReplaceAllがtrueのとき全て書き換え、falseのとき空気のみ書き換える。
        AbstractExtentMask mask = new AbstractExtentMask(editSession) {
            @Override
            public boolean test(BlockVector3 vector) {
                // ブロックを取得、getBlockと比べ、getLazyBlockはブロックの種類だけを取得できるため軽い
                BlockState currentBlock = parent.wWorld.getBlock(vector);
                BaseBlock compare = currentBlock.toBaseBlock();
                // 変更がなければ更新しない (差分が増えることでメモリを圧迫するため)
                if (compare.equalsFuzzy(pattern.applyBlock(vector))) {
                    return false;
                }
                // ラピスラズリブロックは置き換え
                if (compare.equalsFuzzy(Const.lapisblock)) {
                    return true;
                }

                return true;
            }

            @Nullable
            @Override
            public Mask2D toMask2D() {
                return null;
            }
        };
        // 縦長の範囲を置き換えする
        // ラピスラズリブロックを消去したうえで、標高の地点まで土を盛っていく
        try {
            editSession.replaceBlocks(miniRegion, mask, pattern);
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }

    }
    private void Ground(int xPoint, int zPoint, int x, int z) {
        // 範囲、幅と奥行が1マスの縦長の範囲
        CuboidRegion miniRegion = new CuboidRegion(parent.wWorld,
                BlockVector3.at(xPoint, minY, zPoint),
                BlockVector3.at(xPoint, maxY, zPoint));
        // 改変後の地形のパターン
        Pattern pattern = new Pattern() {
            @Override
            public BaseBlock applyBlock(BlockVector3 position) {
                    return groundPattern.getBlock(position);
            }
        };
        // bReplaceAllがtrueのとき全て書き換え、falseのとき空気のみ書き換える。
        AbstractExtentMask mask = new AbstractExtentMask(editSession) {
            @Override
            public boolean test(BlockVector3 vector) {
                // ブロックを取得、getBlockと比べ、getLazyBlockはブロックの種類だけを取得できるため軽い
                BlockState currentBlock = parent.wWorld.getBlock(vector);
                BaseBlock compare = currentBlock.toBaseBlock();
                // 変更がなければ更新しない (差分が増えることでメモリを圧迫するため)
                if (compare.equalsFuzzy(pattern.applyBlock(vector))) {
                    return false;
                }
                if(material_judge.GroundStoneJudge(parent.wWorld, vector)) {
                    return true;
                }
                return parent.commandParser.Box;
            }
            @Nullable
            @Override
            public Mask2D toMask2D() {
                return null;
            }
        };
        // 縦長の範囲を置き換えする
        // ラピスラズリブロックを消去したうえで、標高の地点まで土を盛っていく
        try {
            editSession.replaceBlocks(miniRegion, mask, pattern);
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }
    }

    private void GrassHill(int xPoint, int zPoint, int x, int z) {
        int top = parent.heightmapArray[x][z];
        // 範囲、幅と奥行が1マスの縦長の範囲
        CuboidRegion miniRegion = new CuboidRegion(parent.wWorld,
                BlockVector3.at(xPoint, minY, zPoint),
                BlockVector3.at(xPoint, maxY, zPoint));
        // 改変後の地形のパターン
        Pattern pattern = new Pattern() {
            @Override
            public BaseBlock applyBlock(BlockVector3 position) {
                // 表面からの距離に応じて違うブロックにする
                int yPoint = position.getBlockY();
                if (top - yPoint < 0) {
                    return Const.air;
                } else if (top - yPoint < 1) {
                    return Const.grass;
                } else if (top - yPoint < 6) {
                    return Const.dirt;
                } else if (yPoint < 2) {
                    return Const.bedrock;
                } else {
                    return groundPattern.getBlock(position);
                }
            }
        };
        // bReplaceAllがtrueのとき全て書き換え、falseのとき空気のみ書き換える。
        AbstractExtentMask mask = new AbstractExtentMask(editSession) {
            @Override
            public boolean test(BlockVector3 vector) {
                // ブロックを取得、getBlockと比べ、getLazyBlockはブロックの種類だけを取得できるため軽い
                BlockState currentBlock = parent.wWorld.getBlock(vector);
                BaseBlock compare = currentBlock.toBaseBlock();
                // 変更がなければ更新しない (差分が増えることでメモリを圧迫するため)
                if (compare.equalsFuzzy(pattern.applyBlock(vector))) {
                    return false;
                }
                // ラピスラズリブロックは置き換え
                if (compare.equalsFuzzy(Const.lapisblock)) {
                    return true;
                }
                return vector.getY() > parent.oldHeightmapArray[x][z];
            }

            @Nullable
            @Override
            public Mask2D toMask2D() {
                return null;
            }
        };
        // 縦長の範囲を置き換えする
        // ラピスラズリブロックを消去したうえで、標高の地点まで土を盛っていく
        try {
            editSession.replaceBlocks(miniRegion, mask, pattern);
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }
    }

    private void SandHill(int xPoint, int zPoint, int x, int z) {
        int top = parent.heightmapArray[x][z];
        // 範囲、幅と奥行が1マスの縦長の範囲
        CuboidRegion miniRegion = new CuboidRegion(parent.wWorld,
                BlockVector3.at(xPoint, minY, zPoint),
                BlockVector3.at(xPoint, maxY, zPoint));
        // 改変後の地形のパターン
        Pattern pattern = new Pattern() {
            @Override
            public BaseBlock applyBlock(BlockVector3 position) {
                // 表面からの距離に応じて違うブロックにする
                int yPoint = position.getBlockY();
                if (top - yPoint < 0) {
                    return Const.air;
                } else if (top - yPoint < 3) {
                    return Const.SAND;
                } else if (top - yPoint < 6) {
                    return Const.SANDSTONE;
                } else if (yPoint < 2) {
                    return Const.bedrock;
                } else {
                    return groundPattern.getBlock(position);
                }
            }
        };
        // bReplaceAllがtrueのとき全て書き換え、falseのとき空気のみ書き換える。
        AbstractExtentMask mask = new AbstractExtentMask(editSession) {
            @Override
            public boolean test(BlockVector3 vector) {
                // ブロックを取得、getBlockと比べ、getLazyBlockはブロックの種類だけを取得できるため軽い
                BlockState currentBlock = parent.wWorld.getBlock(vector);
                BaseBlock compare = currentBlock.toBaseBlock();
                // 変更がなければ更新しない (差分が増えることでメモリを圧迫するため)
                if (compare.equalsFuzzy(pattern.applyBlock(vector))) {
                    return false;
                }
                // ラピスラズリブロックは置き換え
                if (compare.equalsFuzzy(Const.lapisblock)) {
                    return true;
                }
                return vector.getY() > parent.oldHeightmapArray[x][z];
            }

            @Nullable
            @Override
            public Mask2D toMask2D() {
                return null;
            }
        };
        // 縦長の範囲を置き換えする
        // ラピスラズリブロックを消去したうえで、標高の地点まで土を盛っていく
        try {
            editSession.replaceBlocks(miniRegion, mask, pattern);
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }
    }
    private void StoneHill(int xPoint, int zPoint, int x, int z) {
        int top = parent.heightmapArray[x][z];
        // 範囲、幅と奥行が1マスの縦長の範囲
        CuboidRegion miniRegion = new CuboidRegion(parent.wWorld,
                BlockVector3.at(xPoint, minY, zPoint),
                BlockVector3.at(xPoint, maxY, zPoint));
        // 改変後の地形のパターン
        Pattern pattern = new Pattern() {
            @Override
            public BaseBlock applyBlock(BlockVector3 position) {
                // 表面からの距離に応じて違うブロックにする
                int yPoint = position.getBlockY();
                if (top - yPoint < 0) {
                    return Const.air;
                } else if (yPoint < 2) {
                    return Const.bedrock;
                } else {
                    return groundPattern.getBlock(position);
                }
            }
        };
        // bReplaceAllがtrueのとき全て書き換え、falseのとき空気のみ書き換える。
        AbstractExtentMask mask = new AbstractExtentMask(editSession) {
            @Override
            public boolean test(BlockVector3 vector) {
                // ブロックを取得、getBlockと比べ、getLazyBlockはブロックの種類だけを取得できるため軽い
                BlockState currentBlock = parent.wWorld.getBlock(vector);
                BaseBlock compare = currentBlock.toBaseBlock();
                // 変更がなければ更新しない (差分が増えることでメモリを圧迫するため)
                if (compare.equalsFuzzy(pattern.applyBlock(vector))) {
                    return false;
                }
                // ラピスラズリブロックは置き換え
                if (compare.equalsFuzzy(Const.lapisblock)) {
                    return true;
                }
                return vector.getY() > parent.oldHeightmapArray[x][z];
            }

            @Nullable
            @Override
            public Mask2D toMask2D() {
                return null;
            }
        };
        // 縦長の範囲を置き換えする
        // ラピスラズリブロックを消去したうえで、標高の地点まで土を盛っていく
        try {
            editSession.replaceBlocks(miniRegion, mask, pattern);
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }
    }
    private void StoneCeiling(int xPoint, int zPoint, int x, int z) {
        int top = parent.heightmapArray[x][z];
        // 範囲、幅と奥行が1マスの縦長の範囲
        CuboidRegion miniRegion = new CuboidRegion(parent.wWorld,
                BlockVector3.at(xPoint, minY, zPoint),
                BlockVector3.at(xPoint, maxY, zPoint));
        // 改変後の地形のパターン
        Pattern pattern = new Pattern() {
            @Override
            public BaseBlock applyBlock(BlockVector3 position) {
                // 表面からの距離に応じて違うブロックにする
                int yPoint = position.getBlockY();
                if (top == yPoint)  {
                    return groundPattern.getBlock(position);
                }
                return  Const.air;
            }
        };
        // bReplaceAllがtrueのとき全て書き換え、falseのとき空気のみ書き換える。
        AbstractExtentMask mask = new AbstractExtentMask(editSession) {
            @Override
            public boolean test(BlockVector3 vector) {
                // ブロックを取得、getBlockと比べ、getLazyBlockはブロックの種類だけを取得できるため軽い
                BlockState currentBlock = parent.wWorld.getBlock(vector);
                BaseBlock compare = currentBlock.toBaseBlock();
                // 変更がなければ更新しない (差分が増えることでメモリを圧迫するため)
                if (compare.equalsFuzzy(pattern.applyBlock(vector))) {
                    return false;
                }
                // ラピスラズリブロックは置き換え
                if (compare.equalsFuzzy(Const.lapisblock)) {
                    return true;
                }
                return vector.getY() > parent.oldHeightmapArray[x][z];
            }

            @Nullable
            @Override
            public Mask2D toMask2D() {
                return null;
            }
        };
        // 縦長の範囲を置き換えする
        // ラピスラズリブロックを消去したうえで、標高の地点まで土を盛っていく
        try {
            editSession.replaceBlocks(miniRegion, mask, pattern);
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }
    }

}

