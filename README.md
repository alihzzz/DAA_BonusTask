# Aho–Corasick vs KMP

This project searches many patterns in a text. It has my Aho-Corasick implementation and a comparison with KMP, where KMP is run separately for each pattern.

## Files
- `AhoCorasick.java` - trie, failure links, search.
- `KMP.java` - KMP for one pattern, and a method that runs it for all patterns.
- `Tests.java` - correctness tests.
- `Main.java` - speed measurements (also makes the random text and patterns).

## How to run
```
javac *.java
java Tests
java Main
```

## What is implemented
1. **Trie.** All patterns are inserted into one trie.
2. **Failure links.** They are built with BFS. A failure link of a node points to the node for the longest proper suffix of its string that is also a prefix of some pattern. Missing transitions are filled in during BFS, so the search never needs a loop to go back.
3. **Matched patterns (instead of output links).** For every node I do `count[v] += count[fail[v]]`. So `count[v]` is the number of patterns that end at this node or at any of its suffixes. During the search I just add `count[node]`.
4. **Search.** The text is read one time, from left to right. All occurrences are found, including overlapping ones.

## Tests
`Tests.java` checks these cases:
- the classic example `he, she, his, hers` in `ushers`
- overlapping matches (`aa` in `aaaa`)
- patterns that are suffixes of other patterns (`abc`, `bc`, `c`)
- no matches
- the same pattern given twice
- 20 random tests where the result is compared with KMP

All tests pass.

## Experiment setup
- Letters `a-z`, random text and random patterns (fixed seed), each pattern has length 4.
- Number of patterns: 10, 100, 1000.
- Text length: 1,000, 10,000, 100,000.
- Time is measured with `System.nanoTime()`. Each test runs 5 times and I take the average.
- For Aho–Corasick the time includes building the automaton and searching.

## Results (average of 5 runs, in milliseconds)

| Patterns | Text length | Aho–Corasick | KMP | Matches found |
|---------:|------------:|-------------:|----:|--------------:|
| 10   | 1,000   | 0.896 | 0.731   | 0   |
| 10   | 10,000  | 0.909 | 4.136   | 0   |
| 10   | 100,000 | 2.125 | 9.259   | 1   |
| 100  | 1,000   | 0.667 | 0.380   | 0   |
| 100  | 10,000  | 0.696 | 4.027   | 2   |
| 100  | 100,000 | 2.408 | 43.555  | 21  |
| 1000 | 1,000   | 1.148 | 3.743   | 3   |
| 1000 | 10,000  | 0.856 | 40.061  | 23  |
| 1000 | 100,000 | 1.167 | 393.814 | 236 |


## Complexity

Letters used:
- `n` = text length
- `m` = total length of all patterns
- `k` = number of patterns
- `z` = number of matches
- `σ` = alphabet size (26 here)

**Aho–Corasick**
- Build time: `O(m·σ)`, which is `O(m)` when the alphabet is fixed.
- Search time: `O(n)` in my version, because `count` already has the number of matches. If we need to print every match, it is `O(n + z)`.
- Memory: `O(m·σ)` for the transition table.

**KMP for each pattern separately**
- Time: `O(k·n + m)`.
- Memory: `O(m)`.

## Why trie and failure links?
- The **trie** keeps all patterns in one structure. Patterns with the same beginning share the same path.
- A **failure link** tells us where to go when the next letter does not match. We do not move back in the text, so every letter of the text is read only once.
- Patterns that are the end of another pattern (like `c` in `abc`) are not lost, because their count is added through the failure links.

## Why Aho–Corasick is good for many patterns
It reads the text only one time, no matter how many patterns there are. KMP reads the text once for each pattern, so it gets slower and slower when there are more patterns.

## Discussion
- Aho–Corasick is much faster when there are many patterns and a long text. With 1000 patterns and 100,000 characters it was about 300 times faster than KMP.
- For very small inputs the difference is small or not visible, because building the automaton takes some time and the measurement is noisy.

**Aho–Corasick: advantages**
- One pass over the text.
- Time almost does not depend on the number of patterns.

**Aho–Corasick: limitations**
- Uses more memory (the table is `m·σ`).
- Harder to write and understand.
- Not good for a big alphabet like Unicode if we use a full table.

**KMP: advantages**
- Simple and uses little memory.
- Good when there is only one pattern.

**KMP: limitations**
- With `k` patterns it needs `k` passes over the text, so it does not scale well.
