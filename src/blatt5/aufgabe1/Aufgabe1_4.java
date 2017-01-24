package blatt5.aufgabe1;

import java.util.Scanner;

public class Aufgabe1_4 {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int n = in.nextInt();
    int[] D = new int[n];
    for (int i = 0; i < n; i++) {
      D[i] = in.nextInt();
    }
    int index = in.nextInt();

    in.close();

    int[] X = revInd(n, D, index);

    System.out.print(X[0]);
    for (int i = 1; i < n; i++) {
      System.out.print(" " + X[i]);
    }

  }

  public static int[] revInd(int n, int[] D, int index) {
    int[] X = new int[n];
    for (int i = 0; i < n; i++) {
      X[i] = index % D[i];
      index = index / D[i];
    }

    return X;
  }

}
