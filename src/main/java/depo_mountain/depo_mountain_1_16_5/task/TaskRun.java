package depo_mountain.depo_mountain_1_16_5.task;

import depo_mountain.depo_mountain_1_16_5.MyProperties;
import depo_mountain.depo_mountain_1_16_5.command.CmdName;

import java.util.Objects;

public class TaskRun {
    private final _1_Collector collector;
    private final _2_Calculator calculator;
    private final _3_Edit blockEdit;
    private final MyProperties prop;

    public TaskRun(MyProperties prop) {
        this.prop = prop;
        this.collector = new _1_Collector(prop);
        this.calculator = new _2_Calculator(prop);
        this.blockEdit = new _3_Edit(prop);
    }

    public void run() {
        if (!Objects.equals(prop.mode, CmdName.Ground)) {
            prop.player.sendMessage("ラピスラズリ集計中");
            collector.run();
        }
        prop.player.sendMessage("地形計算中");
        calculator.run();
        prop.player.sendMessage("地形反映中");
        blockEdit.edit();
    }
}
