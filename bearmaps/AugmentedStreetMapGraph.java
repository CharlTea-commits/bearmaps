package bearmaps;

import bearmaps.utils.Constants;
import bearmaps.utils.graph.Trie;
import bearmaps.utils.graph.streetmap.Node;
import bearmaps.utils.graph.streetmap.StreetMapGraph;
import bearmaps.utils.ps.KDTree;
import bearmaps.utils.ps.Point;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private HashMap<Point, Long> pointyBoy;
    private HashMap <String, LinkedList<Node>> compoundBoy;
    private KDTree xmasTree;
    private Trie weLoveEnglish;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();
        pointyBoy = new HashMap<>();
        compoundBoy = new HashMap<String, LinkedList<Node>>();
        for (Node item : this.getAllNodes()) {
            if (item.name() != null) {
                if (compoundBoy.containsKey(cleanString(item.name()))) {
                    compoundBoy.get(cleanString(item.name())).add(item);
                } else {
                    LinkedList<Node> listyBoy = new LinkedList<Node>();
                    listyBoy.add(item);
                    compoundBoy.put(cleanString(item.name()), listyBoy);
                }
            }
        }
        for (Node item : nodes) {
            if (neighbors(item.id()).isEmpty()) { continue; }
            pointyBoy.put(new Point(projectToX(item.lon(), item.lat()), projectToY(item.lon(), item.lat())), item.id());
        }
        xmasTree = new KDTree(new ArrayList<>(pointyBoy.keySet()));
        weLoveEnglish = new Trie(new ArrayList<String>(compoundBoy.keySet()));
    }


    /**
     * For Project Part III
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point retPt = xmasTree.nearest(projectToX(lon, lat), projectToY(lon, lat));
        return pointyBoy.get(retPt);
    }

    /**
     * Return the Euclidean x-value for some point, p, in Berkeley. Found by computing the
     * Transverse Mercator projection centered at Berkeley.
     * @param lon The longitude for p.
     * @param lat The latitude for p.
     * @return The flattened, Euclidean x-value for p.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    static double projectToX(double lon, double lat) {
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);
        double b = Math.sin(dlon) * Math.cos(phi);
        return (K0 / 2) * Math.log((1 + b) / (1 - b));
    }

    /**
     * Return the Euclidean y-value for some point, p, in Berkeley. Found by computing the
     * Transverse Mercator projection centered at Berkeley.
     * @param lon The longitude for p.
     * @param lat The latitude for p.
     * @return The flattened, Euclidean y-value for p.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    static double projectToY(double lon, double lat) {
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);
        double con = Math.atan(Math.tan(phi) / Math.cos(dlon));
        return K0 * (con - Math.toRadians(ROOT_LAT));
    }


    /**
     * For Project Part IV (extra credit)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> rv = weLoveEnglish.keysWithPrefix(cleanString(prefix));
        List <String> aakhir = new ArrayList<String>();
        for (String s : rv) {
            for (Node item : compoundBoy.get(s)) {
                aakhir.add(item.name());
            }
        }
        return aakhir;
    }

    /**
     * For Project Part IV (extra credit)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> oMega = new ArrayList<Map<String, Object>>();
        if (locationName == null) {return oMega;}
        if (!(compoundBoy.containsKey(cleanString(locationName)))) {return oMega;}
        LinkedList <Node> nodeyBoy = compoundBoy.get(cleanString(locationName));
        for (Node n : nodeyBoy) {
            HashMap<String, Object> mappy = new HashMap<>();
            mappy.put("lat", n.lat());
            mappy.put("lon", n.lon());
            mappy.put("name", n.name());
            mappy.put("id", n.id());
            oMega.add(mappy);
        }
        return oMega;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

        
    /**
     * Scale factor at the natural origin, Berkeley. Prefer to use 1 instead of 0.9996 as in UTM.
     * @source https://gis.stackexchange.com/a/7298
     */
    private static final double K0 = 1.0;
    /** Latitude centered on Berkeley. */
    private static final double ROOT_LAT = (Constants.ROOT_ULLAT + Constants.ROOT_LRLAT) / 2;
    /** Longitude centered on Berkeley. */
    private static final double ROOT_LON = (Constants.ROOT_ULLON + Constants.ROOT_LRLON) / 2;

}
