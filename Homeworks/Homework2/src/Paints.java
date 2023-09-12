import tester.*;
import java.awt.Color;

/*
 *                +--------+
 *                | IPaint |
 *                +--------+
 *                     |
 *        ---------------------------
 *        |                         |
 * +-------------+        +--------------------+
 * | Solid       |        | Combo              |
 * +-------------+        +--------------------+
 * | String name |        | String name        |
 * | Color color |        | IMixture operation |--+
 * +-------------+        +--------------------+  |
 *                                                |
 *                                          +----------+
 *                                          | IMixture |
 *                                          +----------+
 *                                          +----------+
 *                                                |
 *                                               / \
 *                                               ---
 *                                                |
 *                      ----------------------------------------------------
 *                      |                         |                        |
 *               +--------------+          +--------------+       +-----------------+
 *               | Darken       |          | Brighten     |       | Blend           |
 *               +--------------+          +--------------+       +-----------------+
 *               | IPaint color |          | IPaint color |       | IPaint paint1 |
 *               +--------------+          +--------------+       | IPaint paint2 |
 *                                                                +-----------------+
 *                                                                    
 */


// to represent a paint
interface IPaint {
  // to get the final color of a paint
  Color getFinalColor();
  
  // to get the number of Solid paints
  int countPaints();
  
  // to compute the number of times a paint was mixed
  int countMixes();
  
  // to compute the how deeply a mixture is nested in the paint's formula
  int formulaDepth();
  
  // to invert the darkened and brightened mixtures of a paint
  IPaint invert();
  
  // to return a String that represents the content of the paint
  String mixingFormula(int depth);
}

// to represent a Solid paint with a name and color
class Solid implements IPaint {
  String name;
  Color color;
  
  Solid(String name, Color color) {
    this.name = name;
    this.color = color;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.name ...  -- String
   * ... this.color ... -- Color
   * 
   * METHODS:
   * ... this.getFinalColor() ... -- Color
   * ... this.countPaints() ...   -- int
   * ... this.countMixes() ...    -- int
   * ... this.formulaPaint() ...  -- int
   * ... this.invert() ...        -- IPaint
   * ... this.mixingFormula(int depth) ... -- String
   */
  
  // to get the color of a Solid
  public Color getFinalColor() {
    return this.color;
  }
  
  // to count how many paints are in a Solid
  public int countPaints() {
    return 1;
  }
  
  // to count how many times a Solid was mixed
  public int countMixes() {
    return 0;
  }
  
  // to compute how deeply a mixture is nested in a Solid
  public int formulaDepth() {
    return 0;
  }
  
  // to invert the darkened and brightened mixtures of a Solid
  public IPaint invert() {
    return this;
  }
  
  // to get a String that describes the mixing formula of the Solid, given a depth
  public String mixingFormula(int depth) { 
    return this.name;
  }
}

// to represent a Combo paint with a name and operation
class Combo implements IPaint {
  String name;
  IMixture operation;
  
  Combo(String name, IMixture operation) {
    this.name = name;
    this.operation = operation;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.name ...      -- String
   * ... this.operation ... -- IMixture
   * 
   * METHODS:
   * ... this.getFinalColor() ... -- Color
   * ... this.countPaints() ...   -- int
   * ... this.countMixes() ...    -- int
   * ... this.formulaDepth() ...  -- int
   * ... this.invert() ...        -- IPaint
   * ... this.mixingFormula() ... -- String
   * 
   * METHODS OF FIELDS:
   * ... this.operation.getFinalColor() ...          -- Color
   * ... this.operation.countPaints() ...            -- int
   * ... this.operation.countMixes() ...             -- int
   * ... this.operation.formulaDepth() ...           -- int
   * ... this.operation.invert() ...                 -- IPaint
   * ... this.operation.mixingFormula(int depth) ... -- String
   */
  
  // to get the color of a Combo paint
  public Color getFinalColor() {
    return this.operation.getFinalColor();
  }
  
  // to count how many paints are in a Combo paint
  public int countPaints() {
    return this.operation.countPaints();
  }
  
  // to count how many times a Combo was mixed
  public int countMixes() {
    return this.operation.countMixes();
  }
  
  // to compute how deeply a mixture is nested in a Combo paint
  public int formulaDepth() {
    return this.operation.formulaDepth();
  }
  
  // to invert the darkened and brightened mixtures of a Combo paint
  public IPaint invert() {
    return new Combo(this.name, this.operation.invert());
  }
  
  // to get a String that describes the mixing formula of the Combo paint, given a depth
  public String mixingFormula(int depth) {
    if (depth <= 0) {
      return this.name;
    }
    else {
      return this.operation.mixingFormula(depth - 1);
    }
  }
}

// to represent a recipe for making a paint color
interface IMixture {
  // to get the final color of a paint mixture
  Color getFinalColor();
  
  // to count the number of Solid paints that compose of a mixture
  int countPaints();
  
  // to count the number of times a paint was mixed
  int countMixes();
  
  // to compute the how deeply a mixture is nested in the paint's formula
  int formulaDepth();
  
  // to invert the darkened and brightened mixtures of a paint
  IMixture invert();
  
  // to return a String that represents the content of the paint
  String mixingFormula(int depth);
}

// to represent a Darkened paint color
class Darken implements IMixture {
  IPaint paint;
  
  Darken(IPaint paint) {
    this.paint = paint;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.color ...  -- IPaint
   * 
   * METHODS:
   * ... this.getFinalColor() ...          -- Color
   * ... this.countPaints() ...            -- int
   * ... this.countMixes() ...             -- int
   * ... this.formulaDepth() ...           -- int
   * ... this.invert() ...                 -- IMixture
   * ... this.mixingFormula(int depth) ... -- String
   * 
   * METHODS OF FIELDS: 
   * ... this.paint.getFinalCOlor() ...          -- Color
   * ... this.paint.countPaints() ...            -- int
   * ... this.paint.countMixes() ...             -- int
   * ... this.paint.formulaDepth() ...           -- int
   * ... this.paint.mixingFormula(int depth) ... -- String
   */
  
  // to get the final color of a darkened mixture
  public Color getFinalColor() {
    return this.paint.getFinalColor().darker();
  }
  
  // to count how many paints are in a darkened mixture
  public int countPaints() {
    return 1 + this.paint.countPaints();
  }
  
  // to count how many times a darkened mixture was mixed
  public int countMixes() {
    return 1 + this.paint.countMixes();
  }
  
  // to compute how deeply a mixture is nested in a darkened mixture
  public int formulaDepth() {
    return 1 + this.paint.formulaDepth();
  }
  
  // to invert a darkened mixture to a brightened mixture
  public IMixture invert() { 
    return new Brighten(this.paint);
  }
  
  // to produce a String that describes the mixing formula of a darkened mixture, given a depth
  public String mixingFormula(int depth) {
    return "darken(" + this.paint.mixingFormula(depth) + ")";
  }
}

// to represent a Brightened paint color
class Brighten implements IMixture {
  IPaint paint;
  
  Brighten(IPaint paint) {
    this.paint = paint;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.paint ...  -- IPaint
   * 
   * METHODS:
   * ... this.getFinalColor() ... -- Color
   * ... this.countPaints() ...   -- int
   * ... this.countMixes() ...    -- int
   * ... this.formulaDepth() ...  -- int
   * ... this.invert() ...        -- IMixture
   * ... this.mixingFormula() ... -- String
   * 
   * METHODS OF FIELDS: 
   * ... this.paint.getFinalCOlor() ...          -- Color
   * ... this.paint.countPaints() ...            -- int
   * ... this.paint.countMixes() ...             -- int
   * ... this.paint.formulaDepth() ...           -- int
   * ... this.paint.mixingFormula(int depth) ... -- String
   */
  
  // to get the final color of a brightened mixture
  public Color getFinalColor() {
    return this.paint.getFinalColor().brighter(); // !!!
  }
  
  // to count how many paints are in a brightened mixture
  public int countPaints() {
    return 1 + this.paint.countPaints();
  }
  
  // to count how many times a brightened mixture was mixed
  public int countMixes() {
    return 1 + this.paint.countMixes();
  }
  
  // to compute how deeply a mixture is nested in a brightened mixture
  public int formulaDepth() {
    return 1 + this.paint.formulaDepth();
  }
  
  // to invert a brightened mixture to a darkened mixture
  public IMixture invert() {
    return new Darken(this.paint);
  }
  
  // to produce a String that describes the mixing formula of a Brightened mixture, given a depth
  public String mixingFormula(int depth) {
    return "brighten(" + this.paint.mixingFormula(depth) + ")";
  }
}

// to represent a Blend of two colors
class Blend implements IMixture {
  IPaint paint1;
  IPaint paint2;
  
  Blend(IPaint paint1, IPaint paint2) {
    this.paint1 = paint1;
    this.paint2 = paint2;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.paint1 ...  -- IPaint
   * ... this.paint2 ...  -- IPaint
   * 
   * METHODS:
   * ... this.getFinalColor() ... -- Color
   * ... this.countPaints() ...   -- int
   * ... this.countMixes() ...    -- int
   * ... this.formulaDepth() ...  -- int
   * ... this.invert() ...        -- IMixture 
   * ... this.mixingFormula() ... -- String
   * 
   * METHODS OF FIELDS:
   * ... this.paint1.getFinalColor() ... -- Color
   * ... this.paint2.getFinalColor() ... -- Color
   * ... this.paint1.countPaints() ...   -- int
   * ... this.paint2.countPaints() ...   -- int
   * ... this.paint1.countMixes() ...    -- int
   * ... this.paint2.countMixes() ...    -- int
   * ... this.paint1.formulaDepth() ...  -- int
   * ... this.paint2.formulaDepth() ...  -- int
   * ... this.paint1.invert() ...        -- IPaint
   * ... this.paint2.invert() ...        -- IPaint
   */
  
  // to get the final Color of a blended mixture
  public Color getFinalColor() {
    return new Color((this.paint1.getFinalColor().getRed() / 2)
        + (this.paint2.getFinalColor().getRed() / 2),
        (this.paint1.getFinalColor().getGreen() / 2)
        + (this.paint2.getFinalColor().getGreen() / 2),
        (this.paint1.getFinalColor().getBlue() / 2)
        + (this.paint2.getFinalColor().getBlue() / 2));
  }
  
  // to count how many paints are in a Blended mixture
  public int countPaints() {
    return this.paint1.countPaints() + this.paint2.countPaints();
  }
  
  // to count how many times a Blended mixture was mixed
  public int countMixes() {
    return 1 + this.paint1.countMixes() + this.paint2.countMixes();
  }
  
  // to compute how deeply a mixture is nested in a Blended mixture
  public int formulaDepth() {
    if (this.paint1.formulaDepth() > this.paint2.formulaDepth()) {
      return 1 + this.paint1.formulaDepth();
    }
    else {
      return 1 + this.paint2.formulaDepth();
    }
  }
  
  // to invert the darkened and brightened mixtures of a Blend
  public IMixture invert() {
    return new Blend(this.paint1.invert(), this.paint2.invert());
  }
  
  // to produce a String that describes the mixing formula of a Blend, given a depth
  public String mixingFormula(int depth) { 
    return "blend(" + this.paint1.mixingFormula(depth) + ", "
        + this.paint2.mixingFormula(depth) + ")";
  }
}

// examples of paint colors
class ExamplesPaint {
  ExamplesPaint() {
  }
  
  IPaint red = new Solid("red", new Color(255, 0, 0));
  IPaint green = new Solid("green", new Color(0, 255, 0));
  IPaint blue = new Solid("blue", new Color(0, 0, 255));
  IPaint purple = new Combo("purple", new Blend(this.red, this.blue));
  IPaint darkPurple = new Combo("dark purple", new Darken(this.purple));
  IPaint khaki = new Combo("khaki", new Blend(this.red, this.green));
  IPaint yellow = new Combo("yellow", new Brighten(this.khaki));
  IPaint mauve = new Combo("mauve", new Blend(this.purple, this.khaki));
  IPaint pink = new Combo("pink", new Brighten(this.mauve));
  IPaint coral = new Combo("coral", new Blend(this.pink, this.khaki));
  IPaint brown = new Combo("brown", new Darken(this.red));
  IPaint darkdarkPurple = new Combo("dark purple", new Brighten(this.purple));
  IPaint brightPurple = new Combo("dark dark purple", new Blend(this.darkPurple, this.darkPurple));
  
  boolean testFinalColor(Tester t) {
    return t.checkExpect(this.red.getFinalColor(), new Color(255, 0, 0))
        && t.checkExpect(this.blue.getFinalColor(), new Color(0, 0, 255))
        && t.checkExpect(this.purple.getFinalColor(), new Color(127, 0, 127))
        && t.checkExpect(this.khaki.getFinalColor(), new Color(127, 127, 0))
        && t.checkExpect(this.darkPurple.getFinalColor(), new Color(88, 0, 88))
        && t.checkExpect(this.brown.getFinalColor(), new Color(178, 0, 0))
        && t.checkExpect(this.pink.getFinalColor(), new Color(180, 90, 90))
        && t.checkExpect(this.yellow.getFinalColor(), new Color(181, 181, 0));
  }
  
  boolean testCountPaints(Tester t) {
    return t.checkExpect(this.red.countPaints(), 1)
        && t.checkExpect(this.blue.countPaints(), 1)
        && t.checkExpect(this.purple.countPaints(), 2)
        && t.checkExpect(this.khaki.countPaints(), 2)
        && t.checkExpect(this.darkPurple.countPaints(), 3)
        && t.checkExpect(this.pink.countPaints(), 5)
        && t.checkExpect(this.yellow.countPaints(), 3)
        && t.checkExpect(this.coral.countPaints(), 7);
  }
  
  boolean testCountMixes(Tester t) {
    return t.checkExpect(this.red.countMixes(), 0)
        && t.checkExpect(this.blue.countMixes(), 0)
        && t.checkExpect(this.purple.countMixes(), 1)
        && t.checkExpect(this.darkPurple.countMixes(), 2)
        && t.checkExpect(this.yellow.countMixes(), 2)
        && t.checkExpect(this.brightPurple.countMixes(), 5)
        && t.checkExpect(this.coral.countMixes(), 6);
  }
  
  boolean testFormulaDepth(Tester t) {
    return t.checkExpect(this.red.formulaDepth(), 0)
        && t.checkExpect(this.blue.formulaDepth(), 0)
        && t.checkExpect(this.purple.formulaDepth(), 1)
        && t.checkExpect(this.khaki.formulaDepth(), 1)
        && t.checkExpect(this.darkPurple.formulaDepth(), 2)
        && t.checkExpect(this.yellow.formulaDepth(), 2)
        && t.checkExpect(this.pink.formulaDepth(), 3)
        && t.checkExpect(this.brightPurple.formulaDepth(), 3)
        && t.checkExpect(this.coral.formulaDepth(), 4);
  }
  
  boolean testInvert(Tester t) {
    return t.checkExpect(this.red.invert(), this.red)
        && t.checkExpect(this.blue.invert(), this.blue)
        && t.checkExpect(this.purple.invert(), this.purple)
        && t.checkExpect(this.brown.invert(), new Combo("brown", new Brighten(this.red)))
        && t.checkExpect(this.darkPurple.invert(), this.darkdarkPurple)
        && t.checkExpect(this.yellow.invert(), new Combo("yellow", new Darken(this.khaki)))
        && t.checkExpect(this.pink.invert(), new Combo("pink", new Darken(this.mauve)))
        && t.checkExpect(this.coral.invert(), new Combo("coral", new Blend(new Combo("pink",
            new Darken(this.mauve)), this.khaki)));
  }
  
  boolean testMixingFormula(Tester t) {
    return t.checkExpect(this.red.mixingFormula(0), "red")
        && t.checkExpect(this.blue.mixingFormula(-1), "blue")
        && t.checkExpect(this.yellow.mixingFormula(2), "brighten(blend(red, green))")
        && t.checkExpect(this.pink.mixingFormula(2), "brighten(blend(purple, khaki))")
        && t.checkExpect(this.darkdarkPurple.mixingFormula(3), "brighten(blend(red, blue))")
        && t.checkExpect(this.pink.mixingFormula(3), "brighten(blend(blend(red, blue),"
            + " blend(red, green)))")
        && t.checkExpect(this.coral.mixingFormula(5), "blend(brighten(blend(blend(red, blue),"
            + " blend(red, green))), blend(red, green))");
  }
}
  
