import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class TetrisShape {

    Tetris.Shapes shapes;

    private enum ShapeEnum {Z_SHAPE, S_SHAPE, T_SHAPE, L_SHAPE, J_SHAPE, O_SHAPE, I_SHAPE}

    ShapeEnum currShape;
    Color[] colors = {Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED};
    int anchorX;
    int anchorY;
    public static final int CELL_WIDTH = Tetris.BOARD_WIDTH / Tetris.HORIZONTAL_CELLS;
    public static final int CELL_HEIGHT = Tetris.BOARD_HEIGHT / (Tetris.VERTICAL_CELLS - 2);
    Tetris.Cell cell1;
    Tetris.Cell cell2;
    Tetris.Cell cell3;
    Tetris.Cell cell4;
    Tetris.Pair[] pairs = {new Tetris.Pair(-1, -1), new Tetris.Pair(0, -1), new Tetris.Pair(1, -1), new Tetris.Pair(1, 0), new Tetris.Pair(1, 1), new Tetris.Pair(0, 1), new Tetris.Pair(-1, 1), new Tetris.Pair(-1, 0),
            new Tetris.Pair(2, 0), new Tetris.Pair(0, 2), new Tetris.Pair(0, 2)};
    CopyOnWriteArrayList<Integer> shape;
    Random rand = new Random();
    Color color;
    int rotations;

    public TetrisShape(boolean nextShape) {
        // this.shape = shape;
        shapes = new Tetris.Shapes();
        System.out.println(ShapeEnum.Z_SHAPE.ordinal());
        currShape = ShapeEnum.values()[rand.nextInt(ShapeEnum.values().length)];
        shape = shapes.getShapes().get(currShape.ordinal());
        color = colors[rand.nextInt(colors.length)];
        if (!nextShape) {
            anchorX = Tetris.BOARD_WIDTH / 2;
            anchorY = -CELL_HEIGHT;
        }
        else
        {
            anchorX = 75;
            anchorY = 75;
        }
        rotations = 0;
        updateCells();

    }

    public void setX(int x)
    {
        anchorX = x;
        updateCells();
    }

    public void setY(int y)
    {
        anchorY = y;
        updateCells();
    }
    private void updateCells() {
        cell1 = new Tetris.Cell(anchorX, anchorY, CELL_WIDTH, CELL_HEIGHT, color);
        cell2 = new Tetris.Cell(anchorX + (pairs[shape.get(0)].d1 * CELL_WIDTH), anchorY + (pairs[shape.get(0)].d2 * CELL_WIDTH), CELL_WIDTH, CELL_HEIGHT, color);
        cell3 = new Tetris.Cell(anchorX + (pairs[shape.get(1)].d1 * CELL_WIDTH), anchorY + (pairs[shape.get(1)].d2 * CELL_WIDTH), CELL_WIDTH, CELL_HEIGHT, color);
        cell4 = new Tetris.Cell(anchorX + (pairs[shape.get(2)].d1 * CELL_WIDTH), anchorY + (pairs[shape.get(2)].d2 * CELL_WIDTH), CELL_WIDTH, CELL_HEIGHT, color);
    }

    //rotate any tetrominoe right
    private void rotateRight() {
        int index = 0;
        for (Integer i : shape) {
            if (currShape == ShapeEnum.I_SHAPE) {
                if (i == 7)
                    shape.set(index, (i + 2) % 8);
                else
                    shape.set(index, (i + 2) % 11);
            } else
                shape.set(index, (i + 2) % 8);
            index++;
        }

    }

    //rotate any tetrominoe left
    private void rotateLeft() {
        int index = 0;
        for (Integer i : shape) {
            if (currShape == ShapeEnum.I_SHAPE) {
                if (i == 1)
                    shape.set(index, mod_floor(i - 2, 8));
                else
                    shape.set(index, mod_floor(i - 2, 11));
            } else
                shape.set(index, mod_floor(i - 2, 8));
            index++;
        }
    }

    private int mod_floor(int a, int n) {
        return ((a % n) + n) % n;
    }

    //rotate z and s tetrominoes
    private void zAndSRotate() {
        if (rotations % 2 == 0) {
            if (currShape == ShapeEnum.S_SHAPE) {
                rotateLeft();
            } else {
                rotateRight();
            }
        } else {
            if (currShape == ShapeEnum.S_SHAPE) {
                rotateRight();
            } else {
                rotateLeft();
            }
        }
        rotations++;
    }

    private void iRotate() {
        if (rotations % 2 == 0) {
            rotateRight();
        } else {
            rotateLeft();
        }
        rotations++;
    }

    public void rotate() {
        switch (currShape) {
            case J_SHAPE:
            case L_SHAPE:
            case T_SHAPE:
                rotateRight();
                break;
            case S_SHAPE:
            case Z_SHAPE:
                zAndSRotate();
                break;
            case I_SHAPE:
                iRotate();
                break;
            case O_SHAPE:
            default:
                break;
        }
        updateCells();
    }

    private void projectionRotate() {
        switch (currShape) {
            case J_SHAPE:
            case L_SHAPE:
            case T_SHAPE:
                rotateRight();
                break;
            case S_SHAPE:
            case Z_SHAPE:
                zAndSRotate();
                break;
            case I_SHAPE:
                iRotate();
                break;
            case O_SHAPE:
            default:
                break;
        }

    }

    public void moveDown() {
        anchorY += CELL_HEIGHT;
        updateCells();
    }


    public void moveLeft() {
        boolean collision;
        collision = shapeCollision(-CELL_WIDTH, 0);
        for (Tetris.Cell c : getCells()) {
            if (c.getX() - 5 < 0) {
                collision = true;
                break;
            }
        }
        if (!collision) {
            anchorX -= CELL_WIDTH;
        }
        updateCells();
    }

    public void moveRight() {
        boolean collision;
        collision = shapeCollision(CELL_WIDTH, 0);
        for (Tetris.Cell c : getCells()) {
            if ((int) (c.getMaxX() + CELL_WIDTH) > Tetris.BOARD_WIDTH) {

                collision = true;
                break;
            }
        }

        if (!collision) {
            anchorX += CELL_WIDTH;
        }
        updateCells();
    }

    public boolean rotationCollision() {
        boolean collisionDetected = false;
        CopyOnWriteArrayList<Integer> shapeCopy = new CopyOnWriteArrayList<>(shape);
        projectionRotate();
        rotations--;

        Tetris.Cell temp1 = new Tetris.Cell(anchorX, anchorY, CELL_WIDTH, CELL_HEIGHT, color);
        Tetris.Cell temp2 = new Tetris.Cell(anchorX + (pairs[shape.get(0)].d1 * CELL_WIDTH), anchorY + (pairs[shape.get(0)].d2 * CELL_WIDTH), CELL_WIDTH, CELL_HEIGHT, color);
        Tetris.Cell temp3 = new Tetris.Cell(anchorX + (pairs[shape.get(1)].d1 * CELL_WIDTH), anchorY + (pairs[shape.get(1)].d2 * CELL_WIDTH), CELL_WIDTH, CELL_HEIGHT, color);
        Tetris.Cell temp4 = new Tetris.Cell(anchorX + (pairs[shape.get(2)].d1 * CELL_WIDTH), anchorY + (pairs[shape.get(2)].d2 * CELL_WIDTH), CELL_WIDTH, CELL_HEIGHT, color);
        ArrayList<Tetris.Cell> tempL = new ArrayList<>();
        Collections.addAll(tempL, temp1, temp2, temp3, temp4);

        for (Tetris.Cell c : tempL) {
            System.out.println("min x: " + c.getMinX());
            if (c.getX() < 0
                    || c.getX() >= Tetris.BOARD_WIDTH
                    || c.getY() >= Tetris.BOARD_HEIGHT) {
                collisionDetected = true;
                break;
            }
        }

        for (Tetris.Cell c : Tetris.cells) {
            for (Tetris.Cell myCell : tempL) {
                if (myCell.intersects(c)) {
                    collisionDetected = true;
                    break;
                }
            }
        }
        shape = shapeCopy;
        System.out.println("Return " + collisionDetected + "from collision detection");
        return collisionDetected;
    }


    public boolean shapeCollision(int x, int y) {
        boolean collisionDetected = false;
        for (Tetris.Cell c : Tetris.cells) {
            for (Tetris.Cell myCell : getCells()) {
                if (new Tetris.Cell((int) (myCell.getX() + x), (int) (myCell.getY() + y), CELL_WIDTH, CELL_HEIGHT, myCell.getColor()).intersects(c)) {
                    collisionDetected = true;
                    break;
                }
            }
        }

        return collisionDetected;
    }

    public boolean isAtBottom() {
        boolean atBottom = false;
        atBottom = shapeCollision(0, CELL_HEIGHT);
        for (Tetris.Cell c : getCells()) {
            if (c.getMaxY() + 5 > Tetris.BOARD_HEIGHT) {
                atBottom = true;
                break;
            }
        }

        return atBottom;
    }

    public ArrayList<Tetris.Cell> getCells() {
        ArrayList<Tetris.Cell> cells = new ArrayList<>();
        cells.add(cell1);
        cells.add(cell2);
        cells.add(cell3);
        cells.add(cell4);
        return cells;
    }

}