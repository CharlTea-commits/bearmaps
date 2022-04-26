package bearmaps.utils.graph;

import bearmaps.utils.pq.MinHeapPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.*;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private HashMap<Vertex, Double> distTo;
    private HashMap<Vertex, Double> estimates;
    private HashMap<Vertex, Double> fringeDist;
    private HashMap<Vertex, Vertex> edgeTo;
    private MinHeapPQ<Vertex> fringe;
    private HashSet<Vertex> visited;
    private List<Vertex> retLst;
    private double timeSpent;
    private boolean greatSuccess = false;
    private boolean overtime = false;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        //initialization and setup
        Stopwatch sw = new Stopwatch();
        distTo = new HashMap<>();
        estimates = new HashMap<>();
        fringeDist = new HashMap<>();
        edgeTo = new HashMap<>();
        fringe = new MinHeapPQ<>();
        visited = new HashSet<>();
        retLst = new ArrayList<>();
        distTo.put(start, 0.0);
        estimates.put(start, input.estimatedDistanceToGoal(start, end));
        fringeDist.put(start, distTo.get(start) + estimates.get(start));
        edgeTo.put(start, null);
        fringe.insert(start, fringeDist.get(start));
        //
        while (fringe.size() > 0) {
            if (sw.elapsedTime() > timeout) {
                overtime = true;
                break;
            }
            Vertex p = fringe.poll();
            visited.add(p);
            if (p.equals(end)) {
                timeSpent = sw.elapsedTime();
                greatSuccess = true;
                Vertex pointer = p;
                while (pointer != null) {
                    retLst.add(pointer);
                    pointer = edgeTo.get(pointer);
                }
                Collections.reverse(retLst);
                break;
            }
            for (WeightedEdge<Vertex> item : input.neighbors(p)) {
                if (visited.contains(item.to())) { continue; }
                Double testDist = distTo.get(item.from()) + item.weight() +
                        input.estimatedDistanceToGoal(item.to(), end);
                if (!(fringe.contains(item.to()))) {
                    distTo.put(item.to(), distTo.get(item.from()) + item.weight());
                    estimates.put(item.to(), input.estimatedDistanceToGoal(item.to(), end));
                    fringeDist.put(item.to(), testDist);
                    edgeTo.put(item.to(), item.from());
                    fringe.insert(item.to(), testDist);
                }
                else if (fringe.contains(item.to()) && fringeDist.get(item.to()) > testDist) {
                    distTo.put(item.to(), distTo.get(item.from()) + item.weight());
                    estimates.put(item.to(), input.estimatedDistanceToGoal(item.to(), end));
                    fringeDist.put(item.to(), testDist);
                    edgeTo.put(item.to(), item.from());
                    fringe.changePriority(item.to(), testDist);
                }
            }
        }
    }
    public SolverOutcome outcome() {
        if (greatSuccess) { return SolverOutcome.SOLVED; }
        else if (overtime) { return SolverOutcome.TIMEOUT; }
        else { return SolverOutcome.UNSOLVABLE; }
    }
    public List<Vertex> solution() {
        return retLst;
    }
    public double solutionWeight() {
        return distTo.get(retLst.get(retLst.size() - 1));
    }
    public int numStatesExplored() {
        return visited.size();
    }
    public double explorationTime() {
        return timeSpent;
    }

}
