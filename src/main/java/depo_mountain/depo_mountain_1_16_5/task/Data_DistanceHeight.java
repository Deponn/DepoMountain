package depo_mountain.depo_mountain_1_16_5.task;

/**
 * ある点Pとの距離の二乗と高さの関係
 */
public class Data_DistanceHeight {
    /**
     * xz面上での距離
     */
    public final double xzDistance;
    /**
     * yの高さ
     */
    public final double yHeight;

    /**
     * ある点Pとの距離の二乗と高さの関係
     *
     * @param xzDistance 点Pとのxz面上での距離
     * @param yHeight    yの高さ
     */
    public Data_DistanceHeight(double xzDistance, double yHeight) {
        this.xzDistance = xzDistance;
        this.yHeight = yHeight;
    }
}
