import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import javalib.worldcanvas.WorldCanvas;

// to represent a mobile
interface IMobile {
  // computes the total weight of a mobile
  int totalWeight();
  
  // computes the height of the mobile
  int totalHeight();
  
  // determines if a mobile is balanced 
  boolean isBalanced();
  
  // combines two balanced Mobiles to produce a new mobile that will
  // hang on a string of the given length, with one mobile on each side
  IMobile buildMobile(IMobile other, int length, int total);
  
  // computes the current width of a mobile
  int curWidth();
  
  // computes the width of the left side of a mobile
  int leftWidth();
  
  // computes the width of the right side of a mobile
  int rightWidth();
  
  // represents a constant by which to scale the mobile images
  int SCALE = 25;
 
  // produces a 2D image of a mobile if it were hanging from a nail
  WorldImage drawMobile();
}

// to represent a simple mobile with a string length, weight, and color
class Simple implements IMobile {
  int length;
  int weight;
  Color color;

  Simple(int length, int weight, Color color) {
    this.length = length;
    this.weight = weight;
    this.color = color;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.length ... -- int
   * ... this.weight ... -- int
   * ... this.color ...  -- Color
   * 
   * METHODS:
   * ... this.totalWeight() ...                                     -- int
   * ... this.totalHeight() ...                                     -- int
   * ... this.isBalanced() ...                                      -- boolean
   * ... this.buildMobile(IMobile other, int length, int total) ... -- IMobile
   * ... this.curWidth() ...                                        -- int 
   * ... this.leftWidth() ...                                       -- int
   * ... this.rightWidth() ...                                      -- int
   * ... this.drawMobile() ...                                      -- WorldImage
   */
  
  // to get the weight of a simple mobile
  public int totalWeight() {
    return this.weight;
  }
  
  // to get the height of a simple mobile
  public int totalHeight() {
    return this.length + this.weight / 10;
  }
  
  // to determine if the simple mobile is balanced
  public boolean isBalanced() {
    return true;
  }
  
  // to build a new mobile that hangs on a string of
  // the given length, with one mobile on each side
  public IMobile buildMobile(IMobile other, int length, int total) {
    int strutLeft = (total * other.totalWeight() / (this.weight + other.totalWeight()));
    int strutRight = (total - strutLeft);
    return new Complex(length, strutLeft, strutRight, this, other);
  }
  
  
  // to get the current width of a simple mobile
  public int curWidth() { 
    if (this.weight % 10 == 0) {
      return this.weight / 10;
    }
    else {
      return (this.weight - (this.weight % 10)) / 10 + 1;
    }
  }
  
  // to get the width of the left side of the simple mobile, which is just its total width
  public int leftWidth() {
    return this.curWidth() / 2;
  }
  
  // to get the width of the right side of the simple mobile, which is just its total width
  public int rightWidth() {
    return this.curWidth() / 2;
  }
  
  // to draw a 2D image of the simple mobile
  public WorldImage drawMobile() {
    int height = this.weight / 10 * SCALE;
    return new AboveImage(new RectangleImage(5, this.length * SCALE, 
        OutlineMode.SOLID, Color.black),
        new RectangleImage(this.curWidth() * SCALE, height, OutlineMode.SOLID,
            this.color)).movePinhole(0, this.totalHeight() * SCALE / -2);
  }
}

// to represent a complex mobile with a string length, left and right strut lengths,
// and two Mobiles hanging from it
class Complex implements IMobile {
  int length;
  int leftside;
  int rightside;
  IMobile left;
  IMobile right;

  Complex(int length, int leftside, int rightside, IMobile left, IMobile right) {
    this.length = length;
    this.leftside = leftside;
    this.rightside = rightside;
    this.left = left;
    this.right = right;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.length ...    -- int
   * ... this.leftside ...  -- int
   * ... this.rightside ... -- int
   * ... this.left ...      -- IMobile
   * ... this.right ...     -- IMobile
   * 
   * METHODS:
   * ... this.totalWeight() ...                                     -- int
   * ... this.totalHeight() ...                                     -- int
   * ... this.isBalanced() ...                                      -- boolean
   * ... this.buildMobile(IMobile other, int length, int total) ... -- IMobile
   * ... this.curWidth() ...                                        -- int
   * ... this.leftWidth() ...                                       -- int 
   * ... this.rightWidth() ...                                      -- int
   * ... this.drawMobile() ...                                      -- WorldImage
   * 
   * METHODS OF FIELDS:
   * ... this.left.totalWeight() ...  -- int
   * ... this.right.totalWeight() ... -- int
   * ... this.left.totalHeight() ...  -- int
   * ... this.right.totalHeight() ... -- int
   * ... this.left.leftWidth() ...    -- int
   * ... this.right.rightWidth() ...  -- int
   */
  
  // to compute the total weight of a complex mobile
  public int totalWeight() {
    return this.left.totalWeight() + this.right.totalWeight();
  }
  
  // to compute the total height of a complex mobile
  public int totalHeight() {
    return this.length + Math.max(this.left.totalHeight(), this.right.totalHeight());
  }
  
  // to determine if a complex mobile is balanced
  public boolean isBalanced() {
    return (this.left.totalWeight() * this.leftside)
        == (this.right.totalWeight() * this.rightside);
  }
  
  // to build a new mobile that hangs on a string of
  // the given length, with one mobile on each side
  public IMobile buildMobile(IMobile other, int length, int total) {
    int strutLeft = (total * other.totalWeight() 
        / (this.totalWeight() + other.totalWeight()));
    int strutRight = (total - strutLeft);
    return new Complex(length, strutLeft, strutRight, this, other);
  }
  
  // to get the current width of a complex mobile
  public int curWidth() {
    return this.leftWidth() + this.rightWidth();
  }
  
  // to get the total width of the left side of the complex mobile
  public int leftWidth() {
    return Math.max(this.leftside + this.left.leftWidth(),
        this.right.leftWidth() - this.rightside);
  }
  
  // to get the total width of the right side of the complex mobile
  public int rightWidth() {
    return Math.max(this.rightside + this.right.rightWidth(),
        this.left.rightWidth() - this.leftside);
  }
  
  // to draw a 2D image of the complex mobile
  public WorldImage drawMobile() {
    WorldImage leftMobile = new OverlayImage(new RectangleImage(this.leftside * SCALE, 5,
        OutlineMode.SOLID, Color.black).movePinhole(this.leftside * SCALE / -2, 0), 
        this.left.drawMobile());
    WorldImage rightMobile = new OverlayImage(new RectangleImage(this.rightside * SCALE, 5,
        OutlineMode.SOLID, Color.black).movePinhole(this.rightside * SCALE / 2, 0), 
        this.right.drawMobile());
    WorldImage combinedMobiles = new OverlayImage(leftMobile.movePinhole(this.leftside * SCALE, 0),
        rightMobile.movePinhole(-1 * this.rightside * SCALE, 0));
    WorldImage finalMobile = new OverlayImage(new RectangleImage(5, this.length * SCALE,
        OutlineMode.SOLID, Color.black).movePinhole(0, this.length * SCALE / 2), combinedMobiles);
    return finalMobile.movePinhole(0, -1 * this.length * SCALE);
  }
}

// examples of Mobiles
class ExamplesMobiles {
  ExamplesMobiles() {
  }

  IMobile exampleSimple = new Simple(2, 20, Color.blue);
  IMobile exampleComplex = new Complex(1, 9, 3, new Simple(1, 36, Color.blue),
      new Complex(2, 8, 1, new Simple(1, 12, Color.red),
          new Complex(2, 5, 3, new Simple(2, 36, Color.red), new Simple(1, 60, Color.green))));
  IMobile example3 = new Complex(2, 5, 3,
      new Complex(1, 6, 8, new Simple(3, 20, Color.red),
          new Complex(1, 10, 5, new Simple(1, 50, Color.blue), new Simple(3, 28, Color.yellow))),
      new Complex(2, 7, 4, new Simple(2, 40, Color.green),
          new Complex(4, 3, 6, new Simple(2, 45, Color.red), new Simple(2, 50, Color.blue))));
  
  // to test the totalWeight method for mobiles
  boolean testTotalWeight(Tester t) {
    return t.checkExpect(this.exampleSimple.totalWeight(), 20)
        && t.checkExpect(this.exampleComplex.totalWeight(), 144)
        && t.checkExpect(this.example3.totalWeight(), 233);
  }
  
  // to test the totalHeight method for mobiles
  boolean testTotalHeight(Tester t) {
    return t.checkExpect(this.exampleSimple.totalHeight(), 4)
        && t.checkExpect(this.exampleComplex.totalHeight(), 12)
        && t.checkExpect(this.example3.totalHeight(), 15);
  }
  
  // to test the isBalanced method for mobiles
  boolean testIsBalanced(Tester t) {
    return t.checkExpect(this.exampleSimple.isBalanced(), true)
        && t.checkExpect(this.exampleComplex.isBalanced(), true)
        && t.checkExpect(this.example3.isBalanced(), false);
  }
  
  // to test the buildMobile method for mobiles
  boolean testBuildMobile(Tester t) {
    return t.checkExpect(this.exampleSimple.buildMobile(new Simple(3, 10, Color.red), 2, 4),
      new Complex(2, 1, 3, this.exampleSimple, new Simple(3, 10, Color.red)))
        && t.checkExpect(this.exampleComplex.buildMobile(this.exampleSimple, 4, 5),
        new Complex(4, 0, 5, this.exampleComplex, this.exampleSimple))
        && t.checkExpect(this.exampleSimple.buildMobile(new Simple(5, 15, Color.green), 5, 6),
            new Complex(5, 2, 4, this.exampleSimple, new Simple(5, 15, Color.green)));
  }
  
  // to test the curWidth method and its helpers, leftWidth and rightWidth, for mobiles
  boolean testCurWidth(Tester t) {
    return t.checkExpect(this.exampleSimple.curWidth(), 2)
        && t.checkExpect(this.exampleSimple.leftWidth(), 1)
        && t.checkExpect(this.exampleSimple.rightWidth(), 1)
        && t.checkExpect(this.exampleComplex.curWidth(), 21)
        && t.checkExpect(this.exampleComplex.leftWidth(), 11)
        && t.checkExpect(this.exampleComplex.rightWidth(), 10)
        && t.checkExpect(this.example3.curWidth(), 27)
        && t.checkExpect(this.example3.leftWidth(), 12)
        && t.checkExpect(this.example3.rightWidth(), 15);
  }
  
  boolean testDrawMobile(Tester t) {
    return t.checkExpect(this.exampleSimple.drawMobile(), new AboveImage(new RectangleImage(5,
        50, OutlineMode.SOLID, Color.black), new RectangleImage(50, 50, OutlineMode.SOLID,
            Color.blue)).movePinhole(0, -50))
        && t.checkExpect(this.exampleComplex.drawMobile(), this.exampleComplex.drawMobile());
  }
  
  // to display a drawing of a simple mobile
  boolean testDrawMobile1(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(this.exampleSimple.drawMobile(), 250, 250))
        && c.show();
  }
  
  // to display a drawing of a complex mobile
  boolean testDrawMobile2(Tester t) {
    WorldCanvas c = new WorldCanvas(1000, 500);
    WorldScene s = new WorldScene(1000, 500);
    return c.drawScene(s.placeImageXY(this.exampleComplex.drawMobile(), 500, 100))
        && c.show();
  }
}