package blatt5.aufgabe1;

import java.util.Scanner;

public class Aufgabe1 {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    int[] D = new int[n];
    for (int i = 0; i < n; i++) {
      D[i] = in.nextInt();
    }

    int k = in.nextInt();
    int[][] factors = new int[k][];
    double[][] MDAs = new double[k][];
    for (int i = 0; i < k; i++) {
      int l = in.nextInt();
      int[] S = new int[l];
      for (int j = 0; j < l; j++) {
        S[j] = in.nextInt();
      }
      factors[i] = S;
      int size = 1;
      for (int j = 0; j < l; j++) {
        size *= D[S[j]];
      }
      double[] MDA = new double[size];
      for (int j = 0; j < size; j++) {
        MDA[j] = in.nextDouble();
      }
      MDAs[i] = MDA;
    }

    in.close();
    double[] result = mult(n, D, k, factors, MDAs);

    System.out.print(result[0]);
    for (int i = 1; i < result.length; i++) {
      System.out.print(" " + result[i]);
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

  public static double[] mult(int n, int[] D, int k, int[][] factors, double[][] MDAs) {
    int[] variables = new int[n];
    int resultSize = 1;
    for (int i = 0; i < D.length; i++) {
      resultSize *= D[i];
    }
    double[] results = new double[resultSize];
    for (int i = 0; i < resultSize; i++) {
      double result = 1;
      for (int j = 0; j < k; j++) {
        int[] currentFactors = factors[j];
        double[] currentMDA = MDAs[j];
        int[] newD = new int[currentFactors.length];

        for (int l = 0; l < newD.length; l++) {
          newD[l] = D[currentFactors[l]];
        }

        int[] newVars = new int[currentFactors.length];
        for (int l = 0; l < newVars.length; l++) {
          newVars[l] = variables[currentFactors[l]];
        }

        result *= currentMDA[index(currentFactors.length, newD, newVars)];
      }
      results[i] = round(result, 2);

      increaseIndex(D, variables);
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

  public static void increaseIndex(int[] D, int[] variables) {
    for (int i = 0; i < variables.length; i++) {
      variables[i]++;
      if (variables[i] >= D[i]) {
        variables[i] = 0;
      } else {
        break;
      }
    }
  }
}
