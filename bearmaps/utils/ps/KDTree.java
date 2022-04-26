package bearmaps.utils.ps;

import java.util.List;
// @source hug's videos linked from the spec
public class KDTree implements PointSet {
    private Node root;
    public KDTree(List<Point> points) {
        root = null;
        for (Point p : points) {
            root = insert(root, p, true);
        }
    }

    private Node insert(Node n, Point p , Boolean basedX ) {
         Node rv = new Node (p, null, null, basedX);
        if (n == null) {
            return rv;
        }
        if (n.compareTo(rv) < 0) {
            n.right = insert(n.right, p, !(basedX));
        } else {
            n.left = insert(n.left, p, !(basedX));
        }
        return n;
        }


    @Override
    public Point nearest(double x, double y) {
        return nearestHelper(root, new Point(x, y), root).p;
    }

    public Node nearestHelper (Node n, Point end, Node best ) {
        if (n == null) {
            return best;
        }
        if (Point.distance(n.p, end) < Point.distance(best.p, end)) {
            best = n;
        }
        Node goodBoy = null;
        Node badBoy = null;
        if (n.compareTo(new Node(end, null, null, true)) > 0) {
             goodBoy = n.left;
             badBoy = n.right;
        } else {
             goodBoy = n.right;
             badBoy = n.left;
        }
        best = nearestHelper(goodBoy, end, best);
        double comp = 0;
        if (n.compareX) {
            comp = (n.p.getX() - end.getX()) * (n.p.getX() - end.getX());
        } else {
            comp = (n.p.getY() - end.getY()) * (n.p.getY() - end.getY());
        }
        if ( comp < Point.distance(end, best.p)) {
            best = nearestHelper(badBoy, end, best);
        }
        return best;
    }

    public class Node implements Comparable<Node> {
        public Point p;
        public Node left;
        public Node right;
        public Boolean compareX;

        public Node(Point pt, Node l , Node r, Boolean b) {
            this.p = pt;
            this.left = l;
            this.right = r;
            this.compareX = b;
        }

        @Override
        public int compareTo(Node o) {
            if (this.compareX) {
                return Double.compare(p.getX(), o.p.getX());
            } else {
                return Double.compare(p.getY(), o.p.getY());
            }
        }
    }
}
