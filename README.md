# Assignment 4. Patient Zero

Student ID: 255869  
Name: [fill in your name]  
Group: [fill in your group]

## Program output

The 9 lines printed on my data (`java -cp out Main`):

```text
Mutations: 103
Shared core: 1068
Marker hits: 5
First hit: 4464
Border length: 3
Shared words: 410
Tests needed: 29
Cheapest testing: 135
Longest chain: 27
```

My outbreak report is `output/outbreak.html`. Save a screenshot as `output/outbreak.png` before submission.

## Questions

**Mission 1.** `d[i][j]` is the minimum number of insertions, deletions, and replacements needed to turn the first `i` letters of A into the first `j` letters of B. It is the minimum of deleting the last letter of A, inserting the last letter of B, or matching/replacing those two letters; the first row and column are initialized to the number of insertions or deletions.

**Mission 2.** A subsequence keeps the same order but may skip letters, while a substring must be contiguous. The shared core can exceed the number of equal letters at the same positions because matching letters can occur at different indices and still form an ordered subsequence.

**Mission 3.** The prefix function gives the longest proper prefix that is also a suffix for every marker prefix. On mismatch KMP uses that information to move the marker state back while keeping the text index moving forward, so text characters are not rechecked. My marker's border length is 3: its first and last three letters are equal.

**Mission 4.** Different words can have the same hash, so equal hashes only identify candidates; comparing letters prevents false matches. Many equal-hash windows increase the number of candidate comparisons and can make Rabin–Karp slower.

**Mission 5.** For each person the DP stores the minimum cost when that person is tested and when they are not. If a person is not tested, every child must be tested; if tested, each child can use its cheaper state. Greedy by number of links can fail because test costs and the shape of the whole tree affect which combination is cheapest.

## AI use

ChatGPT helped debug the five algorithms.

## Timing (Mission 7)

Milliseconds, average of 3 runs from `java -cp out Bench`:

| test | size | time |
|---|---:|---:|
| naive search | 20,000 | 144.8 |
| naive search | 40,000 | 249.5 |
| naive search | 80,000 | 515.8 |
| KMP | 20,000 | 11.1 |
| KMP | 40,000 | 3.6 |
| KMP | 80,000 | 1.0 |
| edit distance | 500 | 19.6 |
| edit distance | 1,000 | 21.4 |
| edit distance | 2,000 | 41.7 |
| edit distance | 4,000 | 153.8 |

The naive search time roughly doubles when the text doubles in this test, while KMP is linear in text length but these small timings vary with JVM warm-up. Edit distance grows roughly quadratically: doubling both strings eventually takes about four times as long.

## Complexity

Here `n` is text/strain length, `m` is pattern length, `k` is window length, and `v` is the number of people.

| algorithm | time | space | assumptions |
|---|---|---|---|
| Edit distance | O(nm) | O(nm) | Full dynamic programming table |
| LCS | O(nm) | O(nm) | Full dynamic programming table |
| Prefix function and KMP | O(n + m + h) | O(m + h) | `h` is the number of reported matches; output array included |
| Rabin–Karp (shared words) | Expected O((n + m)k), worst O(nmk) | O(m) | `c` is the number of same-hash candidate windows checked; exact character comparison protects against collisions |
| Tree DP | O(v) | O(v) | Adjacency list; includes DFS arrays and recursion stack |

## Before submission

- [ ] Add `output/outbreak.png` screenshot.
- [ ] Fill in name and group.
- [x] All three supplied examples pass `java tools/Check.java`.
- [ ] Commit and push the project to the required public repository and submit its link in Moodle.
