package blatt6.aufgabe1.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

public class Aufgabe1 {

  // public static void main(String[] args) throws FileNotFoundException {
  public static void main(String[] atgs) {
    Scanner in = new Scanner(System.in);
    // Scanner in = new Scanner(new File("src/blatt6/aufgabe1/input.txt"));
    String line = in.nextLine();
    while (line.startsWith("c")) {
      line = in.nextLine();
    }
    int numberOfClauses = Integer.parseInt(line.split(" ")[3]);
    ArrayList<Clause> clauses = new ArrayList<>();
    for (int i = 0; i < numberOfClauses; i++) {
      HashSet<Atom> atoms = new HashSet<>();
      int number = in.nextInt();
      while (number != 0) {
        atoms.add(new Atom(number));
        number = in.nextInt();
      }
      Clause newClause = new Clause(atoms);
      clauses.add(newClause);
    }

    in.close();

    System.out.println(isInconsistent(clauses));
  }

  public static boolean isInconsistent(Collection<Clause> clauses) {
    HashSet<Clause> clausesNew = new HashSet<>();
    ArrayList<Clause> clausesAll = new ArrayList<>(clauses);
    while (true) {
      for (int i = 0; i < clausesAll.size(); i++) {
        Clause ci = clausesAll.get(i);
        for (int j = i + 1; j < clausesAll.size(); j++) {
          Clause cj = clausesAll.get(j);
          HashSet<Clause> resolvents = new HashSet<>(plResolve(ci, cj));
          // System.out.println(ci + ", " + cj + ", " + resolvents.toString());
          if (resolvents.contains(Clause.EMPTY)) {
            return true;
          }
          clausesNew.addAll(resolvents);
        }
      }
      if (clausesAll.containsAll(clausesNew)) {
        return false;
      }
      clausesAll.addAll(clausesNew);
    }
  }

  public static Collection<Clause> plResolve(Clause first, Clause second) {
    HashSet<Clause> resolvent = new HashSet<>();
    for (Atom atom : first.atoms) {
      if (second.atoms.contains(atom.not())) {
        HashSet<Atom> newAtoms = new HashSet<>(first.atoms);
        newAtoms.remove(atom);
        HashSet<Atom> newSecondAtoms = new HashSet<>(second.atoms);
        newSecondAtoms.remove(atom.not());

        newAtoms.addAll(newSecondAtoms);

        Clause newClause = new Clause(newAtoms);
        resolvent.add(newClause);
      }
    }
    return resolvent;
  }

}


class Clause {
  public static final Clause EMPTY = new Clause(Collections.<Atom>emptyList());

  public HashSet<Atom> atoms;

  public Clause(Collection<Atom> atoms) {
    this.atoms = new HashSet<>(atoms);
  }

  @Override
  public boolean equals(Object o) {
    Clause that = (Clause) o;
    return atoms.containsAll(that.atoms);
  }

  @Override
  public int hashCode() {
    int hash = 31;
    for (Atom atom : atoms) {
      hash += atom.hashCode();
    }
    return hash * 13;
  }

  @Override
  public String toString() {
    return "[atoms = " + atoms.toString() + "]";
  }
}


class Atom {
  public int number;
  public boolean sgn;

  public Atom(int number) {
    if (number < 0) {
      this.sgn = false;
    } else {
      this.sgn = true;
    }
    this.number = Math.abs(number);
  }

  public Atom not() {
    return new Atom(sgn ? -number : number);
  }

  @Override
  public boolean equals(Object o) {
    Atom that = (Atom) o;

    return number == that.number && (sgn == that.sgn);
  }

  @Override
  public int hashCode() {
    int hash = 1;
    hash = hash * 13 + number;
    hash = hash * 2351 + (sgn ? 1787 : 773);
    return hash;
  }

  @Override
  public String toString() {
    return "[" + (sgn ? number : -number) + "]";
  }
}
