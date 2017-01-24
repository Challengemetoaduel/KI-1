package blatt2.aufgabe1;

public class Test {
  public static void main(String[] args) {
    Pair pair = new Pair(1, 2);

    System.out.println(pair.toString());
    test(pair);
    System.out.println(pair);
  }

  public static void test(Pair pair) {
    Pair b = pair;
    b.x = 10;
    b.y = -100;
  }
}


class Pair {
  public int x;
  public int y;

  public Pair(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "[" + x + ", " + y + "]";
  }
}
