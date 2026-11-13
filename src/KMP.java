public class KMP {

    private static int[] prefixFunction(String p) {
        int[] pi = new int[p.length()];
        int k = 0;
        for (int i = 1; i < p.length(); i++) {
            while (k > 0 && p.charAt(i) != p.charAt(k)) {
                k = pi[k - 1];
            }
            if (p.charAt(i) == p.charAt(k)) {
                k++;
            }
            pi[i] = k;
        }
        return pi;
    }

    public static long search(String text, String pattern) {
        int[] pi = prefixFunction(pattern);
        long result = 0;
        int k = 0;
        for (int i = 0; i < text.length(); i++) {
            while (k > 0 && text.charAt(i) != pattern.charAt(k)) {
                k = pi[k - 1];
            }
            if (text.charAt(i) == pattern.charAt(k)) {
                k++;
            }
            if (k == pattern.length()) {
                result++;
                k = pi[k - 1];
            }
        }
        return result;
    }

    public static long searchAll(String text, String[] patterns) {
        long result = 0;
        for (String p : patterns) {
            result += search(text, p);
        }
        return result;
    }
}