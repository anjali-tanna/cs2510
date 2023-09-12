import tester.*;
import java.util.function.BiFunction;

interface IList<T> {
  // adds the given element to this list
  public IList<T> updateList(T item);
  
  // returns true if this list contains the given element
  public boolean contains(T item);
  
  // returns true if any element in this list is present in the given one
  public boolean anySame(IList<T> list);
  
  // performs the given function on the list and returns a single value
  <U> U foldr(BiFunction<T, U, U> func, U base);
  
  // combines this list with the given list
  public IList<T> combine(IList<T> list);
  
  // count the number of times that the given item is present in this list
  public int count(T item);
}

class MtList<T> implements IList<T> {
  // adds the given element to this list
  public IList<T> updateList(T item) {
    return new ConsList<T>(item, new MtList<T>());
  }
  
  // returns true if this list contains the given element
  public boolean contains(T item) {
    return false;
  }
  
  // returns true if any element in this list is present in the given one
  public boolean anySame(IList<T> c) {
    return false;
  }
  
  // performs the given function on the list and returns a single value
  public <U> U foldr(BiFunction<T, U, U> func, U base) {
    return base;
  }
  
  // combines this list with the given list
  public IList<T> combine(IList<T> list) {
    return list;
  }
  
  // count the number of times that the given item is present in this list
  public int count(T item) {
    return 0;
  }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;
  
  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  
  // adds the given element to this list
  public IList<T> updateList(T item) {
    return new ConsList<T>(item, this);
  }
  
  // returns true if this list contains the given element
  public boolean contains(T item) {
    return this.first.equals(item) || this.rest.contains(item);
  }
  
  // returns true if any element in this list is present in the given one
  public boolean anySame(IList<T> list) {
    return list.contains(this.first) || this.rest.anySame(list);
  }
  
  // performs the given function on the list and returns a single value
  public <U> U foldr(BiFunction<T, U, U> func, U base) {
    return func.apply(this.first,
                      this.rest.foldr(func, base));
  }
  
  // combines this list with the given list
  public IList<T> combine(IList<T> list) {
    return new ConsList<T>(this.first, list.combine(this.rest));
  }
  
  // count the number of times that the given item is present in this list
  public int count(T item) {
    if (this.first.equals(item)) {
      return 1 + this.rest.count(item);
    }
    else {
      return this.rest.count(item);
    }
  }
}

// represents a student with a name, id number, and courses they're taking
class Student {
  String name;
  int id;
  IList<Course> courses;
  
  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new MtList<Course>();
  }
  
  // EFFECT: modifies this student's list of courses to include the given one
  // and modifies the course's list of students to include this student
  void enroll(Course c) {
    this.courses = new ConsList<Course>(c, this.courses);
    c.updateStudents(this);
  }
  
  // returns true if the given student is in any of the same classes as this student
  public boolean classmates(Student c) {
    return c.classmatesHelper(this.courses);
  }
  
  // returns true if any of the courses in this list are present in the given list
  public boolean classmatesHelper(IList<Course> courses) {
    return this.courses.anySame(courses);
  }
}

// represents an instructor with a name and courses they're teaching
class Instructor {
  String name;
  IList<Course> courses;
  
  Instructor(String name) {
    this.name = name;
    this.courses = new MtList<Course>();
  }
  
  // EFFECT: modifies the instructor's list of courses to include the given course
  void updateCourses(Course c) {
    this.courses = new ConsList<Course>(c, this.courses);
  }
  
  // returns true if the given student is in more than one of this instructor's courses
  public boolean dejavu(Student c) {
    return this.dejavuHelper().count(c) > 1;
  }
  
  // combines the lists of students in all of this instructor's courses into one list
  public IList<Student> dejavuHelper() {
    return this.courses.foldr(new CombineLists(), new MtList<Student>());
  }
}

// implements a function taking a Course and an IList<Student> and returning an IList<Student,
// combines the course's list of students to the given one
class CombineLists implements BiFunction<Course, IList<Student>, IList<Student>> {
  public IList<Student> apply(Course c1, IList<Student> s) {
    return c1.students.combine(s);
  }
}

// represents a course with a name, instructor, and a list of enrolled students
class Course {
  String name;
  Instructor prof;
  IList<Student> students;
  
  Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.students = new MtList<Student>();
    this.prof.updateCourses(this);
  }
  
  // EFFECT: modifies this course's list of students to include the given one
  void updateStudents(Student c) {
    this.students = new ConsList<Student>(c, this.students);
  }
  
  // returns true if the given student is enrolled in this course
  public boolean containsStudent(Student c) {
    return this.students.contains(c);
  }
}

class ExamplesIList {
  ExamplesIList() {
  }
  
  // examples of IList<T>
  IList<String> emptyString = new MtList<String>();
  IList<String> fruits = new ConsList<String>("apple", new ConsList<String>("banana",
      new ConsList<String>("orange", new MtList<String>())));
  IList<String> colors = new ConsList<String>("red", new ConsList<String>("orange",
      new ConsList<String>("green", new MtList<String>())));
  IList<String> utensils = new ConsList<String>("spoon", new ConsList<String>("fork",
      new ConsList<String>("knife", new MtList<String>())));
  IList<Integer> numbers = new ConsList<Integer>(1, new ConsList<Integer>(2,
      new ConsList<Integer>(1, new MtList<Integer>())));
  
  // tests the updateList method
  boolean testUpdateList(Tester t) {
    return t.checkExpect(this.emptyString.updateList("juice"),
        new ConsList<String>("juice", new MtList<String>()))
        && t.checkExpect(this.fruits.updateList("mango"),
            new ConsList<String>("mango", this.fruits))
        && t.checkExpect(this.colors.updateList("blue"),
            new ConsList<String>("blue", this.colors));
  }
  
  // tests the contains method
  boolean testContains(Tester t) {
    return t.checkExpect(this.emptyString.contains("apple"), false)
        && t.checkExpect(this.fruits.contains("apple"), true)
        && t.checkExpect(this.colors.contains("apple"), false)
        && t.checkExpect(this.utensils.contains("knife"), true);
  }
  
  // tests the anySame method
  boolean testAnySame(Tester t) {
    return t.checkExpect(this.fruits.anySame(this.colors), true)
        && t.checkExpect(this.colors.anySame(this.fruits), true)
        && t.checkExpect(this.fruits.anySame(this.utensils), false)
        && t.checkExpect(this.fruits.anySame(this.emptyString), false);
  }
  
  // tests the combine method
  boolean testCombine(Tester t) {
    return t.checkExpect(this.emptyString.combine(this.fruits), this.fruits)
        && t.checkExpect(this.colors.combine(this.colors), new ConsList<String>("red",
            new ConsList<String>("red", new ConsList<String>("orange",
                new ConsList<String>("orange", new ConsList<String>("green",
                    new ConsList<String>("green", new MtList<String>())))))))
        && t.checkExpect(this.utensils.combine(this.fruits), new ConsList<String>("spoon",
            new ConsList<String>("apple", new ConsList<String>("fork",
                new ConsList<String>("banana", new ConsList<String>("knife",
                    new ConsList<String>("orange", new MtList<String>())))))))
        && t.checkExpect(this.fruits.combine(this.emptyString), this.fruits);
  }
  
  // tests the count method
  boolean testCount(Tester t) {
    return t.checkExpect(this.fruits.count("apple"), 1)
        && t.checkExpect(this.numbers.count(1), 2)
        && t.checkExpect(this.emptyString.count("hi"), 0)
        && t.checkExpect(this.colors.count("blue"), 0);
  }
}

class ExamplesRegistrar {
  ExamplesRegistrar() {
  }
  
  // examples of students, instructors, and courses
  Student timmy = new Student("Timmy", 1234);
  Student max = new Student("Max", 4632);
  Student ruby = new Student("Ruby", 8327);
  Student abel = new Student("Abel", 1120);
  Student kelly = new Student("Kelly", 3280);
  Instructor amit = new Instructor("Amit");
  Instructor ben = new Instructor("Ben");
  Instructor jerry = new Instructor("Jerry");
  Course fundies = new Course("Fundies", this.amit);
  Course discrete = new Course("Discrete", this.ben);
  Course chemistry = new Course("Chemistry", this.jerry);
  Course biology = new Course("Biology", this.jerry);
  
  // EFFECT: sets up the initial conditions for our tests by reinitializing examples
  void initTestConditions() {
    this.timmy = new Student("Timmy", 1234);
    this.max = new Student("Max", 4632);
    this.ruby = new Student("Ruby", 8327);
    this.abel = new Student("Abel", 1120);
    this.kelly = new Student("Kelly", 3280);
    this.amit = new Instructor("Amit");
    this.ben = new Instructor("Ben");
    this.jerry = new Instructor("Jerry");
    this.fundies = new Course("Fundies", this.amit);
    this.discrete = new Course("Discrete", this.ben);
    this.chemistry = new Course("Chemistry", this.jerry);
    this.biology = new Course("Biology", this.jerry);
  }
  
  // tests the updateCourses method and makes sure that the courses an instructor teachers 
  // are in their list of courses when initialized
  boolean testUpdateCourses(Tester t) {
    this.initTestConditions();
    return t.checkExpect(this.amit.courses, new ConsList<Course>(this.fundies,
            new MtList<Course>()))
        && t.checkExpect(this.ben.courses, new ConsList<Course>(this.discrete,
            new MtList<Course>()))
        && t.checkExpect(this.jerry.courses, new ConsList<Course>(this.biology,
            new ConsList<Course>(this.chemistry, new MtList<Course>())));
  }
  
  // tests the enroll method
  boolean testEnroll(Tester t) {
    this.initTestConditions();
    this.timmy.enroll(this.fundies);
    boolean test1 = t.checkExpect(this.timmy.courses, new ConsList<Course>(this.fundies,
        new MtList<Course>()));
    boolean test2 = t.checkExpect(this.fundies.students, new ConsList<Student>(this.timmy,
        new MtList<Student>()));
    this.max.enroll(this.discrete);
    boolean test3 = t.checkExpect(this.max.courses, new ConsList<Course>(this.discrete,
        new MtList<Course>()));
    boolean test4 = t.checkExpect(this.discrete.students, new ConsList<Student>(this.max,
        new MtList<Student>()));
    this.max.enroll(this.fundies);
    boolean test5 = t.checkExpect(this.max.courses, new ConsList<Course>(this.fundies,
        new ConsList<Course>(this.discrete, new MtList<Course>())));
    boolean test6 = t.checkExpect(this.fundies.students, new ConsList<Student>(this.max,
        new ConsList<Student>(this.timmy, new MtList<Student>())));
    
    return test1 && test2 && test3 && test4 && test5 && test6;
  }
  
  // tests the classmates method
  boolean testClassmates(Tester t) {
    this.initTestConditions();
    this.timmy.enroll(this.fundies);
    boolean test1 = t.checkExpect(this.timmy.courses, new ConsList<Course>(this.fundies,
        new MtList<Course>()));
    this.max.enroll(this.fundies);
    boolean test2 = t.checkExpect(this.fundies.students, new ConsList<Student>(this.max,
        new ConsList<Student>(this.timmy, new MtList<Student>())));
    this.max.enroll(this.discrete);
    boolean test3 = t.checkExpect(this.max.courses, new ConsList<Course>(this.discrete,
        new ConsList<Course>(this.fundies, new MtList<Course>())));
    boolean test4 = t.checkExpect(this.discrete.students, new ConsList<Student>(this.max,
        new MtList<Student>()));
    this.ruby.enroll(this.discrete);
    boolean test5 = t.checkExpect(this.ruby.courses, new ConsList<Course>(this.discrete,
        new MtList<Course>()));
    
    return test1 && test2 && test3 && test4 && test5
        && t.checkExpect(this.timmy.classmates(this.max), true)
        && t.checkExpect(this.max.classmates(this.ruby), true)
        && t.checkExpect(this.timmy.classmates(this.ruby), false)
        && t.checkExpect(this.abel.classmates(this.kelly), false)
        && t.checkExpect(this.kelly.classmates(this.timmy), false);
  }
  
  // tests the classmatesHelper method
  boolean testClassMatesHelper(Tester t) {
    this.initTestConditions();
    this.timmy.enroll(this.fundies);
    boolean test1 = t.checkExpect(this.timmy.courses, new ConsList<Course>(this.fundies,
        new MtList<Course>()));
    this.kelly.enroll(this.fundies);
    boolean test2 = t.checkExpect(this.kelly.courses, new ConsList<Course>(this.fundies,
        new MtList<Course>()));
    boolean test3 = t.checkExpect(this.fundies.students, new ConsList<Student>(this.kelly,
        new ConsList<Student>(this.timmy, new MtList<Student>())));
    this.kelly.enroll(this.chemistry);
    boolean test4 = t.checkExpect(this.kelly.courses, new ConsList<Course>(this.chemistry,
        new ConsList<Course>(this.fundies, new MtList<Course>())));
    this.max.enroll(this.chemistry);
    boolean test5 = t.checkExpect(this.max.courses, new ConsList<Course>(this.chemistry, 
        new MtList<Course>()));
    boolean test6 = t.checkExpect(this.chemistry.students, new ConsList<Student>(this.max, 
        new ConsList<Student>(this.kelly, new MtList<Student>())));
    this.ruby.enroll(this.biology);
    boolean test7 = t.checkExpect(this.ruby.courses, new ConsList<Course>(this.biology, 
        new MtList<Course>()));
    
    return test1 && test2 && test3 && test4 && test5 && test6 && test7
        && t.checkExpect(this.timmy.classmatesHelper(this.abel.courses), false)
        && t.checkExpect(this.timmy.classmatesHelper(this.kelly.courses), true)
        && t.checkExpect(this.kelly.classmatesHelper(this.max.courses), true)
        && t.checkExpect(this.ruby.classmatesHelper(this.max.courses), false);
  }
  
  // tests the updateStudents method
  boolean testUpdateStudents(Tester t) {
    this.initTestConditions();
    boolean test1 = t.checkExpect(this.chemistry.students, new MtList<Student>());
    this.chemistry.updateStudents(this.kelly);
    boolean test2 = t.checkExpect(this.chemistry.students, new ConsList<Student>(this.kelly,
        new MtList<Student>()));
    this.chemistry.updateStudents(this.max);
    boolean test3 = t.checkExpect(this.chemistry.students, new ConsList<Student>(this.max,
        new ConsList<Student>(this.kelly, new MtList<Student>())));
    this.discrete.updateStudents(this.abel);
    boolean test4 = t.checkExpect(this.discrete.students, new ConsList<Student>(this.abel,
        new MtList<Student>()));
    this.biology.updateStudents(this.ruby);
    boolean test5 = t.checkExpect(this.biology.students, new ConsList<Student>(this.ruby,
        new MtList<Student>()));
    return test1 && test2 && test3 && test4 && test5;
  }
  
  // tests the dejavu method
  boolean testDejavu(Tester t) {
    this.initTestConditions();
    boolean test1 = t.checkExpect(this.jerry.courses, new ConsList<Course>(this.biology,
        new ConsList<Course>(this.chemistry, new MtList<Course>())));
    this.timmy.enroll(this.chemistry);
    boolean test2 = t.checkExpect(this.chemistry.students, new ConsList<Student>(this.timmy,
        new MtList<Student>()));
    this.timmy.enroll(this.biology);
    boolean test3 = t.checkExpect(this.biology.students, new ConsList<Student>(this.timmy,
        new MtList<Student>()));
    this.abel.enroll(this.chemistry);
    boolean test4 = t.checkExpect(this.chemistry.students, new ConsList<Student>(this.abel,
        new ConsList<Student>(this.timmy, new MtList<Student>())));
    this.kelly.enroll(this.fundies);
    boolean test5 = t.checkExpect(this.fundies.students, new ConsList<Student>(this.kelly,
        new MtList<Student>()));
    this.ruby.enroll(this.fundies);
    boolean test6 = t.checkExpect(this.fundies.students, new ConsList<Student>(this.ruby,
        new ConsList<Student>(this.kelly, new MtList<Student>())));
    return test1 && test2 && test3 && test4 && test5 && test6
        && t.checkExpect(this.jerry.dejavu(this.timmy), true)
        && t.checkExpect(this.jerry.dejavu(this.abel), false)
        && t.checkExpect(this.amit.dejavu(this.kelly), false);
  }
  
  // tests the dejavuHelper method
  boolean testDejavuHelper(Tester t) {
    this.initTestConditions();
    boolean test1 = t.checkExpect(this.jerry.courses, new ConsList<Course>(this.biology,
        new ConsList<Course>(this.chemistry, new MtList<Course>())));
    this.timmy.enroll(this.chemistry);
    boolean test2 = t.checkExpect(this.chemistry.students, new ConsList<Student>(this.timmy,
        new MtList<Student>()));
    this.timmy.enroll(this.biology);
    boolean test3 = t.checkExpect(this.biology.students, new ConsList<Student>(this.timmy,
        new MtList<Student>()));
    this.abel.enroll(this.chemistry);
    boolean test4 = t.checkExpect(this.chemistry.students, new ConsList<Student>(this.abel,
        new ConsList<Student>(this.timmy, new MtList<Student>())));
    this.kelly.enroll(this.fundies);
    boolean test5 = t.checkExpect(this.fundies.students, new ConsList<Student>(this.kelly,
        new MtList<Student>()));
    this.ruby.enroll(this.fundies);
    boolean test6 = t.checkExpect(this.fundies.students, new ConsList<Student>(this.ruby,
        new ConsList<Student>(this.kelly, new MtList<Student>())));
    return test1 && test2 && test3 && test4 && test5 && test6
        && t.checkExpect(this.amit.dejavuHelper(), new ConsList<Student>(this.ruby,
            new ConsList<Student>(this.kelly, new MtList<Student>())))
        && t.checkExpect(this.jerry.dejavuHelper(), new ConsList<Student>(this.timmy,
            new ConsList<Student>(this.abel, new ConsList<Student>(this.timmy,
                new MtList<Student>()))))
        && t.checkExpect(this.ben.dejavuHelper(), new MtList<Student>());
  }
}