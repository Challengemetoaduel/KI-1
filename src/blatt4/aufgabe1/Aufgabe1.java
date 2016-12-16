package blatt4.aufgabe1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Aufgabe1 {

  // public static int utilities[][];
  // private static HashMap<State, Integer> hashMap;
  private static HashSet<State> hashSet;

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int a = in.nextInt();
    int b = in.nextInt();
    in.close();


    int maxDepth = 1;
    int x = Math.max(a, b);
    int y = Math.min(a, b);

    while (y > 0) {
      maxDepth += x / y;
      int temp = y;
      y = x % y;
      x = temp;
    }
    x = Math.max(a, b);
    y = Math.min(a, b);
    hashSet = new HashSet<>(maxDepth * (x / y));
    // hashMap = new HashMap<>(maxDepth * (x / y));

    if (x % y == 0) {
      System.out.println("Stan wins");
      System.exit(0);
    }

    State step = new State(a, b, 0, maxDepth);
    step = alphaBetaSearch(step);

    while (!step.isTerminal()) {
      step = step.succesor(step.utility);
    }

    if (step.a == 0 || step.b == 0) {
      if (step.getPathLength() % 2 == 1) {
        System.out.println("Stan wins");
      } else {
        System.out.println("Ollie wins");
      }
    } else {
      if (step.getPathLength() % 2 == 0) {
        System.out.println("Stan wins");
      } else {
        System.out.println("Ollie wins");
      }
    }
  }

  public static State alphaBetaSearch(State state) {
    int v = maxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE);
    // printTree(state);
    return state.succesor(v);
  }

  public static int maxValue(State state, int alpha, int beta) {
    if (state.isTerminal()) {
      return state.utility;
    }
    if (hashSet.contains(state)) {
      return state.utility;
    }

    int v = Integer.MIN_VALUE;

    for (State s : state.succesors()) {
      v = Math.max(v, minValue(s, alpha, beta));
      if (v >= beta) {
        state.utility = v;
        hashSet.add(state);
        // hashMap.put(state, v);
        return v;
      }
      alpha = Math.max(alpha, v);
    }
    state.utility = v;
    // hashMap.put(state, v);
    hashSet.add(state);
    return v;
  }

  public static int minValue(State state, int alpha, int beta) {
    if (state.isTerminal()) {
      return state.utility;
    }
    if (hashSet.contains(state)) {
      return state.utility;
    }

    int v = Integer.MAX_VALUE;

    for (State s : state.succesors()) {
      v = Math.min(v, maxValue(s, alpha, beta));
      // ?
      if (v <= alpha) {
        state.utility = v;
        // hashMap.put(state, v);
        hashSet.add(state);
        return v;
      }
      beta = Math.min(beta, v);
    }
    state.utility = v;
    // hashMap.put(state, v);
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
  private int pathLength;
  int a;
  int b;
  int utility;
  private boolean isTerminal;
  private final int maxDepth;
  private ArrayList<State> succesors;

  @Override
  public String toString() {
    return "[a=" + a + ", b=" + b + ", utility=" + utility + ", pathLength=" + pathLength + "]";
  }

  public State(int a, int b, int pathLength, int maxDepth) {
    this.a = Math.max(a, b);
    this.b = Math.min(a, b);
    this.maxDepth = maxDepth;
    this.pathLength = pathLength;

    isTerminal = (a == 0 || b == 0);
    if (isTerminal) {
      utility = maxDepth - pathLength;
      utility *= (pathLength % 2 == 1) ? 1 : -1;

    } else if (Math.max(a, b) % Math.min(a, b) == 0) {
      utility = maxDepth - pathLength - 1;
      utility *= ((pathLength + 1) % 2 == 1) ? 1 : -1;
      isTerminal = true;
    } else {
      utility = 0;
    }

    succesors = setSuccesors();
  }

  public State succesor(int v) {
    for (State s : succesors) {
      if (s.utility == v) {
        return s;
      }
    }
    return null;
  }

  public int getPathLength() {
    return pathLength;
  }

  public boolean isTerminal() {
    return isTerminal;
  }

  public ArrayList<State> succesors() {
    return succesors;
  }

  private ArrayList<State> setSuccesors() {
    if (isTerminal) {
      return new ArrayList<>();
    }

    int x = Math.max(a, b);
    int y = Math.min(a, b);

    ArrayList<State> succesors = new ArrayList<>(x / y);
    x -= y;

    while (x >= 0) {
      succesors.add(new State(x, y, pathLength + 1, maxDepth));
      x -= y;
    }
    Collections.reverse(succesors);
    return succesors;
  }

  @Override
  public int hashCode() {
    int hash = 1;
    hash = hash * 17 + Math.max(a, b);
    hash = hash * 31 + (pathLength % 2);
    hash = hash * 13 + Math.min(a, b);
    return hash;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || o.getClass().equals(getClass())) {
      return false;
    }
    if (o == this) {
      return true;
    }

    State that = (State) o;

    return this.a == that.a && this.b == that.b && (this.pathLength % 2) == (that.pathLength % 2);
  }
}
