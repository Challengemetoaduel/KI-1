package blatt5.aufgabe1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Aufgabe1 {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    int[] D = new int[n];
    int maxSize = 1;
    for (int i = 0; i < n; i++) {
      D[i] = in.nextInt();
      maxSize *= D[i];
    }

    int k = in.nextInt();
    Factor[] factors = new Factor[k];

    HashMap<Tupel, Integer> map = new HashMap<>(k * maxSize);
    for (int i = 0; i < k; i++) {
      int l = in.nextInt();
      int[] S = new int[l];
      for (int j = 0; j < l; j++) {
        S[j] = in.nextInt();
      }
      int[] newD = new int[l];

      for (int j = 0; j < l; j++) {
        newD[j] = D[S[j]];
      }

      factors[i] = new Factor(i, S, newD);

      String line = in.nextLine();
      line = line.trim();
      String[] splitLine = line.split(" ");
      int[] values = new int[splitLine.length];
      for (int j = 0; j < splitLine.length; j++) {
        values[j] = (int) (Double.parseDouble(splitLine[j]) * 100);
      }

      int[] coor = new int[l];

      for (int j = 0; j < splitLine.length; j++) {
        map.put(new Tupel(i, coor.clone()), values[j]);
        coor = increaseIndex(factors[i].D, coor);
      }
    }

    in.close();
    int[] result = mult(n, D, k, factors, map);
    double factor = Math.pow(100, k);
    System.out.print(result[0] / factor);
    for (int i = 1; i < result.length; i++) {
      System.out.print(" " + (result[i] / factor));
    }
    System.out.println();
  }

  public static int index(int n, int[] D, int[] X) {
    int[] prods = new int[n + 1];
    prods[0] = 1;

    for (int i = 0; i < n; i++) {
      prods[i + 1] = prods[i] * D[i];
    }

    int index = 0;

    for (int i = 0; i < n; i++) {
      index += X[i] * prods[i];
    }

    return index;
  }

  public static int[] revInd(int n, int[] D, int index) {
    int[] X = new int[n];
    for (int i = 0; i < n; i++) {
      X[i] = index % D[i];
      index = index / D[i];
    }

    return X;
  }

  public static int[] mult(int n, int[] D, int k, Factor[] factors, HashMap<Tupel, Integer> MDA) {

    int[] vars = new int[n];
    int resultSize = 1;
    for (int i = 0; i < D.length; i++) {
      resultSize *= D[i];
    }
    int[] results = new int[resultSize];
    for (int i = 0; i < resultSize; i++) {
      int result = 1;
      for (int j = 0; j < k; j++) {

        Factor current = factors[j];

        int[] newVars = new int[current.vars.length];
        for (int m = 0; m < current.D.length; m++) {
          newVars[m] = vars[current.vars[m]];
        }
        result *= MDA.get(new Tupel(current.factorNumber, newVars.clone()));
      }
      results[i] = result;

      increaseIndex(D, vars);
    }

    return results;
  }

  public static double round(double value, int places) {
    if (places < 0) {
      throw new IllegalArgumentException();
    }

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
  }

  public static int[] increaseIndex(int[] D, int[] variables) {
    for (int i = 0; i < variables.length; i++) {
      variables[i]++;
      if (variables[i] >= D[i]) {
        variables[i] = 0;
      } else {
        break;
      }
    }
    return variables;
  }
}


class Factor {
  int factorNumber;
  int[] vars;
  int[] D;

  public Factor(int factorNumber, int[] vars, int[] D) {
    this.factorNumber = factorNumber;
    this.vars = vars;
    this.D = D;
  }
}


class Tupel {
  int factorIndex;
  int[] coordinates;

  public Tupel(int factorIndex, int[] coordinates) {
    this.factorIndex = factorIndex;
    this.coordinates = coordinates;
  }

  @Override
  public boolean equals(Object o) {
    Tupel that = (Tupel) o;

    return that.factorIndex == factorIndex && Arrays.equals(coordinates, that.coordinates);
  }

  @Override
  public int hashCode() {
    int hash = 1;
    hash = hash * 31 + factorIndex;
    hash = hash * 11 + Arrays.hashCode(coordinates);
    return hash;
  }

  @Override
  public String toString() {
    return "[" + factorIndex + ", " + Arrays.toString(this.coordinates) + "]";
  }
}
