package blatt2;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Aufgabe2 {

  static Boolean[][] free;
  static List<Point> destinations;

  public static void main(String[] args) throws IOException {
    State start = readInput();
    start.setField(free);

    List<State> states = new ArrayList<State>();
    states.add(start);

    State finalState = null;
    while (!states.isEmpty()) {
      State current = states.remove(0);
      if (destinations.containsAll(current.getBoxes())) {
        finalState = current;
        break;
      }

      states.addAll(current.nextStates());
    }

    StringBuilder builder = new StringBuilder();

    while (finalState != null) {
      builder.append(finalState.direction.toString());
      finalState = finalState.cameFrom;
    }

    System.out.println(builder.reverse().toString());
  }

  public static State readInput() throws IOException {
    File file = new File("src/blatt2/eingabe.txt");
    Scanner in = new Scanner(file);

    // Scanner in = new Scanner(System.in);

    String readLine;
    List<Boolean[]> booleanArrayList = new ArrayList<>();
    List<Point> boxes = new ArrayList<Point>();
    Point start = null;
    destinations = new ArrayList<Point>();

    int lineCnt = -1;
    int charCnt = 0;
    while (in.hasNext()) {
      readLine = in.nextLine();
      charCnt = readLine.length();
      lineCnt++;
      Boolean[] tempBoolArray = new Boolean[charCnt];
      for (int i = 0; i < charCnt; i++) {
        tempBoolArray[i] = true;
        switch (readLine.charAt(i)) {
          case '#':
            tempBoolArray[i] = false;
            break;
          case '.':
            destinations.add(new Point(lineCnt, i));
            break;
          case '@':
            start = new Point(lineCnt, i);
            break;
          case '$':
            boxes.add(new Point(lineCnt, i));
            break;
          default:
            break;
        }
      }
      booleanArrayList.add(tempBoolArray);
    }
    in.close();

    free = booleanArrayList.toArray(new Boolean[lineCnt + 1][charCnt]);
    return new State(start, null, boxes, null);
  }
}
