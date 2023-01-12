package depo_mountain.depo_mountain_1_16_5;

import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Comparator;

public class _2_CalculationSurface extends BukkitRunnable {

    private final int kNum;
    private final CuboidRegion region;
    int[][] heightmapArray;
    private final ArrayList<Data_ControlPoint> heightControlPoints;
    private final double b_degree;

    private final _3_BlockEdit a3BlockEdit;

    public _2_CalculationSurface(int kNum, CuboidRegion region, int[][] heightmapArray, ArrayList<Data_ControlPoint> heightControlPoints, double b_degree, _3_BlockEdit a3BlockEdit){
        this.kNum = kNum;
        this.region = region;
        this.heightmapArray = heightmapArray;
        this.heightControlPoints = heightControlPoints;
        this.b_degree = b_degree;
        this.a3BlockEdit = a3BlockEdit;
    }

    @Override
    public void run() {
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
            // ラピスラズリブロックがなかった場合、k近傍法を参考にし、y=sum(yn/((x-xn)^2+(z-zn)^2))/sum(1/((x-xn)^2+(z-zn)^2))で標高計算。あった場合そのy座標が標高
            if (top == -1) {
                // 距離のリストに変換。
                ArrayList<Data_DistanceHeight> trainingFixedList = new ArrayList<>();
                for (Data_ControlPoint line : heightControlPoints) {
                    trainingFixedList.add(new Data_DistanceHeight(
                            Math.pow(Math.pow(xPoint - line.xPoint, 2) + Math.pow(zPoint - line.zPoint, 2), b_degree / 2),
                            line.yHeight
                    ));
                }
                // 距離順にする
                trainingFixedList.sort(Comparator.comparingDouble(a -> a.xzDistance));

                // 計算
                double numerator = 0;
                double denominator = 0;
                for (int j = 0; j < kNum; j++) {
                    numerator += trainingFixedList.get(j).yHeight / trainingFixedList.get(j).xzDistance;
                    denominator += 1 / trainingFixedList.get(j).xzDistance;
                }
                // ハイトマップを補間
                top = (int) Math.round(numerator / denominator);
            }
            heightmapArray[xPoint][zPoint] = top;
        }

        a3BlockEdit.runTaskLater(JavaPlugin.getPlugin(Depo_Mountain_1_16_5.class),10);

    }
}
