import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tetris extends JFrame implements KeyListener, ActionListener {

    static final int BOARD_WIDTH = 250;
    static final int BOARD_HEIGHT = 500;
    static final int VERTICAL_CELLS = 22;
    static final int HORIZONTAL_CELLS = 10;
    TetrisPanel panel = new TetrisPanel();
    JPanel panel2 = new JPanel();

    public Tetris()
    {
        Timer timer = new Timer(1000, this);
        panel.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        panel.setBackground(Color.BLACK);
        panel.setShape(new TetrisShape());
        setLayout(null);
        add(panel);
        setBackground(Color.BLACK);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        addKeyListener(this);
        timer.start();
        setVisible(true);
    }




    @Override
    public void keyTyped(KeyEvent e) {



    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_N)
        {
            panel.setShape(new TetrisShape());
        }
        else
        {
            panel.update();
        }


        panel.repaint();
        System.out.println("gjkfjkjkds");
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!panel.getShape().isAtBottom())
            panel.getShape().moveDown();
        panel.repaint();
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
            tShape.rotate();
        }
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            for (Cell c : tShape.getCells())
            {
                System.out.println("yoooo");
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
        /*
        CopyOnWriteArrayList<Integer> shape = new CopyOnWriteArrayList<Integer>(Arrays.asList(1,7,8))
        shape = Tetris.rotate(shape);
        shape.forEach(integer -> {
            System.out.println(integer);
        });
         */

        new Tetris();
    }

}
