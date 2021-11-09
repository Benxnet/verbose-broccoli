[![pipeline status](https://git.fh-muenster.de/ci-hpk/PG5_G1/axela/badges/master/pipeline.svg)](https://git.fh-muenster.de/ci-hpk/PG5_G1/axela/-/commits/master)
[![test coverage](https://git.fh-muenster.de/ci-hpk/PG5_G1/axela/badges/master/coverage.svg)](https://git.fh-muenster.de/ci-hpk/PG5_G1/axela/-/commits/master)

# Axela & Iris

HPK WS20/21 Praktikum PG5 G1

von Ricardo Frank Sendes & Jörn Koenen


### Matrix Multiplikation

Ergebnis der Optimierung und Parallelisierung mit 6 Kernen in der VM

```
+------+-----------+------------------+------------------+---------+--------+
|  dim |  baseline |     optimized    |   parallelized   | speedUp | Amdahl |
+------+-----------+----------+-------+---------+--------+---------+--------+
|   n  |   b[μs]   |   s[μs]  |  b/s  |  p[μs]  |   b/p  |   s/p   |  pi[%] |
+------+-----------+----------+-------+---------+--------+---------+--------+
|  128 |      5709 |     3686 |  1.55 |   24964 |   0.23 |    0.15 |      0 |
|  256 |     46555 |    22682 |  2.05 |   16734 |   2.78 |    1.36 |     31 |
|  512 |    370814 |   185505 |  2.00 |   58474 |   6.34 |    3.17 |     82 |
| 1024 |   3604566 |  1353920 |  2.66 |  257180 |  14.02 |    5.26 |     97 |
| 2048 | 116213131 | 11328835 | 10.26 | 2083778 |  55.77 |    5.44 |     98 |
+------+-----------+----------+-------+---------+--------+---------+--------+
```

