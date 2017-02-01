package csp;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Backtracking {

  public static void main(String[] args) {

  }

  public static Collection<Variable> backtrackingSearch(CSP csp) {
    return recursiveBacktracking(new HashSet<>(), csp);
  }

  public static Collection<Variable> recursiveBacktracking(Collection<Variable> assignment,
      CSP csp) {
    if (isComplete(assignment)) {
      return assignment;
    }
    Variable var = selectUnassignedVariable(csp.variables, assignment, csp);

    for(int x : var.domain) {
      if
    }
    
    return null;
  }

  public static boolean isComplete(Collection<Variable> assignment) {
    for (Variable var : assignment) {
      if (var.domain.size() > 1) {
        return false;
      }
    }
    return true;
  }

  public static Variable selectUnassignedVariable(Collection<Variable> variables,
      Collection<Variable> assignment, CSP csp) {
    PriorityQueue<Variable> priorityQueue = new PriorityQueue<>(new Comparator<Variable>() {
      @Override
      public int compare(Variable o1, Variable o2) {
        if (o1.domain.size() == 1) {
          return -1;
        }
        return Integer.compare(o1.domain.size(), o2.domain.size());
      }
    });
    priorityQueue.addAll(variables);
    return priorityQueue.element();
  }
}
