package blatt2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class State {
  private static Boolean[][] free;

  private Point currentPosition;
  private List<Point> boxes;
  public State cameFrom;

  public enum Direction {
    UP, RIGHT, DOWN, LEFT;

    public static Direction inverse(Direction dir) {
      switch (dir) {
        case UP:
          return DOWN;
        case RIGHT:
          return LEFT;
        case DOWN:
          return UP;
        case LEFT:
          return RIGHT;
        default:
          return null;
      }
    }

    public String toString() {
      switch (this) {
        case UP:
          return "U";
        case RIGHT:
          return "R";
        case DOWN:
          return "D";
        case LEFT:
          return "L";
        default:
          return null;
      }
    }
  };

  public Direction direction;

  public State(Point position, State cameFrom, List<Point> boxes, Direction direction) {
    currentPosition = position;
    this.cameFrom = cameFrom;
    this.boxes = boxes;
    this.direction = direction;
  }

  public void setField(Boolean[][] field) {
    free = field;
  }

  public List<Point> getBoxes() {
    return boxes;
  }

  public List<State> nextStates() {
    List<State> nextStates = new ArrayList<>();

    for (Direction dir : Direction.values()) {
      State temp = move(dir);
      if (temp != null
          && !(dir.equals(Direction.inverse(cameFrom.direction)) && cameFrom.boxes
              .equals(temp.boxes))) {
        nextStates.add(temp);
      }
    }

    return nextStates;
  }

  private State move(Direction direction) {
    int transX = 0;
    int transY = 0;
    switch (direction) {
      case UP:
        transX = 1;
        break;
      case DOWN:
        transX = -1;
        break;
      case RIGHT:
        transY = 1;
        break;
      case LEFT:
        transY = -1;
        break;
      default:
        break;
    }
    Point newPosition = new Point(currentPosition);
    newPosition.translate(transX, transY);

    Point nextPosition = new Point(newPosition);
    nextPosition.translate(transX, transY);


    List<Point> newBoxes = new ArrayList<>(boxes);
    if (free[newPosition.x][newPosition.y]) {
      if (boxes.contains(newPosition)) {
        if (boxes.contains(nextPosition) || !free[nextPosition.x][nextPosition.y]) {
          return null;
        } else {
          newBoxes.remove(newPosition);
          newBoxes.add(nextPosition);
        }
      }
      return new State(newPosition, this, newBoxes, direction);
    } else {
      return null;
    }
  }
}
