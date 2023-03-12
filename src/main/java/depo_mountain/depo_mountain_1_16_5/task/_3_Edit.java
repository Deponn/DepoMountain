package depo_mountain.depo_mountain_1_16_5.task;

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
import depo_mountain.depo_mountain_1_16_5.Constant.Const;
import depo_mountain.depo_mountain_1_16_5.Constant.Material_judge;
import depo_mountain.depo_mountain_1_16_5.Constant.Object_GroundPattern;
import depo_mountain.depo_mountain_1_16_5.MyProperties;
import depo_mountain.depo_mountain_1_16_5.command.CmdName;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public class _3_Edit extends _0_taskTemplate {

    private final Object_GroundPattern groundPattern;
    private final Material_judge material_judge;
    private EditSession editSession;
    private final Random random;

    public _3_Edit(MyProperties parent) {
        super(parent);
        // ブロックを予め定義
        this.groundPattern = new Object_GroundPattern(prop.region.getMaximumPoint(), prop.region.getMinimumPoint(), prop.commandParser.resource);
        this.material_judge = new Material_judge();
        this.random = new Random();
    }


    public void edit() {
        this.editSession = prop.worldEditPlugin.createEditSession(prop.player);
        LocalSession session = WorldEdit.getInstance().getSessionManager().get(prop.wPlayer);

        //範囲
        CuboidRegion miniRegion = new CuboidRegion(prop.wWorld,
                BlockVector3.at(minX, minY, minZ),
                BlockVector3.at(maxX, maxY, maxZ));
        // 改変後の地形のパターン
        Pattern pattern = getPattern();
        // bReplaceAllがtrueのとき全て書き換え、falseのとき空気のみ書き換える。
        AbstractExtentMask mask = getMask(pattern);
        // ラピスラズリブロックを消去したうえで、標高の地点まで土を盛っていく
        try {
            editSession.replaceBlocks(miniRegion, mask, pattern);
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }

        editSession.close();
        session.remember(editSession);
    }

    @Override
    protected void miniTask(int xPoint, int zPoint, int x, int z) {

    }

    private Pattern getPattern(){
        if (Objects.equals(prop.mode, CmdName.Mountain)) {
            return MountainPattern();
        } else if (Objects.equals(prop.mode, CmdName.Ground)) {
            return GroundPattern();
        }else if (Objects.equals(prop.mode, CmdName.GrassHill)) {
            return GrassHillPattern();
        }else if (Objects.equals(prop.mode, CmdName.SandHill)) {
            return SandHillPattern();
        }else if (Objects.equals(prop.mode, CmdName.StoneHill)) {
            return StoneHillPattern();
        }else if (Objects.equals(prop.mode, CmdName.StoneCeiling)) {
            return StoneCeilingPattern();
        }
        return null;
    }
    private AbstractExtentMask getMask(Pattern pattern){
        if (Objects.equals(prop.mode, CmdName.Mountain)) {
            return MountainMask(pattern);
        } else if (Objects.equals(prop.mode, CmdName.Ground)) {
            return GroundMask(pattern);
        }else if (Objects.equals(prop.mode, CmdName.GrassHill)) {
            return HillMask(pattern);
        }else if (Objects.equals(prop.mode, CmdName.SandHill)) {
            return HillMask(pattern);
        }else if (Objects.equals(prop.mode, CmdName.StoneHill)) {
            return HillMask(pattern);
        }else if (Objects.equals(prop.mode, CmdName.StoneCeiling)) {
            return HillMask(pattern);
        }
        return null;
    }
    private Pattern MountainPattern(){
        return new Pattern() {
            @Override
            public BaseBlock applyBlock(BlockVector3 position) {
                // 表面からの距離に応じて違うブロックにする
                int yPoint = position.getBlockY();
                int top = prop.heightmapArray[position.getBlockX() - minX][position.getBlockZ() - minZ];
                if (yPoint > top) {
                    return Const.air;
                }else if (yPoint < 2) {
                    return Const.bedrock;
                } else {
                    return groundPattern.getBlock(position);
                }
            }
        };
    }
    private AbstractExtentMask MountainMask(Pattern pattern){
        return new AbstractExtentMask(editSession) {
            @Override
            public boolean test(BlockVector3 vector) {
                // ブロックを取得、getBlockと比べ、getLazyBlockはブロックの種類だけを取得できるため軽い
                BlockState currentBlock = prop.wWorld.getBlock(vector);
                BaseBlock compare = currentBlock.toBaseBlock();
                // 変更がなければ更新しない (差分が増えることでメモリを圧迫するため)
                return !compare.equalsFuzzy(pattern.applyBlock(vector));
            }
            @Nullable
            @Override
            public Mask2D toMask2D() {
                return null;
            }
        };
    }
    private Pattern GroundPattern(){
        return new Pattern() {
            @Override
            public BaseBlock applyBlock(BlockVector3 position) {
                return groundPattern.getBlock(position);
            }
        };
    }

    private AbstractExtentMask GroundMask(Pattern pattern){
        return new AbstractExtentMask(editSession) {
            @Override
            public boolean test(BlockVector3 vector) {
                // ブロックを取得、getBlockと比べ、getLazyBlockはブロックの種類だけを取得できるため軽い
                BlockState currentBlock = prop.wWorld.getBlock(vector);
                BaseBlock compare = currentBlock.toBaseBlock();
                // 変更がなければ更新しない (差分が増えることでメモリを圧迫するため)
                if (compare.equalsFuzzy(pattern.applyBlock(vector))) {
                    return false;
                }
                if(material_judge.GroundStoneJudge(prop.wWorld, vector)) {
                    return true;
                }
                return prop.commandParser.Box;
            }
            @Nullable
            @Override
            public Mask2D toMask2D() {
                return null;
            }
        };
    }
    private Pattern GrassHillPattern(){
       return new Pattern() {
            @Override
            public BaseBlock applyBlock(BlockVector3 position) {
                // 表面からの距離に応じて違うブロックにする
                int yPoint = position.getBlockY();
                int top = prop.heightmapArray[position.getBlockX() - minX][position.getBlockZ() - minZ];
                if (yPoint > top + 1) {
                    return Const.air;
                } else if (yPoint > top) {
                    double ran = random.nextDouble();
                    if(ran < 0.3){
                        return Const.GRASS;
                    }
                    return Const.air;
                }else if (yPoint > top - 1) {
                    return Const.grassBlock;
                } else if ((yPoint > top - 5)) {
                    return Const.dirt;
                } else if (yPoint < 2) {
                    return Const.bedrock;
                } else {
                    return groundPattern.getBlock(position);
                }
            }
        };
    }

    private Pattern SandHillPattern(){
        return new Pattern() {
            @Override
            public BaseBlock applyBlock(BlockVector3 position) {
                // 表面からの距離に応じて違うブロックにする
                int yPoint = position.getBlockY();
                int top = prop.heightmapArray[position.getBlockX() - minX][position.getBlockZ() - minZ];
                if (yPoint > top + 1) {
                    return Const.air;
                }else if (yPoint > top) {
                    double ran = random.nextDouble();
                    if(ran < 0.01){
                        return Const.cactus;
                    }else if(ran < 0.1){
                        return Const.deadBush;
                    }
                    return Const.air;
                } else if (yPoint > top - 3) {
                    return Const.SAND;
                } else if (yPoint > top - 5) {
                    return Const.SANDSTONE;
                } else if (yPoint < 2) {
                    return Const.bedrock;
                } else {
                    return groundPattern.getBlock(position);
                }
            }
        };
    }

    private Pattern StoneHillPattern(){
        return new Pattern() {
            @Override
            public BaseBlock applyBlock(BlockVector3 position) {
                // 表面からの距離に応じて違うブロックにする
                int yPoint = position.getBlockY();
                int top = prop.heightmapArray[position.getBlockX() - minX][position.getBlockZ() - minZ];
                if (top - yPoint < 0) {
                    return Const.air;
                } else if (yPoint < 2) {
                    return Const.bedrock;
                } else {
                    return groundPattern.getBlock(position);
                }
            }
        };
    }

    private Pattern StoneCeilingPattern(){
        return new Pattern() {
            @Override
            public BaseBlock applyBlock(BlockVector3 position) {
                // 表面からの距離に応じて違うブロックにする
                int yPoint = position.getBlockY();
                int top = prop.heightmapArray[position.getBlockX() - minX][position.getBlockZ() - minZ];
                if (top == yPoint)  {
                    return groundPattern.getBlock(position);
                }
                return  Const.air;
            }
        };
    }
    private AbstractExtentMask HillMask(Pattern pattern){
        return new AbstractExtentMask(editSession) {
            @Override
            public boolean test(BlockVector3 vector) {
                // ブロックを取得、getBlockと比べ、getLazyBlockはブロックの種類だけを取得できるため軽い
                BlockState currentBlock = prop.wWorld.getBlock(vector);
                BaseBlock compare = currentBlock.toBaseBlock();
                // 変更がなければ更新しない (差分が増えることでメモリを圧迫するため)
                if (compare.equalsFuzzy(pattern.applyBlock(vector))) {
                    return false;
                }
                // ラピスラズリブロックは置き換え
                if (compare.equalsFuzzy(Const.lapisblock)) {
                    return true;
                }
                return vector.getY() > prop.oldHeightmapArray[vector.getBlockX() - minX][vector.getBlockZ() - minZ];
            }

            @Nullable
            @Override
            public Mask2D toMask2D() {
                return null;
            }
        };
    }
}
