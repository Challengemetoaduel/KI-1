package blatt6.aufgabe1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Set;

public class Aufgabe1 {

  public static void main(String[] args) throws FileNotFoundException {
    Scanner in = new Scanner(new File("src/blatt6/aufgabe1/input.txt"));
    // Scanner in = new Scanner(System.in);

    int numberOfClauses = 0;

    while (in.hasNextLine()) {
      String line = in.nextLine();
      if (line.startsWith("p")) {
        String[] splitLine = line.split(" ");
        numberOfClauses = Integer.parseInt(splitLine[3]);
        break;
      }
    }

    List<Clause> clauses = new ArrayList<>(numberOfClauses);
    for (int i = 0; i < numberOfClauses; i++) {
      ArrayList<Atom> atoms = new ArrayList<>();
      int atomNumber = 1;
      while (true) {
        atomNumber = in.nextInt();
        if (atomNumber == 0) {
          break;
        }
        Atom atom = new Atom(Math.abs(atomNumber));
        if (atomNumber < 0) {
          atom = atom.not();
        }
        atoms.add(atom);
      }
      clauses.add(new Clause(atoms));
    }

    System.out.println(isInconsistent(clauses));

    in.close();
  }

  public static boolean isInconsistent(List<Clause> clausesAll) {
    Set<Clause> clausesNew = new HashSet<>();

    while (true) {
      for (ListIterator<Clause> iterA = clausesAll.listIterator(); iterA.hasNext();) {
        Clause ci = iterA.next();
        for (ListIterator<Clause> iterB = clausesAll.listIterator(iterA.previousIndex() + 1); iterB
            .hasNext();) {
          Clause cj = iterB.next();

          Set<Clause> resolventen = plResolve(ci, cj);
          System.out
              .println("first:" + ci + ", snd:" + cj + ", resolvents:" + resolventen.toString());
          System.out.println();
          if (resolventen.contains(Clause.EMPTY)) {
            return true;
          }
          clausesNew.addAll(resolventen);
        }
      }
      if (clausesAll.containsAll(clausesNew)) {
        return false;
      }
      clausesAll.addAll(clausesNew);
    }

  }

  public static Set<Clause> plResolve(Clause first, Clause second) {
    Set<Clause> resolvents = new HashSet<>();
    // if (first.equals(second)) {
    // resolvents.add(first);
    // return resolvents;
    // }

    for (int i = 0; i < second.atoms.size(); i++) {
      Atom atom = second.atoms.get(i);
      // if (first.contains(atom.not()) || second.contains(atom.not())) {
      // if (second.contains(atom.not())) {
      // continue;
      // }
      if (first.contains(atom.not())) {
        HashSet<Atom> newAtoms = new HashSet<>(first.atoms);
        newAtoms.addAll(second.atoms);
        newAtoms.remove(atom);
        newAtoms.remove(atom.not());
        // if ((second.contains(atom.not()))) {
        // newAtoms.add(atom.not());
        // }
        // if ((second.contains(atom))) {
        // newAtoms.add(atom);
        // }
        Clause clause = new Clause(new ArrayList<>(newAtoms));
        resolvents.add(clause);
      }
    }
    return resolvents;
  }
}


class Clause implements Iterable<Atom> {
  public static final Clause EMPTY = new Clause(Collections.<Atom>emptyList());
  public List<Atom> atoms;

  public Clause(List<Atom> atoms) {
    this.atoms = atoms;
  }

  public boolean contains(Atom atom) {
    return atoms.contains(atom);
  }

  @Override
  public Iterator<Atom> iterator() {
    return atoms.iterator();
  }

  @Override
  public String toString() {
    return "[atoms = " + atoms.toString() + "]";
  }

  @Override
  public int hashCode() {
    return atoms.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || !(o.getClass().equals(getClass()))) {
      return false;
    }
    Clause that = (Clause) o;

    return atoms.equals(that.atoms);
  }
}


class Atom {
  int number;
  boolean negated;

  public Atom(int number) {
    this.number = number;
    negated = false;
  }

  private Atom(int number, boolean negated) {
    this.number = number;
    this.negated = negated;

  }

  public Atom not() {
    return new Atom(number, !negated);
  }

  @Override
  public int hashCode() {
    int hash = 1;
    hash = hash * 1033 + number;
    hash = hash * 31 + (negated ? 2243 : 1231);
    return hash;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || !(o.getClass().equals(getClass()))) {
      return false;
    }
    Atom that = (Atom) o;
    if (that == this) {
      return true;
    }
    return (number == that.number) && (negated == that.negated);
  }

  @Override
  public String toString() {
    return "[" + (negated ? 0 - number : number) + "]";
  }

}
