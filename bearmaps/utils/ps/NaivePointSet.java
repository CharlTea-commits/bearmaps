package bearmaps.utils.ps;
import java.util.List;

public class NaivePointSet implements PointSet {
    private List<Point> points;
    public NaivePointSet(List<Point> ps) {
        this.points = ps;
    }

    @Override
    public Point nearest(double x, double y) {
        Point compare = new Point(x, y);
        Point smallest = points.get(0);
        double minDist = Point.distance(points.get(0),compare);
        for (int i = 1; i < points.size(); i++) {
            double thisDist = Point.distance(points.get(i), compare);
            if ( thisDist < minDist) {
                minDist = thisDist;
                smallest = points.get(i);
            }
        }
        return smallest;
    }
}
