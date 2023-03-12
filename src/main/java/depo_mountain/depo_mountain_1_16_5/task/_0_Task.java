package depo_mountain.depo_mountain_1_16_5.task;

import depo_mountain.depo_mountain_1_16_5.MyProperties;

public abstract class _0_Task {

    protected final MyProperties prop;
    protected final int minX;
    protected final int minY;
    protected final int minZ;
    protected final int maxX;
    protected final int maxY;
    protected final int maxZ;
    protected final int width;
    protected final int length;


    public _0_Task(MyProperties prop){
        this.prop = prop;
        // 操作
        this.minX = prop.region.getMinimumPoint().getBlockX();
        this.minY = prop.region.getMinimumPoint().getBlockY();
        this.minZ = prop.region.getMinimumPoint().getBlockZ();
        this.maxX = prop.region.getMaximumPoint().getBlockX();
        this.maxY = prop.region.getMaximumPoint().getBlockY();
        this.maxZ = prop.region.getMaximumPoint().getBlockZ();
        this.width = prop.region.getWidth();
        this.length = prop.region.getLength();
    }

    public void run(){
        int start = 0;
        int end = width * length;
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
