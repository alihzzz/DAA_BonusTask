import java.util.Random;

public class Main {

    static String randomString(Random random, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb.toString();
    }

    static String[] randomPatterns(Random random, int count, int length) {
        String[] patterns = new String[count];
        for (int i = 0; i < count; i++) {
            patterns[i] = randomString(random, length);
        }
        return patterns;
    }

    public static void main(String[] args) {
        int[] patternCounts = {10, 100, 1000};
        int[] textLengths = {1000, 10000, 100000};
        int patternLength = 4;
        int runs = 5;

        System.out.println("patterns | text    | AC (ms)  | KMP (ms) | matches");

        for (int k : patternCounts) {
            for (int n : textLengths) {
                Random random = new Random(42);
                String[] patterns = randomPatterns(random, k, patternLength);
                String text = randomString(random, n);

                long acTime = 0;
                long kmpTime = 0;
                long acMatches = 0;
                long kmpMatches = 0;

                for (int i = 0; i < runs; i++) {
                    long start = System.nanoTime();
                    AhoCorasick ac = new AhoCorasick(patterns);
                    acMatches = ac.search(text);
                    acTime += System.nanoTime() - start;

                    start = System.nanoTime();
                    kmpMatches = KMP.searchAll(text, patterns);
                    kmpTime += System.nanoTime() - start;
                }

                if (acMatches != kmpMatches) {
                    System.out.println("Results are different!");
                }

                System.out.printf("%8d | %7d | %8.3f | %8.3f | %d%n",
                        k, n, acTime / runs / 1e6, kmpTime / runs / 1e6, acMatches);
            }
        }
    }
}