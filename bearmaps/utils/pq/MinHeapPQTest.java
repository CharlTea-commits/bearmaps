package bearmaps.utils.pq;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Random;

public class MinHeapPQTest {

    @Test
    public void testHeap() {
        Random r = new Random(400);
        HashMap<Integer, Double> order = new HashMap<>();
        MinHeapPQ<Integer> dumb = new MinHeapPQ<>();
        NaiveMinPQ <Integer> dumber = new NaiveMinPQ<>();
        for (int i =0; i < 1000; i++) {
            order.put(i, r.nextDouble());
        }
        for (Integer i : order.keySet()) {
            dumb.insert(i, order.get(i));
            dumber.insert(i, order.get(i));
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals(dumb.poll(), dumber.poll());
        }
    }
}