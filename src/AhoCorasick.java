import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class AhoCorasick {
    private static final int ALPHABET = 26;

    private int[][] go;
    private int[] fail;
    private int[] count;
    private int size = 1;

    public AhoCorasick(String[] patterns) {
        int maxNodes = 1;
        for (String p : patterns) {
            maxNodes += p.length();
        }
        go = new int[maxNodes][ALPHABET];
        for (int[] row : go) {
            Arrays.fill(row, -1);
        }
        fail = new int[maxNodes];
        count = new int[maxNodes];

        for (String p : patterns) {
            addPattern(p);
        }
        buildLinks();
    }

    private void addPattern(String p) {
        int node = 0;
        for (int i = 0; i < p.length(); i++) {
            int c = p.charAt(i) - 'a';
            if (go[node][c] == -1) {
                go[node][c] = size;
                size++;
            }
            node = go[node][c];
        }
        count[node]++;
    }

    private void buildLinks() {
        Queue<Integer> queue = new LinkedList<>();

        for (int c = 0; c < ALPHABET; c++) {
            if (go[0][c] == -1) {
                go[0][c] = 0;
            } else {
                fail[go[0][c]] = 0;
                queue.add(go[0][c]);
            }
        }

        while (!queue.isEmpty()) {
            int v = queue.poll();
            count[v] += count[fail[v]];

            for (int c = 0; c < ALPHABET; c++) {
                int u = go[v][c];
                if (u == -1) {
                    go[v][c] = go[fail[v]][c];
                } else {
                    fail[u] = go[fail[v]][c];
                    queue.add(u);
                }
            }
        }
    }

    public long search(String text) {
        long result = 0;
        int node = 0;
        for (int i = 0; i < text.length(); i++) {
            node = go[node][text.charAt(i) - 'a'];
            result += count[node];
        }
        return result;
    }
}