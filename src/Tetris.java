import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tetris extends JFrame implements KeyListener, ActionListener {

    public static final CopyOnWriteArrayList<Cell> cells = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<Cell> toDelete = new CopyOnWriteArrayList<>();
    private TetrisShape nextShape = new TetrisShape(true);
    static final int BOARD_WIDTH = 350;
    static final int BOARD_HEIGHT = 700;
    static final int VERTICAL_CELLS = 22;
    static final int HORIZONTAL_CELLS = 10;
    TetrisPanel panel = new TetrisPanel();
    NextShapePanel panel2 = new NextShapePanel();
    Timer timer;

    public Tetris()
    {
        timer = new Timer(400, this);
        panel.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        panel.setBackground(Color.BLACK);
        panel.setShape(new TetrisShape(false));
        panel2.setBounds(375, 100, 150, 150);
        panel2.setBackground(Color.BLACK);
        setLayout(null);
        add(panel);
        add(panel2);
        setTitle("Tetris");
        setBackground(Color.BLACK);
        setSize(BOARD_WIDTH+200, BOARD_HEIGHT+100);
        addKeyListener(this);
        setResizable(false);
        timer.start();
        setVisible(true);
    }



    class NextShapePanel extends JPanel
    {

        public NextShapePanel()
        {

        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            for (Cell c : nextShape.getCells())
            {
                g.setColor(Color.WHITE);
                g.drawRect((int) c.getX(), (int) c.getY(), (int) c.getWidth(), (int) c.getHeight());
                g.setColor(c.getColor());
                g.fillRect((int) c.getX(), (int) c.getY(), (int) c.getWidth(), (int) c.getHeight());

            }
            g.setColor(Color.RED);
            g.drawString("NEXT SHAPE", 65, 140);
        }


    }


    @Override
    public void keyTyped(KeyEvent e) {



    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_N)
        {
            panel.setShape(new TetrisShape(false));
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            System.out.println("moving left");
            panel.getShape().moveLeft();
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            panel.getShape().moveRight();
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            if (!panel.getShape().rotationCollision())
                panel.getShape().rotate();
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            if (!panel.getShape().isAtBottom())
                panel.getShape().moveDown();
        }

        else
        {
            panel.update();
        }


        panel.repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!panel.getShape().isAtBottom()) {
            panel.getShape().moveDown();
        }
        else {
            cells.addAll(panel.getShape().getCells());
            nextShape.setX(BOARD_WIDTH/2);
            nextShape.setY(-TetrisShape.CELL_HEIGHT);
            panel.setShape(nextShape);
            nextShape = new TetrisShape(true);
        }
        checkAndProcessCompleteRows();
        gameLost();
        panel.repaint();
        panel2.repaint();
    }


    private void gameLost()
    {
        for (Cell cell : cells)
        {
            if (cell.getY() < 0.0)
            {
                JOptionPane.showMessageDialog(null, "you lose");
                timer.stop();
                break;
            }
        }
    }
    private void checkAndProcessCompleteRows()
    {
        HashMap<Double, CopyOnWriteArrayList<Cell>> map = new HashMap<>();

            for (Cell cell : cells)
            {
                if (map.get(cell.getY()) == null)
                {
                    CopyOnWriteArrayList<Cell> newL = new CopyOnWriteArrayList<>();
                    newL.add(cell);
                    map.put(cell.getY(), newL);
                }
                else
                {
                    map.get(cell.getY()).add(cell);
                }
            }
        double highestY = 0.0;
        int rowsDeleted = 0;

        for (Map.Entry<Double, CopyOnWriteArrayList<Cell>> entry : map.entrySet())
        {
            if (entry.getValue().size() == HORIZONTAL_CELLS)
            {
                toDelete.addAll(entry.getValue());
                highestY = entry.getKey();
                cells.removeAll(toDelete);
                toDelete.clear();
                int index = 0;
                for (Cell cell : cells)
                {
                    if (cell.getY() < highestY)
                    {
                        Cell tmp = new Cell((int) cell.getX(), (int) cell.getY() + (TetrisShape.CELL_HEIGHT),
                                TetrisShape.CELL_WIDTH, TetrisShape.CELL_HEIGHT, cell.getColor());
                        cells.set(index, tmp);
                    }
                    index++;
                }
            }
        }



    }


    static class Shapes
    {
        CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>> shapes = new CopyOnWriteArrayList<>();
        public Shapes()
        {
            makeShapes();
        }

        private void makeShapes()
        {
            shapes.add(new CopyOnWriteArrayList<>(Arrays.asList(0,1,3))); //z shape
            shapes.add(new CopyOnWriteArrayList<>(Arrays.asList(7,1,2))); //s shape
            shapes.add(new CopyOnWriteArrayList<>(Arrays.asList(3,5,7))); //t shape
            shapes.add(new CopyOnWriteArrayList<>(Arrays.asList(3,7,6))); //L shape
            shapes.add(new CopyOnWriteArrayList<>(Arrays.asList(7,3,4))); //J shape
            shapes.add(new CopyOnWriteArrayList<>(Arrays.asList(0,1,7))); //O shape
            shapes.add(new CopyOnWriteArrayList<>(Arrays.asList(7,3,8))); //I shape
        }

        public CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>> getShapes(){
            return shapes;
        }
    }



    static class Pair
    {
        public int d1;
        public int d2;

        public Pair(int d1, int d2)
        {
            this.d1 = d1;
            this.d2 = d2;
        }
    }
    static class Cell extends Rectangle
    {
        Color c;
        public Cell(int x, int y, int width, int height, Color c)
        {
            super(x, y, width, height);
            this.c = c;
        }

        public Color getColor()
        {
            return c;
        }

    }

    static class TetrisPanel extends JPanel
    {
        final int CELL_WIDTH = BOARD_WIDTH/HORIZONTAL_CELLS;
        final int CELL_HEIGHT = BOARD_HEIGHT/VERTICAL_CELLS-2;
        TetrisShape tShape = null;
        public TetrisPanel()
        {
            super();
        }



        void update()
        {

        }
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            for (Cell c : tShape.getCells())
            {
                g.setColor(Color.WHITE);
                g.drawRect((int) c.getX(), (int) c.getY(), (int) c.getWidth(), (int) c.getHeight());
                g.setColor(c.getColor());
                g.fillRect((int) c.getX(), (int) c.getY(), (int) c.getWidth(), (int) c.getHeight());
            }

            for (Cell c : cells) {
                g.setColor(Color.WHITE);
                g.drawRect((int) c.getX(), (int) c.getY(), (int) c.getWidth(), (int) c.getHeight());
                g.setColor(c.getColor());
                g.fillRect((int) c.getX(), (int) c.getY(), (int) c.getWidth(), (int) c.getHeight());
            }



        }

        public void setShape(TetrisShape shape)
        {
            tShape = shape;
        }

        public TetrisShape getShape()
        {
            return tShape;
        }



    }


    public static void main(String[] args)
    {
        new Tetris();
    }

}
