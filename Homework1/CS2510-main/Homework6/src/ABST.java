import tester.*;
import java.util.Comparator;

abstract class ABST<T> {
  Comparator<T> order;

  ABST(Comparator<T> order) {
    this.order = order;
  }

  /*
   * TEMPLATE 
   * FIELDS: 
   * ... this.order 
   * ... -- Comparator<T>
   * 
   * METHODS:
   * ... this.insert(T data) ...             -- ABST<T>
   * ... this.present(T data) ...            -- boolean
   * ... this.getLeftmost() ...              -- T
   * ... this.getLeftmostHelper() ...        -- T
   * ... this.getRight() ...                 -- ABST<T>
   * ... this.sameTree(ABST<T> abst)         -- boolean
   * ... this.sameTreeHelper(Node<T> n) ...  -- boolean
   * ... this.sameTreeHelper2(Leaf<T> l) ... -- boolean
   * ... this.sameData(ABST<T> abst) ...     -- boolean
   */

  // inserts the given data into the tree
  public abstract ABST<T> insert(T data);
  
  // returns true if the given data is in the tree
  public abstract boolean present(T data);
  
  // returns the leftmost item in the tree
  public abstract T getLeftmost();
  
  // returns the leftmost item in the tree
  public abstract T getLeftmostHelper(T data);
  
  // returns the tree containing all but the leftmost item
  public abstract ABST<T> getRight();
  
  // returns true if the given tree is the same as this one
  public abstract boolean sameTree(ABST<T> abst);
  
  // returns true if the given node has the same data as this one, and
  // the rest of the tree is the same
  public abstract boolean sameTreeHelper(Node<T> n);
  
  // returns true if the given tree is the same as this tree
  public abstract boolean sameTreeHelper2(Leaf<T> l);
  
  // returns true if the given tree has the same data as this one
  public abstract boolean sameData(ABST<T> abst);
  
  // produces a list of items in the tree in the sorted order
  public abstract IList<T> buildList();
}

class Leaf<T> extends ABST<T> {
  Leaf(Comparator<T> order) {
    super(order);
  }

  /*
   * TEMPLATE
   * FIELDS: 
   * ... this.order ... -- Comparator<T>
   * 
   * METHODS:
   * ... this.insert(T data) ...               -- ABST<T>
   * ... this.present(T data) ...              -- boolean
   * ... this.getLeftmost() ...                -- T
   * ... this.getLeftmost(T data) ...          -- T
   * ... this.getRight() ...                   -- ABST<T>
   * ... this.sameTree(ABST<T> abst) ...       -- boolean
   * ... this.sameTreeHelper(Node<T> n) ...    -- boolean
   * ... this.sameTreeHelper2(Leaf<T> l) ...   -- boolean
   * ... this.sameData(ABST<T> abst) ...       -- boolean
   * ... this.buildList() ...                  -- IList<T>
   */
  
  // inserts the given data into the tree
  public ABST<T> insert(T data) {
    return new Node<T>(this.order, data,
        new Leaf<T>(this.order),
        new Leaf<T>(this.order));
  }
  
  // returns true if the given data is in the tree
  public boolean present(T data) {
    return false;
  }
  
  // returns the leftmost item in the tree
  public T getLeftmost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }
  
  // returns the leftmost item in the tree
  public T getLeftmostHelper(T data) {
    return data;
  }
  
  // returns the tree containing all but the leftmost item
  public ABST<T> getRight() {
    throw new RuntimeException("No right of an empty tree");
  }
  
  // returns true if the given tree is the same as this one
  public boolean sameTree(ABST<T> abst) {
    return abst.sameTreeHelper2(this);
  }

  // returns true if the given node has the same data as this one, and
  // the rest of the tree is the same
  public boolean sameTreeHelper(Node<T> n) {
    return false;
  }
  
  // returns true if the given tree is the same as this tree
  public boolean sameTreeHelper2(Leaf<T> l) {
    return true;
  }
  
  // returns true if the given tree has the same data as this one
  public boolean sameData(ABST<T> abst) {
    return true;
  }
  
  // produces a list of items in the tree in the sorted order
  public IList<T> buildList() {
    return new MtList<T>();
  }
}

class Node<T> extends ABST<T> {
  T data;
  ABST<T> left;
  ABST<T> right;

  Node(Comparator<T> order, T data, ABST<T> left, ABST<T> right) {
    super(order);
    this.data = data;
    this.left = left;
    this.right = right;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.order ... -- Comparator<T>
   * ... this.data ...  -- T
   * ... this.left ...  -- ABST<T>
   * ... this.right ... -- ABST<T>
   * 
   * MEHODS:
   * ... this.insert(T data) ...               -- ABST<T>
   * ... this.present(T data) ...              -- boolean
   * ... this.getLeftmost() ...                -- T
   * ... this.getRight() ...                   -- ABST<T>
   * ... this.sameTree(ABST<T> abst) ...       -- boolean
   * ... this.sameTreeHelper(Node<T> n) ...    -- boolean
   * ... this.sameTreeHelper2(Leaf<T> l) ...   -- boolean
   * ... this.sameData(ABST<T> data) ...       -- boolean
   * ... this.buildList() ...                  -- IList<T>
   */
  
  // inserts the given data into the tree
  public ABST<T> insert(T data) {
    if (this.order.compare(this.data, data) <= 0) {
      return new Node<T>(this.order, this.data, this.left, this.right.insert(data));
    }
    else {
      return new Node<T>(this.order, this.data, this.left.insert(data), this.right);
    }
  }
  
  // returns true if the given data is in the tree
  public boolean present(T data) {
    return (this.order.compare(this.data, data) == 0)
        || this.left.present(data)
        || this.right.present(data);
  }
  
  // returns the leftmost item in the tree
  public T getLeftmost() {
    return this.getLeftmostHelper(this.data);
  }
  
  // returns the leftmost item in the tree
  public T getLeftmostHelper(T data) {
    return this.left.getLeftmostHelper(this.data);
  }
  
  // returns the tree containing all but the leftmost item
  public ABST<T> getRight() {
    if (this.order.compare(this.data, this.getLeftmost()) == 0) {
      return this.right;
    }
    else {
      return new Node<T>(this.order, this.data, this.left.getRight(), this.right);
    }
  }
  
  // returns true if the given tree is the same as this one
  public boolean sameTree(ABST<T> abst) {
    return abst.sameTreeHelper(this);
  }
  
  // returns true if the given node has the same data as this one, and
  // the rest of the tree is the same
  public boolean sameTreeHelper(Node<T> n) {
    return this.order.compare(this.data, n.data) == 0
        && this.left.sameTree(n.left)
        && this.right.sameTree(n.right);
  }
  
  // returns true if the given tree is the same as this tree
  public boolean sameTreeHelper2(Leaf<T> l) {
    return false;
  }
  
  // returns true if the given tree has the same data as this one
  public boolean sameData(ABST<T> abst) {
    return this.order.compare(this.getLeftmost(), abst.getLeftmost()) == 0
        && this.getRight().sameData(abst.getRight());
  }
  
  // produces a list of items in the tree in the sorted order
  public IList<T> buildList() {
    return new ConsList<T>(this.getLeftmost(), this.getRight().buildList());
  }
}

class Book {
  String title;
  String author;
  int price;

  Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }

  /*
   * TEMPLATE 
   * FIELDS: 
   * ... this.title ... -- String 
   * ... this.author ... -- String
   * ... this.price ... -- int
   */  
}

// comparing books by title
class BooksByTitle implements Comparator<Book> {
  public int compare(Book b1, Book b2) {
    if (b1.title.compareTo(b2.title) < 0) {
      return -1;
    }
    else if (b1.title.compareTo(b2.title) > 0) {
      return 1;
    }
    else {
      return 0;
    }
  }
}

// comparing books by author
class BooksByAuthor implements Comparator<Book> {
  public int compare(Book b1, Book b2) {
    if (b1.author.compareTo(b2.author) < 0) {
      return -1;
    }
    else if (b1.author.compareTo(b2.author) > 0) {
      return 1;
    }
    else {
      return 0;
    }
  }
}

// comparing books by price
class BooksByPrice implements Comparator<Book> {
  public int compare(Book b1, Book b2) {
    if (b1.price < b2.price) {
      return -1;
    }
    else if (b1.price > b2.price) {
      return 1;
    }
    else {
      return 0;
    }
  }
}

// comparing strings
class CompareString implements Comparator<String> {
  public int compare(String s1, String s2) {
    if (s1.compareTo(s2) < 0) {
      return -1;
    }
    else if (s1.compareTo(s2) > 0) {
      return 1;
    }
    else {
      return 0;
    }
  }
}

// comparing integers
class CompareInteger implements Comparator<Integer> {
  public int compare(Integer i1, Integer i2) {
    if (i1 < i2) {
      return -1;
    }
    else if (i1 > i2) {
      return 1;
    }
    else {
      return 0;
    }
  }
}

interface IList<T> { 
}

class MtList<T> implements IList<T> {
  MtList() {
    
  }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first,IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
}

class ExamplesABST {
  ExamplesABST() {}
  
  Book matilda = new Book("Matilda", "Roald Dahl", 15);
  Book snowyDay = new Book("Snowy Day", "Ezra Jack Keats", 5);
  Book charlottesWeb = new Book("Charlotte's Web", "E.B.White", 7);
  Book catHat = new Book("Cat in the Hat", "Dr. Seuss", 6);
  Book goodnightMoon = new Book("Goodnight Moon", "Margaret Wise Brown", 3);
  Book givingTree = new Book("Giving Tree", "Shel Silverstein", 10);
  
  ABST<Book> titleLeaf = new Leaf<Book>(new BooksByTitle());
  ABST<Book> authorLeaf = new Leaf<Book>(new BooksByAuthor());
  ABST<Book> priceLeaf = new Leaf<Book>(new BooksByPrice());
  ABST<String> stringLeaf = new Leaf<String>(new CompareString());
  ABST<Integer> intLeaf = new Leaf<Integer>(new CompareInteger());
  
  Node<Book> matildaTitle = new Node<Book>(new BooksByTitle(), this.matilda,
      this.titleLeaf,
      this.titleLeaf);
  Node<Integer> oneNode = new Node<Integer>(new CompareInteger(), 1,
      this.intLeaf,
      this.intLeaf);
  
  // ABST of the example books sorted by title
  ABST<Book> bookTitle =
      new Node<Book>(new BooksByTitle(), this.matilda,
          new Node<Book>(new BooksByTitle(), this.charlottesWeb,
              new Node<Book>(new BooksByTitle(), this.catHat,
                  this.titleLeaf,
                  this.titleLeaf),
              new Node<Book>(new BooksByTitle(), this.goodnightMoon,
                  new Node<Book>(new BooksByTitle(), this.givingTree,
                      this.titleLeaf,
                      this.titleLeaf),
                  this.titleLeaf)),
          new Node<Book>(new BooksByTitle(), this.snowyDay,
              this.titleLeaf,
              this.titleLeaf));
  
  // ABST of the example books sorted by author, with no left side
  ABST<Book> bookAuthor =
      new Node<Book>(new BooksByAuthor(), this.catHat,
          this.authorLeaf,
          new Node<Book>(new BooksByAuthor(), this.charlottesWeb,
              this.authorLeaf,
              new Node<Book>(new BooksByAuthor(), this.snowyDay,
                  this.authorLeaf,
                  new Node<Book>(new BooksByAuthor(), this.goodnightMoon,
                      this.authorLeaf,
                      new Node<Book>(new BooksByAuthor(), this.matilda,
                          this.authorLeaf,
                          new Node<Book>(new BooksByAuthor(), this.givingTree,
                              this.authorLeaf,
                              this.authorLeaf))))));
  
  // ABST of the example books sorted by price, with no right side
  ABST<Book> bookPrice =
      new Node<Book>(new BooksByPrice(), this.matilda,
          new Node<Book>(new BooksByPrice(), this.givingTree,
              new Node<Book>(new BooksByPrice(), this.charlottesWeb,
                  new Node<Book>(new BooksByPrice(), this.catHat,
                      new Node<Book>(new BooksByPrice(), this.snowyDay,
                          new Node<Book>(new BooksByPrice(), this.goodnightMoon,
                              this.priceLeaf,
                              this.priceLeaf),
                          this.priceLeaf),
                      this.priceLeaf),
                  this.priceLeaf),
              this.priceLeaf),
          this.priceLeaf);
  
  // ABST of Strings with duplicate
  ABST<String> stringTree = 
      new Node<String>(new CompareString(), "m",
          new Node<String>(new CompareString(), "d",
              new Node<String>(new CompareString(), "a",
                  this.stringLeaf,
                  this.stringLeaf),
              new Node<String>(new CompareString(), "k",
                  this.stringLeaf,
                  this.stringLeaf)),
          new Node<String>(new CompareString(), "p",
              this.stringLeaf,
              new Node<String>(new CompareString(), "p",
                  this.stringLeaf,
                  this.stringLeaf)));
  
  // ABST of Strings with a missing node
  ABST<String> stringTree2 = 
      new Node<String>(new CompareString(), "m",
          new Node<String>(new CompareString(), "d",
              new Node<String>(new CompareString(), "a",
                  this.stringLeaf,
                  this.stringLeaf),
              this.stringLeaf),
          new Node<String>(new CompareString(), "p",
              this.stringLeaf,
              new Node<String>(new CompareString(), "p",
                  this.stringLeaf,
                  this.stringLeaf)));
  
  // bstA is the sameTree as bstB, sameData as bstB and bstC
  ABST<Integer> bstA = 
      new Node<Integer>(new CompareInteger(), 3,
          new Node<Integer>(new CompareInteger(), 2,
              new Node<Integer>(new CompareInteger(), 1,
                  this.intLeaf,
                  this.intLeaf),
              this.intLeaf),
          new Node<Integer>(new CompareInteger(), 4,
              this.intLeaf,
              this.intLeaf));
  
  ABST<Integer> bstB =
      new Node<Integer>(new CompareInteger(), 3,
          new Node<Integer>(new CompareInteger(), 2,
              new Node<Integer>(new CompareInteger(), 1,
                  this.intLeaf,
                  this.intLeaf),
              this.intLeaf),
          new Node<Integer>(new CompareInteger(), 4,
              this.intLeaf,
              this.intLeaf));
  
  ABST<Integer> bstC = 
      new Node<Integer>(new CompareInteger(), 2,
          new Node<Integer>(new CompareInteger(), 1, 
              this.intLeaf,
              this.intLeaf),
          new Node<Integer>(new CompareInteger(), 4,
              new Node<Integer>(new CompareInteger(), 3,
                  this.intLeaf,
                  this.intLeaf),
              this.intLeaf));
  
  ABST<Integer> bstD = 
      new Node<Integer>(new CompareInteger(), 3,
          new Node<Integer>(new CompareInteger(), 1,
              this.intLeaf,
              this.intLeaf),
          new Node<Integer>(new CompareInteger(), 4,
              this.intLeaf,
              new Node<Integer>(new CompareInteger(), 5,
                  this.intLeaf,
                  this.intLeaf)));
  
  // ABST of Integers
  ABST<Integer> intTree =
      new Node<Integer>(new CompareInteger(), 4,
          new Node<Integer>(new CompareInteger(), 2,
              new Node<Integer>(new CompareInteger(), 1,
                  this.intLeaf,
                  this.intLeaf),
              new Node<Integer>(new CompareInteger(), 3,
                  this.intLeaf,
                  this.intLeaf)),
          new Node<Integer>(new CompareInteger(), 5,
              this.intLeaf,
              this.intLeaf));
  
  // test the insert method
  boolean testInsert(Tester t) {
    return t.checkExpect(this.titleLeaf.insert(this.matilda),
        new Node<Book>(new BooksByTitle(), this.matilda, this.titleLeaf, this.titleLeaf))
        && t.checkExpect(this.intTree.insert(5),
            new Node<Integer>(new CompareInteger(), 4,
                new Node<Integer>(new CompareInteger(), 2,
                    new Node<Integer>(new CompareInteger(), 1,
                        this.intLeaf,
                        this.intLeaf),
                    new Node<Integer>(new CompareInteger(), 3,
                        this.intLeaf,
                        this.intLeaf)),
                new Node<Integer>(new CompareInteger(), 5,
                    this.intLeaf,
                    new Node<Integer>(new CompareInteger(), 5,
                        this.intLeaf,
                        this.intLeaf))))
        && t.checkExpect(this.stringTree.insert("o"), 
            new Node<String>(new CompareString(), "m",
                new Node<String>(new CompareString(), "d",
                    new Node<String>(new CompareString(), "a",
                        this.stringLeaf,
                        this.stringLeaf),
                    new Node<String>(new CompareString(), "k",
                        this.stringLeaf,
                        this.stringLeaf)),
                new Node<String>(new CompareString(), "p",
                    new Node<String>(new CompareString(), "o",
                        this.stringLeaf,
                        this.stringLeaf),
                    new Node<String>(new CompareString(), "p",
                        this.stringLeaf,
                        this.stringLeaf))));

  }
  
  // testing the present method
  boolean testPresent(Tester t) {
    return t.checkExpect(this.authorLeaf.present(this.catHat), false)
        && t.checkExpect(this.bookTitle.present(new Book("Matilda", "John Smith", 5)), true)
        && t.checkExpect(this.bookAuthor.present(new Book("The Giver", "Roald Dahl", 7)), true)
        && t.checkExpect(this.bookPrice.present(new Book("Charlotte's Web", "E.B.Green", 7)), true)
        && t.checkExpect(this.bookTitle.present(new Book("Kevin", "Roald Dahl", 3)), false)
        && t.checkExpect(this.stringLeaf.present("b"), false)
        && t.checkExpect(this.intLeaf.present(6), false);
  }
  
  // testing the getLeftmost method 
  boolean testGetLeftmost(Tester t) {
    return t.checkException(new RuntimeException("No leftmost item of an empty tree"),
        this.authorLeaf, "getLeftmost")
        && t.checkExpect(this.bookTitle.getLeftmost(), this.catHat)
        && t.checkExpect(this.bookAuthor.getLeftmost(), this.catHat)
        && t.checkExpect(this.bookPrice.getLeftmost(), this.goodnightMoon)
        && t.checkExpect(this.stringTree.getLeftmost(), "a")
        && t.checkExpect(this.intTree.getLeftmost(), 1);
  }
  
  // testing the getLeftmostHelper method
  boolean testGetLeftmostHelper(Tester t) {
    return t.checkExpect(this.titleLeaf.getLeftmostHelper(this.snowyDay), this.snowyDay)
        && t.checkExpect(this.bookTitle.getLeftmostHelper(this.catHat), this.catHat)
        && t.checkExpect(this.bookAuthor.getLeftmostHelper(this.matilda), this.catHat)
        && t.checkExpect(this.bookPrice.getLeftmostHelper(this.goodnightMoon), this.goodnightMoon)
        && t.checkExpect(this.stringTree.getLeftmostHelper("a"), "a")
        && t.checkExpect(this.intTree.getLeftmostHelper(2), 1);
  }
  
  // testing the getRight method
  boolean testGetRight(Tester t) {
    return t.checkException(new RuntimeException("No right of an empty tree"),
        this.titleLeaf, "getRight")
        && t.checkExpect(this.bookTitle.getRight(),
            new Node<Book>(new BooksByTitle(), this.matilda,
                new Node<Book>(new BooksByTitle(), this.charlottesWeb,
                    this.titleLeaf,
                    new Node<Book>(new BooksByTitle(), this.goodnightMoon,
                        new Node<Book>(new BooksByTitle(), this.givingTree,
                            this.titleLeaf,
                            this.titleLeaf),
                        this.titleLeaf)),
                new Node<Book>(new BooksByTitle(), this.snowyDay,
                    this.titleLeaf,
                    this.titleLeaf)))
        && t.checkExpect(this.bookAuthor.getRight(),
            new Node<Book>(new BooksByAuthor(), this.charlottesWeb,
                this.authorLeaf,
                    new Node<Book>(new BooksByAuthor(), this.snowyDay,
                        this.authorLeaf,
                        new Node<Book>(new BooksByAuthor(), this.goodnightMoon,
                            this.authorLeaf,
                            new Node<Book>(new BooksByAuthor(), this.matilda,
                                this.authorLeaf,
                                new Node<Book>(new BooksByAuthor(), this.givingTree,
                                    this.authorLeaf,
                                    this.authorLeaf))))))
        && t.checkExpect(this.bookPrice.getRight(),
            new Node<Book>(new BooksByPrice(), this.matilda,
                new Node<Book>(new BooksByPrice(), this.givingTree,
                    new Node<Book>(new BooksByPrice(), this.charlottesWeb,
                        new Node<Book>(new BooksByPrice(), this.catHat,
                            new Node<Book>(new BooksByPrice(), this.snowyDay,
                                this.priceLeaf,
                                this.priceLeaf),
                            this.priceLeaf),
                        this.priceLeaf),
                    this.priceLeaf),
                this.priceLeaf))
        && t.checkExpect(this.stringTree.getRight(),
            new Node<String>(new CompareString(), "m",
                new Node<String>(new CompareString(), "d",
                    this.stringLeaf,
                    new Node<String>(new CompareString(), "k",
                        this.stringLeaf,
                        this.stringLeaf)),
                new Node<String>(new CompareString(), "p",
                    this.stringLeaf,
                    new Node<String>(new CompareString(), "p",
                        this.stringLeaf,
                        this.stringLeaf))))
        && t.checkExpect(this.intTree.getRight(),
            new Node<Integer>(new CompareInteger(), 4,
                new Node<Integer>(new CompareInteger(), 2,
                    this.intLeaf,
                    new Node<Integer>(new CompareInteger(), 3,
                        this.intLeaf,
                        this.intLeaf)),
                new Node<Integer>(new CompareInteger(), 5,
                    this.intLeaf,
                    this.intLeaf)));
  }
  
  // testing the sameTree method
  boolean testSameTree(Tester t) {
    return t.checkExpect(this.bookTitle.sameTree(this.bookTitle), true)
        && t.checkExpect(this.bstA.sameTree(this.bstB), true)
        && t.checkExpect(this.bstA.sameTree(this.bstC), false)
        && t.checkExpect(this.bstA.sameTree(this.bstD), false)
        && t.checkExpect(this.stringTree.sameTree(this.stringTree2), false)
        && t.checkExpect(this.authorLeaf.sameTree(this.bookAuthor), false);
  }
  
  // testing the sameTreeHelper method
  boolean testSameTreeHelper(Tester t) {
    return t.checkExpect(this.bookTitle.sameTreeHelper(this.matildaTitle), false)
        && t.checkExpect(this.bstA.sameTreeHelper(this.oneNode), false)
        && t.checkExpect(this.bstB.sameTreeHelper(this.oneNode), false)
        && t.checkExpect(this.bstC.sameTreeHelper(this.oneNode), false);
  }
  
  // testing the sameTreeHelper2 method
  boolean testSameTreeHelper2(Tester t) {
    return t.checkExpect(this.bookAuthor.sameTreeHelper2(new Leaf<Book>(new BooksByAuthor())),
        false)
        && t.checkExpect(new Leaf<Book>(new BooksByAuthor()).sameTreeHelper2(
            new Leaf<Book>(new BooksByAuthor())), true);
  }
  
  // testing the sameData method
  boolean testSameData(Tester t) {
    return t.checkExpect(this.stringLeaf.sameData(this.stringTree), true)
        && t.checkExpect(this.bstA.sameData(this.bstA), true)
        && t.checkExpect(this.bstA.sameData(this.bstB), true)
        && t.checkExpect(this.bstA.sameData(this.bstC), true)
        && t.checkExpect(this.bstA.sameData(this.bstD), false);
  }
  
  // testing the buildList method
  boolean testBuildList(Tester t) {
    return t.checkExpect(this.titleLeaf.buildList(), new MtList<Book>())
        && t.checkExpect(this.bookTitle.buildList(), new ConsList<Book>(this.catHat,
            new ConsList<Book>(this.charlottesWeb, new ConsList<Book>(this.givingTree, 
                new ConsList<Book>(this.goodnightMoon, new ConsList<Book>(this.matilda,
                    new ConsList<Book>(this.snowyDay, new MtList<Book>())))))))
        && t.checkExpect(this.stringTree.buildList(), new ConsList<String>("a",
            new ConsList<String>("d", new ConsList<String>("k", new ConsList<String>("m",
                new ConsList<String>("p", new ConsList<String>("p", new MtList<String>())))))))
        && t.checkExpect(this.intTree.buildList(), new ConsList<Integer>(1, new ConsList<Integer>(2,
            new ConsList<Integer>(3, new ConsList<Integer>(4, new ConsList<Integer>(5,
                new MtList<Integer>()))))));
  }
}