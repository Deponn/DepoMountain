package depo_mountain.depo_mountain_1_16_5.task;


import depo_mountain.depo_mountain_1_16_5.MyProperties;

import java.util.ArrayList;
import java.util.Comparator;

public class _2_Calculator extends _0_Task {
    public _2_Calculator(MyProperties parent) {
        super(parent);
    }

    @Override
    protected void miniTask(int xPoint, int zPoint, int x, int z) {
        int top = prop.heightmapArray[x][z];
        // ラピスラズリブロックがなかった場合、k近傍法を参考にし、y=sum(yn/((x-xn)^2+(z-zn)^2))/sum(1/((x-xn)^2+(z-zn)^2))で標高計算。あった場合そのy座標が標高
        if (top == -1) {
            // 距離のリストに変換。
            ArrayList<Data_DistanceHeight> trainingFixedList = new ArrayList<>();
            for (Data_ControlPoint line : prop.heightControlPoints) {
                trainingFixedList.add(new Data_DistanceHeight(
                        Math.pow(Math.pow(xPoint - line.xPoint, 2) + Math.pow(zPoint - line.zPoint, 2), prop.commandParser.b_degree / 2),
                        line.yHeight
                ));
            }
            // 距離順にする
            trainingFixedList.sort(Comparator.comparingDouble(a -> a.xzDistance));

            // 計算
            double numerator = 0;
            double denominator = 0;
            int kNum;
            if (prop.commandParser.kNum != 0 & prop.commandParser.kNum < trainingFixedList.size()) {
                kNum = prop.commandParser.kNum;
            }else {
                kNum = trainingFixedList.size();
            }

            for (int j = 0; j < kNum; j++) {
                numerator += trainingFixedList.get(j).yHeight / trainingFixedList.get(j).xzDistance;
                denominator += 1 / trainingFixedList.get(j).xzDistance;
            }

            // ハイトマップを補間
            top = (int) Math.round(numerator / denominator);
        }
        prop.heightmapArray[x][z] = top;
    }
}

