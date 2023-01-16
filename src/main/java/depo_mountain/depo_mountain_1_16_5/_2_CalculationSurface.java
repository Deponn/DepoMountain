package depo_mountain.depo_mountain_1_16_5;


import java.util.ArrayList;
import java.util.Comparator;

public class _2_CalculationSurface extends _0_taskTemplate {
    public _2_CalculationSurface(Operator_CreateMountain parent) {
        super(parent);
    }

    @Override
    protected void miniTask(int xPoint, int zPoint, int x, int z) {
        int top = parent.heightmapArray[x][z];
        // ラピスラズリブロックがなかった場合、k近傍法を参考にし、y=sum(yn/((x-xn)^2+(z-zn)^2))/sum(1/((x-xn)^2+(z-zn)^2))で標高計算。あった場合そのy座標が標高
        if (top == -1) {
            // 距離のリストに変換。
            ArrayList<Data_DistanceHeight> trainingFixedList = new ArrayList<>();
            for (Data_ControlPoint line : parent.heightControlPoints) {
                trainingFixedList.add(new Data_DistanceHeight(
                        Math.pow(Math.pow(xPoint - line.xPoint, 2) + Math.pow(zPoint - line.zPoint, 2), parent.commandParser.b_degree / 2),
                        line.yHeight
                ));
            }
            // 距離順にする
            trainingFixedList.sort(Comparator.comparingDouble(a -> a.xzDistance));

            // 計算
            double numerator = 0;
            double denominator = 0;
            int kNum;
            if (parent.commandParser.kNum != 0) {
                kNum = parent.commandParser.kNum;
            } else {
                kNum = trainingFixedList.size();
            }
            for (int j = 0; j < kNum; j++) {
                numerator += trainingFixedList.get(j).yHeight / trainingFixedList.get(j).xzDistance;
                denominator += 1 / trainingFixedList.get(j).xzDistance;
            }

            // ハイトマップを補間
            top = (int) Math.round(numerator / denominator);
        }
        parent.heightmapArray[x][z] = top;
    }
}

