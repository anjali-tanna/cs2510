// to represent a Party, which can be either a 
// Course, a Committee, or a Person
interface IParty { }

// represents a Course with a name and count of enrollment
class Course implements IParty {
  String name;
  int enrollment;
  
  Course(String name, int enrollment) {
    this.name = name;
    this.enrollment = enrollment;
  }
}

// represents a Committee with a name, mission, and count of members
class Committee implements IParty {
  String name;
  String mission;
  int members;
  
  Committee(String name, String mission, int members) {
    this.name = name;
    this.mission = mission;
    this.members = members;
  }
}

// represents a Person with a name and id number assigned by the application
class Person implements IParty {
  String name;
  int id;
  
  Person(String name, int id) {
    this.name = name;
    this.id = id;
  }
}

// to represent a Meeting, which can be a 
// Squad or Zing
interface IMeeting { }

// represents a type of meeting with a from party that is a Person,
// a to party, a name, and an owner who is a Person
class Squad implements IMeeting {
  IParty from;
  IParty to;
  String name;
  IParty owner;
  
  Squad(IParty from, IParty to, String name, IParty owner) {
    this.from = from;
    this.to = to;
    this.name = name;
    this.owner = owner;
  }
}

// represents a type of meeting with a from party that is a Person,
// a to party, and a hasWaiting that represents if there is a waiting 
// room or not
class Zing implements IMeeting {
  IParty from;
  IParty to;
  boolean hasWaiting;
  
  Zing(IParty from, IParty to, boolean hasWaiting) {
    this.from = from;
    this.to = to;
    this.hasWaiting = hasWaiting;
  }
}

// examples of Meetings
class ExamplesMeeting {
  ExamplesMeeting() {}
  
  IParty alice = new Person("Alice", 12345);
  IParty social = new Committee("Social Committee", "Socially Distant Social Activities", 23);
  IParty cs2510 = new Course("Fundies 2", 650);
  IParty steve = new Person("Steve", 98765);
  IParty academic = new Committee("Academic Committee", "Academic Games", 19);
  IParty chem2311 = new Course("Organic Chemistry", 390);
  IMeeting squad1 = new Squad(this.alice, this.steve, "Chemistry Group", this.alice);
  IMeeting squad2 = new Squad(this.steve, this.alice, "Fundies Gang", this.steve);
  IMeeting zing1 = new Zing(this.alice, this.steve, true);
  IMeeting zing2 = new Zing(this.steve, this.alice, false);
}
     

// I do not believe this was the best data design. It would help
// to include templates as well as organizing the data in a more readable way.
// For instance, the examples would have been easier to understand if the IParty 
// examples were grouped with the IParty interface definitions. While
// this design was not completely bad, it could have been better.
