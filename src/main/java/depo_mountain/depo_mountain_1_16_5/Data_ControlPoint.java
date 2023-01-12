package depo_mountain.depo_mountain_1_16_5;

/**
 * ハイトマップx,z地点における高さyのデータ
 */
public class Data_ControlPoint {
    /**
     * x, z座標 (検索用)
     */
    public final int xPoint, zPoint;
    /**
     * y座標 (取り出し用)
     */
    public final int yHeight;

    /**
     * ハイトマップx,z地点における高さyのデータ
     *
     * @param xPoint  x座標 (検索用)
     * @param zPoint  z座標 (検索用)
     * @param yHeight y座標 (取り出し用)
     */
    public Data_ControlPoint(int xPoint, int zPoint, int yHeight) {
        this.xPoint = xPoint;
        this.zPoint = zPoint;
        this.yHeight = yHeight;
    }
}
