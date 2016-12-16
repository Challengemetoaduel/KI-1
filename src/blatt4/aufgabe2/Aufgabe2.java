package blatt4.aufgabe2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Aufgabe2 {
  public static int a;
  public static int b;
  public static int maxDepth;
  private static HashSet<State> hashSet;

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int x = in.nextInt();
    int y = in.nextInt();

    in.close();

    a = Math.max(x, y);
    b = Math.min(x, y);

    maxDepth = 1;
    x = a;
    y = b;
    while (y > 0) {
      maxDepth += x / y;
      int temp = y;
      y = x % y;
      x = temp;
    }

    hashSet = new HashSet<>(3 * maxDepth);

    State start = new State(a, b, -1);

    State result = alphaBetaSearch(start);

    if (result.utility == 1) {
      System.out.println("Stan wins");
    } else {
      System.out.println("Ollie wins");
    }
    // System.out.println(result);
    // printTree(result);
  }

  public static State alphaBetaSearch(State state) {
    maxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE);
    return state;
  }

  public static int maxValue(State state, int alpha, int beta) {
    if (state.utility != 0 && hashSet.contains(state) || state.isTerminal || state.isPreterminal) {
      return state.utility;
    }

    int v = Integer.MIN_VALUE;

    for (State s : state.succesors()) {
      v = Math.max(v, minValue(s, alpha, beta));
      if (v >= beta) {
        state.utility = v;
        return v;
      }

      alpha = Math.max(alpha, v);
    }
    state.utility = v;
    hashSet.add(state);
    return v;
  }

  public static int minValue(State state, int alpha, int beta) {
    if (state.utility != 0 && hashSet.contains(state) || state.isTerminal || state.isPreterminal) {
      return state.utility;
    }

    int v = Integer.MAX_VALUE;

    for (State s : state.succesors()) {
      v = Math.min(v, maxValue(s, alpha, beta));
      if (v <= alpha) {
        state.utility = v;
        return v;
      }

      beta = Math.min(beta, v);
    }
    state.utility = v;
    hashSet.add(state);
    return v;
  }

  public static void printTree(State start) {
    Queue<State> queue = new LinkedList<>();
    queue.add(start);
    queue.add(null);

    while (!queue.isEmpty()) {
      State current = queue.poll();
      if (current == null) {
        System.out.println();
        continue;
      } else {
        queue.addAll(current.succesors());
        System.out.println(current);
        queue.add(null);
      }
    }
  }
}


class State {
  int a, b, utility, sgn;
  boolean isTerminal;
  boolean isPreterminal;
  static HashSet<State> succesorsSet = new HashSet<>(3 * Aufgabe2.maxDepth);
  static HashMap<State, LinkedList<State>> succesorMap = new HashMap<>(3 * Aufgabe2.maxDepth);

  public State(int x, int y, int sgn) {
    this.a = Math.max(x, y);
    this.b = Math.min(x, y);
    this.sgn = sgn;

    isTerminal = (a == 0 || b == 0);
    isPreterminal = b != 0 && a % b == 0;

    if (isTerminal) {
      utility = sgn;
    } else if (isPreterminal) {
      utility = sgn * (-1);
    } else {
      utility = 0;
    }
  }

  public LinkedList<State> succesors() {
    if (succesorsSet.contains(this)) {
      // System.out.println(this + ": " + Arrays.toString(succesorMap.get(this).toArray()));
      return succesorMap.get(this);
    }
    LinkedList<State> succesors = new LinkedList<>();
    if (isTerminal) {
      return succesors;
    }

    int x = a % b;
    int y = b;

    while (x < a) {
      State current = new State(x, y, sgn * (-1));
      succesors.add(current);
      x += y;
    }
    succesorsSet.add(this);
    succesorMap.put(this, succesors);
    // System.out.println(this + ": " + Arrays.toString(succesors.toArray()));
    return succesors;
  }

  @Override
  public int hashCode() {
    int hash = 1;
    hash = hash * 17 + a;
    hash = hash * 31 + sgn;
    hash = hash * 13 + b;
    return hash;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || !o.getClass().equals(getClass())) {
      return false;
    }
    if (o == this) {
      return true;
    }

    State that = (State) o;

    return a == that.a && b == that.b && sgn == that.sgn;
  }

  @Override
  public String toString() {
    return "[(" + a + "," + b + "), u=" + utility + ", sgn=" + sgn + "]";
  }
}
