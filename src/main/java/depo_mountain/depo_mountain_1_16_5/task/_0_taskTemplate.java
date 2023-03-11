package depo_mountain.depo_mountain_1_16_5.task;

import depo_mountain.depo_mountain_1_16_5.MyProperties;

public abstract class _0_taskTemplate {

    protected final MyProperties parent;
    protected final int minX;
    protected final int minY;
    protected final int minZ;
    protected final int maxX;
    protected final int maxY;
    protected final int maxZ;
    protected final int width;
    protected final int length;


    public _0_taskTemplate(MyProperties parent){
        this.parent = parent;
        // 操作
        this.minX = parent.region.getMinimumPoint().getBlockX();
        this.minY = parent.region.getMinimumPoint().getBlockY();
        this.minZ = parent.region.getMinimumPoint().getBlockZ();
        this.maxX = parent.region.getMaximumPoint().getBlockX();
        this.maxY = parent.region.getMaximumPoint().getBlockY();
        this.maxZ = parent.region.getMaximumPoint().getBlockZ();
        this.width = parent.region.getWidth();
        this.length = parent.region.getLength();
    }

    public void run(int start, int end){
        if(end > width * length){
            end = width * length;
        }
        for (int i = start; i < end; i++) {
            int x = i % width;
            int z = i / width;
            // x座標方向の座標
            int xPoint = x + minX;
            // z座標方向の座標
            int zPoint = z + minZ;
            miniTask(xPoint,zPoint,x,z);
        }
    }

    protected abstract void miniTask(int xPoint, int zPoint,int x,int z);
}
