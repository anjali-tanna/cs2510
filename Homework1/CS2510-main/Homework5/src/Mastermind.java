import tester.*;
import javalib.worldimages.*; 
import javalib.funworld.*;  
import java.awt.Color;          
import java.util.Random;

class OurWorld extends World {
  boolean duplicateEntries;
  boolean hasWon;
  int length;
  int guesses;
  ILoColor colors;
  ILoColor correctSequence;
  ILoColor currentGuess;
  ILoLoColor previousGuesses;
  int worldWidth;
  int worldHeight;
  final int RADIUS = 20;
  final int PIECE_SIZE = 50;
  
  OurWorld(boolean duplicateEntries, int length, int guesses, ILoColor colors) {
    this.duplicateEntries = duplicateEntries;
    this.length = new Utils().checkRange(length, "Invalid length: " + length);
    this.guesses = new Utils().checkRange(guesses, "Invalid guesses: " + guesses);
    this.colors = new Utils().checkLength(colors, this.duplicateEntries, this.length,
        "Invalid length of list of colors: 0");
    this.correctSequence = this.colors.makeCorrectSequence(this.length, this.duplicateEntries);
    this.currentGuess = new MtLoColor();
    this.previousGuesses = new MtLoLoColor();
    this.worldWidth = PIECE_SIZE * (this.length + 2);
    this.worldHeight = PIECE_SIZE * (this.guesses + 2);
    this.hasWon = false;
  }
  
  OurWorld(boolean hasWon, int length, int guesses, ILoColor colors, ILoColor correctSequence, 
      ILoColor currentGuess, ILoLoColor previousGuesses) {
    this.hasWon = hasWon;
    this.length = length;
    this.guesses = guesses;
    this.colors = colors;
    this.correctSequence = correctSequence;
    this.currentGuess = currentGuess;
    this.previousGuesses = previousGuesses;
    this.worldWidth = PIECE_SIZE * (this.length + 2);
    this.worldHeight = PIECE_SIZE * (this.guesses + 2);
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.duplicateEntries ... -- boolean
   * ... this.hasWon ...          -- boolean
   * ... this.length ...          -- int
   * ... this.guesses ...         -- int
   * ... this.colors ...          -- ILoColors
   * ... this.correctSequence ... -- ILoColor
   * ... this.currentGuess ...    -- ILoColor
   * ... this.previousGuesses ... -- ILoLoColor
   * 
   * METHODS:
   * ... this.makeScene() ...                                          -- WorldScene
   * ... this.winOrLose() ...                                          -- WorldImage
   * ... this.drawAllRows(int height) ...                              -- WorldImage
   * ... this.drawRow(int length) ...                                  -- WorldImage
   * ... this.drawPiece(OutlineMode mode, Color c) ...                 -- WorldImage
   * ... this.drawColorBank(int length) ...                            -- WorldImage
   * ... this.drawHiddenSequence() ...                                 -- WorldImage
   * ... this.drawCorrectSequence(int length) ...                      -- WorldImage
   * ... this.drawCurrentGuess(int length) ...                         -- WorldImage
   * ... this.drawPreviousGuesses(int length) ...                      -- WorldImage
   * ... this.drawOnePreviousGuess(int length, ILoColor prevGuess) ... -- WorldImage
   * ... this.drawFeedback(ILoColor curGuess) ...                      -- WorldImage
   * ... this.onKeyEvent(String key) ...                               -- World
   * ... this.enter() ...                                              -- World
   * ... this.delete() ...                                             -- World
   * ... this.number(String key) ...                                   -- World
   */
  
  /////////////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////////// DRAWING THE WORLD ///////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////////////
  
  // draws the current state of the world
  public WorldScene makeScene() {
    WorldScene bg = this.getEmptyScene();
    WorldImage background = new RectangleImage(this.worldWidth, this.worldHeight, OutlineMode.SOLID,
        Color.white);
    WorldImage backgroundOutline = new RectangleImage(this.worldWidth, this.worldHeight,
        OutlineMode.OUTLINE, Color.black);
    WorldImage rows = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM, 
        this.drawAllRows(this.guesses), 0, 0,
           new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, 
               this.drawCurrentGuess(currentGuess.length()), 0, PIECE_SIZE, 
               this.drawPreviousGuesses(previousGuesses.length())));    
    WorldImage gameBoard = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP,
        backgroundOutline, 0, 0, 
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, this.winOrLose(), 0, 0,
            new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM, rows, 0, PIECE_SIZE,
                new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM, 
                    this.drawColorBank(this.colors.length()), 0, 0, background))));
    return bg.placeImageXY(gameBoard, this.worldWidth / 2, this.worldHeight / 2); 
  }
  
  // reveals the correct sequence once the game is over and tells the winner if they won or lost
  public WorldImage winOrLose() {
    if (this.hasWon) { // player guessed correctly
      return new BesideImage(this.drawCorrectSequence(length), 
          new TextImage(" Win!", 20, Color.black));
    }
    else if (this.previousGuesses.length() == this.guesses) { // player is out of guesses
      return new BesideImage(this.drawCorrectSequence(length), 
          new TextImage(" Lose!", 20, Color.black));
    }
    else { // still guessing
      return this.drawHiddenSequence();
    }
  }
  
  
  // draw all the rows of empty pieces
  public WorldImage drawAllRows(int height) {
    WorldImage row = this.drawRow(this.length);
    if (height == 0) {
      return new EmptyImage();
    }
    else {
      return new AboveImage(row, drawAllRows(height - 1));
    }
  }
  
  // draw a row of empty pieces
  public WorldImage drawRow(int length) {
    WorldImage emptyPiece = this.drawPiece(OutlineMode.OUTLINE, Color.black);
    if (length == 0) {
      return new EmptyImage();
    }
    else {
      return new BesideImage(emptyPiece, drawRow(length - 1));
    }
  }
  
  // draws a piece
  public WorldImage drawPiece(OutlineMode mode, Color c) {
    WorldImage circle = new CircleImage(RADIUS, mode, c);
    WorldImage piece = new RectangleImage(PIECE_SIZE, PIECE_SIZE, OutlineMode.OUTLINE, Color.white);
    return new OverlayImage(circle, piece);
  }
  
  // draws the ILoColor with the color bank
  public WorldImage drawColorBank(int length) {
    if (length == 0) {
      return new EmptyImage();
    }
    else {
      WorldImage current = drawPiece(OutlineMode.SOLID, this.colors.reverse().getIndex(length - 1));
      return new BesideImage(current, drawColorBank(length - 1));
    }
  }
  
  // draws the black rectangle hiding the correct sequence at the top of the board
  public WorldImage drawHiddenSequence() {
    return new RectangleImage(this.length * PIECE_SIZE, this.PIECE_SIZE, OutlineMode.SOLID, 
        Color.black);
  }
  
  // draws the correct sequence after the game ends
  public WorldImage drawCorrectSequence(int length) {
    if (length == 0) {
      return new EmptyImage();
    }
    else {
      WorldImage current = drawPiece(OutlineMode.SOLID, 
          this.correctSequence.reverse().getIndex(length - 1));
      return new BesideImage(current, drawCorrectSequence(length - 1));
    }
  }
  
  // draws the current guess
  public WorldImage drawCurrentGuess(int length) {
    if (length == 0) {
      return new EmptyImage();
    }
    else {
      WorldImage current = drawPiece(OutlineMode.SOLID, 
          this.currentGuess.reverse().getIndex(length - 1));
      return new BesideImage(drawCurrentGuess(length - 1), current);
    }
  }
  
  // draws all the previous guesses
  public WorldImage drawPreviousGuesses(int length) {
    if (length == 0) {
      return new EmptyImage();
    }
    else {
      ILoColor curGuess = this.previousGuesses.getIndex(length - 1);
      WorldImage currentRow = this.drawOnePreviousGuess(this.length,
          this.previousGuesses.getIndex(length - 1));
      WorldImage currentFeedback = this.drawFeedback(curGuess);
      WorldImage current = new BesideImage(currentRow, currentFeedback);
      return new AboveImage(drawPreviousGuesses(length - 1), current);
    }
  }
  
  // draws a single previous guess
  public WorldImage drawOnePreviousGuess(int length, ILoColor prevGuess) {
    if (length == 0) {
      return new EmptyImage();
    }
    else {
      WorldImage current = drawPiece(OutlineMode.SOLID, prevGuess.reverse().getIndex(length - 1));
      return new BesideImage(drawOnePreviousGuess(length - 1, prevGuess), current);
    }
  }
  
  // draws the feedback numbers next to a row of guesses
  public WorldImage drawFeedback(ILoColor curGuess) {
    String exact = String.valueOf(curGuess.countExact(this.correctSequence));
    String inexact = String.valueOf(curGuess.countInexact(this.correctSequence));
    return new BesideImage(new TextImage("     " + exact + "     ", 20, Color.black),
        new TextImage(inexact, 20, Color.black));
  }
  
  /////////////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////// KEY HANDLERS //////////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////////////

  // handles on-key events
  public World onKeyEvent(String key) {
    if (key.equals("enter")) {
      return this.enter();
    }
    else if (key.equals("backspace")) {
      return this.delete();
    }
    else if ("123456789".contains(key) && Integer.valueOf(key) <= this.colors.length()
        && this.currentGuess.length() < this.length) {
      return this.number(key);
    }
    else { // another key was pressed, so do nothing
      return this;
    }
  }
  
  // handles functionality for when the "enter" key is pressed
  public World enter() {
    if (this.currentGuess.length() == this.length) { // one row of guesses was fully completed
      if (this.currentGuess.sameList(this.correctSequence.reverse())) { // guess is correct
        return new OurWorld(true, this.length, this.guesses, this.colors, this.correctSequence,
            this.currentGuess, this.previousGuesses);
      }
      else { // guess is wrong
        return new OurWorld(false, this.length, this.guesses, this.colors, this.correctSequence, 
            new MtLoColor(), this.previousGuesses.append(this.currentGuess));
      }
    }
    else { // one row hasn't been completed
      return this;
    }
  }
  
  // handles functionality for when the "delete" key is pressed
  public World delete() {
    return new OurWorld(this.hasWon, this.length, this.guesses, this.colors, this.correctSequence, 
        this.currentGuess.removeFirst(), this.previousGuesses);
  }
  
  // handles functionality for when a number key is pressed
  public World number(String key) {
    Color chosen = this.colors.getIndex(Integer.valueOf(key) - 1);
    ILoColor newCurrentGuess = this.currentGuess.append(chosen);
    return new OurWorld(this.hasWon, this.length, this.guesses, this.colors, this.correctSequence,
        newCurrentGuess, this.previousGuesses);
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////// UTILS /////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////

class Utils {
  int checkRange(int val, String msg) {
    if (val > 0) {
      return val;
    }
    else {
      throw new IllegalArgumentException(msg);
    }
  }
  
  ILoColor checkLength(ILoColor colors, boolean duplicateEntries, int length, String msg) {
    if (!duplicateEntries && length > colors.length()) {
      throw new IllegalArgumentException(msg);
    }
    else {
      return colors;
    }
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////// ILOCOLOR ////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////

// represents a list of colors
interface ILoColor {
  // find the length of an ILoColor
  int length();
  
  // generate the correct sequence that the player has to guess
  ILoColor makeCorrectSequence(int length, boolean duplicateEntries);
  
  // generate the correct sequence where duplicates are allowed
  ILoColor makeDuplicatesSequence(int index, Random c);
  
  // generate the correct sequence where no duplicates are allowed
  ILoColor makeNoDuplicatesSequence(int index, Random c);
  
  // gets the color from the color bank that corresponds with the random number generated
  Color getIndex(int x);
  
  // reverses an ILoColor
  ILoColor reverse();
  
  // helps to reverse an ILoColor
  ILoColor reverseHelper(ILoColor listSoFar);
  
  // adds a Color an ILoColor
  ILoColor append(Color c);
  
  // removes the first Color from an ILoColor
  ILoColor removeFirst();
  
  // is this ILoColor the same as that one?
  boolean sameList(ILoColor other);
  
  // is this MtLoColor the same as that one?
  boolean sameMtLoColor(MtLoColor other);
  
  // is this ConsLoColor the same as that one?
  boolean sameConsLoColor(ConsLoColor other);
  
  // removes the first instance of the given Color from an ILoColor
  ILoColor removeColor(Color c);
  
  // checks if the given Color is in the ILoColor
  boolean contains(Color c);
  
  // counts the number of exact matches 
  int countExact(ILoColor correctSequence);
  
  // checks if the first Colors of two ILoColors are the same
  boolean countExactHelper(Color c);
  
  // finds exact matches in the rest of the correctSequence 
  int countExactRest(ILoColor guessList);
  
  // counts the number of inexact matches
  int countInexact(ILoColor correctSequence);

  // counts the number of total matches, inexact and exact
  public int countInexactHelper(ILoColor guessList);
 
}

// represents an empty list of colors
class MtLoColor implements ILoColor {
 
  /* TEMPLATE
   * FIELDS:
   * 
   * METHODS:
   * ... this.length() ...                             -- int
   * ... this.makeCorrectSequence() ...                -- ILoColor
   * ... this.makeDuplicatesSequence(in index, boolean duplicateEntries) ... -- ILoColor
   * ... this.makeNoDuplicatesSequence(int index) ...  -- ILoColor
   * ... this.getIndex(int index) ...                  -- Color
   * ... this.reverse() ...                            -- ILoColor
   * ... this.reverseHelper() ...                      -- ILoColor
   * ... this.append() ...                             -- ILoColor
   * ... this.removeFirst() ...                        --ILoColor
   * ... this.sameList(ILoColor other) ...             -- boolean
   * ... this.sameMtLoColor(MtLoColor other) ...       -- boolean
   * ... this.sameConsLoColor(ConsLoColor other) ...   -- boolean
   * ... this.removeColor(Color c) ...                 -- ILoColor
   * ... this.contains(Color c) ...                    -- boolean
   * ... this.countExact(ILoColor correctSequence) ... -- int
   * ... this.countExactHelper(Color c) ...            -- boolean
   * ... this.countExactRest(ILoColor guessList) ...   -- int
   * ... this.countInexact(ILoColor correctSequence) ... -- int
   * ... this.countInexactHelper(ILoColor guessList) ... -- int
   * 
   */
  
  // find the length of an ILoColor
  public int length() {
    return 0;
  }
  
  // generate the correct sequence that the player has to guess
  public ILoColor makeCorrectSequence(int index, boolean duplicateEntries) {
    return new MtLoColor();
  }
  
  // generate the correct sequence where duplicates are allowed
  public ILoColor makeDuplicatesSequence(int index, Random c) {
    return new MtLoColor();
  }
  
  // generate the correct sequence where no duplicates are allowed
  public ILoColor makeNoDuplicatesSequence(int index, Random c) {
    return new MtLoColor();
  }
  
  // gets the color from the color bank that corresponds with the random number generated
  public Color getIndex(int index) {
    throw new IllegalArgumentException("Index not valid: " + index);
  }
  
  // reverses an ILoColor
  public ILoColor reverse() {
    return new MtLoColor();
  }
  
  // helps to reverse an ILoColor
  public ILoColor reverseHelper(ILoColor listSoFar) { 
    return listSoFar;
  }
  
  // adds a Color an ILoColor
  public ILoColor append(Color c) {
    return new ConsLoColor(c, new MtLoColor());
  }
  
  // removes the first Color from an ILoColor
  public ILoColor removeFirst() {
    return new MtLoColor();
  }

  // is this ILoColor the same as that one?
  public boolean sameList(ILoColor other) {
    return other.sameMtLoColor(this);
  }
  
  // is this MtLoColor the same as that one?
  public boolean sameMtLoColor(MtLoColor other) {
    return true;
  }
  
  // is this ConsLoColor the same as that one?
  public boolean sameConsLoColor(ConsLoColor other) {
    return false;
  }
  
  // removes the first instance of the given Color from an ILoColor
  public ILoColor removeColor(Color c) {
    return new MtLoColor();
  }
  
  // checks if the given Color is in the ILoColor
  public boolean contains(Color c) {
    return false;
  }
  
  // counts the number of exact matches
  public int countExact(ILoColor correctSequence) {
    return 0;
  }

  // checks if the first Colors of two ILoColors are the same
  public boolean countExactHelper(Color c) {
    return false;
  }
  
  // finds exact matches in the rest of the correctSequence 
  public int countExactRest(ILoColor guessList) {
    return 0;
  }
  
  // counts the number of inexact matches
  public int countInexact(ILoColor correctSequence) {
    return 0;
  }
  
  // counts the number of total matches, inexact and exact
  public int countInexactHelper(ILoColor guessList) {
    return 0;
  }

}

// represents a list of colors
class ConsLoColor implements ILoColor {
  Color first;
  ILoColor rest;
  
  ConsLoColor(Color first, ILoColor rest) {
    this.first = first;
    this.rest = rest;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.first ... -- Color
   * ... this.rest ...  -- ILoColor
   * 
   * METHODS:
   * ... this.length() ...                             -- int
   * ... this.makeCorrectSequence() ...                -- ILoColor
   * ... this.makeDuplicatesSequence(in index, boolean duplicateEntries) ... -- ILoColor
   * ... this.makeNoDuplicatesSequence(int index) ...  -- ILoColor
   * ... this.getIndex(int index) ...                  -- Color
   * ... this.reverse() ...                            -- ILoColor
   * ... this.reverseHelper() ...                      -- ILoColor
   * ... this.append() ...                             -- ILoColor
   * ... this.removeFirst() ...                        --ILoColor
   * ... this.sameList(ILoColor other) ...             -- boolean
   * ... this.sameMtLoColor(MtLoColor other) ...       -- boolean
   * ... this.sameConsLoColor(ConsLoColor other) ...   -- boolean
   * ... this.removeColor(Color c) ...                 -- ILoColor
   * ... this.contains(Color c) ...                    -- boolean
   * ... this.countExact(ILoColor correctSequence) ... -- int
   * ... this.countExactHelper(Color c) ...            -- boolean
   * ... this.countExactRest(ILoColor guessList) ...   -- int
   * ... this.countInexact(ILoColor correctSequence) ... -- int
   * ... this.countInexactHelper(ILoColor guessList) ... -- int
   * 
   * METHODS OF FIELDS:
   * ... this.rest.length() ...                        -- int
   * ... this.rest.getIndex() ...                      -- Color
   * ... this.rest.reverseHelper() ...                 -- ILoColor
   * ... this.rest.sameList() ...                      -- boolean
   * ... this.rest.removeColor() ...                   -- ILoColor
   * ... this.rest.contains() ...                      -- boolean
   * ... this.rest.countExact() ...                    -- int
   * ... this.rest.countInexactHelper() ...            -- int
   */ 
  

  // find the length of an ILoColor
  public int length() {
    return 1 + this.rest.length();
  }
  
  // generate the correct sequence that the player has to guess
  public ILoColor makeCorrectSequence(int index, boolean duplicateEntries) {
    Random c = new Random();
    if (duplicateEntries) {
      return this.makeDuplicatesSequence(index, c);
    }
    else {
      return this.makeNoDuplicatesSequence(index, c);
    }
  }
  
  // generate the correct sequence where duplicates are allowed
  public ILoColor makeDuplicatesSequence(int index, Random c) {
    if (index > 0) {
      return new ConsLoColor(this.getIndex(c.nextInt(this.length())),
          makeDuplicatesSequence(index - 1, c));
    }
    else {
      return new MtLoColor();
    }
  }
  
  // generate the correct sequence where no duplicates are 
  public ILoColor makeNoDuplicatesSequence(int index, Random c) {
    if (index > 0) {
      Color chosen = this.getIndex(c.nextInt(this.length()));
      return new ConsLoColor(chosen, 
          this.removeColor(chosen).makeNoDuplicatesSequence(index - 1, c));
    }
    else {
      return new MtLoColor();
    }
  }
  
  // gets the color from the color bank that corresponds with the random number generated
  public Color getIndex(int index) {
    if (index == 0) {
      return this.first;
    }
    else {
      return this.rest.getIndex(index - 1);
    }
  }
  
  // produce a new list of Strings with the list in reverse order
  public ILoColor reverse() {
    return this.reverseHelper(new MtLoColor());
  }
  
  // produce a new list of Strings with the list in reverse order
  public ILoColor reverseHelper(ILoColor listSoFar) {
    return this.rest.reverseHelper(new ConsLoColor(this.first, listSoFar));
  }
  
  // adds a Color an ILoColor
  public ILoColor append(Color c) {
    return new ConsLoColor(c, this);
  }
  
  // removes the first Color from an ILoColor
  public ILoColor removeFirst() {
    return this.rest;
  }
  
  // is this ILoColor the same as that one?
  public boolean sameList(ILoColor other) {
    return other.sameConsLoColor(this);
  }
  
  // is this MtLoColor the same as that one?
  public boolean sameMtLoColor(MtLoColor other) {
    return false;
  }
  
  // is this ConsLoColor the same as that one?
  public boolean sameConsLoColor(ConsLoColor other) {
    return this.first.equals(other.first) && this.rest.sameList(other.rest);
  }

  // removes the first instance of the given Color from an ILoColor
  public ILoColor removeColor(Color c) {
    if (this.first.equals(c)) {
      return this.rest;
    }
    else {
      return new ConsLoColor(this.first, this.rest.removeColor(c));
    }
  }
  
  // checks if the given Color is in the ILoColor
  public boolean contains(Color c) {
    return this.first.equals(c) || this.rest.contains(c);
  }
  
  // counts the number of exact matches
  public int countExact(ILoColor correctSequence) {
    if (correctSequence.countExactHelper(this.first)) {
      return 1 + this.rest.countExact(correctSequence.removeColor(this.first));
    }
    else {
      return correctSequence.countExactRest(this.rest);
    }
  }
  
  // checks if the first Colors of two ILoColors are the same
  public boolean countExactHelper(Color c) {
    return this.first.equals(c);
  }
  
  // finds exact matches in the rest of the correctSequence 
  public int countExactRest(ILoColor guessList) {
    return this.rest.countExact(guessList);
  }
  
  // counts the number of inexact matches
  public int countInexact(ILoColor correctSequence) {
    int exact = this.countExact(correctSequence);
    int inexact = this.countInexactHelper(correctSequence);
    return inexact - exact;
  }
  
  // counts the number of total matches, inexact and exact
  public int countInexactHelper(ILoColor correctSequence) {
    if (correctSequence.contains(this.first)) {
      return 1 + this.rest.countInexactHelper(correctSequence.removeColor(this.first));
    }
    else {
      return this.rest.countInexactHelper(correctSequence);
    }
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////// ILOLOCOLOR //////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////

interface ILoLoColor {
  // adds a new ILoColor to the ILoLoColor
  ILoLoColor append(ILoColor loc);
  
  // returns the length of an ILoLoColor
  int length();
  
  // returns the ILoColor at the given index
  ILoColor getIndex(int index);
}

class MtLoLoColor implements ILoLoColor {
  
  /* TEMPLATE
   * FIELDS:
   * 
   * METHODS:
   * ... this.append(ILoColor loc) ...        -- ILoLoColor
   * ... this.length() ...                    -- int
   * ... this.getIndex(int index) ...         -- ILoColor
   * 
   */

   
  // adds a new ILoColor to the ILoLoColor
  public ILoLoColor append(ILoColor loc) {
    return new ConsLoLoColor(loc, new MtLoLoColor());
  }
  
  // returns the length of an ILoLoColor
  public int length() {
    return 0;
  }
  
  // returns the ILoColor at the given index
  public ILoColor getIndex(int index) {
    throw new IllegalArgumentException("Index not valid: " + index);
  }
}

class ConsLoLoColor implements ILoLoColor {
  ILoColor first;
  ILoLoColor rest;
  
  ConsLoLoColor(ILoColor first, ILoLoColor rest) {
    this.first = first;
    this.rest = rest;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.first ...                     -- ILoColor
   * ... this.rest ...                      -- ILoLoColor
   * 
   * METHODS:
   * ... this.append(ILoColor loc) ...      -- ILoLoColor
   * ... this.length() ...                  -- int
   * ... this.getIndex(int index) ...       -- ILoColor
   * 
   * METHODS OF FIELDS:
   * ... this.rest.length() ...             -- int
   * ... this.rest.getIndex(int index) ...  -- ILoColor
   * 
   */
  
  // adds a new ILoColor to the ILoLoColor
  public ILoLoColor append(ILoColor loc) {
    return new ConsLoLoColor(loc, this);
  }
  
  // returns the length of an ILoLoColor
  public int length() {
    return 1 + this.rest.length();
  }
  
  // returns the ILoColor at the given index
  public ILoColor getIndex(int index) {
    if (index == 0) {
      return this.first;
    }
    else {
      return this.rest.getIndex(index - 1);
    }
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////// TESTS /////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////

class ExamplesWorld {
  ExamplesWorld() {
  }
  
  ILoColor colorBank = new ConsLoColor(Color.red, new ConsLoColor(Color.blue,
      new ConsLoColor(Color.green, new ConsLoColor(Color.pink, new ConsLoColor(Color.yellow, 
          new MtLoColor())))));
  ILoColor red = new ConsLoColor(Color.red, new MtLoColor());
  OurWorld world1 = new OurWorld(true, 2, 5, new ConsLoColor(Color.red, new ConsLoColor(Color.blue,
      new ConsLoColor(Color.green, new MtLoColor()))));
  OurWorld world2 = new OurWorld(false, 1, 1, this.red);
  OurWorld world3 = new OurWorld(false, 1, 1, this.red, this.red, this.red,
      new ConsLoLoColor(this.red, new MtLoLoColor()));
  OurWorld world4 = new OurWorld(false, 4, 8, new ConsLoColor(Color.red, new ConsLoColor(Color.blue, 
      new ConsLoColor(Color.pink, new ConsLoColor(Color.green, new ConsLoColor(Color.yellow, new ConsLoColor(Color.orange,
          new ConsLoColor(Color.black, new ConsLoColor(Color.gray, new MtLoColor())))))))));
  
  
  // play the game
  boolean testBigBang(Tester t) {
    return this.world1.bigBang(200, 350, 0.5);
  }
  
  
  // test the makeScene method
  boolean testMakeScene(Tester t) {
    return t.checkExpect(this.world2.makeScene(), this.world2.makeScene());
  }
  
  // test the winOrLose method
  boolean testWinOrLose(Tester t) {
    return t.checkExpect(this.world3.winOrLose(),
        new BesideImage(new BesideImage(new OverlayImage(new CircleImage(20, OutlineMode.SOLID,
            Color.red),
            new RectangleImage(50, 50, OutlineMode.OUTLINE, Color.white)),
            new EmptyImage()),
            new TextImage(" Lose!", 20, Color.black)));
  }
  
  // test the checkRange method
  boolean testCheckRange(Tester t) {
    return t.checkExpect(new Utils().checkRange(4, "Index is valid"), 4)
        && t.checkException(new IllegalArgumentException("Invalid length: 0"),
            new Utils(), "checkRange", 0, "Invalid length: 0");
  }
  
  // test the drawAllRows method
  boolean testDrawAllRows(Tester t) {
    return t.checkExpect(this.world2.drawAllRows(1),
        new AboveImage(new BesideImage(new OverlayImage(new CircleImage(20, OutlineMode.OUTLINE,
            Color.black),
            new RectangleImage(50, 50, OutlineMode.OUTLINE, Color.white)),
            new EmptyImage()), new EmptyImage()));
  }
  
  // test the drawRow method
  boolean testDrawRow(Tester t) {
    return t.checkExpect(this.world2.drawRow(0), new EmptyImage())
        && t.checkExpect(this.world3.drawRow(1),
            new BesideImage(new OverlayImage(new CircleImage(20, OutlineMode.OUTLINE, Color.black),
                new RectangleImage(50, 50, OutlineMode.OUTLINE, Color.white)),
                new EmptyImage()));
  }
  
  // test the drawPiece method
  boolean testDrawPiece(Tester t) {
    return t.checkExpect(this.world2.drawPiece(OutlineMode.SOLID, Color.red),
        new OverlayImage(new CircleImage(20, OutlineMode.SOLID, Color.red),
            new RectangleImage(50, 50, OutlineMode.OUTLINE, Color.white)));
  }
  
  // test the drawColorBank method
  boolean testDrawColorBank(Tester t) {
    return t.checkExpect(this.world2.drawColorBank(1),
        new BesideImage(new OverlayImage(new CircleImage(20, OutlineMode.SOLID, Color.red),
            new RectangleImage(50, 50, OutlineMode.OUTLINE, Color.white)), new EmptyImage()));
  }
  
  // test the drawHiddenSequence method
  boolean testDrawHiddenSequence(Tester t) {
    return t.checkExpect(this.world2.drawHiddenSequence(),
        new RectangleImage(50, 50, OutlineMode.SOLID, Color.black));
  }
  
  // test the drawCorrectSequence method
  boolean testDrawCorrectSequence(Tester t) {
    return t.checkExpect(this.world2.drawCorrectSequence(1),
        new BesideImage(new OverlayImage(new CircleImage(20, OutlineMode.SOLID, Color.red),
            new RectangleImage(50, 50, OutlineMode.OUTLINE, Color.white)),
            new EmptyImage()));
  }
  
  // test the drawCurrentGuess method
  boolean testDrawCurrentGuess(Tester t) {
    return t.checkExpect(this.world3.drawCurrentGuess(1), new BesideImage(new EmptyImage(),
        new OverlayImage(new CircleImage(20, OutlineMode.SOLID, Color.red),
            new RectangleImage(50, 50, OutlineMode.OUTLINE, Color.white))));
  }
  
  // test the drawPreviousGuesses method
  boolean testDrawPreviousGuesses(Tester t) {
    return t.checkExpect(this.world2.drawPreviousGuesses(0), new EmptyImage());
  }
  
  // test the drawOnePreviousGuess method
  boolean testDrawOnePreviousGuess(Tester t) {
    return t.checkExpect(this.world3.drawOnePreviousGuess(1, this.red),
        new BesideImage(new EmptyImage(),
            new OverlayImage(new CircleImage(20, OutlineMode.SOLID,Color.red),
            new RectangleImage(50, 50, OutlineMode.OUTLINE, Color.white))));
  }
  
  // test the drawFeedback method
  boolean testDrawFeedback(Tester t) {
    return t.checkExpect(this.world3.drawFeedback(this.red),
        new BesideImage(new TextImage("     1     ", 20, Color.black),
            new TextImage("0", 20, Color.black)));
  }
  
  // test the onKeyEvent method
  boolean testOnKeyEvent(Tester t) {
    return t.checkExpect(this.world2.onKeyEvent("enter"), this.world2.enter())
        && t.checkExpect(this.world2.onKeyEvent("delete"), this.world2.delete())
        && t.checkExpect(this.world2.onKeyEvent("1"), this.world2.number("1"))
        && t.checkExpect(this.world2.onKeyEvent("3"), this.world2);
  }
}

class ExamplesILoColor {
  ExamplesILoColor() {
  }
  
  ILoColor emptyList = new MtLoColor();
  ILoColor listOne = new ConsLoColor(Color.red, new ConsLoColor(Color.blue, 
      new ConsLoColor(Color.yellow, new MtLoColor())));
  ILoColor listTwo = new ConsLoColor(Color.blue, new ConsLoColor(Color.green, 
      new ConsLoColor(Color.pink, new MtLoColor())));
  ILoColor listThree = new ConsLoColor(Color.blue, new ConsLoColor(Color.red, 
      new ConsLoColor(Color.yellow, new ConsLoColor(Color.green, new MtLoColor()))));
  Color red = Color.red; 
  
  // tests the length method
  boolean testLength(Tester t) {
    return t.checkExpect(this.emptyList.length(),0)
        && t.checkExpect(this.listOne.length(), 3)
        && t.checkExpect(this.listTwo.length(), 3)
        && t.checkExpect(this.listThree.length(), 4);
  }
  
  
  // tests the getIndex method
  boolean testGetIndex(Tester t) {
    return t.checkExpect(this.listOne.getIndex(2), Color.yellow)
        && t.checkExpect(listTwo.getIndex(0), Color.blue)
        && t.checkException(new IllegalArgumentException("Index not valid: -1"), new MtLoColor(), 
            "getIndex", -1);
  }

  // tests the reverse method
  boolean testReverse(Tester t) {
    return t.checkExpect(this.emptyList.reverse(), new MtLoColor())
        && t.checkExpect(this.listOne.reverse(), new ConsLoColor(Color.yellow, 
            new ConsLoColor(Color.blue, new ConsLoColor(Color.red, new MtLoColor()))))
        && t.checkExpect(this.listTwo.reverse(), new ConsLoColor(Color.pink, 
            new ConsLoColor(Color.green, new ConsLoColor(Color.blue, new MtLoColor()))));
  }
  
  // tests the reverseHelper method
  boolean testReverseHelper(Tester t) {
    return t.checkExpect(this.emptyList.reverseHelper(this.emptyList), new MtLoColor())
        && t.checkExpect(this.listOne.reverseHelper(this.emptyList), new ConsLoColor(Color.yellow, 
            new ConsLoColor(Color.blue, new ConsLoColor(Color.red, this.emptyList))))
        && t.checkExpect(this.listTwo.reverseHelper(this.emptyList), new ConsLoColor(Color.pink, 
            new ConsLoColor(Color.green, new ConsLoColor(Color.blue, this.emptyList))));
  }
  
  // tests the append method
  boolean testAppend(Tester t) {
    return t.checkExpect(this.emptyList.append(red), new ConsLoColor(Color.red, new MtLoColor()))
        && t.checkExpect(this.listOne.append(red), new ConsLoColor(Color.red, 
            new ConsLoColor(Color.red, new ConsLoColor(Color.blue, 
                new ConsLoColor(Color.yellow, this.emptyList)))))
        && t.checkExpect(this.listTwo.append(red), new ConsLoColor(Color.red, 
            new ConsLoColor(Color.blue, new ConsLoColor(Color.green, 
                new ConsLoColor(Color.pink, this.emptyList)))));
  }
  
  // tests the removeFirst method
  boolean testRemoveFirst(Tester t) {
    return t.checkExpect(this.emptyList.removeFirst(), new MtLoColor())
        && t.checkExpect(this.listOne.removeFirst(), new ConsLoColor(Color.blue, 
            new ConsLoColor(Color.yellow, new MtLoColor())))
        && t.checkExpect(this.listTwo.removeFirst(), new ConsLoColor(Color.green, 
            new ConsLoColor(Color.pink, new MtLoColor())));
  }
  
  // tests the sameList method
  boolean testSameList(Tester t) {
    return t.checkExpect(this.emptyList.sameList(this.emptyList), true)
        && t.checkExpect(this.listOne.sameList(this.listTwo), false)
        && t.checkExpect(this.listTwo.sameList(this.listTwo), true);
  }
  
  // tests the sameMtLoColor method
  boolean testSameMtLoColor(Tester t) {
    return t.checkExpect(this.emptyList.sameMtLoColor(new MtLoColor()), true)
        && t.checkExpect(this.listOne.sameMtLoColor(new MtLoColor()), false)
        && t.checkExpect(this.listTwo.sameMtLoColor(new MtLoColor()), false);
  }
 
  // tests the sameConsLoColor method
  boolean testSameConsLoColor(Tester t) {
    return t.checkExpect(this.emptyList.sameConsLoColor(new ConsLoColor(Color.red,
        new ConsLoColor(Color.blue, new MtLoColor()))), false)
        && t.checkExpect(this.listOne.sameConsLoColor(new ConsLoColor(Color.red,
            new ConsLoColor(Color.blue, new MtLoColor()))), false)
        && t.checkExpect(this.listTwo.sameConsLoColor(new ConsLoColor(Color.blue,
            new ConsLoColor(Color.green, new ConsLoColor(Color.pink, 
                new MtLoColor())))), true);
  }

  // tests the removeColor method
  boolean testRemoveColor(Tester t) {
    return t.checkExpect(this.emptyList.removeColor(red), new MtLoColor())
        && t.checkExpect(this.listOne.removeColor(red), new ConsLoColor(Color.blue, 
            new ConsLoColor(Color.yellow, new MtLoColor())))
        && t.checkExpect(this.listTwo.removeColor(red), new ConsLoColor(Color.blue, 
            new ConsLoColor(Color.green, new ConsLoColor(Color.pink, new MtLoColor()))));
  }
  
  // tests the contains method
  boolean testContains(Tester t) {
    return t.checkExpect(this.emptyList.contains(red), false)
        && t.checkExpect(this.listOne.contains(red), true)
        && t.checkExpect(this.listTwo.contains(red), false);
  }
  
  // tests the countExact method
  boolean testCountExact(Tester t) {
    return t.checkExpect(this.emptyList.countExact(this.emptyList), 0)
        && t.checkExpect(this.listOne.countExact(this.listThree), 1)
        && t.checkExpect(this.listTwo.countExact(this.listTwo), 3);
  }
  
  // tests the countExactHelper method
  boolean testCountExactHelper(Tester t) {
    return t.checkExpect(this.emptyList.countExactHelper(red), false)
        && t.checkExpect(this.listOne.countExactHelper(red), true)
        && t.checkExpect(this.listTwo.countExactHelper(red), false);
  }
  
  // tests the countExactRest method
  boolean testCountExactRest(Tester t) {
    return t.checkExpect(this.emptyList.countExactRest(this.emptyList), 0)
        && t.checkExpect(this.listOne.countExactRest(this.listTwo), 1)
        && t.checkExpect(this.listTwo.countExactRest(this.listTwo), 0);
  }
  
  // tests the countInexact method
  boolean testCountInexact(Tester t) {
    return t.checkExpect(this.emptyList.countInexact(this.emptyList), 0)
        && t.checkExpect(this.listOne.countInexact(this.listTwo), 1)
        && t.checkExpect(this.listTwo.countInexact(this.listThree), 1);
  }
  
  // tests the countInexactHelper method             
  boolean testCountInexactHelper(Tester t) {
    return t.checkExpect(this.emptyList.countInexactHelper(this.emptyList), 0)
        && t.checkExpect(this.listOne.countInexactHelper(this.listTwo), 1)
        && t.checkExpect(this.listTwo.countInexactHelper(this.listThree), 2);
  }
}

class ExamplesILoLoColor {
  ExamplesILoLoColor() {
  }
  
  ILoColor list1 = new ConsLoColor(Color.red, new ConsLoColor(Color.blue, 
      new ConsLoColor(Color.green, new MtLoColor())));
  ILoColor list2 = new ConsLoColor(Color.orange, new ConsLoColor(Color.pink, 
      new ConsLoColor(Color.yellow, new MtLoColor())));
  ILoColor list3 = new ConsLoColor(Color.green, new ConsLoColor(Color.pink, 
      new ConsLoColor(Color.blue, new MtLoColor())));
  ILoLoColor emptyList = new MtLoLoColor();
  ILoLoColor loloc1 = new ConsLoLoColor(this.list1, new ConsLoLoColor(this.list2, 
      new MtLoLoColor()));
  ILoLoColor loloc2 = new ConsLoLoColor(this.list1, new ConsLoLoColor(this.list3, 
      new MtLoLoColor()));
      
  // tests for append method
  boolean testAppend(Tester t) {
    return t.checkExpect(this.loloc1.append(this.list3), new ConsLoLoColor(this.list3,
        new ConsLoLoColor(this.list1, new ConsLoLoColor(this.list2, new MtLoLoColor()))))
        && t.checkExpect(this.loloc2.append(this.list2), new ConsLoLoColor(this.list2,
            new ConsLoLoColor(this.list1, new ConsLoLoColor(this.list3, new MtLoLoColor()))))
        && t.checkExpect(this.emptyList.append(this.list1), new ConsLoLoColor(this.list1,
            new MtLoLoColor()));
  }
  
  // tests for length method
  boolean testLength(Tester t) {
    return t.checkExpect(this.loloc1.length(), 2)
        && t.checkExpect(this.loloc2.length(), 2)
        && t.checkExpect(this.emptyList.length(), 0);
  }
  
  // tests for the getIndex method
  boolean testGetIndex(Tester t) {
    return t.checkExpect(this.loloc2.getIndex(1), this.list3)
        && t.checkExpect(this.loloc1.getIndex(0), this.list1)
        && t.checkException(new IllegalArgumentException("Index not valid: -1"), new MtLoLoColor(),
            "getIndex", -1);
  }
}