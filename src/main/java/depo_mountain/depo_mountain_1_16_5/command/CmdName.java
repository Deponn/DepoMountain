package depo_mountain.depo_mountain_1_16_5.command;

public enum CmdName {


    Mountain("/DpMountain"),
    Ground("/DpGround"),
    GrassHill("/DpGrassHill"),
    SandHill("/DpSandHill"),
    StoneHill("/DpStoneHill"),
    StoneCeiling("/DpStoneCeiling");

    private final String Command;

    // コンストラクタを定義
    CmdName(String Command) {
        this.Command = Command;
    }

    // メソッド
    public String getCmd() {
        return this.Command;
    }
}

