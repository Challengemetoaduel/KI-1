package blatt4.aufgabe3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Aufgabe3 {
  public static int a;
  public static int b;
  public static int maxDepth;
  public static HashSet<State> values;

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

    values = new HashSet<>(maxDepth);

    State result = alphaBetaSearch(new State(a, b));

    if (result.getUtility(true) == 1) {
      System.out.println("Stan wins");
    } else {
      System.out.println("Ollie wins");
    }
  }

  public static State alphaBetaSearch(State state) {
    maxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE);
    return state;
  }

  public static int maxValue(State state, int alpha, int beta) {
    if (values.contains(state)) {
      return state.getUtility(true);
    }

    int v = Integer.MIN_VALUE;

    for (State s : state.succesors()) {
      v = Math.max(v, minValue(s, alpha, beta));

      alpha = Math.max(alpha, v);
      if (beta <= alpha) {
        return v;
      }
    }
    state.setUtility(v, true);
    values.add(state);
    return v;
  }

  public static int minValue(State state, int alpha, int beta) {

    if (values.contains(state)) {
      return state.getUtility(false);
    }

    int v = Integer.MAX_VALUE;

    for (State s : state.succesors()) {
      v = Math.min(v, maxValue(s, alpha, beta));
      beta = Math.min(beta, v);
      if (beta <= alpha) {
        return v;
      }
    }

    state.setUtility(v, false);
    values.add(state);
    return v;
  }

  /*****************************************
   * TODO: Remove after
   * 
   * @throws IOException
   */
  public static void printTree(State start) throws IOException {
    FileWriter writer = new FileWriter(new File("src/blatt4/tree.txt"));
    Queue<State> queue = new LinkedList<>();
    queue.add(start);
    queue.add(null);

    while (!queue.isEmpty()) {
      State current = queue.poll();
      if (current == null) {
        System.out.println();
        writer.write("\n");
        continue;
      } else {
        queue.addAll(current.succesors());
        System.out.println(current);
        writer.write(current.toString() + "\n");
        queue.add(null);
      }
    }
    writer.flush();
    writer.close();
  }

}


class State {
  public int a, b;
  private int utilityMax;

  public static HashMap<State, Tupel<State, LinkedList<State>>> succesorMap =
      new HashMap<>(Aufgabe3.maxDepth);


  public State(int x, int y) {
    this.a = Math.max(x, y);
    this.b = Math.min(x, y);

    boolean isTerminal = b == 0;
    boolean isPreterminal = !isTerminal && a % b == 0;

    if (isTerminal) {
      utilityMax = -1;
      Aufgabe3.values.add(this);
    } else if (isPreterminal) {
      utilityMax = 1;
      Aufgabe3.values.add(this);
    } else {
      utilityMax = 0;
    }
  }

  public int getUtility(boolean max) {
    if (max) {
      return utilityMax;
    } else {
      return 0 - utilityMax;
    }
  }

  public void setUtility(int v, boolean max) {
    if (max) {
      utilityMax = v;
    } else {
      utilityMax = 0 - v;
    }
  }

  public LinkedList<State> succesors() {
    Tupel<State, LinkedList<State>> tupel = succesorMap.get(this);
    LinkedList<State> succesors;
    if (tupel != null) {
      succesors = tupel.second;
      return succesors;
    }

    succesors = new LinkedList<>();

    if (b == 0) {
      succesorMap.put(this, new Tupel<>(this, succesors));
      return succesors;
    }

    int x = a % b;
    int y = b;

    while (x < a) {
      State current = new State(x, y);
      tupel = succesorMap.get(current);
      if (tupel != null) {
        current = tupel.first;
      }
      succesors.add(current);
      x += y;
    }

    succesorMap.put(this, new Tupel<>(this, succesors));
    return succesors;
  }

  @Override
  public int hashCode() {
    int hash = 1;
    hash = hash * 17 + a;
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

    return a == that.a && b == that.b;
  }

  @Override
  public String toString() {
    return "[(" + a + "," + b + "), u=" + utilityMax + "]";
  }
}


class Tupel<X, Y> {
  public final X first;
  public final Y second;

  public Tupel(X first, Y second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public int hashCode() {
    int hash = 1;
    hash = hash * 17 + first.hashCode();
    hash = hash * 31 + second.hashCode();
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

    Tupel<State, LinkedList<State>> that = (Tupel<State, LinkedList<State>>) o;

    return this.first.equals(that.first);
  }
}
