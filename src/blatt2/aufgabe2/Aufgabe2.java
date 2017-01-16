package blatt2.aufgabe2;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Aufgabe2 {
  public static final int boxesSize = 5;
  public static int n, m;
  public static int notFreeSize;

  public static void main(String[] args) throws FileNotFoundException {
    // Scanner in = new Scanner(System.in);
    Scanner in = new Scanner(new File("src/blatt2/aufgabe2/eingabe.txt"));
    HashSet<Point> walls = null;
    HashSet<Point> boxesStart = new HashSet<>(boxesSize);
    HashSet<Point> boxesFinish = new HashSet<>(boxesSize);
    Point startPosition = null;
    n = 0;
    m = 0;
    while (in.hasNextLine()) {
      String line = in.nextLine();
      n++;
      if (n == 1) {
        m = line.length();
        notFreeSize = m * m;
        walls = new HashSet<>(notFreeSize);
      }

      for (int i = 0; i < m; i++) {
        char c = line.charAt(i);
        switch (c) {
          case '#':
            walls.add(new Point(n, i + 1));
            break;
          case '@':
            startPosition = new Point(n, i + 1);
            break;
          case '$':
            boxesStart.add(new Point(n, i + 1));
            break;
          case '.':
            boxesFinish.add(new Point(n, i + 1));
            break;
        }
      }
    }
    in.close();
    HashSet<Point> notFree = new HashSet<>(m * n);
    notFree.add(startPosition);
    State initialState = new State(startPosition, boxesStart, notFree);
    State finalState = new State(null, boxesFinish, null);
    Problem problem = new Problem(initialState, finalState, walls);
    Node result = treeSearch(problem);

    if (result == null) {
      System.out.println();
    } else {

      StringBuilder builder = new StringBuilder();
      while (result.action != null) {
        builder.append(result.action.name());
        result = result.parentNode;
      }
      System.out.println(builder.reverse().toString());
    }
  }

  public static Node treeSearch(Problem problem) {
    Queue<Node> fringe = new LinkedList<>();
    fringe.add(new Node(problem.initialState));
    while (true) {
      if (fringe.isEmpty()) {
        return null;
      }
      Node node = fringe.poll();
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
  public HashSet<Point> walls;

  public Problem(State initialState, State finalState, HashSet<Point> walls) {
    this.initialState = initialState;
    this.finalState = finalState;
    this.walls = walls;
  }

  public boolean goalTest(Node node) {
    return finalState.boxes.containsAll(node.state.boxes);
  }


  public LinkedList<Tupel<Action, State>> succesorFn(State state) {
    LinkedList<Tupel<Action, State>> succesorFn = new LinkedList<>();
    Point current = state.coordinates;

    for (Action action : Action.values()) {
      Point next = new Point(current.x + action.x, current.y + action.y);

      if (!(state.notFree.contains(next) || walls.contains(next))) {
        HashSet<Point> newBoxes;
        HashSet<Point> newNotFree;
        State newState;
        if (state.boxes.contains(next)) {
          Point nextNext = new Point(next.x + action.x, next.y + action.y);
          if (state.boxes.contains(nextNext) || walls.contains(nextNext)) {
            continue;
          }
          newBoxes = new HashSet<>(state.boxes);
          newBoxes.remove(next);
          newBoxes.add(nextNext);
          newNotFree = new HashSet<>(Aufgabe2.notFreeSize);
        } else {
          newNotFree = new HashSet<>(state.notFree);
          newNotFree.add(next);
          newBoxes = new HashSet<>(state.boxes);
        }
        newState = new State(next, newBoxes, newNotFree);
        succesorFn.add(new Tupel<Action, State>(action, newState));
      }
    }
    return succesorFn;
  }
}


class State {
  public Point coordinates;
  public HashSet<Point> boxes;
  public HashSet<Point> notFree;

  public State(Point coordinates, HashSet<Point> boxes, HashSet<Point> notFree) {
    this.coordinates = coordinates;
    this.boxes = boxes;
    this.notFree = notFree;
  }

  public void printField() {
    for (int i = 1; i <= Aufgabe2.n; i++) {
      for (int j = 1; j <= Aufgabe2.m; j++) {
        if (new Point(i, j).equals(coordinates)) {
          System.out.print("@");
        } else if (notFree.contains(new Point(i, j))) {
          System.out.print("#");
        } else if (boxes.contains(new Point(i, j))) {
          System.out.print("$");
        } else {
          System.out.println(" ");
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
