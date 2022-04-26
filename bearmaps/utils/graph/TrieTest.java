package bearmaps.utils.graph;

import static org.junit.Assert.*;

import bearmaps.AugmentedStreetMapGraph;
import org.junit.Test;
import static bearmaps.utils.Constants.BASE_DIR_PATH;
import java.util.ArrayList;
import java.util.List;

public class TrieTest {
    private static final String OSM_DB_PATH = BASE_DIR_PATH + "data/proj3_xml/berkeley-2020.osm.xml";

    @Test
    public void test1() {
        List <String> rofl = List.of("farmer", "christian", "sad", "single", "fanta", "fabulous", "frame", "zigzagoon");
        Trie t = new Trie(rofl);
        System.out.println(t.keysWithPrefix("fa"));
    }

    @Test
    public void test2() {
        AugmentedStreetMapGraph graph = new AugmentedStreetMapGraph(OSM_DB_PATH);
        graph.getLocations("Top Dog");
        graph.getLocationsByPrefix("S");
    }
}