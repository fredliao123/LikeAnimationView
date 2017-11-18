package bupt.liao.fred.likeanimationview.model;

import android.graphics.Point;

/**
 * Created by Fred.Liao on 2017/11/18.
 * Email:fredliaobupt@qq.com
 * Description:
 */

public class LavPoint {
    public float x;
    public float y;

    public LavPoint() {
    }

    public LavPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        if (y != point.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        return result;
    }

    @Override
    public String toString() {
        return "Point(" + x + ", " + y + ")";
    }

}

