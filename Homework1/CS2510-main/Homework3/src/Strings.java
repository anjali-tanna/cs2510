import tester.*;

// to represent a list of Strings
interface ILoString {
  // combine all Strings in this list into one
  String combine();
  
  // produce a new list with the list of Strings in alphabetical order
  ILoString sort();
  
  // insert the given String into a list of Strings
  ILoString insert(String str);
  
  // determine whether a list of Strings is sorted in alphabetical order
  boolean isSorted();
  
  // determine where a String should go in a list of Strings according to in alphabetical order
  boolean isSortedHelper(String str);
  
  // combine two lists of Strings by alternating their elements
  ILoString interleave(ILoString other);
  
  // combine two sorted lists of Strings into a new sorted list of Strings
  ILoString merge(ILoString other);
  
  // combine two sorted lists of Strings into a new sorted list of Strings
  ILoString mergeHelper(ILoString listSoFar);
  
  // produce a new list of Strings with the list in reverse order
  ILoString reverse();
  
  // produce a new list of Strings with the list in reverse order
  ILoString reverseHelper(ILoString listSoFar);
  
  // determine if this list contains pairs of identical Strings
  boolean isDoubledList();
  
  // determine if this list contains pairs of identical Strings
  boolean isDoubledListHelper(String str, ILoString listSoFar, int count);
  
  // determine whether this list contains the same words reading the list in either order
  boolean isPalindromeList();
  
}

// to represent an empty list of Strings
class MtLoString implements ILoString {
  MtLoString() {
  }

  // combine all Strings in this list into one
  public String combine() {
    return "";
  }
  
  // produce a new list with the list of Strings in alphabetical order
  public ILoString sort() {
    return new MtLoString();
  }
  
  // insert a String into a list of String
  public ILoString insert(String str) {
    return new ConsLoString(str, this);
  }
  
  // determine whether a list of Strings is sorted in alphabetical order
  public boolean isSorted() {
    return true;
  }
  
  // determine where a String should go in a list of Strings according to in alphabetical order
  public boolean isSortedHelper(String str) {
    return true;
  }
  
  // combine two lists of Strings by alternating their elements
  public ILoString interleave(ILoString other) {
    return other;
  }
  
  // combine two sorted lists of Strings into a new sorted list of Strings
  public ILoString merge(ILoString other) {
    return other;
  }
  
  // combine two sorted lists of Strings into a new sorted list of Strings
  public ILoString mergeHelper(ILoString listSoFar) {
    return listSoFar;
  }
  
  // produce a new list of Strings with the list in reverse order
  public ILoString reverse() {
    return new MtLoString();
  }
  
  // produce a new list of Strings with the list in reverse order
  public ILoString reverseHelper(ILoString listSoFar) {
    return listSoFar;
  }
  
  // determine if this list contains pairs of identical Strings
  public boolean isDoubledList() {
    return true;
  }
  
  // determine if this list had an even number of Strings
  public boolean isDoubledListHelper(String str, ILoString listSoFar, int count) {
    return (count - 1) % 2 == 0;
  }
 
  // determine whether this list contains the same words reading the list in either order
  public boolean isPalindromeList() {
    return true;
  }
}

// to represent a nonempty list of Strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * TEMPLATE FIELDS:
   * ... this.first ... -- String
   * ... this.rest ...  -- ILoString
   * 
   * METHODS
   * ... this.combine() ...                                                       -- String
   * ... this.sort() ...                                                          -- ILoString
   * ... this.insert() ...                                                        -- ILoString
   * ... this.isSorted() ...                                                      -- boolean
   * ... this.isSortedHelper(String str) ...                                      -- boolean
   * ... this.interleave(ILoString other) ...                                     -- ILoString
   * ... this.merge(ILoString other) ...                                          -- ILoString
   * ... this.mergeHelper(ILoString listSoFar) ...                                -- ILoString
   * ... this.reverse() ...                                                       -- ILoString
   * ... this.reverseHelper(ILoString listSoFar) ...                              -- ILoString
   * ... this.isDoubledList() ...                                                 -- boolean
   * ... this.isDoubledListHelper(String str, ILoString listSoFar, int count) ... -- boolean
   * ... this.isPalindromeList() ...                                              -- boolean
   * 
   * METHODS FOR FIELDS 
   * ... this.first.concat(String) ...                                                 -- String
   * ... this.first.compareTo(String) ...                                              -- int 
   * ... this.rest.combine() ...                                                       -- String
   * ... this.rest.sort() ...                                                          -- ILoString
   * ... this.rest.insert(String) ...                                                  -- ILoString
   * ... this.rest.isSortedHelper(String str) ...                                      -- boolean
   * ... this.rest.mergeHelper(ILoString listSoFar) ...                                -- ILoString
   * ... this.rest.reverseHelper(ILoString listSoFar) ...                              -- ILoString
   * ... this.rest.isDoubledListHelper(String str, ILoString listSoFar, int count) ... -- boolean
   * 
   */

  // combine all Strings in this list into one
  public String combine() {
    return this.first.concat(this.rest.combine());
  }
  
  // produce a new list with the list of Strings in alphabetical order
  public ILoString sort() {
    return this.rest.sort().insert(this.first);
  }
  
  // insert the given String into a list of Strings
  public ILoString insert(String str) {
    if (this.first.compareToIgnoreCase(str) >= 0) {
      return new ConsLoString(str, this);
    }
    else {
      return new ConsLoString(this.first, this.rest.insert(str));
    }
  }
  
  // determine whether a list of Strings is sorted in alphabetical order
  public boolean isSorted() {
    return this.rest.isSortedHelper(this.first);
  }
  
  // determine where a String should go in a list of Strings according to in alphabetical order
  public boolean isSortedHelper(String str) {
    return (str.compareToIgnoreCase(this.first) <= 0)
        && this.isSorted();
  }
  
  // combines two lists of Strings by alternating their elements
  public ILoString interleave(ILoString other) {
    return new ConsLoString(this.first, other.interleave(this.rest));
  }
  
  // combine two sorted lists of Strings into a new sorted list of Strings
  public ILoString merge(ILoString other) {
    return this.mergeHelper(other);
  }
  
  // combine two sorted lists of Strings into a new sorted list of Strings
  public ILoString mergeHelper(ILoString listSoFar) {
    return this.rest.mergeHelper(listSoFar.insert(this.first));
  } 
  
  // produce a new list of Strings with the list in reverse order
  public ILoString reverse() {
    return this.reverseHelper(new MtLoString());
  }
  
  // produce a new list of Strings with the list in reverse order
  public ILoString reverseHelper(ILoString listSoFar) {
    return this.rest.reverseHelper(new ConsLoString(this.first, listSoFar));
  }
  
  // determine if this list contains pairs of identical Strings
  public boolean isDoubledList() {
    return this.rest.isDoubledListHelper(this.first, this.rest, 0);
  }
  
  // determine if this list contains pairs of identical Strings
  public boolean isDoubledListHelper(String str, ILoString listSoFar, int count) {
    if (count % 2 == 0) {
      return str.equals(this.first)
          && this.rest.isDoubledListHelper(this.first, this.rest, count + 1);
    }
    else {
      return this.rest.isDoubledListHelper(this.first, this.rest, count + 1);
    }
  }
  
  // determine whether this list contains the same words reading the list in either order
  public boolean isPalindromeList() {
    return this.interleave(this.reverse()).isDoubledList();
  }
} 

// to represent examples for lists of strings
class ExamplesStrings {
 
  ILoString empty = new MtLoString();
  ILoString mary = new ConsLoString("Mary ", new ConsLoString("had ", new ConsLoString("a ",
      new ConsLoString("little ", new ConsLoString("lamb.", new MtLoString())))));
  ILoString abcd = new ConsLoString("d", new ConsLoString("b", new ConsLoString("a",
      new ConsLoString("c", new MtLoString()))));
  ILoString acegSorted = new ConsLoString("a", new ConsLoString("c", new ConsLoString("e",
      new ConsLoString("g", new MtLoString()))));
  ILoString bdfhSorted = new ConsLoString("b", new ConsLoString("d", new ConsLoString("f",
      new ConsLoString("h", new MtLoString()))));
  ILoString doubledList = new ConsLoString("hi", new ConsLoString("hi", new ConsLoString("hello",
      new ConsLoString("hello", new ConsLoString("world", new ConsLoString("world",
          new MtLoString()))))));
  ILoString palindrome = new ConsLoString("hello", new ConsLoString("it", new ConsLoString("is",
      new ConsLoString("it", new ConsLoString("hello", new MtLoString())))));
  ILoString helloWorld = new ConsLoString("hello", new ConsLoString("world", new MtLoString()));
  ILoString sortedFruits = new ConsLoString("apple", new ConsLoString("banana",
      new ConsLoString("grape", new MtLoString())));
  ILoString sortedItems = new ConsLoString("banana", new ConsLoString("car",
      new ConsLoString("keys", new MtLoString())));
  ILoString falseDoubledList = new ConsLoString("apple", new ConsLoString("boy",
      new ConsLoString("boy", new ConsLoString("apple", new ConsLoString("cat",
          new ConsLoString("cat", new MtLoString()))))));
  ILoString palindromeTwo = new ConsLoString("i", new ConsLoString("love", new ConsLoString("cows",
      new ConsLoString("love", new ConsLoString("i", new MtLoString())))));
  ILoString alphabet = new ConsLoString("a", new ConsLoString("b", new ConsLoString("d",
      new ConsLoString("e", new ConsLoString("e", new ConsLoString("d", new ConsLoString("c",
          new ConsLoString("b", new ConsLoString("a", new MtLoString())))))))));

  // test the method combine for the lists of Strings
  boolean testCombine(Tester t) {
    return t.checkExpect(this.mary.combine(), "Mary had a little lamb.")
        && t.checkExpect(this.abcd.combine(), "dbac")
        && t.checkExpect(this.acegSorted.combine(), "aceg")
        && t.checkExpect(this.bdfhSorted.combine(), "bdfh")
        && t.checkExpect(this.empty.combine(), "");
  }
  
  // test the method sort for the lists of Strings
  boolean testSort(Tester t) {
    return t.checkExpect(this.mary.sort(), new ConsLoString("a ", new ConsLoString("had ",
        new ConsLoString("lamb.", new ConsLoString("little ", new ConsLoString("Mary ",
            new MtLoString()))))))
        && t.checkExpect(this.abcd.sort(), new ConsLoString("a", new ConsLoString("b",
            new ConsLoString("c", new ConsLoString("d", new MtLoString())))))
        && t.checkExpect(this.acegSorted.sort(), this.acegSorted)
        && t.checkExpect(this.empty.sort(), this.empty);
  }
  
  // test the method isSorted for the lists of Strings
  boolean testIsSorted(Tester t) {
    return t.checkExpect(this.abcd.isSorted(), false)
        && t.checkExpect(this.acegSorted.isSorted(), true)
        && t.checkExpect(this.bdfhSorted.isSorted(), true)
        && t.checkExpect(this.mary.isSorted(), false)
        && t.checkExpect(this.empty.isSorted(), true)
        && t.checkExpect(this.alphabet.isSorted(), false);
  }
  
  // test the helper method isSortedHelper for the lists of Strings
  boolean testIsSortedHelper(Tester t) {
    return t.checkExpect(this.acegSorted.isSortedHelper("b"), false)
        && t.checkExpect(this.acegSorted.isSortedHelper("a"), true)
        && t.checkExpect(this.mary.isSortedHelper("apple"), false)
        && t.checkExpect(this.mary.isSortedHelper("snowman"), false)
        && t.checkExpect(this.empty.isSortedHelper("cat"), true);
  }
  
  // test the method interleave for the lists of Strings
  boolean testInterleave(Tester t) {
    return t.checkExpect(this.mary.interleave(this.abcd),
        new ConsLoString("Mary ", new ConsLoString("d", new ConsLoString("had ", 
            new ConsLoString("b", new ConsLoString("a ", new ConsLoString("a",
                new ConsLoString("little ", new ConsLoString("c", new ConsLoString("lamb.",
                    new MtLoString()))))))))))
        && t.checkExpect(this.acegSorted.interleave(this.bdfhSorted), new ConsLoString("a",
            new ConsLoString("b", new ConsLoString("c", new ConsLoString("d",
                new ConsLoString("e", new ConsLoString("f", new ConsLoString("g",
                    new ConsLoString("h", new MtLoString())))))))))
        && t.checkExpect(this.helloWorld.interleave(this.acegSorted), new ConsLoString("hello",
            new ConsLoString("a", new ConsLoString("world", new ConsLoString("c",
                new ConsLoString("e", new ConsLoString("g", new MtLoString())))))))
        && t.checkExpect(this.empty.interleave(this.sortedFruits), this.sortedFruits);
  }
  
  // test the method merge for the lists of Strings
  boolean testMerge(Tester t) {
    return t.checkExpect(this.acegSorted.merge(this.bdfhSorted), new ConsLoString("a", 
        new ConsLoString("b", new ConsLoString("c", new ConsLoString("d", new ConsLoString("e",
            new ConsLoString("f", new ConsLoString("g", new ConsLoString("h",
                new MtLoString())))))))))
        && t.checkExpect(this.sortedFruits.merge(this.sortedItems), new ConsLoString("apple",
            new ConsLoString("banana", new ConsLoString("banana", new ConsLoString("car",
                new ConsLoString("grape", new ConsLoString("keys", new MtLoString())))))))
        && t.checkExpect(this.helloWorld.merge(this.acegSorted), new ConsLoString("a",
            new ConsLoString("c", new ConsLoString("e", new ConsLoString("g",
                new ConsLoString("hello", new ConsLoString("world", new MtLoString())))))))
        && t.checkExpect(this.empty.merge(this.sortedFruits), this.sortedFruits);
  }
  
  // test the helper method mergeHelper for the lists of Strings
  boolean testMergeHelper(Tester t) {
    return t.checkExpect(this.acegSorted.mergeHelper(this.bdfhSorted), new ConsLoString("a", 
        new ConsLoString("b", new ConsLoString("c", new ConsLoString("d", new ConsLoString("e",
            new ConsLoString("f", new ConsLoString("g", new ConsLoString("h",
                new MtLoString())))))))))
        && t.checkExpect(this.sortedFruits.mergeHelper(this.sortedItems), new ConsLoString("apple",
            new ConsLoString("banana", new ConsLoString("banana", new ConsLoString("car",
                new ConsLoString("grape", new ConsLoString("keys", new MtLoString())))))))
        && t.checkExpect(this.helloWorld.mergeHelper(this.acegSorted), new ConsLoString("a",
            new ConsLoString("c", new ConsLoString("e", new ConsLoString("g",
                new ConsLoString("hello", new ConsLoString("world", new MtLoString())))))))
        && t.checkExpect(this.empty.mergeHelper(this.sortedItems), this.sortedItems);
  }
  
  // test the method reverse for the lists of Strings
  boolean testReverse(Tester t) {
    return t.checkExpect(this.mary.reverse(), new ConsLoString("lamb.", new ConsLoString("little ",
        new ConsLoString("a ", new ConsLoString("had ",
            new ConsLoString("Mary ", new MtLoString()))))))
        && t.checkExpect(this.acegSorted.reverse(), new ConsLoString("g", new ConsLoString("e", 
            new ConsLoString("c", new ConsLoString("a", new MtLoString())))))
        && t.checkExpect(this.palindrome.reverse(), this.palindrome)
        && t.checkExpect(this.doubledList.reverse(), new ConsLoString("world",
            new ConsLoString("world", new ConsLoString("hello", new ConsLoString("hello",
                new ConsLoString("hi", new ConsLoString("hi", new MtLoString())))))))
        && t.checkExpect(this.empty.reverse(), this.empty);
  }
  
  // test the helper method reverseHelper for the lists of Strings
  boolean testReverseHelper(Tester t) {
    return t.checkExpect(this.mary.reverseHelper(this.helloWorld), new ConsLoString("lamb.",
        new ConsLoString("little ", new ConsLoString("a ", new ConsLoString("had ",
            new ConsLoString("Mary ", this.helloWorld))))))
        && t.checkExpect(this.sortedFruits.reverseHelper(new MtLoString()),
            new ConsLoString("grape", new ConsLoString("banana", new ConsLoString("apple",
                new MtLoString()))))
        && t.checkExpect(this.empty.reverseHelper(this.sortedItems), new ConsLoString("banana",
            new ConsLoString("car", new ConsLoString("keys", new MtLoString()))));
  }
  
  // test the method isDoubledList for the lists of Strings
  boolean testIsDoubledList(Tester t) {
    return t.checkExpect(this.mary.isDoubledList(), false)
        && t.checkExpect(this.abcd.isDoubledList(), false)
        && t.checkExpect(this.doubledList.isDoubledList(), true)
        && t.checkExpect(this.falseDoubledList.isDoubledList(), false)
        && t.checkExpect(this.empty.isDoubledList(), true);
  }
  
  // test the helper method isDoubledListHelper for the lists of Strings
  boolean testIsDoubledListHelper(Tester t) {
    return t.checkExpect(new ConsLoString("mary",
        new MtLoString()).isDoubledListHelper("mary", this.mary, 2), true)
        && t.checkExpect(this.sortedFruits.isDoubledListHelper("apple", this.sortedFruits, 0),
            false)
        && t.checkExpect(this.empty.isDoubledListHelper("dog", this.sortedItems, 1), true);
  }
  
  // test the method isPalindromeList for the lists of Strings
  boolean testIsPalindromeList(Tester t) {
    return t.checkExpect(this.palindrome.isPalindromeList(), true)
        && t.checkExpect(this.mary.isPalindromeList(), false)
        && t.checkExpect(this.palindromeTwo.isPalindromeList(), true)
        && t.checkExpect(this.empty.isPalindromeList(), true);
  }
}