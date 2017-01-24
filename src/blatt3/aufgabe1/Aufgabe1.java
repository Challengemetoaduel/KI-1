package blatt3.aufgabe1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Aufgabe1 {
  static ArrayList<ArrayList<Variable>> variables;

  public static void main(String[] args) {
    LinkedHashSet<Arc> constraints = readInput();

    arcConsistency3(new LinkedList<>(constraints));

    for (ArrayList<Variable> row : variables) {
      for (Variable var : row) {
        System.out.print(var.printDomain());
      }
      System.out.println();
    }
  }

  public static LinkedHashSet<Arc> readInput() {
    LinkedHashSet<Arc> arcs = new LinkedHashSet<>();
    variables = new ArrayList<>(9);

    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      for (int i = 0; i < 9; i++) {
        String line = reader.readLine();
        ArrayList<Variable> rowVariables = new ArrayList<>(9);

        for (int j = 0; j < 9; j++) {
          int value = Integer.parseInt(line.charAt(j) + "");
          rowVariables.add(new Variable(i, j, value));
        }

        variables.add(rowVariables);
      }

      for (ArrayList<Variable> list : variables) {
        for (Variable current : list) {
          arcs.addAll(current.setNeighbors(variables));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return arcs;
  }


  public static void arcConsistency3(Queue<Arc> queue) {
    while (!queue.isEmpty()) {
      Arc current = queue.poll();

      if (removeInconsistentValues(current)) {
        for (Variable neighbor : current.first.neighbors) {
          queue.add(new Arc(neighbor, current.first));
        }
      }
    }
  }

  public static boolean removeInconsistentValues(Arc current) {
    boolean removed = false;
    Variable first = current.first;
    Variable second = current.second;

    Iterator<Integer> iter = first.domain.iterator();

    while (iter.hasNext()) {
      Integer x = iter.next();
      if (second.domain.size() == 1 && second.domain.contains(x)) {
        iter.remove();
        removed = true;
      }
    }

    return removed;
  }
}


class Arc {
  Variable first, second;

  public Arc(Variable first, Variable second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || !o.getClass().equals(getClass())) {
      return false;
    }
    if (o == this) {
      return true;
    }
    Arc that = (Arc) o;
    return (first.equals(that.first) && second.equals(that.second))
        || (first.equals(that.second) && second.equals(that.second));
  }

  @Override
  public String toString() {
    return first + " =/= " + second;
  }
}


class Variable {
  int x;
  int y;

  HashSet<Integer> domain = new HashSet<>();
  LinkedHashSet<Variable> neighbors = new LinkedHashSet<>(24);


  public Variable(int x, int y, int value) {
    this.x = x;
    this.y = y;

    if (value == 0) {
      for (int i = 1; i < 10; i++) {
        domain.add(i);
      }
    } else {
      domain.add(value);
    }
  }

  public LinkedList<Arc> setNeighbors(ArrayList<ArrayList<Variable>> variables) {
    neighbors.addAll(variables.get(x));

    for (int i = 0; i < 9; i++) {
      neighbors.add(variables.get(i).get(y));
    }

    for (int i = x / 3 * 3; i < x / 3 * 3 + 3; i++) {
      for (int j = y / 3 * 3; j < y / 3 * 3 + 3; j++) {
        neighbors.add(variables.get(i).get(j));
      }
    }

    neighbors.remove(this);

    LinkedList<Arc> arcs = new LinkedList<>();

    for (Variable var : neighbors) {
      arcs.add(new Arc(this, var));
    }

    return arcs;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || !o.getClass().equals(getClass())) {
      return false;
    }
    if (o == this) {
      return true;
    }
    Variable that = (Variable) o;
    return x == that.x && y == that.y;
  }

  @Override
  public String toString() {
    return "[" + x + ", " + y + "]";
  }

  public String printDomain() {
    StringBuilder builder = new StringBuilder("{");

    for (int i = 1; i < 10; i++) {
      if (domain.contains(i)) {
        builder.append(i + ",");
      }
    }
    builder.deleteCharAt(builder.length() - 1);
    builder.append("}");
    return builder.toString();
  }
}
