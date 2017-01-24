package blatt3.aufgabe2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Predicate;

public class Aufgabe2 {
  static ArrayList<ArrayList<Variable>> variables;
  static ArrayList<Variable> unSetVariables;
  static Stack<StackVariable> stack;

  public static void main(String[] args) {

    LinkedHashSet<Arc> startConstraints = readInput();

    arcConsistency3(new LinkedList<>(startConstraints));

    unSetVariables = new ArrayList<>(81);

    for (ArrayList<Variable> row : variables) {
      unSetVariables.addAll(row);
    }

    unSetVariables.sort(new Comparator<Variable>() {
      @Override
      public int compare(Variable o1, Variable o2) {
        return Integer.compare(o1.domain.size(), o2.domain.size());
      }
    });


    unSetVariables.removeIf(new Predicate<Variable>() {
      @Override
      public boolean test(Variable t) {
        return t.domain.size() <= 1;
      }
    });

    Stack<Variable> tempSetVariables = new Stack<>();
    while (!unSetVariables.isEmpty()) {
      stack.push(null);
      Variable first = unSetVariables.remove(0);
      // Variable firstSmallerDomain = new Variable(first);
      tempSetVariables.push(new Variable(first));
      tempSetVariables.push(null);
      try {
        if (!first.setDomain()) {
          first.domain = first.originalDomain;
          first.originalDomain = new HashSet<>();

          tempSetVariables.pop();

          tempSetVariables.pop();

          while (!stack.isEmpty()) {
            StackVariable current = stack.pop();
            if (current == null) {
              break;
            }
            current.variable.domain.add(current.value);
          }
          unSetVariables.add(first);
          throw new DomainEmpty();
        }
        arcConsistency3Rec(new LinkedList<>(startConstraints));

        unSetVariables.sort(new Comparator<Variable>() {
          @Override
          public int compare(Variable o1, Variable o2) {
            return Integer.compare(o1.domain.size(), o2.domain.size());
          }
        });

        while (unSetVariables.isEmpty()) {
          Variable var = unSetVariables.get(0);
          if (var.domain.size() > 1) {
            break;
          }
          tempSetVariables.push(var);
          unSetVariables.remove(0);
        }
      } catch (DomainEmpty exc) {
        // tempSetVariables.pop();
        while (!tempSetVariables.isEmpty()) {
          Variable current = tempSetVariables.pop();
          if (current == null) {
            break;
          }
          unSetVariables.add(current);
        }
        Variable changedFirst = tempSetVariables.pop();
        unSetVariables.add(changedFirst);
        variables.get(changedFirst.x).remove(changedFirst.y);
        variables.get(changedFirst.x).add(changedFirst);
        variables.get(changedFirst.x).sort(new Comparator<Variable>() {

          @Override
          public int compare(Variable o1, Variable o2) {
            return Integer.compare(o1.y, o2.y);
          }
        });

        while (!stack.isEmpty()) {
          StackVariable current = stack.pop();
          if (current == null) {
            break;
          }
          current.variable.domain.add(current.value);
        }

        unSetVariables.sort(new Comparator<Variable>() {
          @Override
          public int compare(Variable o1, Variable o2) {
            return Integer.compare(o1.domain.size(), o2.domain.size());
          }
        });

        continue;
      }
    }

    for (ArrayList<Variable> row : variables) {
      for (Variable var : row) {
        System.out.print(var.printValue());
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

  // TODO: Maybe back to LinkedHashSet<Arc> set
  public static void arcConsistency3(Queue<Arc> queue) {

    // Queue<Arc> queue = new LinkedList<>(set);

    while (!queue.isEmpty()) {
      Arc current = queue.poll();

      if (removeInconsistentValues(current)) {
        for (Variable neighbor : current.first.neighbors) {
          queue.add(new Arc(neighbor, current.first));
        }
      }
    }

    // set = new LinkedHashSet<>(queue);
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

  public static void arcConsistency3Rec(Queue<Arc> queue) throws DomainEmpty {

    // Queue<Arc> queue = set;

    while (!queue.isEmpty()) {
      Arc current = queue.poll();

      if (removeInconsistentValuesRec(current)) {
        for (Variable neighbor : current.first.neighbors) {
          queue.add(new Arc(neighbor, current.first));
        }
      }
    }

    // set = new LinkedHashSet<>(queue);
  }

  public static boolean removeInconsistentValuesRec(Arc current) throws DomainEmpty {
    boolean removed = false;
    Variable first = current.first;
    Variable second = current.second;

    Iterator<Integer> iter = first.domain.iterator();

    while (iter.hasNext()) {
      Integer x = iter.next();
      if (second.domain.size() == 1 && second.domain.contains(x)) {
        stack.push(new StackVariable(first, x));

        iter.remove();
        removed = true;
      }
    }

    if (first.domain.size() == 0) {
      throw new DomainEmpty();
    }

    return removed;
  }
}


class DomainEmpty extends Exception {
  public DomainEmpty() {
    super();
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
  HashSet<Integer> originalDomain = new HashSet<>();
  LinkedHashSet<Variable> neighbors = new LinkedHashSet<>(24);

  public Variable(Variable that) {
    x = that.x;
    y = that.y;
    neighbors = that.neighbors;
    originalDomain = that.originalDomain;

    int i = 1;
    for (; i < 10; i++) {
      if (that.domain.contains(i)) {
        break;
      }
    }

    for (int j = i + 1; j < 10; j++) {
      if (that.domain.contains(i)) {
        domain.add(i);
      }
    }
  }

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

  public boolean setDomain() {
    if (originalDomain.isEmpty()) {
      originalDomain = new HashSet<>(domain);
    }

    for (Integer i = 1; i < 10; i++) {
      if (domain.contains(i)) {
        domain.clear();
        domain.add(i);
        return true;
      }
    }
    return false;
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

  // public String printDomain() {
  // StringBuilder builder = new StringBuilder("{");
  //
  // for (int i = 1; i < 10; i++) {
  // if (domain.contains(i)) {
  // builder.append(i + ",");
  // }
  // }
  // builder.deleteCharAt(builder.length() - 1);
  // builder.append("}");
  // return builder.toString();
  // }

  public String printValue() {
    for (int i = 1; i < 10; i++) {
      if (domain.contains(i)) {
        return i + "";
      }
    }
    return "";
  }

}


class StackVariable {
  Variable variable;
  int value;

  public StackVariable(Variable variable, int value) {
    this.variable = variable;
    this.value = value;
  }
}
