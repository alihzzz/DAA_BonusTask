import java.util.Random;

public class Tests {

    static int failed = 0;

    static void check(String name, long expected, long actual) {
        if (expected == actual) {
            System.out.println("OK   " + name);
        } else {
            System.out.println("FAIL " + name + " (expected " + expected + ", got " + actual + ")");
            failed++;
        }
    }

    public static void main(String[] args) {
        AhoCorasick ac = new AhoCorasick(new String[]{"he", "she", "his", "hers"});
        check("ushers", 3, ac.search("ushers"));

        ac = new AhoCorasick(new String[]{"aa"});
        check("overlap aa in aaaa", 3, ac.search("aaaa"));

        ac = new AhoCorasick(new String[]{"abc", "bc", "c"});
        check("suffix patterns", 3, ac.search("abc"));

        ac = new AhoCorasick(new String[]{"xyz"});
        check("no match", 0, ac.search("abcabc"));

        ac = new AhoCorasick(new String[]{"a", "a"});
        check("same pattern twice", 4, ac.search("aa"));

        Random random = new Random(1);
        for (int t = 1; t <= 20; t++) {
            String[] patterns = Main.randomPatterns(random, 10, 3);
            String text = Main.randomString(random, 2000);
            check("random test " + t,
                    KMP.searchAll(text, patterns),
                    new AhoCorasick(patterns).search(text));
        }

        System.out.println(failed == 0 ? "All tests passed" : failed + " tests failed");
    }
}