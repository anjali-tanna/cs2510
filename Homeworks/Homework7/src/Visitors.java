import tester.Tester;


interface IArith {
  // returns the result of applying the given visitor to this IArith
  <R> R accept(IArithVisitor<R> visitor);
}

class Const implements IArith {
  double num;
  
  Const(double num) {
    this.num = num;
  }
  
  // returns the result of applying the given visitor to this Const
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConst(this);
  }
}

class Formula implements IArith {
  IFunc2<Double, Double, Double> fun;
  String name;
  IArith left;
  IArith right;
  
  Formula(IFunc2<Double, Double, Double> fun, String name, IArith left, IArith right) {
    this.fun = fun;
    this.name = name;
    this.left = left;
    this.right = right;
  }
  
  // returns the result of applying the given visitor to this Formula
  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitFormula(this);
  }
}

interface IFunc<A, R> {
  R apply(A arg);
}

interface IFunc2<A1, A2, R> {
  R apply(A1 arg1, A2 arg2);
}

// implements a function taking two Doubles and returning a Double, that sums the Doubles
class Add implements IFunc2<Double, Double, Double> {
  public Double apply(Double d1, Double d2) {
    return d1 + d2;
  }
}

// implements a function taking two Doubles and returning a Double, that subtracts the Doubles
class Subtract implements IFunc2<Double, Double, Double> {
  public Double apply(Double d1, Double d2) {
    return d1 - d2;
  }
}

// implements a function taking two Doubles and returning a Double, that multiples the Doubles
class Multiply implements IFunc2<Double, Double, Double> {
  public Double apply(Double d1, Double d2) {
    return d1 * d2;
  }
}

// implements a function taking two Doubles and returning a Double, that divides the Doubles
class Divide implements IFunc2<Double, Double, Double> {
  public Double apply(Double d1, Double d2) {
    return d1 / d2;
  }
}

// An IArithVisitor is a function over IAriths
interface IArithVisitor<R> extends IFunc<IArith, R> {
  // visiting the Const class
  R visitConst(Const arith);
  
  // visiting the Formula class
  R visitFormula(Formula arith);
}

// EvalVisitor is a function object over IAriths that evaluates the tree to a Double answer
class EvalVisitor implements IArithVisitor<Double> {
  // from the IFunc interface:
  public Double apply(IArith arith) {
    return arith.accept(this);
  }
  
  // evaluates the value of the Constant
  public Double visitConst(Const arith) {
    return arith.num; 
  }
  
  // evaluates the tree to a Double answer
  public Double visitFormula(Formula arith) {
    return arith.fun.apply(this.apply(arith.left), this.apply(arith.right));
  }
}

// PrintVisitor is a function object over IAriths that produces a String showing its expression
class PrintVisitor implements IArithVisitor<String> {
  // from the IFunc interface:
  public String apply(IArith arith) {
    return arith.accept(this);
  }
  
  // produces the given Constant's value as a String
  public String visitConst(Const arith) {
    return Double.toString(arith.num);
  }
  
  // produces a String that shows the given tree as an expression
  public String visitFormula(Formula arith) {
    return "(" + arith.name + " " + this.apply(arith.left) + " " + this.apply(arith.right) + ")";
  }
}

// DoublerVisitor is a function object over IAriths that produces
// another IArith where every Const in the tree is doubled
class DoublerVisitor implements IArithVisitor<IArith> {
  // from the IFunc interface:
  public IArith apply(IArith arith) {
    return arith.accept(this);
  }
  
  // doubles the value of the given Constant
  public IArith visitConst(Const arith) {
    return new Const(arith.num * 2);
  }
  
  // doubles the value of every Constant in the given tree
  public IArith visitFormula(Formula arith) {
    return new Formula(arith.fun, arith.name, this.apply(arith.left), this.apply(arith.right));
  }
}

// AllSmallVisitor is a function object over IAriths that returns
// true if every every constant in the tree is less than 10
class AllSmallVisitor implements IArithVisitor<Boolean> {
  // from the IFunc interface:
  public Boolean apply(IArith arith) {
    return arith.accept(this);
  }
  
  // returns true if the given Constant's value is less than 10
  public Boolean visitConst(Const arith) {
    return arith.num < 10;
  }
  
  // returns true if every Constant in the given tree is less than 10
  public Boolean visitFormula(Formula arith) {
    return this.apply(arith.left) && this.apply(arith.right);
  }
}

// examples and tests for IArith and related classes/interfaces
class ExamplesArith {
  ExamplesArith() {
  }
  
  IFunc2<Double, Double, Double> add = new Add();
  IFunc2<Double, Double, Double> subtract = new Subtract();
  IFunc2<Double, Double, Double> multiply = new Multiply();
  IFunc2<Double, Double, Double> divide = new Divide();
  Const negTwo = new Const(-2.0);
  Const zero = new Const(0.0);
  Const one = new Const(1.0);
  Const two = new Const(2.2);
  Const three = new Const(3.5);
  Const twelve = new Const(12.0);
  IArith negTwoArith = this.negTwo;
  IArith zeroArith = this.zero;
  IArith oneArith = this.one;
  IArith twoArith = this.two;
  IArith threeArith = this.three;
  IArith twelveArith = this.twelve;
  Formula formulaOne = new Formula(this.add, "plus", this.oneArith, this.zeroArith);
  Formula formulaTwo = new Formula(this.subtract, "minus", this.twelveArith, this.threeArith);
  Formula formulaThree = new Formula(this.divide, "div", this.twoArith, this.zeroArith);
  Formula formulaFour = new Formula(this.subtract, "minus", this.formulaOne, this.formulaTwo);
  Formula formulaFive = new Formula(this.divide, "div", this.formulaFour, this.formulaFour);
  Formula formulaSix = new Formula(this.multiply, "times", this.formulaFour, this.twelveArith);
  IArith formulaOneArith = this.formulaOne;
  IArith formulaTwoArith = this.formulaTwo;
  IArith formulaThreeArith = this.formulaThree;
  IArith formulaFourArith = this.formulaFour;
  IArith formulaFiveArith = this.formulaFive;
  IArith formulaSixArith = this.formulaSix;
  IArithVisitor<Double> evalVisitor = new EvalVisitor();
  IArithVisitor<String> printVisitor = new PrintVisitor();
  IArithVisitor<IArith> doublerVisitor = new DoublerVisitor();
  IArithVisitor<Boolean> allSmallVisitor = new AllSmallVisitor();
  
  // tests the apply method for EvalVisitor
  boolean testApplyEvalVisitor(Tester t) {
    return t.checkExpect(this.evalVisitor.apply(this.negTwoArith), -2.0)
        && t.checkExpect(this.evalVisitor.apply(this.zeroArith), 0.0)
        && t.checkExpect(this.evalVisitor.apply(this.oneArith), 1.0)
        && t.checkExpect(this.evalVisitor.apply(this.twoArith), 2.2)
        && t.checkExpect(this.evalVisitor.apply(this.threeArith), 3.5)
        && t.checkExpect(this.evalVisitor.apply(this.twelveArith), 12.0)
        && t.checkExpect(this.evalVisitor.apply(this.formulaOneArith), 1.0)
        && t.checkExpect(this.evalVisitor.apply(this.formulaTwoArith), 8.5)
        && t.checkExpect(this.evalVisitor.apply(this.formulaThreeArith).isInfinite(), true)
        && t.checkExpect(this.evalVisitor.apply(this.formulaFourArith), -7.5)
        && t.checkExpect(this.evalVisitor.apply(this.formulaFiveArith), 1.0)
        && t.checkExpect(this.evalVisitor.apply(this.formulaSixArith), -90.0);
  }
  
  // tests the visitConst method for EvalVisitor
  boolean testVisitConstEvalVisitor(Tester t) {
    return t.checkExpect(this.evalVisitor.visitConst(this.negTwo), -2.0)
        && t.checkExpect(this.evalVisitor.visitConst(this.zero), 0.0)
        && t.checkExpect(this.evalVisitor.visitConst(this.one), 1.0)
        && t.checkExpect(this.evalVisitor.visitConst(this.two), 2.2)
        && t.checkExpect(this.evalVisitor.visitConst(this.three), 3.5)
        && t.checkExpect(this.evalVisitor.visitConst(this.twelve), 12.0);
  }
  
  // tests the visitFormula method for EvalVisitor
  boolean testVisitFormulaEvalVisitor(Tester t) {
    return t.checkExpect(this.evalVisitor.visitFormula(this.formulaOne), 1.0)
        && t.checkExpect(this.evalVisitor.visitFormula(this.formulaTwo), 8.5)
        && t.checkExpect(this.evalVisitor.visitFormula(this.formulaThree).isInfinite(), true)
        && t.checkExpect(this.evalVisitor.visitFormula(this.formulaFour), -7.5)
        && t.checkExpect(this.evalVisitor.visitFormula(this.formulaFive), 1.0)
        && t.checkExpect(this.evalVisitor.visitFormula(this.formulaSix), -90.0);
  }
  
  // tests the apply method for PrintVisitor
  boolean testApplyPrintVisitor(Tester t) {
    return t.checkExpect(this.printVisitor.apply(this.negTwoArith), "-2.0")
        && t.checkExpect(this.printVisitor.apply(this.zeroArith), "0.0")
        && t.checkExpect(this.printVisitor.apply(this.oneArith), "1.0")
        && t.checkExpect(this.printVisitor.apply(this.twoArith), "2.2")
        && t.checkExpect(this.printVisitor.apply(this.threeArith), "3.5")
        && t.checkExpect(this.printVisitor.apply(this.twelveArith), "12.0")
        && t.checkExpect(this.printVisitor.apply(this.formulaOneArith), "(plus 1.0 0.0)")
        && t.checkExpect(this.printVisitor.apply(this.formulaTwoArith), "(minus 12.0 3.5)")
        && t.checkExpect(this.printVisitor.apply(this.formulaThreeArith), "(div 2.2 0.0)")
        && t.checkExpect(this.printVisitor.apply(this.formulaFourArith),
            "(minus (plus 1.0 0.0) (minus 12.0 3.5))")
        && t.checkExpect(this.printVisitor.apply(this.formulaFiveArith),
            "(div (minus (plus 1.0 0.0) (minus 12.0 3.5)) (minus (plus 1.0 0.0) (minus 12.0 3.5)))")
        && t.checkExpect(this.printVisitor.apply(this.formulaSixArith),
            "(times (minus (plus 1.0 0.0) (minus 12.0 3.5)) 12.0)");
  }
  
  // tests the visitConst method for PrintVisitor
  boolean testVisitConstPrintVisitor(Tester t) {
    return t.checkExpect(this.printVisitor.visitConst(this.negTwo), "-2.0")
        && t.checkExpect(this.printVisitor.visitConst(this.zero), "0.0")
        && t.checkExpect(this.printVisitor.visitConst(this.one), "1.0")
        && t.checkExpect(this.printVisitor.visitConst(this.two), "2.2")
        && t.checkExpect(this.printVisitor.visitConst(this.three), "3.5")
        && t.checkExpect(this.printVisitor.visitConst(this.twelve), "12.0");
  }
  
  // tests the visitFormula method for PrintVisitor
  boolean testVisitFormulaPrintVisitor(Tester t) {
    return t.checkExpect(this.printVisitor.visitFormula(this.formulaOne), "(plus 1.0 0.0)")
        && t.checkExpect(this.printVisitor.visitFormula(this.formulaTwo), "(minus 12.0 3.5)")
        && t.checkExpect(this.printVisitor.visitFormula(this.formulaThree), "(div 2.2 0.0)")
        && t.checkExpect(this.printVisitor.visitFormula(this.formulaFour),
            "(minus (plus 1.0 0.0) (minus 12.0 3.5))")
        && t.checkExpect(this.printVisitor.visitFormula(this.formulaFive),
            "(div (minus (plus 1.0 0.0) (minus 12.0 3.5)) (minus (plus 1.0 0.0) (minus 12.0 3.5)))")
        && t.checkExpect(this.printVisitor.visitFormula(this.formulaSix),
            "(times (minus (plus 1.0 0.0) (minus 12.0 3.5)) 12.0)");
  }
  
  // tests the apply method for DoublerVisitor
  boolean testApplyDoublerVisitor(Tester t) {
    return t.checkExpect(this.doublerVisitor.apply(this.negTwoArith), new Const(-4.0))
        && t.checkExpect(this.doublerVisitor.apply(this.zeroArith), new Const(0.0))
        && t.checkExpect(this.doublerVisitor.apply(this.oneArith), new Const(2.0))
        && t.checkExpect(this.doublerVisitor.apply(this.twoArith), new Const(4.4))
        && t.checkExpect(this.doublerVisitor.apply(this.threeArith), new Const(7.0))
        && t.checkExpect(this.doublerVisitor.apply(this.twelveArith), new Const(24.0))
        && t.checkExpect(this.doublerVisitor.apply(this.formulaOneArith),
            new Formula(this.add, "plus", new Const(2.0), new Const(0.0)))
        && t.checkExpect(this.doublerVisitor.apply(this.formulaTwoArith),
            new Formula(this.subtract, "minus", new Const(24.0), new Const(7.0)))
        && t.checkExpect(this.doublerVisitor.apply(this.formulaThreeArith),
            new Formula(this.divide, "div", new Const(4.4), new Const(0.0)))
        && t.checkExpect(this.doublerVisitor.apply(this.formulaFourArith),
            new Formula(this.subtract, "minus",
                new Formula(this.add, "plus", new Const(2.0), new Const(0.0)),
                new Formula(this.subtract, "minus", new Const(24.0), new Const(7.0))))
        && t.checkExpect(this.doublerVisitor.apply(this.formulaFiveArith), 
            new Formula(this.divide, "div",
                new Formula(this.subtract, "minus",
                    new Formula(this.add, "plus", new Const(2.0), new Const(0.0)),
                    new Formula(this.subtract, "minus", new Const(24.0), new Const(7.0))),
                new Formula(this.subtract, "minus",
                    new Formula(this.add, "plus", new Const(2.0), new Const(0.0)),
                    new Formula(this.subtract, "minus", new Const(24.0), new Const(7.0)))))
        && t.checkExpect(this.doublerVisitor.apply(this.formulaSixArith),
            new Formula(this.multiply, "times",
                new Formula(this.subtract, "minus",
                    new Formula(this.add, "plus", new Const(2.0), new Const(0.0)),
                    new Formula(this.subtract, "minus", new Const(24.0), new Const(7.0))),
                new Const(24.0)));
  }
  
  // tests the visitConst method for DoublerVisitor
  boolean testVisitConstDoublerVisitor(Tester t) {
    return t.checkExpect(this.doublerVisitor.visitConst(this.negTwo), new Const(-4.0))
        && t.checkExpect(this.doublerVisitor.visitConst(this.zero), new Const(0.0))
        && t.checkExpect(this.doublerVisitor.visitConst(this.one), new Const(2.0))
        && t.checkExpect(this.doublerVisitor.visitConst(this.two), new Const(4.4))
        && t.checkExpect(this.doublerVisitor.visitConst(this.three), new Const(7.0))
        && t.checkExpect(this.doublerVisitor.visitConst(this.twelve), new Const(24.0));
  }
  
  // tests the visitFormula method for DoublerVisitor
  boolean testVisitFormulaDoublerVisitor(Tester t) {
    return t.checkExpect(this.doublerVisitor.visitFormula(this.formulaOne),
        new Formula(this.add, "plus", new Const(2.0), new Const(0.0)))
        && t.checkExpect(this.doublerVisitor.visitFormula(this.formulaTwo),
            new Formula(this.subtract, "minus", new Const(24.0), new Const(7.0)))
        && t.checkExpect(this.doublerVisitor.visitFormula(this.formulaThree),
            new Formula(this.divide, "div", new Const(4.4), new Const(0.0)))
        && t.checkExpect(this.doublerVisitor.visitFormula(this.formulaFour),
            new Formula(this.subtract, "minus",
                new Formula(this.add, "plus", new Const(2.0), new Const(0.0)),
                new Formula(this.subtract, "minus", new Const(24.0), new Const(7.0))))
        && t.checkExpect(this.doublerVisitor.visitFormula(this.formulaFive), 
            new Formula(this.divide, "div",
                new Formula(this.subtract, "minus",
                    new Formula(this.add, "plus", new Const(2.0), new Const(0.0)),
                    new Formula(this.subtract, "minus", new Const(24.0), new Const(7.0))),
                new Formula(this.subtract, "minus",
                    new Formula(this.add, "plus", new Const(2.0), new Const(0.0)),
                    new Formula(this.subtract, "minus", new Const(24.0), new Const(7.0)))))
        && t.checkExpect(this.doublerVisitor.visitFormula(this.formulaSix),
            new Formula(this.multiply, "times",
                new Formula(this.subtract, "minus",
                    new Formula(this.add, "plus", new Const(2.0), new Const(0.0)),
                    new Formula(this.subtract, "minus", new Const(24.0), new Const(7.0))),
                new Const(24.0)));
  }
  
  // tests the apply method for AllSmallVisitor
  boolean testApplyAllSmallVisitor(Tester t) {
    return t.checkExpect(this.allSmallVisitor.apply(this.negTwoArith), true)
        && t.checkExpect(this.allSmallVisitor.apply(this.zeroArith), true)
        && t.checkExpect(this.allSmallVisitor.apply(this.oneArith), true)
        && t.checkExpect(this.allSmallVisitor.apply(this.twoArith), true)
        && t.checkExpect(this.allSmallVisitor.apply(this.threeArith), true)
        && t.checkExpect(this.allSmallVisitor.apply(this.twelveArith), false)
        && t.checkExpect(this.allSmallVisitor.apply(this.formulaOneArith), true)
        && t.checkExpect(this.allSmallVisitor.apply(this.formulaTwoArith), false)
        && t.checkExpect(this.allSmallVisitor.apply(this.formulaThreeArith), true)
        && t.checkExpect(this.allSmallVisitor.apply(this.formulaFourArith), false)
        && t.checkExpect(this.allSmallVisitor.apply(this.formulaFiveArith), false)
        && t.checkExpect(this.allSmallVisitor.apply(this.formulaSixArith), false);
  }
  
  // tests the visitConst method for AllSmallVisitor
  boolean testVisitConstAllSmallVisitor(Tester t) {
    return t.checkExpect(this.allSmallVisitor.visitConst(this.negTwo), true)
        && t.checkExpect(this.allSmallVisitor.visitConst(this.zero), true)
        && t.checkExpect(this.allSmallVisitor.visitConst(this.one), true)
        && t.checkExpect(this.allSmallVisitor.visitConst(this.two), true)
        && t.checkExpect(this.allSmallVisitor.visitConst(this.three), true)
        && t.checkExpect(this.allSmallVisitor.visitConst(this.twelve), false);
  }
  
  // tests the visitFormula method for AllSmallVisitor
  boolean testVisitFormulaAllSmallVisitor(Tester t) {
    return t.checkExpect(this.allSmallVisitor.visitFormula(this.formulaOne), true)
        && t.checkExpect(this.allSmallVisitor.visitFormula(this.formulaTwo), false)
        && t.checkExpect(this.allSmallVisitor.visitFormula(this.formulaThree), true)
        && t.checkExpect(this.allSmallVisitor.visitFormula(this.formulaFour), false)
        && t.checkExpect(this.allSmallVisitor.visitFormula(this.formulaFive), false)
        && t.checkExpect(this.allSmallVisitor.visitFormula(this.formulaSix), false);
  }
}