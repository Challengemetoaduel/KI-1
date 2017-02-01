package csp2;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

import sun.security.krb5.Asn1Exception;

public class AC_3 {
  public static void main(String[] args) {

  }

  public static <X> void ac_3(CSP<X> csp) {
    Queue<Arc<X>> queue = new LinkedList<>(csp.arcs);

    while (!queue.isEmpty()) {
      Arc<X> arc = queue.poll();
      if (removeInconsistentValues(arc)) {
        for (Variable<X> neighbor : arc.first.neighbors) {
          queue.add(new Arc<X>(neighbor, arc.first));
        }
      }
    }
  }

  public static <X> boolean removeInconsistentValues(Arc<X> arc) {
    boolean removed = false;
    for (Iterator<X> iterator = arc.first.domain.iterator(); iterator.hasNext();) {
      X x = iterator.next();
      boolean satisfied = false;
      for (X y : arc.second.domain) {
        if (arc.predicate.test(new Tupel<X>(x, y))) {
          satisfied = true;
          break;
        }
      }
      if (!satisfied) {
        iterator.remove();
        removed = true;
      }
    }
    return removed;
  }

  public static <X> void backtrackingSearch(CSP<X> csp) {}

  public static <X> void recursiveBacktracking(CSP<X> csp) {
    Variable<X> var = selectUnassignedVariable();
    for (Iterator<X> iterator = var.domain.iterator(); iterator.hasNext();) {
      X value = iterator.next();
      boolean consistent = true;

      for (Arc<X> arc : csp.arcs) {
        boolean arcConsistent = false;
        for (X x : arc.second.domain) {
          if (arc.predicate.test(new Tupel<X>(value, x))) {
            arcConsistent = true;
            break;
          }
        }
        if (!arcConsistent) {
          consistent = false;
          break;
        }
      }

      if (consistent) {
        // TODO: somehow create copy of csp
      }

    }
  }

  public static <X> Variable<X> selectUnassignedVariable() {
    return null;
  }
}


class CSP<X> {
  Collection<Variable<X>> variables;
  Collection<Arc<X>> arcs;

  public CSP(Collection<Variable<X>> variables, Collection<Arc<X>> arcs) {
    this.variables = variables;
    this.arcs = arcs;
  }
}


class Variable<X> {
  HashSet<X> domain;
  HashSet<Variable<X>> neighbors;

  public Variable(HashSet<X> domain) {
    this.domain = domain;
    neighbors = new HashSet<>();
  }
}


class Arc<X> {
  Variable<X> first;
  Variable<X> second;
  Predicate<Tupel<X>> predicate;

  public Arc(Variable<X> first, Variable<X> second) {
    this.first = first;
    this.second = second;
  }
}


class Tupel<X> {
  X first;
  X second;

  public Tupel(X first, X second) {
    this.first = first;
    this.second = second;
  }
}
