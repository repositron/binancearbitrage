package dev.ljw.binance.test.tools;

import dev.ljw.binance.CoinPair;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class AssertSameCoinPair {
  public static void assertSameListOfCoinPair(List<CoinPair> expected, List<CoinPair> actual) {
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < expected.size(); i++) {
      assertSame(expected.get(i), actual.get(i));
    }
  }
}
