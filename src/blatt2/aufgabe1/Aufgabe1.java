package blatt2.aufgabe1;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Aufgabe1 {
  public static int n, m;

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    // Scanner in = new Scanner(new File("src/blatt2/aufgabe1/eingabe.txt"));
    HashSet<Tupel<Integer, Integer>> notFree = null;

    Tupel<Integer, Integer> initialTupel = null, finalTupel = null;
    n = 0;
    m = 0;
    while (in.hasNextLine()) {
      String line = in.nextLine();
      n++;
      if (n == 1) {
        m = line.length();
        notFree = new HashSet<>(m * m * 2);
      }

      for (int i = 0; i < m; i++) {
        char c = line.charAt(i);
        switch (c) {
          case '#':
            notFree.add(new Tupel<>(n, i + 1));
            break;
          case '@':
            initialTupel = new Tupel<>(n, i + 1);
            notFree.add(initialTupel);
            break;
          case '.':
            finalTupel = new Tupel<>(n, i + 1);
            break;
        }
      }
    }
    in.close();

    State initialState = new State(initialTupel, notFree);
    State finalState = new State(finalTupel, null);
    Problem problem = new Problem(initialState, finalState);
    Node result = treeSearch(problem);
    System.out.println((result == null) ? "" : result.depth);


  }

  public static Node treeSearch(Problem problem) {
    Queue<Node> fringe = new LinkedList<>();
    fringe.add(new Node(problem.initialState));
    while (true) {
      if (fringe.isEmpty()) {
        return null;
      }
      Node node = fringe.poll();
      node.state.printField();
      if (problem.goalTest(node)) {
        return node;
      }
      fringe.addAll(expand(node, problem));
    }
  }

  public static LinkedList<Node> expand(Node node, Problem problem) {
    LinkedList<Node> successors = new LinkedList<>();

    for (Tupel<Action, State> t : problem.succesorFn(node.state)) {
      Node s = new Node();
      s.parentNode = node;
      s.action = t.x;
      s.state = t.y;
      s.pathCost = node.pathCost + stepCost(node.state, t.x, t.y);
      s.depth = node.depth + 1;
      successors.add(s);
    }
    return successors;
  }

  public static int stepCost(State state, Action action, State succ) {
    return 1;
  }
}


class Problem {
  public State initialState, finalState;

  public Problem(State initialState, State finalState) {
    this.initialState = initialState;
    this.finalState = finalState;
  }

  public boolean goalTest(Node node) {
    return finalState.coordinates.equals(node.state.coordinates);
  }

  public LinkedList<Tupel<Action, State>> succesorFn(State state) {
    LinkedList<Tupel<Action, State>> succesorFn = new LinkedList<>();
    Tupel<Integer, Integer> current = state.coordinates;
    for (Action action : Action.values()) {
      Tupel<Integer, Integer> next = new Tupel<>(current.x + action.x, current.y + action.y);
      if (!state.notFree.contains(next)) {
        HashSet<Tupel<Integer, Integer>> newNotFree = new HashSet<>(state.notFree);
        newNotFree.add(next);
        succesorFn.add(new Tupel<>(action, new State(next, newNotFree)));
      }
    }
    return succesorFn;
  }

}


class State {
  public Tupel<Integer, Integer> coordinates;
  public HashSet<Tupel<Integer, Integer>> notFree;

  public State(Tupel<Integer, Integer> coordinates, HashSet<Tupel<Integer, Integer>> notFree) {
    this.coordinates = coordinates;
    this.notFree = notFree;
  }

  public void printField() {
    for (int i = 1; i <= Aufgabe1.n; i++) {
      for (int j = 1; j <= Aufgabe1.m; j++) {
        if (new Tupel<>(i, j).equals(coordinates)) {
          System.out.print("@");
        } else if (notFree.contains(new Tupel<>(i, j))) {
          System.out.print("#");
        } else {
          System.out.print(" ");
        }
      }
      System.out.println();
    }
    System.out.println();
  }
}


class Tupel<X, Y> {
  public final X x;
  public final Y y;

  public Tupel(X first, Y second) {
    this.x = first;
    this.y = second;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || !o.getClass().equals(getClass())) {
      return false;
    }
    if (o == this) {
      return true;

    }
    Tupel<X, Y> that = (Tupel<X, Y>) o;
    return that.x.equals(x) && that.y.equals(y);
  }

  @Override
  public int hashCode() {
    int hash = 1;
    hash = hash * 31 + x.hashCode();
    hash = hash * 17 + y.hashCode();
    return hash;
  }
}


enum Action {
  U(-1, 0), R(0, 1), D(1, 0), L(0, -1);

  public int x;
  public int y;

  private Action(int dx, int dy) {
    this.x = dx;
    this.y = dy;
  }
}


class Node {
  public Node parentNode;
  public Action action;
  public State state;
  public int pathCost;
  public int depth;

  public Node() {}

  public Node(State state) {
    this.state = state;
    pathCost = 0;
    depth = 0;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || !o.getClass().equals(getClass())) {
      return false;
    }
    if (o == this) {
      return true;
    }

    Node that = (Node) o;
    return that.state.equals(state);
  }
}
