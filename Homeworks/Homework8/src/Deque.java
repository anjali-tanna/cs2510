import tester.Tester;
import java.util.function.Predicate;

// returns true if this string is lexographically after "c"
class AfterC implements Predicate<String> {
  public boolean test(String str) {
    return str.compareTo("c") > 0;
  }
}

// returns true if this string has a length equal to 2
class Length2 implements Predicate<String> {
  public boolean test(String str) {
    return str.length() == 2;
  }
}

// returns true if this integer is positive
class Negative implements Predicate<Integer> {
  public boolean test(Integer n) {
    return n < 0;
  }
}

// returns true if this integer is even
class Even implements Predicate<Integer> {
  public boolean test(Integer n) {
    return n % 2 == 0;
  }
}

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;
  
  // counts the number of nodes
  public abstract int size();
  
  // EFFECT: removes a node from this list
  // returns the removed data
  public T removeFrom() {
    this.next.prev = this.prev;
    this.prev.next = this.next;
    return this.getData();
  }
  
  // returns this data
  public abstract T getData();
  
  // returns the first node in which the given predicate returns true
  public abstract ANode<T> find(Predicate<T> pred);
}

class Node<T> extends ANode<T> {
  T data;
  
  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }
  
  Node(T data, ANode<T> next, ANode<T> prev) {
    if (next == null || prev == null) {
      throw new IllegalArgumentException("node is null");
    }
    this.data = data;
    this.next = next;
    this.prev = prev;
    next.prev = this;
    prev.next = this;
  }
  
  // counts the number of nodes
  public int size() {
    return 1 + this.next.size();
  }
  
  // returns this data
  public T getData() {
    return this.data;
  }
  
  // returns the first node in which the given predicate is true
  public ANode<T> find(Predicate<T> pred) {
    if (pred.test(this.data)) {
      return this;
    }
    else {
      return this.next.find(pred);
    }
  }
}

class Sentinel<T> extends ANode<T> {
  Sentinel() {
    this.next = this;
    this.prev = this;
  }
  
  // circled back to the sentinel, which isn't counted in the size of a Deque
  public int size() {
    return 0;
  }
  
  // EFFECT: inserts the given value of type T at the front of the list
  public void addAtHead(T value) {
    new Node<T>(value, this.next, this);
  }
  
  // EFFECT: inserts the given value of type T at the tail of this list
  public void addAtTail(T value) {
    new Node<T>(value, this, this.prev);
  }
  
  // EFFECT: removes the first node from this list
  // returns the removed data
  public T removeFromHead() {
    return this.next.removeFrom();
  }
  
  // EFFECT: removes the last node from this list
  // returns the removed data
  public T removeFromTail() {
    return this.prev.removeFrom();
  }
  
  // returns this data
  public T getData() {
    return null; // no data of a Sentinel
  }
  
  // returns this sentinel for when no item in the list is true for the given predicate 
  public ANode<T> find(Predicate<T> pred) {
    return this;
  }
}

class Deque<T> {
  Sentinel<T> header;
  
  Deque() {
    this.header = new Sentinel<T>();
  }
  
  Deque(Sentinel<T> header) {
    this.header = header;
  }
  
  // counts the number of nodes in this Deque
  public int size() {
    return this.header.next.size();
  }
  
  // EFFECT: modifies this Deque by inserting the given value of type T at the front of this list
  public void addAtHead(T value) {
    this.header.addAtHead(value);
  }
  
  // EFFECT: modifies this Deque by inserting the given value of type T at the tail of this list
  public void addAtTail(T value) {
    this.header.addAtTail(value);
  }
  
  // EFFECT: removes the first node from this list
  // returns the removed data
  public T removeFromHead() {
    if (this.size() == 0) {
      throw new RuntimeException("cannot remove node from empty list");
    }
    else {
      return this.header.removeFromHead();
    }
  }
  
  // EFFECT: removes the last node from this list 
  // returns the removed data
  public T removeFromTail() {
    if (this.size() == 0) {
      throw new RuntimeException("cannot remove node from empty list");
    }
    else {
      return this.header.removeFromTail();
    }
  }
  
  // returns the first node in this Deque for which the given predicate returns true
  // or the header if none are true
  public ANode<T> find(Predicate<T> pred) {
    return this.header.next.find(pred);
  }
  
  // EFFECT: removes the given node from this Deque
  public void removeNode(ANode<T> node) {
    if (! node.equals(this.header)) {
      node.removeFrom();
    }
  }
}

class ExamplesDeque {
  ExamplesDeque() {
  }
  
  Deque<String> deque1 = new Deque<String>();
  
  Sentinel<String> stringSentinel = new Sentinel<String>();
  Node<String> abc = new Node<String>("abc", this.stringSentinel, this.stringSentinel);
  Node<String> bcd = new Node<String>("bcd", this.stringSentinel, this.abc);
  Node<String> cde = new Node<String>("cde", this.stringSentinel, this.bcd);
  Node<String> def = new Node<String>("def", this.stringSentinel, this.cde);
  Deque<String> deque2 = new Deque<String>(this.stringSentinel);
  
  Sentinel<Integer> intSentinel = new Sentinel<Integer>();
  Node<Integer> two = new Node<Integer>(2, this.intSentinel, this.intSentinel);
  Node<Integer> one = new Node<Integer>(1, this.intSentinel, this.two);
  Node<Integer> six = new Node<Integer>(6, this.intSentinel, this.one);
  Node<Integer> three = new Node<Integer>(3, this.intSentinel, this.six);
  Node<Integer> five = new Node<Integer>(5, this.intSentinel, this.three);
  Node<Integer> four = new Node<Integer>(4, this.intSentinel, this.five);
  Deque<Integer> deque3 = new Deque<Integer>(this.intSentinel);
  
  // EFFECT: sets up the initial conditions for our tests by reinitializing examples
  void initTestConditions() {
    this.deque1 = new Deque<String>();
    
    this.stringSentinel = new Sentinel<String>();
    this.abc = new Node<String>("abc", this.stringSentinel, this.stringSentinel);
    this.bcd = new Node<String>("bcd", this.stringSentinel, this.abc);
    this.cde = new Node<String>("cde", this.stringSentinel, this.bcd);
    this.def = new Node<String>("def", this.stringSentinel, this.cde);
    this.deque2 = new Deque<String>(this.stringSentinel);
    
    this.intSentinel = new Sentinel<Integer>();
    this.two = new Node<Integer>(2, this.intSentinel, this.intSentinel);
    this.one = new Node<Integer>(1, this.intSentinel, this.two);
    this.six = new Node<Integer>(6, this.intSentinel, this.one);
    this.three = new Node<Integer>(3, this.intSentinel, this.six);
    this.five = new Node<Integer>(5, this.intSentinel, this.three);
    this.four = new Node<Integer>(4, this.intSentinel, this.five);
    this.deque3 = new Deque<Integer>(this.intSentinel);
  }
  
  // tests the initTestConditions method
  boolean testInitTestConditions(Tester t) {
    this.initTestConditions();
    boolean test1 = t.checkExpect(this.deque1, new Deque<String>());
    
    Sentinel<String> stringSentinel2 = new Sentinel<String>();
    Node<String> abc = new Node<String>("abc", stringSentinel2, stringSentinel2);
    Node<String> bcd = new Node<String>("bcd", stringSentinel2, abc);
    Node<String> cde = new Node<String>("cde", stringSentinel2, bcd);
    Node<String> def = new Node<String>("def", stringSentinel2, cde);
    Deque<String> newDeque2 = new Deque<String>(stringSentinel2);
    boolean test2 = t.checkExpect(this.deque2, newDeque2);
    
    Sentinel<Integer> intSentinel2 = new Sentinel<Integer>();
    Node<Integer> two = new Node<Integer>(2, intSentinel2, intSentinel2);
    Node<Integer> one = new Node<Integer>(1, intSentinel2, two);
    Node<Integer> six = new Node<Integer>(6, intSentinel2, one);
    Node<Integer> three = new Node<Integer>(3, intSentinel2, six);
    Node<Integer> five = new Node<Integer>(5, intSentinel2, three);
    Node<Integer> four = new Node<Integer>(4, intSentinel2, five);
    Deque<Integer> newDeque3 = new Deque<Integer>(intSentinel2);
    boolean test3 = t.checkExpect(this.deque3, newDeque3);
    
    return test1 && test2 && test3;
  }
  
  // tests the size method for Deque
  boolean testSizeDeque(Tester t) {
    this.initTestConditions();
    return t.checkExpect(this.deque1.size(), 0)
        && t.checkExpect(this.deque2.size(), 4)
        && t.checkExpect(this.deque3.size(), 6);
  }
  
  // tests the size method for Node
  boolean testSizeNode(Tester t) {
    this.initTestConditions();
    return t.checkExpect(this.abc.size(), 4)
        && t.checkExpect(this.cde.size(), 2)
        && t.checkExpect(this.three.size(), 3);
  }
  
  // tests the size method for Sentinel
  boolean testSizeSentinel(Tester t) {
    this.initTestConditions();
    return t.checkExpect(this.stringSentinel.size(), 0)
        && t.checkExpect(this.intSentinel.size(), 0);
  }
  
  // tests the addAtHead method for Deque
  boolean testAddAtHeadDeque(Tester t) {
    this.initTestConditions();
    
    // add "def", "cde", "bcd", and "abc" to the empty Deque
    this.deque1.addAtHead("def");
    this.deque1.addAtHead("cde");
    this.deque1.addAtHead("bcd");
    this.deque1.addAtHead("abc");
    boolean test1 = t.checkExpect(this.deque1, this.deque2);
    
    // adds "aaa" to the head of deque2
    Node<String> aaa = new Node<String>("aaa", this.stringSentinel, this.stringSentinel);
    this.abc = new Node<String>("abc", this.stringSentinel, aaa);
    Deque<String> newDeque2 = new Deque<String>(this.stringSentinel);
    this.deque2.addAtHead("aaa");
    boolean test2 = t.checkExpect(this.deque2, newDeque2);
    
    // adds 7 to the head of deque3
    Node<Integer> seven = new Node<Integer>(7, this.intSentinel, this.intSentinel);
    this.two = new Node<Integer>(2, this.intSentinel, seven);
    Deque<Integer> newDeque3 = new Deque<Integer>(this.intSentinel);
    this.deque3.addAtHead(7);
    boolean test3 = t.checkExpect(this.deque3, newDeque3);
    
    // adds 10 to the head of deque3
    Node<Integer> ten = new Node<Integer>(10, this.intSentinel, this.intSentinel);
    seven = new Node<Integer>(7, this.intSentinel, ten);
    newDeque3 = new Deque<Integer>(this.intSentinel);
    this.deque3.addAtHead(10);
    boolean test4 = t.checkExpect(this.deque3, newDeque3);
    
    return test1 && test2 && test3 && test4;
  }
  
  // tests the addAtHead method for Sentinel
  boolean testAddAtHeadSentinel(Tester t) {
    this.initTestConditions();
    
    // adds "aaa" and then "xyz" to the head of the stringSentinel
    Sentinel<String> stringSentinel2 = new Sentinel<String>();
    Node<String> xyz = new Node<String>("xyz", stringSentinel2, stringSentinel2);
    Node<String> aaa = new Node<String>("aaa", stringSentinel2, xyz);
    Node<String> abc = new Node<String>("abc", stringSentinel2, aaa);
    Node<String> bcd = new Node<String>("bcd", stringSentinel2, abc);
    Node<String> cde = new Node<String>("cde", stringSentinel2, bcd);
    Node<String> def = new Node<String>("def", stringSentinel2, cde);
    this.stringSentinel.addAtHead("aaa");
    this.stringSentinel.addAtHead("xyz");
    boolean test1 = t.checkExpect(this.stringSentinel, stringSentinel2);
    
    // adds 10 to the head of the intSentinel
    Sentinel<Integer> intSentinel2 = new Sentinel<Integer>();
    Node<Integer> ten = new Node<Integer>(10, intSentinel2, intSentinel2);
    Node<Integer> two = new Node<Integer>(2, intSentinel2, ten);
    Node<Integer> one = new Node<Integer>(1, intSentinel2, two);
    Node<Integer> six = new Node<Integer>(6, intSentinel2, one);
    Node<Integer> three = new Node<Integer>(3, intSentinel2, six);
    Node<Integer> five = new Node<Integer>(5, intSentinel2, three);
    Node<Integer> four = new Node<Integer>(4, intSentinel2, five);
    this.intSentinel.addAtHead(10);
    boolean test2 = t.checkExpect(this.intSentinel, intSentinel2);
   
    return test1 && test2;
  }
  
  // tests the addAtTail method for Deque
  boolean testAddAtTailDeque(Tester t) {
    this.initTestConditions();
    
    // adds "abc", "bcd", "cde", and "def" to the tail of the empty Deque
    this.deque1.addAtTail("abc");
    this.deque1.addAtTail("bcd");
    this.deque1.addAtTail("cde");
    this.deque1.addAtTail("def");
    boolean test1 = t.checkExpect(this.deque1, this.deque2);
    
    // adds "efg" to the tail of deque2
    Node<String> efg = new Node<String>("efg", this.stringSentinel, this.def); 
    Deque<String> newDeque2 = new Deque<String>(this.stringSentinel);
    this.deque2.addAtTail("efg");
    boolean test2 = t.checkExpect(this.deque2, newDeque2);
    
    // adds "fgh" to the tail of deque2
    Node<String> fgh = new Node<String>("fgh", this.stringSentinel, efg);
    newDeque2 = new Deque<String>(this.stringSentinel);
    this.deque2.addAtTail("fgh");
    boolean test3 = t.checkExpect(this.deque2, newDeque2);
    
    // adds 10 to the tail of deque3
    Node<Integer> ten = new Node<Integer>(10, this.intSentinel, this.four);
    Deque<Integer> newDeque3 = new Deque<Integer>(this.intSentinel);
    this.deque3.addAtTail(10);
    boolean test4 = t.checkExpect(this.deque3, newDeque3);
    
    return test1 && test2 && test3 && test4;
  }
  
  // tests the addAtTail method for Sentinel
  boolean testAddAtTailSentinel(Tester t) {
    this.initTestConditions();
    
    // adds "efg" to the tail of the stringSentinel
    Sentinel<String> stringSentinel2 = new Sentinel<String>();
    Node<String> abc = new Node<String>("abc", stringSentinel2, stringSentinel2);
    Node<String> bcd = new Node<String>("bcd", stringSentinel2, abc);
    Node<String> cde = new Node<String>("cde", stringSentinel2, bcd);
    Node<String> def = new Node<String>("def", stringSentinel2, cde);
    Node<String> efg = new Node<String>("efg", stringSentinel2, def);
    this.stringSentinel.addAtTail("efg");
    boolean test1 = t.checkExpect(this.stringSentinel, stringSentinel2);
    
    // adds "fgh" to the tail of the stringSentinel
    Node<String> fgh = new Node<String>("fgh", stringSentinel2, efg);
    this.stringSentinel.addAtTail("fgh");
    boolean test2 = t.checkExpect(this.stringSentinel, stringSentinel2);
    
    // adds 10 to the tail of the intSentinel
    Sentinel<Integer> intSentinel2 = new Sentinel<Integer>();
    Node<Integer> two = new Node<Integer>(2, intSentinel2, intSentinel2);
    Node<Integer> one = new Node<Integer>(1, intSentinel2, two);
    Node<Integer> six = new Node<Integer>(6, intSentinel2, one);
    Node<Integer> three = new Node<Integer>(3, intSentinel2, six);
    Node<Integer> five = new Node<Integer>(5, intSentinel2, three);
    Node<Integer> four = new Node<Integer>(4, intSentinel2, five);
    Node<Integer> ten = new Node<Integer>(10, intSentinel2, four);
    this.intSentinel.addAtTail(10);
    boolean test3 = t.checkExpect(this.intSentinel, intSentinel2);
    
    return test1 && test2 && test3;
  }
  
  // tests the removeFrom method for ANode<T>
  boolean testRemoveFromANode(Tester t) {
    this.initTestConditions();
    boolean test1 = t.checkExpect(this.abc.next, this.bcd);
    this.abc.removeFrom();
    boolean test2 = t.checkExpect(this.abc.next, this.bcd);
    
    return test1 && test2
        && t.checkExpect(this.abc.removeFrom(), "abc")
        && t.checkExpect(this.def.removeFrom(), "def")
        && t.checkExpect(this.five.removeFrom(), 5)
        && t.checkExpect(this.stringSentinel.removeFrom(), null)
        && t.checkExpect(this.stringSentinel.removeFrom(), null);
  }
  
  // tests the removeFromHead method for Deque
  boolean testRemoveFromHeadDeque(Tester t) {
    this.initTestConditions();
    
    // adds "abc" to the head of the empty Deque and then removes the first node
    this.deque1.addAtHead("abc");
    this.deque1.removeFromHead();
    boolean test1 = t.checkExpect(this.deque1, new Deque<String>());
    
    // removes the first node from deque2
    Sentinel<String> stringSentinel2 = new Sentinel<String>();
    Node<String> bcd = new Node<String>("bcd", stringSentinel2, stringSentinel2);
    Node<String> cde = new Node<String>("cde", stringSentinel2, bcd);
    Node<String> def = new Node<String>("def", stringSentinel2, cde);
    Deque<String> newDeque2 = new Deque<String>(stringSentinel2);
    this.deque2.removeFromHead();
    boolean test2 = t.checkExpect(this.deque2, newDeque2);
    
    // removes the first two nodes from deque3
    Sentinel<Integer> intSentinel2 = new Sentinel<Integer>();
    Node<Integer> six = new Node<Integer>(6, intSentinel2, intSentinel2);
    Node<Integer> three = new Node<Integer>(3, intSentinel2, six);
    Node<Integer> five = new Node<Integer>(5, intSentinel2, three);
    Node<Integer> four = new Node<Integer>(4, intSentinel2, five);
    Deque<Integer> newDeque3 = new Deque<Integer>(intSentinel2);
    this.deque3.removeFromHead();
    this.deque3.removeFromHead();
    boolean test3 = t.checkExpect(this.deque3, newDeque3);
    
    this.initTestConditions();
    
    return test1 && test2 && test3
        && t.checkException(new RuntimeException("cannot remove node from empty list"),
            this.deque1, "removeFromHead")
        && t.checkExpect(this.deque2.removeFromHead(), "abc")
        && t.checkExpect(this.deque3.removeFromHead(), 2);
  }
  
  // tests the removeFromHead method for Sentinel
  boolean testRemovefromHeadSentinel(Tester t) {
    this.initTestConditions();
    return t.checkExpect(this.stringSentinel.removeFromHead(), "abc")
        && t.checkExpect(this.intSentinel.removeFromHead(), 2);
  }
  
  // tests the removeFromTail method for Deque
  boolean testRemovefromTailDeque(Tester t) {
    this.initTestConditions();
    
    // adds "abc" to the end of this Deque and then removes the last node
    this.deque1.addAtTail("abc");
    this.deque1.removeFromTail();
    boolean test1 = t.checkExpect(this.deque1, new Deque<String>());
    
    // removes the last node from deque2
    Sentinel<String> stringSentinel2 = new Sentinel<String>();
    Node<String> abc = new Node<String>("abc", stringSentinel2, stringSentinel2);
    Node<String> bcd = new Node<String>("bcd", stringSentinel2, abc);
    Node<String> cde = new Node<String>("cde", stringSentinel2, bcd);
    Deque<String> newDeque2 = new Deque<String>(stringSentinel2);
    this.deque2.removeFromTail();
    boolean test2 = t.checkExpect(this.deque2, newDeque2);
    
    // removes the last two nodes from deque3
    Sentinel<Integer> intSentinel2 = new Sentinel<Integer>();
    Node<Integer> two = new Node<Integer>(2, intSentinel2, intSentinel2);
    Node<Integer> one = new Node<Integer>(1, intSentinel2, two);
    Node<Integer> six = new Node<Integer>(6, intSentinel2, one);
    Node<Integer> three = new Node<Integer>(3, intSentinel2, six);
    Deque<Integer> newDeque3 = new Deque<Integer>(intSentinel2);
    this.deque3.removeFromTail();
    this.deque3.removeFromTail();
    boolean test3 = t.checkExpect(this.deque3, newDeque3);
    
    this.initTestConditions();
    
    return test1 && test2 && test3
        && t.checkException(new RuntimeException("cannot remove node from empty list"),
            this.deque1, "removeFromTail")
        && t.checkExpect(this.deque2.removeFromTail(), "def")
        && t.checkExpect(this.deque3.removeFromTail(), 4);
  }
  
  // tests the removeFromTail method for Sentinel
  boolean testRemovefromTailSentinel(Tester t) {
    this.initTestConditions();
    return t.checkExpect(this.stringSentinel.removeFromTail(), "def")
        && t.checkExpect(this.intSentinel.removeFromTail(), 4);
  }
  
  // tests the find method for Deque 
  boolean testFindDeque(Tester t) {
    this.initTestConditions();
    return t.checkExpect(this.deque1.find(new AfterC()), new Sentinel<String>())
        && t.checkExpect(this.deque3.find(new Even()), this.two)
        && t.checkExpect(this.deque2.find(new AfterC()), this.cde)
        && t.checkExpect(this.deque2.find(new Length2()), this.stringSentinel)
        && t.checkExpect(this.deque3.find(new Negative()), this.intSentinel);
  }
  
  // tests the find method for Sentinel
  boolean testFindSentinel(Tester t) {
    this.initTestConditions();
    return t.checkExpect(this.stringSentinel.find(new Length2()), this.stringSentinel)
        && t.checkExpect(this.intSentinel.find(new Negative()), this.intSentinel)
        && t.checkExpect(this.stringSentinel.find(new AfterC()), this.stringSentinel);
  }
  
  // tests the find method for Node
  boolean testFindNode(Tester t) {
    this.initTestConditions();
    return t.checkExpect(this.abc.find(new AfterC()), this.cde)
        && t.checkExpect(this.def.find(new AfterC()), this.def)
        && t.checkExpect(this.one.find(new Even()), this.six)
        && t.checkExpect(this.two.find(new Negative()), this.intSentinel);
  }
  
  // tests the removeNode method for Deque
  boolean testRemoveNodeDeque(Tester t) {
    this.initTestConditions();
    // removes the given node from an empty Deque
    Deque<String> newDeque1 = new Deque<String>();
    this.deque1.removeNode(this.abc);
    boolean test1 = t.checkExpect(this.deque1, newDeque1);
    this.initTestConditions();
    
    // removes the "bcd" node from deque2
    Sentinel<String> stringSentinel2 = new Sentinel<String>();
    Node<String> abc = new Node<String>("abc", stringSentinel2, stringSentinel2);
    Node<String> cde = new Node<String>("cde", stringSentinel2, abc);
    Node<String> def = new Node<String>("def", stringSentinel2, cde);
    Deque<String> newDeque2 = new Deque<String>(stringSentinel2);
    this.deque2.removeNode(this.bcd);
    boolean test2 = t.checkExpect(this.deque2, newDeque2);
    
    // trying to remove the sentinel
    this.deque2.removeNode(this.stringSentinel);
    boolean test3 = t.checkExpect(this.deque2, newDeque2);
    
    // removes the 4 node from deque3
    Sentinel<Integer> intSentinel2 = new Sentinel<Integer>();
    Node<Integer> two = new Node<Integer>(2, intSentinel2, intSentinel2);
    Node<Integer> one = new Node<Integer>(1, intSentinel2, two);
    Node<Integer> six = new Node<Integer>(6, intSentinel2, one);
    Node<Integer> three = new Node<Integer>(3, intSentinel2, six);
    Node<Integer> five = new Node<Integer>(5, intSentinel2, three);
    Deque<Integer> newDeque3 = new Deque<Integer>(intSentinel2);
    this.deque3.removeNode(this.four);
    boolean test4 = t.checkExpect(this.deque3, newDeque3);
    
    return test1 && test2 && test3 && test4;
  }
}