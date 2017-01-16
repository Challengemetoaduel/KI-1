package blatt5.aufgabe1;

import java.util.Scanner;

public class Aufgabe1_5 {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    int[] D = new int[n];
    for (int i = 0; i < n; i++) {
      D[i] = in.nextInt();
    }
    int k = in.nextInt();

    for (int i = 0; i < k; i++) {
      int l = in.nextInt();
      int[] S = new int[l];
      for (int j = 0; j < l; j++) {
        S[j] = in.nextInt();
      }
      int f = calc(l, D, S);
      double[] values = new double[f];
      for (int j = 0; j < l; j++) {
        values[j] = in.nextDouble();
      }
    }
    in.close();
  }

  public static int calc(int l, int[] D, int[] S) {
    int length = 1;
    for (int i = 0; i < l; i++) {
      length *= D[S[i]];
    }
    return length;
  }

  public static int[] revInd(int n, int[] D, int index) {
    int[] X = new int[n];
    for (int i = 0; i < n; i++) {
      X[i] = index % D[i];
      index = index / D[i];
    }

    return X;
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
}
