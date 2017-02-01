package csp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.function.Predicate;

public class AC_3 {

  public static <T> void main(String[] args) throws FileNotFoundException {
    Scanner in = new Scanner(new File("src/res/input.txt"));
    int varsSize = in.nextInt();
    int discrepancy = in.nextInt();
    in.nextLine();
    HashMap<Integer, Variable> vars = new HashMap<>(varsSize);
    HashSet<Arc> constraints = new HashSet<>(varsSize * discrepancy);
    Collection<Integer> domain = new HashSet<>(varsSize);
    for (int i = 0; i < varsSize; i++) {
      domain.add(i);
    }


    while (in.hasNextLine()) {
      String line = in.nextLine();
      String[] splitLine = line.split(" ");
      int[] clause = new int[splitLine.length];
      for (int i = 0; i < splitLine.length; i++) {
        clause[i] = Integer.parseInt(splitLine[i]);
      }

      for (int i : clause) {
        Variable first;
        if (vars.containsKey(i)) {
          first = vars.get(i);
        } else {
          first = new Variable(i, domain);
          vars.put(i, first);
        }


        for (int j : clause) {
          if (i != j) {
            Variable second;
            if (vars.containsKey(j)) {
              second = vars.get(j);
            } else {
              second = new Variable(j, domain);
              vars.put(j, second);
              first.neighbors.add(second);
            }

            Arc constraint = new Arc(first, second);
            constraints.add(constraint);
          }
        }
      }
    }

    Predicate<Tupel> predicate = new Predicate<Tupel>() {
      @Override
      public boolean test(Tupel t) {
        if (t.x == t.y || Math.abs(t.x - t.y) > discrepancy) {
          return false;
        }
        return true;
      }
    };
    Arc.setPredicate(predicate);

    Collection<Integer> setDomain = new LinkedList<>();
    setDomain.add(0);
    vars.get(1).domain = setDomain;

    CSP csp = new CSP(new LinkedList<>(constraints), vars.values());

    for (Variable var : csp.variables) {
      System.out.println(var);
    }

    System.out.println();
    ac_3(csp);

    for (Variable var : csp.variables) {
      System.out.println(var);
    }

    in.close();
  }

  public static CSP ac_3(CSP csp) {
    Queue<Arc> arcs = csp.arcs;
    while (!arcs.isEmpty()) {
      Arc arc = arcs.poll();
      if (removeInconsistentValues(arc)) {
        for (Variable var : arc.first.neighbors) {
          arcs.add(new Arc(var, arc.first));
        }
      }
    }

    return csp;
  }

  public static boolean removeInconsistentValues(Arc arc) {
    boolean removed = false;
    for (Iterator<Integer> iterator = arc.first.domain.iterator(); iterator.hasNext();) {
      int x = (int) iterator.next();
      boolean satisfies = false;
      for (int y : arc.second.domain) {
        if (Arc.predicate.test(new Tupel(x, y))) {
          satisfies = true;
          break;
        }
      }
      if (!satisfies) {
        iterator.remove();
        removed = true;
      }
    }
    return removed;
  }
}


class CSP {
  Collection<Variable> variables;
  Queue<Arc> arcs;

  public CSP(Queue<Arc> arcs, Collection<Variable> variables) {
    this.arcs = arcs;
    this.variables = variables;
  }
}


class Variable {
  int number;
  HashSet<Variable> neighbors;
  Collection<Integer> domain;

  public Variable(int number, Collection<Integer> domain) {
    this.number = number;
    this.domain = domain;
    neighbors = new HashSet<>();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("[number = " + number + ", index = [");

    for (int x : domain) {
      builder.append(x + ", ");
    }

    if (!domain.isEmpty()) {
      builder.delete(builder.length() - 2, builder.length());
    }
    builder.append("]");
    return builder.toString();
  }

}


class Arc {
  Variable first;
  Variable second;
  static Predicate<Tupel> predicate;

  public Arc(Variable first, Variable second) {
    this.first = first;
    this.second = second;
  }

  public static void setPredicate(Predicate<Tupel> predicate) {
    Arc.predicate = predicate;
  }

  @Override
  public String toString() {
    return "[" + first.toString() + ", " + second.toString() + "]";
  }
}


class Tupel {
  int x;
  int y;

  public Tupel(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
