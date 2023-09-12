import tester.*;

// to represent an EmbroideryPiece with a name and motif
class EmbroideryPiece {
  String name;
  IMotif motif;

  EmbroideryPiece(String name, IMotif motif) {
    this.name = name;
    this.motif = motif;
  }

  /*
   * TEMPLATE
   * FIELDS: 
   * ... this.name ...  -- String 
   * ... this.motif ... -- IMotif
   * 
   * METHODS: 
   * ... this.count ...             -- int
   * ... this.averageDifficulty ... -- double
   * ... this.embroideryInfo ...    -- String
   * 
   * METHODS OF FIELDS:
   * ... this.motif.count...          -- int
   * ... this.motif.getDifficulty ... -- double
   * ... this.motif.getInfo() ...     -- String
   */
  
  // to compute the total number of all cross-stitch and chain-stitch
  // motifs in an EmbroideryPiece
  public int count() {
    return this.motif.count();
  }
  
  // to compute the average difficulty of all cross-stitch and chain-stitch
  // motifs in an EmbroideryPiece
  public double averageDifficulty() {
    if (this.motif.count() == 0) {
      return 0;
    }
    else {
      return this.motif.getDifficulty() / this.motif.count();
    }
  }
  
  // to produce a String that includes all the names and stitch types of
  // cross-stitch and chain-stitch motifs in an EmbroideryPiece
  public String embroideryInfo() {
    return this.name + ": " + this.motif.getInfo() + ".";
  }
}

// to represent a Motif
interface IMotif {
  // to return the count of a motif
  int count();
  
  // to compute the difficulty of a motif
  double getDifficulty();
  
  // to produce a String that includes the info of a motif
  String getInfo();
}

// to represent a CrossStitchMotif with a description and difficulty
class CrossStitchMotif implements IMotif {
  String description;
  double difficulty;

  CrossStitchMotif(String description, double difficulty) {
    this.description = description;
    this.difficulty = difficulty;
  }

  /*
   * TEMPLATE 
   * FIELDS: 
   * ... this.description ... -- String 
   * ... this.difficulty ...  -- double
   * 
   * METHODS: 
   * ... this.count() ...         -- int
   * ... this.getDifficulty() ... -- double
   * ... this.getInfo() ...       -- String
   */
  
  // to get the count of a cross-stitch motif
  public int count() {
    return 1;
  }
  
  // to get the difficulty of a cross-stitch motif
  public double getDifficulty() {
    return this.difficulty;
  }
  
  // to return a String with the info of a cross-stitch motif
  public String getInfo() {
    return this.description + " (cross stitch)";
  }
}

// to represent a ChainStitchMotif with a description and difficulty
class ChainStitchMotif implements IMotif {
  String description;
  double difficulty;

  ChainStitchMotif(String description, double difficulty) {
    this.description = description;
    this.difficulty = difficulty;
  }

  /*
   * TEMPLATE 
   * FIELDS: 
   * ... this.description ... -- String 
   * ... this.difficulty ...  -- double
   * 
   * METHODS: 
   * ... this.count() ...         -- int 
   * ... this.getDifficulty() ... -- double 
   * ... this.getInfo() ...       -- String
   */
  
  // to get the count of a chain-stitch motif
  public int count() {
    return 1;
  }
  
  // to get the difficulty of a chain-stitch motif
  public double getDifficulty() {
    return this.difficulty;
  }
  
  // to return a String with the info of a chain-stitch motif
  public String getInfo() {
    return this.description + " (chain stitch)";
  }
}

// to represent a GroupMotif with a description and a list of motifs
class GroupMotif implements IMotif {
  String description;
  ILoMotif motifs;

  GroupMotif(String description, ILoMotif motifs) {
    this.description = description;
    this.motifs = motifs;
  }

  /*
   * TEMPLATE 
   * FIELDS:
   * ... this.description ... -- String 
   * ... this.motifs ...      -- ILoMotif
   * 
   * METHODS:
   * ... this.count() ...         -- int
   * ... this.getDifficulty() ... -- double
   * ... this.getInfo() ...       -- String
   * 
   * METHODS OF FIELDS:
   * ... this.motifs.count() ...         -- int
   * ... this.motifs.getDifficulty() ... -- double
   * ... this.motifs.getInfo() ...       -- String
   */
  
  // to get the count of a list of motifs in a group motif
  public int count() {
    return this.motifs.count();
  }

  // to get the total difficulties of a list of motifs in a group motif
  public double getDifficulty() {
    return this.motifs.getDifficulty();
  }
  
  // to return a String with the info of a list of motifs in a group motif
  public String getInfo() {
    return this.motifs.getInfo();
  }
}

// list of Motifs
interface ILoMotif {
  // to compute the total count of motifs in a list of motifs
  int count();
  
  // to compute the total difficulty of motifs in a list of motifs
  double getDifficulty();
  
  // to produce a String that contains the info of all motifs in a list of motifs
  String getInfo();
}

// empty list of Motifs
class MtLoMotif implements ILoMotif {
  public int count() {
    return 0;
  }
  
  public double getDifficulty() {
    return 0;
  }
  
  public String getInfo() {
    return "";
  }
}

// non empty list of Motifs
class ConsLoMotif implements ILoMotif {
  IMotif first;
  ILoMotif rest;

  ConsLoMotif(IMotif first, ILoMotif rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * TEMPLATE
   * FIELDS: 
   * ... this.first ... -- IMotif 
   * ... this.rest ...  -- ILoMotif
   * 
   * METHODS: 
   * ... this.count() ...          -- int
   * ... this.getDifficulty() ...  -- double
   * ... this.getInfo() ...        -- String  
   * 
   * METHODS OF FIELDS:
   * ... this.rest.count() ...           -- int
   * ... this.first.count() ...          -- int
   * ... this.first.getDifficulty() ...  -- double
   * ... this.rest.getDifficulty() ...   -- double
   * ... this.first.getInfo() ...        -- String
   * ... this.rest.getInfo() ...         -- String
   */

  // to compute the number of motifs in a list of motifs
  public int count() {
    return this.first.count() + this.rest.count();
  }

  // to compute the total difficulties of the motifs in a list of motifs
  public double getDifficulty() {
    return this.first.getDifficulty() + this.rest.getDifficulty();
  }
  
  // to return a String with the info of all the motifs in a list of motifs
  public String getInfo() {
    if (this.rest.getInfo().equals("")) {
      return this.first.getInfo();
    }
    else {
      return this.first.getInfo() + ", " + this.rest.getInfo();
    }
  }
}

// examples of embroidery pieces
class ExamplesEmbroidery {
  ExamplesEmbroidery() {
  }

  IMotif bird = new CrossStitchMotif("bird", 4.5);
  IMotif tree = new ChainStitchMotif("tree", 3.0);
  IMotif rose = new CrossStitchMotif("rose", 5.0);
  IMotif poppy = new ChainStitchMotif("poppy", 4.75);
  IMotif daisy = new CrossStitchMotif("daisy", 3.2);
  ILoMotif mtlist = new MtLoMotif();
  IMotif flowers = new GroupMotif("flowers", new ConsLoMotif(this.rose,
      new ConsLoMotif(this.poppy, new ConsLoMotif(this.daisy, this.mtlist))));
  IMotif nature = new GroupMotif("nature", new ConsLoMotif(this.bird,
      new ConsLoMotif(this.tree, new ConsLoMotif(this.flowers, this.mtlist))));
  EmbroideryPiece pillowCover = new EmbroideryPiece("Pillow Cover", this.nature);

  boolean testCount(Tester t) {
    return t.checkExpect(this.mtlist.count(), 0)
        && t.checkExpect(this.bird.count(), 1)
        && t.checkExpect(this.tree.count(), 1)
        && t.checkExpect(this.flowers.count(), 3)
        && t.checkExpect(this.pillowCover.count(), 5);
  }
  
  boolean testDifficulty(Tester t) {
    return t.checkExpect(this.mtlist.count(), 0)
        && t.checkExpect(this.bird.getDifficulty(), 4.5)
        && t.checkExpect(this.tree.getDifficulty(), 3.0)
        && t.checkExpect(this.flowers.getDifficulty(), 12.95)
        && t.checkExpect(this.pillowCover.averageDifficulty(), 4.09);
  }
  
  boolean testInfo(Tester t) {
    return t.checkExpect(this.mtlist.getInfo(), "")
        && t.checkExpect(this.bird.getInfo(), "bird (cross stitch)")
        && t.checkExpect(this.tree.getInfo(), "tree (chain stitch)")
        && t.checkExpect(this.flowers.getInfo(), "rose (cross stitch), poppy (chain stitch),"
            + " daisy (cross stitch)")
        && t.checkExpect(this.pillowCover.embroideryInfo(), "Pillow Cover: bird"
            + " (cross stitch), tree (chain stitch), rose (cross stitch), poppy "
            + "(chain stitch), daisy (cross stitch).");
  }
}
