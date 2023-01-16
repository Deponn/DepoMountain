package depo_mountain.depo_mountain_1_16_5;

import org.bukkit.scheduler.BukkitRunnable;

public class Operator_TaskRun extends BukkitRunnable {
    private final _1_LapisCollector lapisCollector;
    private final _2_CalculationSurface calculationSurface;
    private final _3_BlockEdit blockEdit;
    private int Counter;
    private final int iterationNum;
    private int iterationCounter;
    private final int step;
    private boolean flag;
    private final Operator_CreateMountain parent;

    public Operator_TaskRun(Operator_CreateMountain parent) {
        this.Counter = 0;
        this.parent = parent;
        this.iterationNum = parent.region.getWidth() * parent.region.getLength();
        this.lapisCollector = new _1_LapisCollector(parent);
        this.calculationSurface = new _2_CalculationSurface(parent);
        this.blockEdit = new _3_BlockEdit(parent);
        this.iterationCounter = 0;
        this.step = 1000;
        this.flag = false;
    }

    @Override
    public void run() {
        if (Counter == 0) {
            if (iterationCounter * step < iterationNum) {
                if (!flag) {
                    flag = true;
                    lapisCollector.run(iterationCounter * step, (iterationCounter + 1) * step);
                    parent.player.sendMessage("ラピスラズリ集計中:" + iterationCounter * step + "/" + iterationNum);
                    iterationCounter += 1;
                    flag = false;
                }
            } else {
                Counter += 1;
                iterationCounter = 0;
            }
        } else if (Counter == 1) {
            if (iterationCounter * step < iterationNum) {
                if (!flag) {
                    flag = true;
                    calculationSurface.run(iterationCounter * step, (iterationCounter + 1) * step);
                    parent.player.sendMessage("地形計算中:" + iterationCounter * step + "/" + iterationNum);
                    iterationCounter += 1;
                    flag = false;
                }
            } else {
                Counter += 1;
                iterationCounter = 0;
            }
        } else if (Counter == 2) {
            blockEdit.start();
            Counter += 1;
        } else if (Counter == 3) {
            if (iterationCounter * step < iterationNum) {
                if (!flag) {
                    flag = true;
                    blockEdit.run(iterationCounter * step, (iterationCounter + 1) * step);
                    parent.player.sendMessage("地形反映中:" + iterationCounter * step + "/" + iterationNum);
                    iterationCounter += 1;
                    flag = false;
                }
            } else {
                Counter += 1;
                iterationCounter = 0;
            }
        } else if (Counter == 4) {
            blockEdit.end();
            Counter += 1;
        } else if (Counter == 5) {
            cancel();
        }
    }
}
