// 2.4 page 17
class Automobile {
  String model;
  int price;
  double mileage;
  boolean used;
  
  Automobile(String model, int price, double mileage, boolean used) {
    this.model = model;
    this.price = price;
    this.mileage = mileage;
    this.used = used;
  }
}

class ExamplesAutomobile {
  ExamplesAutomobile() {}
  
  Automobile hyundai = new Automobile("Tuscon", 70000, 30.5, false);
  Automobile honda = new Automobile("Odessey", 600000, 28.4, true);
  Automobile mercedez = new Automobile("Benz", 800000, 32.0, false);
}


// 3.1 page 25
class RealEstateAssistant {
  String kind;
  int rooms;
  Address address;
  int price;
  
  RealEstateAssistant(String kind, int rooms, Address address, int price) {
    this.kind = kind;
    this.rooms = rooms;
    this.address = address;
    this.price = price;
  }
}

class Address {
  int number;
  String name;
  String city;
  
  Address(int number, String name, String city) {
    this.number = number;
    this.name = name;
    this.city = city;
  }
}

class ExamplesREA {
  ExamplesREA() {}
  
  Address ranchaddy = new Address(23, "Maple Street", "Brookline");
  Address colonialaddy = new Address(5, "Jote Road", "Newton");
  Address capeaddy = new Address(83, "Winslow Road", "Waltham");
  RealEstateAssistant ranch = new RealEstateAssistant("Ranch", 7, this.ranchaddy, 375000);
  RealEstateAssistant colonial = new RealEstateAssistant("Colonial", 9, this.colonialaddy, 450000);
  RealEstateAssistant cape = new RealEstateAssistant("Cape", 6, this.capeaddy, 235000);
}

// 4.4 page 34
interface BankAcc { }

class Checking implements BankAcc {
  int id;
  String name;
  int min;
  int current;
  
  Checking(int id, String name, int min, int current) {
    this.id = id;
    this.name = name;
    this.min = min;
    this.current = current;
  }
}

class Savings implements BankAcc {
  int id;
  String name;
  double interest;
  int current;
  
  Savings(int id, String name, double interest, int current) {
    this.id = id;
    this.name = name;
    this.interest = interest;
    this.current = current;
  }
}

class CD implements BankAcc {
  int id;
  String name;
  double interest;
  String maturity;
  int current;
  
  CD(int id, String name, double interest, String maturity, int current) {
    this.id = id;
    this.name = name;
    this.interest = interest;
    this.maturity = maturity;
    this.current = current;
  }
}

class ExamplesBankAcc {
  ExamplesBankAcc() {}
  
  BankAcc gray = new Checking(1729, "Earl Gray", 500, 1250);
  BankAcc flatt = new CD(4104, "Ima Flatt", 4.0, "June 1 2005", 10123);
  BankAcc proulx = new Savings(2992, "Annie Proulx", 3.5, 800);
}


// 5.3 page 43


// 10.6 page 102


// 11.2 page 113


// 14.7 page 140
