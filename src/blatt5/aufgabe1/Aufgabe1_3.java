package blatt5.aufgabe1;

import java.util.Scanner;

public class Aufgabe1_3 {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int n = in.nextInt();

    int[] D = new int[n];

    for (int i = 0; i < n; i++) {
      D[i] = in.nextInt();
    }

    int X[] = new int[n];
    for (int i = 0; i < n; i++) {
      X[i] = in.nextInt();
    }

    in.close();

    System.out.println(index(n, D, X));
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
