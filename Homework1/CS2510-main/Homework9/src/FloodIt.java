import java.util.*;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

/* Enhancements we added:
 * - displaying the number of moves a player makes in one game
 * - displaying the time (# of seconds) a player takes to flood the board
 * - displaying the player's high score (lowest time)
 */

// Represents a single square of the game area
class Cell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;
  
  public Cell(Color color, boolean flooded) {
    this.color = color;
    this.flooded = flooded;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.x ...        -- int
   * ... this.y ...        -- int
   * ... this.color ...    -- Color
   * ... this.flooded ...  -- boolean
   * ... this.left ...     -- Cell
   * ... this.top ...      -- Cell
   * ... this.right ...    -- Cell
   * ... this.bottom ...   -- Cell
   * 
   * METHODS:
   * ... this.floodCell(Color clicked) ...      -- void
   * ... this.floodNeighbors(Color clicked) ... -- void
   * ... this.flood(Color clicked) ...          -- void
   */
  
  // EFFECT: changes this cell to the clicked color and sets it to flooded
  public void floodCell(Color clicked) {
    this.flooded = true;
    this.floodNeighbors(clicked);
  }
  
  // EFFECT: floods the neighbors of this cell if needed
  public void floodNeighbors(Color clicked) {
    if (this.top != null) {
      this.top.flood(clicked);
    }
    if (this.left != null) {
      this.left.flood(clicked);
    }
    if (this.right != null) {
      this.right.flood(clicked);
    }
    if (this.bottom != null) {
      this.bottom.flood(clicked);
    }
  }
  
  // EFFECT: floods this cell if needed
  public void flood(Color clicked) {
    if (this.color.equals(clicked)) {
      this.flooded = true;
    }
  }
}

// represents the setup and functionality of the game
class FloodItWorld extends World {
  // All the cells of the game
  ArrayList<ArrayList<Cell>> board;
  int size;
  int maxGuesses;
  int moves;
  double time;
  double highScore;
  ArrayList<Cell> floodedCells;
  Color clickedColor;
  
  // color bank
  final ArrayList<Color> COLORS = new ArrayList<Color>(Arrays.asList(Color.red, Color.orange,
      Color.yellow, Color.green, Color.blue, Color.pink));
  // each cell is 30 pixels in width and length
  final int CELL_SIZE = 30;
  
  // a random number generator
  Random rand = new Random();
  
  // initial constructor that takes in the size of the board, assumed to be >0
  FloodItWorld(int size) {
    this.size = new Utils().checkRange(size);
    this.board = this.newBoard();
    this.maxGuesses = this.getMaxGuesses();
    this.moves = 0;
    this.time = 0;
    this.highScore = 0.0;
    this.floodedCells = new ArrayList<Cell>(Arrays.asList());
  }
  
  // constructor that takes in a board for testing purposes
  FloodItWorld(int size, ArrayList<ArrayList<Cell>> board) {
    this.size = new Utils().checkRange(size);
    this.board = board;
    this.maxGuesses = this.getMaxGuesses();
    this.moves = 0;
    this.time = 0.0;
    this.highScore = 0.0;
    this.floodedCells = new ArrayList<Cell>(Arrays.asList());
  }
  
  /* TEMPLATE
   * FIELDS: 
   * ... this.board ...      -- ArrayList<ArrayList<Cell>>
   * ... this.size ...       -- int
   * ... this.maxGuesses ... -- int
   * ... this.moves ...      -- int
   * ... this.time ...       -- double
   * ... this.highScore ...  -- double
   * 
   * METHODS:
   * ... this.getMaxGuesses() ...              -- int 
   * ... this.newBoard() ...                   -- ArrayList<ArrayList<Cell>>
   * ... this.randomColor() ...                -- Color
   * ... this.makeScene() ...                  -- WorldScene *
   * ... this.drawAllRows(int row) ...         -- WorldImage
   * ... this.drawRow(int row, int length) ... -- WorldImage
   * ... this.link() ...                       -- void
   * ... this.onTick() ...                     -- void *
   * ... this.onMouseClicked(Posn pos) ...     -- void
   * ... this.getFloodedCells(Color clicked) ...   -- void
   * ... this.onKeyEvent(String key) ...       -- void
   * ... this.endOfGame() ...                  -- boolean
   * ... this.floodAdjacent() ...              -- void * 
   * ... this.updateHighSchore() ...           -- void
   */
  
  // computes the max number of guesses the player gets based on the board size
  public int getMaxGuesses() {
    if (this.size <= 3) {
      return this.size * 2 - 1;
    }
    else if (this.size <= 9) {
      return this.size * 2 - 2;
    }
    else if (this.size <= 17) {
      return this.size * 2 - 3;
    }
    else if (this.size <= 21) {
      return this.size * 2 - 4;
    }
    else if (this.size <= 25) {
      return this.size * 2 - 5;
    }
    else {
      return this.size * 2 - 6;
    }
  }
  
  // creates a new board with the size given by the player and a random color in each cell
  public ArrayList<ArrayList<Cell>> newBoard() {
    ArrayList<ArrayList<Cell>> board = new ArrayList<ArrayList<Cell>>(this.size);
    for (int i = 0; i < this.size; i++) {
      ArrayList<Cell> row = new ArrayList<Cell>(this.size);
      for (int j = 0; j < this.size; j++) {
        row.add(new Cell(this.randomColor(), false));
      }
      board.add(row);
    }
    return board;
  }
  
  // returns a randomly selected color from the color bank
  public Color randomColor() {
    return COLORS.get(rand.nextInt(COLORS.size()));
  }
  
  // configures the game's World
  public WorldScene makeScene() {
    this.link();
    WorldScene bg = new WorldScene(this.size * 50, this.size * 70);
    WorldImage gameBoard = this.drawAllRows(this.size - 1);
    TextImage numMoves = new TextImage("Moves: " + Integer.toString(this.moves) + "/"
        + this.getMaxGuesses(), 15, Color.black);
    TextImage timeElapsed = new TextImage("Time Elapsed: " + (this.time / 10) + "s", 15,
        Color.black);
    TextImage highScore = new TextImage("High Score: "  + (this.highScore / 10) + "s", 15,
        Color.black);
    bg.placeImageXY(gameBoard, this.size * CELL_SIZE / 2, this.size * CELL_SIZE / 2);
    bg.placeImageXY(numMoves, this.size * 25, this.size * 35);
    bg.placeImageXY(timeElapsed, this.size * 25, this.size * 40);
    bg.placeImageXY(highScore, this.size * 25, this.size * 45);
    if (this.endOfGame()) {
      this.updateHighScore();
      if (this.moves <= this.maxGuesses) {
        bg.placeImageXY(new TextImage("You Win!", 15, Color.red), this.size * 25, this.size * 50);
      }
      else {
        bg.placeImageXY(new TextImage("Good Try", 15, Color.red), this.size * 25, this.size * 50);
      }
    }
    return bg;
  }
  
  // draws all the rows of the board
  public WorldImage drawAllRows(int row) {
    if (row < 0) {
      return new EmptyImage();
    }
    else {
      WorldImage currentRow = this.drawRow(row, this.size - 1);
      return new AboveImage(drawAllRows(row - 1), currentRow);
    }
  }
  
  // draws one row of the board
  public WorldImage drawRow(int row, int length) {
    if (length < 0) {
      return new EmptyImage();
    }
    else {
      WorldImage currentCell = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
          this.board.get(row).get(length).color);
      return new BesideImage(drawRow(row, length - 1), currentCell);
    }
  }
  
  // EFFECT: modifies each cell's left/top/right/bottom fields to link the cells of the board
  public void link() {
    for (int i = 0; i < this.size; i++) {
      for (int j = 0; j < this.size; j++) {
        // non-border cells
        if (i > 0 && i < this.size - 1 && j > 0 && j < this.size - 1) {
          this.board.get(i).get(j).left = this.board.get(i).get(j - 1);
          this.board.get(i).get(j).top = this.board.get(i - 1).get(j);
          this.board.get(i).get(j).right = this.board.get(i).get(j + 1);
          this.board.get(i).get(j).bottom = this.board.get(i + 1).get(j);
        }
        
        // top left corner
        else if (i == 0 && j == 0) {
          this.board.get(i).get(j).left = null;
          this.board.get(i).get(j).right = this.board.get(i).get(j + 1);
          this.board.get(i).get(j).top = null;
          this.board.get(i).get(j).bottom = this.board.get(i + 1).get(j);
        }
        
        // top right corner
        else if (i == 0 && j == this.size - 1) {
          this.board.get(i).get(j).left = this.board.get(0).get(j - 1);
          this.board.get(i).get(j).right = null;
          this.board.get(i).get(j).top = null;
          this.board.get(i).get(j).bottom = this.board.get(1).get(j);
        }
        
        // bottom left corner 
        else if (i == this.size - 1 && j == 0) {
          this.board.get(i).get(j).left = null;
          this.board.get(i).get(j).right = this.board.get(i).get(j + 1);
          this.board.get(i).get(j).top = this.board.get(i - 1).get(j);
          this.board.get(i).get(j).bottom = null;
        }
        
        // bottom right corner
        else if (i == this.size - 1 && j == this.size - 1) {
          this.board.get(i).get(j).left = this.board.get(i).get(j - 1);
          this.board.get(i).get(j).right = null;
          this.board.get(i).get(j).top = this.board.get(i - 1).get(j);
          this.board.get(i).get(j).bottom = null;
        }
        
        // bottom row excluding corners 
        else if (i == this.size - 1 && j > 0 && j < this.size - 1) {
          this.board.get(i).get(j).left = this.board.get(i).get(j - 1);
          this.board.get(i).get(j).right = this.board.get(i).get(j + 1);
          this.board.get(i).get(j).top = this.board.get(i - 1).get(j);
          this.board.get(i).get(j).bottom = null;
        }
        
        // top row excluding corners
        else if (i == 0 && j > 0 && j < this.size - 1) {
          this.board.get(i).get(j).left = this.board.get(i).get(j - 1);
          this.board.get(i).get(j).right = this.board.get(i).get(j + 1);
          this.board.get(i).get(j).top = null;
          this.board.get(i).get(j).bottom = this.board.get(i + 1).get(j);
        }
        
        // leftmost row excluding corners 
        else if (i > 0 && i < this.size - 1 && j == 0) {
          this.board.get(i).get(j).left = null;
          this.board.get(i).get(j).right = this.board.get(i).get(j + 1);
          this.board.get(i).get(j).top = this.board.get(i - 1).get(j);
          this.board.get(i).get(j).bottom = this.board.get(i + 1).get(j);
        }
        
        // rightmost row excluding corners 
        else if (i > 0 && i < this.size - 1 && j == this.size - 1) {
          this.board.get(i).get(j).left = this.board.get(i).get(j - 1);
          this.board.get(i).get(j).right = null;
          this.board.get(i).get(j).top = this.board.get(i - 1).get(j);
          this.board.get(i).get(j).bottom = this.board.get(i + 1).get(j);
        }
      }
    }
  }
  
  // returns true if the entire board has been flooded
  public boolean endOfGame() {
    boolean allFlooded = true;
    for (int i = 0; i < this.size; i++) {
      for (int j = 0; j < this.size; j++) {
        if (! this.board.get(i).get(j).flooded) {
          allFlooded = false;
        }
      }
    }
    return allFlooded;
  }
  
  // EFFECT: updates the high score if needed
  public void updateHighScore() {
    if (this.highScore == 0.0) {
      this.highScore = this.time;
    }
    else if (this.highScore != 0.0 && this.time < this.highScore) {
      this.highScore = this.time;
    }
  }
  
  // EFFECT: increments the time elapsed by 0.5 seconds while the game is running,
  // and changes the color of the flooded cells using a waterfall effect
  public void onTick() {
    if (! this.endOfGame()) {
      time++;
    }
    
    if (this.floodedCells.size() > 0) {
      this.floodedCells.get(0).color = this.clickedColor;
      this.floodedCells.remove(0);
    }
  }
  
  // EFFECT: resets the game and creates a new board when 'r' is pressed
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.board = this.newBoard();
      this.moves = 0;
      this.time = 0;
    }
  }
  
  // EFFECT: gets the cell that the player clicked on, and increases the
  // number of moves by 1 every time a cell of a different color is clicked
  public void onMouseClicked(Posn pos) {
    if (pos.x < this.size * CELL_SIZE && pos.y < this.size * CELL_SIZE) {
      Cell clicked = this.board.get(pos.y / CELL_SIZE).get(pos.x / CELL_SIZE);
      if (! clicked.color.equals(this.board.get(0).get(0).color)) {
        this.moves++;
      }
      this.clickedColor = clicked.color;
      this.getFloodedCells(this.clickedColor);
    }
  }
  
  // EFFECT: adds all the flooded cells and cells that will be flooded to the floodedCells list
  public void getFloodedCells(Color clicked) {
    Cell first = this.board.get(0).get(0);
    first.flooded = true;
    first.floodNeighbors(clicked);
    
    for (int i = 0; i < this.size; i++) {
      for (int j = 0; j < this.size; j++) {
        Cell current = this.board.get(i).get(j);
        if (current.flooded) {
          current.floodCell(clicked);
          this.floodedCells.add(current);
        }
      }
    }
  }
}

// validates arguments that the player passes in through constructor
class Utils {
  // checks that the size of the board is valid
  int checkRange(int val) {
    if (val < 2) {
      throw new IllegalArgumentException("Cannot play game with " + val
          + " dimensions.");
    }
    else if (val > 26) {
      throw new IllegalArgumentException("Invalid board size: " + val
          + " will not fit on the screen.");
    }
    else {
      return val;
    }
  }
}

// class for examples of Cells and testing purposes 
class ExamplesCell {
  ExamplesCell() {
  }
  
  Cell red = new Cell(Color.red, false);
  Cell blue = new Cell(Color.blue, false);
  Cell green = new Cell(Color.green, false);
  
  // configures the initial test conditions
  void initTestConditions() {
    this.red = new Cell(Color.red, false);
    this.blue = new Cell(Color.blue, false);
    this.green = new Cell(Color.green, false);
  }
  
  // tests the floodNeighbors method
  void testFloodCell(Tester t) {
    this.initTestConditions();
    t.checkExpect(this.red.flooded, false);
    this.red.floodCell(Color.blue);
    t.checkExpect(this.red.color, Color.blue);
    t.checkExpect(this.red.flooded, true);
    
    t.checkExpect(this.blue.flooded, false);
    this.blue.floodCell(Color.green);
    t.checkExpect(this.blue.color, Color.green);
    t.checkExpect(this.blue.flooded, true);
    
    t.checkExpect(this.green.flooded, false);
    this.green.floodCell(Color.pink);
    t.checkExpect(this.green.color, Color.pink);
    t.checkExpect(this.green.flooded, true);
  }
  
  // tests the floodNeighbors method
  void testFloodNeighbors(Tester t) {
    this.initTestConditions();
    this.red.top = new Cell(Color.red, false);
    t.checkExpect(this.red.top.flooded, false);
    this.red.floodNeighbors(Color.red);
    t.checkExpect(this.red.top.flooded, true);
    
    this.green.left = new Cell(Color.blue, false);
    t.checkExpect(this.green.left.flooded, false);
    this.green.floodNeighbors(Color.green);
    t.checkExpect(this.green.left.flooded, false);
    
    this.blue.bottom = new Cell(Color.blue, false);
    t.checkExpect(this.blue.bottom.flooded, false);
    this.blue.floodNeighbors(Color.blue);
    t.checkExpect(this.blue.bottom.flooded, true);
  }
  
  // tests the flood method
  void testFlood(Tester t) {
    this.initTestConditions();
    this.red.flood(Color.red);
    t.checkExpect(this.red.flooded, true);
    
    this.blue.flood(Color.green);
    t.checkExpect(this.blue.flooded, false);
    
    this.green.flood(Color.green);
    t.checkExpect(this.green.flooded, true);
  }
}

// class for examples of FloodItWorlds and testing purposes
class ExamplesFloodIt {
  ExamplesFloodIt() {
  }
  
  FloodItWorld world1 = new FloodItWorld(3); // 3x3 board where each cell is a random color
  FloodItWorld world2 = new FloodItWorld(10); // 10x10 board where each cell is a random color
  FloodItWorld world3 = new FloodItWorld(2); // 2x2 board where each cell is a random color
  
  /* REPRESENTS THE FOLLOWING BOARD:
   *   RED   RED
   * YELLOW  BLUE
   */
  FloodItWorld testWorld = new FloodItWorld(2, this.testBoard);
  ArrayList<ArrayList<Cell>> testBoard =
      new ArrayList<ArrayList<Cell>>(Arrays.asList(
          new ArrayList<Cell>(Arrays.asList(
              new Cell(Color.red, false), new Cell(Color.red, false))),
          new ArrayList<Cell>(Arrays.asList(
              new Cell(Color.yellow, false), new Cell(Color.blue, false)))));
  
  // configures the initial test conditions
  void initTestConditions() {
    this.world1 = new FloodItWorld(3);
    this.world2 = new FloodItWorld(10);
    this.world3 = new FloodItWorld(2);
    this.testWorld = new FloodItWorld(2, this.testBoard);
    this.testBoard =
        new ArrayList<ArrayList<Cell>>(Arrays.asList(
            new ArrayList<Cell>(Arrays.asList(
                new Cell(Color.red, false), new Cell(Color.blue, false))),
            new ArrayList<Cell>(Arrays.asList(
                new Cell(Color.yellow, false), new Cell(Color.red, false)))));
  }
  
  // play the game with a 3x3 board
  void testBigBang(Tester t) {
    this.initTestConditions();
    int width = this.world2.size * 50;
    int height = this.world2.size * 55;
    double TICK_RATE = 0.1;
    
    this.world2.bigBang(width, height, TICK_RATE);
  }
  
  // tests the getMaxGueses method
  boolean testMaxGuesses(Tester t) {
    return t.checkExpect(new FloodItWorld(2).getMaxGuesses(), 3)
        && t.checkExpect(new FloodItWorld(6).getMaxGuesses(), 10)
        && t.checkExpect(new FloodItWorld(10).getMaxGuesses(), 17)
        && t.checkExpect(new FloodItWorld(14).getMaxGuesses(), 25)
        && t.checkExpect(new FloodItWorld(26).getMaxGuesses(), 46);
  }
  
  // tests the newBoard method
  boolean newBoard(Tester t) {
    Cell topleft = this.world3.board.get(0).get(0);
    Cell topright = this.world3.board.get(0).get(1);
    Cell bottomleft = this.world3.board.get(1).get(0);
    Cell bottomright = this.world3.board.get(1).get(1);
    boolean test1 = t.checkExpect(this.world3.newBoard(),
        new ArrayList<ArrayList<Cell>>(Arrays.asList(
            new ArrayList<Cell>(Arrays.asList(topleft, topright)),
            new ArrayList<Cell>(Arrays.asList(bottomleft, bottomright)))));
    
    topleft = this.world1.board.get(0).get(0);
    Cell topmiddle = this.world1.board.get(0).get(1);
    topright = this.world1.board.get(0).get(2);
    Cell middleleft = this.world1.board.get(1).get(0);
    Cell center = this.world1.board.get(1).get(1);
    Cell middleright = this.world1.board.get(1).get(2);
    bottomleft = this.world1.board.get(2).get(0);
    Cell bottommiddle = this.world1.board.get(2).get(1);
    bottomright = this.world1.board.get(2).get(2);
    boolean test2 = t.checkExpect(this.world1.newBoard(),
        new ArrayList<ArrayList<Cell>>(Arrays.asList(
            new ArrayList<Cell>(Arrays.asList(topleft, topmiddle, topright)),
            new ArrayList<Cell>(Arrays.asList(middleleft, center, middleright)),
            new ArrayList<Cell>(Arrays.asList(bottomleft, bottommiddle, bottomright)))));
    
    return test1 && test2;
  }
  
  // tests the randomColor method
  boolean testRandomColor(Tester t) {
    return t.checkExpect(this.world1.COLORS.contains(this.world1.randomColor()), true)
        && t.checkExpect(this.world2.COLORS.contains(this.world2.randomColor()), true);
  }
  
  // tests the drawRow method
  boolean testDrawRow(Tester t) {
    Color topleft = this.world1.board.get(0).get(0).color;
    Color topmiddle = this.world1.board.get(0).get(1).color;
    Color topright = this.world1.board.get(0).get(2).color;
    Color middleleft = this.world1.board.get(1).get(0).color;
    Color center = this.world1.board.get(1).get(1).color;
    Color middleright = this.world1.board.get(1).get(2).color;
    Color bottomleft = this.world1.board.get(2).get(0).color;
    Color bottommiddle = this.world1.board.get(2).get(1).color;
    Color bottomright = this.world1.board.get(2).get(2).color;
    
    return t.checkExpect(this.world1.drawRow(0, this.world1.size - 1),
        new BesideImage(new BesideImage(new BesideImage(new EmptyImage(), 
            new RectangleImage(30, 30, OutlineMode.SOLID, topleft)),
            new RectangleImage(30, 30, OutlineMode.SOLID, topmiddle)),
            new RectangleImage(30, 30, OutlineMode.SOLID, topright)))
        && t.checkExpect(this.world1.drawRow(1, this.world1.size - 1),
            new BesideImage(new BesideImage(new BesideImage(new EmptyImage(), 
                new RectangleImage(30, 30, OutlineMode.SOLID, middleleft)),
                new RectangleImage(30, 30, OutlineMode.SOLID, center)),
                new RectangleImage(30, 30, OutlineMode.SOLID, middleright)))
        && t.checkExpect(this.world1.drawRow(2, this.world1.size - 1),
            new BesideImage(new BesideImage(new BesideImage(new EmptyImage(), 
                new RectangleImage(30, 30, OutlineMode.SOLID, bottomleft)),
                new RectangleImage(30, 30, OutlineMode.SOLID, bottommiddle)),
                new RectangleImage(30, 30, OutlineMode.SOLID, bottomright)));
  }
  
  // tests the drawAllRows method
  boolean testDrawAllRows(Tester t) {
    Color topleft = this.world1.board.get(0).get(0).color;
    Color topmiddle = this.world1.board.get(0).get(1).color;
    Color topright = this.world1.board.get(0).get(2).color;
    Color middleleft = this.world1.board.get(1).get(0).color;
    Color center = this.world1.board.get(1).get(1).color;
    Color middleright = this.world1.board.get(1).get(2).color;
    Color bottomleft = this.world1.board.get(2).get(0).color;
    Color bottommiddle = this.world1.board.get(2).get(1).color;
    Color bottomright = this.world1.board.get(2).get(2).color;
    
    return t.checkExpect(this.world1.drawAllRows(this.world1.size - 1),
        new AboveImage(new AboveImage(new AboveImage(new EmptyImage(),
            new BesideImage(new BesideImage(new BesideImage(new EmptyImage(),
                new RectangleImage(30, 30, OutlineMode.SOLID, topleft)),
                new RectangleImage(30, 30, OutlineMode.SOLID, topmiddle)),
                new RectangleImage(30, 30, OutlineMode.SOLID, topright))),
            new BesideImage(new BesideImage(new BesideImage(new EmptyImage(),
                new RectangleImage(30, 30, OutlineMode.SOLID, middleleft)),
                new RectangleImage(30, 30, OutlineMode.SOLID, center)),
                new RectangleImage(30, 30, OutlineMode.SOLID, middleright))),
            new BesideImage(new BesideImage(new BesideImage(new EmptyImage(),
                new RectangleImage(30, 30, OutlineMode.SOLID, bottomleft)),
                new RectangleImage(30, 30, OutlineMode.SOLID, bottommiddle)),
                new RectangleImage(30, 30, OutlineMode.SOLID, bottomright))));
  }
  
  // tests the link method
  boolean testLink(Tester t) {
    this.world1.link();
    return
        // center cell
        t.checkExpect(this.world1.board.get(1).get(1).left, this.world1.board.get(1).get(0))
        && t.checkExpect(this.world1.board.get(1).get(1).right, this.world1.board.get(1).get(2))
        && t.checkExpect(this.world1.board.get(1).get(1).top, this.world1.board.get(0).get(1))
        && t.checkExpect(this.world1.board.get(1).get(1).bottom, this.world1.board.get(2).get(1))
        
        // top left corner
        && t.checkExpect(this.world1.board.get(0).get(0).left, null)
        && t.checkExpect(this.world1.board.get(0).get(0).right, this.world1.board.get(0).get(1))
        && t.checkExpect(this.world1.board.get(0).get(0).top, null)
        && t.checkExpect(this.world1.board.get(0).get(0).bottom, this.world1.board.get(1).get(0))
        
        // top right corner
        && t.checkExpect(this.world1.board.get(0).get(2).left, this.world1.board.get(0).get(1))
        && t.checkExpect(this.world1.board.get(0).get(2).top, null)
        && t.checkExpect(this.world1.board.get(0).get(2).right, null)
        && t.checkExpect(this.world1.board.get(0).get(2).bottom, this.world1.board.get(1).get(2))
        
        // bottom left corner
        && t.checkExpect(this.world1.board.get(2).get(0).left, null)
        && t.checkExpect(this.world1.board.get(2).get(0).right, this.world1.board.get(2).get(1))
        && t.checkExpect(this.world1.board.get(2).get(0).top, this.world1.board.get(1).get(0))
        && t.checkExpect(this.world1.board.get(2).get(0).bottom, null)
        
        // bottom right corner
        && t.checkExpect(this.world1.board.get(2).get(2).left, this.world1.board.get(2).get(1))
        && t.checkExpect(this.world1.board.get(2).get(2).top, this.world1.board.get(1).get(2))
        && t.checkExpect(this.world1.board.get(2).get(2).right, null)
        && t.checkExpect(this.world1.board.get(2).get(2).bottom, null)
        
        // top row excluding corners
        && t.checkExpect(this.world1.board.get(0).get(1).left, this.world1.board.get(0).get(0))
        && t.checkExpect(this.world1.board.get(0).get(1).right, this.world1.board.get(0).get(2))
        && t.checkExpect(this.world1.board.get(0).get(1).top, null)
        && t.checkExpect(this.world1.board.get(0).get(1).bottom, this.world1.board.get(1).get(1))
        
        // bottom row excluding corners
        && t.checkExpect(this.world1.board.get(2).get(1).left, this.world1.board.get(2).get(0))
        && t.checkExpect(this.world1.board.get(2).get(1).right, this.world1.board.get(2).get(2))
        && t.checkExpect(this.world1.board.get(2).get(1).top, this.world1.board.get(1).get(1))
        && t.checkExpect(this.world1.board.get(2).get(1).bottom, null)
      
        // leftmost row excluding corners
        && t.checkExpect(this.world1.board.get(1).get(0).left, null)
        && t.checkExpect(this.world1.board.get(1).get(0).right, this.world1.board.get(1).get(1))
        && t.checkExpect(this.world1.board.get(1).get(0).top, this.world1.board.get(0).get(0))
        && t.checkExpect(this.world1.board.get(1).get(0).bottom, this.world1.board.get(2).get(0))
        
        // rightmost row excluding corners
        && t.checkExpect(this.world1.board.get(1).get(2).left, this.world1.board.get(1).get(1))
        && t.checkExpect(this.world1.board.get(1).get(2).right, null)
        && t.checkExpect(this.world1.board.get(1).get(2).top, this.world1.board.get(0).get(2))
        && t.checkExpect(this.world1.board.get(1).get(2).bottom, this.world1.board.get(2).get(2));
  }
  
  // tests the onKeyEvent method
  void testOnKeyEvent(Tester t) {
    this.initTestConditions();
    this.world1.onKeyEvent("r");
    t.checkExpect(this.world1.size, 3);
    t.checkExpect(this.world1.moves, 0);
    t.checkExpect(this.world1.time, 0.0);
    
    this.world2.onKeyEvent("r");
    t.checkExpect(this.world2.size, 10);
    t.checkExpect(this.world2.moves, 0);
    t.checkExpect(this.world2.time, 0.0);
  }
  
  // tests the endOfGame method
  void testEndOfGame(Tester t) {
    this.initTestConditions();
    t.checkExpect(this.world3.endOfGame(), false);
    this.world3.board.get(0).get(0).flooded = true;
    this.world3.board.get(0).get(1).flooded = true;
    this.world3.board.get(1).get(0).flooded = true;
    t.checkExpect(this.world3.endOfGame(), false);
    
    this.world3.board.get(1).get(1).flooded = true;
    t.checkExpect(this.world3.endOfGame(), true);
  }
  
  // tests the updateHighScore method
  void testUpdateHighScore(Tester t) {
    this.initTestConditions();
    t.checkExpect(this.world1.highScore, 0.0);
    this.world1.time = 5.5;
    t.checkExpect(this.world1.time, 5.5);
  }
  
  // test the checkRange method
  boolean testCheckRange(Tester t) {
    return t.checkExpect(new Utils().checkRange(20), 20)
        && t.checkException(new IllegalArgumentException("Invalid board size: 50 will not fit"
            + " on the screen."), new Utils(), "checkRange", 50)
        && t.checkException(new IllegalArgumentException("Cannot play game with 0 dimensions."),
            new Utils(), "checkRange", 0);
  }
}