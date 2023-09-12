import java.util.*;
import tester.*;

/**
 * A class that defines a new permutation code, as well as methods for encoding
 * and decoding of the messages that use this code.
 */
class PermutationCode {
  // The original list of characters to be encoded
  ArrayList<Character> alphabet = new ArrayList<Character>(
      Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
          'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

  ArrayList<Character> code = new ArrayList<Character>(26);

  // A random number generator
  Random rand = new Random();

  // Create a new instance of the encoder/decoder with a new permutation code
  PermutationCode() {
    this.code = this.initEncoder();
  }

  // Create a new instance of the encoder/decoder with the given code
  PermutationCode(ArrayList<Character> code) {
    this.code = code;
  }

  // Initialize the encoding permutation of the characters
  ArrayList<Character> initEncoder() {
    ArrayList<Character> result = new ArrayList<Character>();
    ArrayList<Character> temp = new ArrayList<Character>();
    for (Character c : this.alphabet) {
      temp.add(c);
    }
    while (temp.size() > 0) {
      int r = this.rand.nextInt(temp.size());
      result.add(temp.remove(r));
    }
    return result;
  }

  // produce an encoded String from the given String
  String encode(String source) {
    int i = source.length() - 1;
    String result = "";
    while (i > -1) {
      result = this.code.get(this.alphabet.indexOf(source.charAt(i))) + result;
      i--;
    }
    return result;
  }

  // produce a decoded String from the given String
  String decode(String code) {
    int i = code.length() - 1;
    String result = "";
    while (i > -1) {
      result = this.alphabet.get(this.code.indexOf(code.charAt(i))) + result;
      i--;
    }
    return result;
  }
}


class ExamplesCode {
  ArrayList<Character> code = 
          new ArrayList<Character>(Arrays.asList(
                      'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 
                      'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 
                      'z', 'x', 'c', 'v', 'b', 'n', 'm'));
  PermutationCode p1 = new PermutationCode(code);
  PermutationCode p2 = new PermutationCode();
  
  void test(Tester t) {
      t.checkExpect(p1.encode("hello"), "itssg");
      t.checkExpect(p1.decode("itssg"), "hello");
      t.checkExpect(p1.encode("howudo"), "igvxrg");
      t.checkExpect(p1.decode("igvxrg"), "howudo");
      t.checkExpect(p2.alphabet.size(), p2.initEncoder().size());
  }
}