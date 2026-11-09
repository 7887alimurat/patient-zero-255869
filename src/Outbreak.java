import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        long[] take = new long[n], skip = new long[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -2);
        vertexDp(0, -1, nb, cost, take, skip, parent);
        boolean[] tested = new boolean[n];
        rebuild(0, take[0] <= skip[0], nb, parent, take, skip, tested);
        return tested;
    }

    private static void vertexDp(int v, int p, int[][] nb, int[] cost, long[] take, long[] skip, int[] parent) {
        parent[v] = p;
        take[v] = cost[v];
        skip[v] = 0;
        for (int child : nb[v]) if (child != p) {
            vertexDp(child, v, nb, cost, take, skip, parent);
            take[v] += Math.min(take[child], skip[child]);
            skip[v] += take[child];
        }
    }

    private static void rebuild(int v, boolean isTaken, int[][] nb, int[] parent, long[] take, long[] skip, boolean[] tested) {
        tested[v] = isTaken;
        for (int child : nb[v]) if (child != parent[v]) {
            boolean childTaken = !isTaken || take[child] <= skip[child];
            rebuild(child, childTaken, nb, parent, take, skip, tested);
        }
    }

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        int[] height = new int[n];
        int[] diameter = new int[1];
        depth(0, -1, nb, height, diameter);
        return diameter[0];
    }

    private static int depth(int v, int p, int[][] nb, int[] height, int[] diameter) {
        int longest = 0, second = 0;
        for (int child : nb[v]) if (child != p) {
            int h = depth(child, v, nb, height, diameter) + 1;
            if (h > longest) { second = longest; longest = h; }
            else if (h > second) second = h;
        }
        height[v] = longest;
        diameter[0] = Math.max(diameter[0], longest + second);
        return longest;
    }
}
