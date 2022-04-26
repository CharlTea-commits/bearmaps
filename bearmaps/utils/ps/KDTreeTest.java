package bearmaps.utils.ps;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
// source @ Hug's video on random testing from slides linked in spec
public class KDTreeTest {

    @Test
    public void test1() {
        Point p1 = new Point(1.1, 2.2);
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);
        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        KDTree kd = new KDTree(List.of(p1,p2,p3));
        assertTrue(nn.nearest(3.0, 4.0).equals(kd.nearest(3.0 ,4.0)));
    }
    private List<Point> rowRow(int n) {
        List <Point> pt = new ArrayList<>();
        Random r = new Random(500);
        for (int i = 1; i <= n; i++) {
            Point p = new Point(r.nextDouble() * 10, r.nextDouble() * 15);
            pt.add(p);
        }
        return pt;
    }

    @Test
    public void bigTest() {
        int numPoint = 50000;
        int numTests = 6000;
        List <Point> points = rowRow(numPoint);
        NaivePointSet easyMode = new NaivePointSet(points);
        KDTree kd = new KDTree(points);
        List <Point> testing = rowRow(numTests);
        for (Point p : testing) {
            Point expected = easyMode.nearest(p.getX(), p.getY());
            Point actual = kd.nearest(p.getX(), p.getY());
            assertTrue(expected == actual);
        }
    }
}