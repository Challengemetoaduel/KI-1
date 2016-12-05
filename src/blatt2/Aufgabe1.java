package blatt2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Aufgabe1 {

  public static void main(String[] args) throws IOException {
    // File file = new File("src/blatt2/eingabe.txt");
    // BufferedReader reader = new BufferedReader(new FileReader(file));
    Scanner in = new Scanner(System.in);

    String readLine;
    List<Boolean[]> booleanArrayList = new ArrayList<>();
    Position end = null;

    Queue<Position> positions = new LinkedBlockingQueue<>();

    int lineCnt = -1;
    int charCnt = 0;
    while (in.hasNext()) {
      readLine = in.nextLine();
      charCnt = readLine.length();
      lineCnt++;
      Boolean[] tempBoolArray = new Boolean[charCnt];
      for (int i = 0; i < charCnt; i++) {
        switch (readLine.charAt(i)) {
          case '#':
            tempBoolArray[i] = false;
            break;
          case '.':
            tempBoolArray[i] = true;
            end = new Position(lineCnt, i);
            break;
          case '@':
            tempBoolArray[i] = false;
            positions.add(new Position(lineCnt, i, 0));
            break;
          case ' ':
            tempBoolArray[i] = true;
            break;
          default:
            break;
        }
      }
      booleanArrayList.add(tempBoolArray);
    }
    in.close();

    Boolean[][] free = booleanArrayList.toArray(new Boolean[lineCnt + 1][charCnt]);

    while (!positions.isEmpty()) {
      // printArray(free);
      // System.out.println(positions.toString());
      Position pos = positions.remove();

      if (pos.equals(end)) {
        end = pos;
        break;
      }

      for (int i = 0; i < 4; i++) {
        Position neighbour = pos.next();
        if (free[neighbour.x][neighbour.y]) {
          positions.add(neighbour);
          free[neighbour.x][neighbour.y] = false;
        }
      }
    }
    System.out.println(end.value);
  }

  public static void printArray(Object[][] array) {
    System.out.println(Arrays.deepToString(array));
  }
}


class Position {
  public int x;
  public int y;
  public int value;
  private int nextCount = 0;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Position(int x, int y, int value) {
    this.x = x;
    this.y = y;
    this.value = value;
  }

  public Position next() {
    nextCount++;
    switch (nextCount) {
      case 1:
        return new Position(x + 1, y, value + 1);
      case 2:
        return new Position(x, y + 1, value + 1);
      case 3:
        return new Position(x - 1, y, value + 1);
      case 4:
        return new Position(x, y - 1, value + 1);
      default:
        return null;
    }
  }

  public boolean equals(Object o) {
    if (o == null || !o.getClass().equals(this.getClass())) {
      return false;
    }
    if (o == this) {
      return true;
    }
    Position that = (Position) o;
    if (this.x == that.x && this.y == that.y) {
      return true;
    }
    return false;
  }

  public String toString() {
    return "Position[x=" + x + ", y=" + y + ", value=" + value + "]";
  }
}
