import tester.*;

interface IVehicle {
  // computes the total revenue of this Vehicle
  double totalRevenue();

  // computes the cost of fully fueling this Vehicle
  double fuelCost();

  // computes the per-passenger profit of this Vehicle
  double perPassengerProfit();

  // produce a String that shows the name and passenger capacity of this Vehicle
  String format();

  // is this IVehicle the same as that one?
  boolean sameVehicle(IVehicle that);
  
  // is this Airplane the same as that one?
  boolean sameAirplane(Airplane that);
  
  // is this Train the same as that one?
  boolean sameTrain(Train that);
  
  // is this Bus the same as that one?
  boolean sameBus(Bus that);
}

abstract class AVehicle implements IVehicle {
  String name;
  int passengerCapacity;
  double fare; // per passenger fare
  int fuelCapacity; // gals of fuel (airplanes: kerosene @ 1.94/gal,buses/trains: diesel @ 2.55/gal)
  
  AVehicle(String name, int passengerCapacity, double fare, int fuelCapacity) {
    this.name = name;
    this.passengerCapacity = passengerCapacity;
    this.fare = fare;
    this.fuelCapacity = fuelCapacity;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.name ...              -- String
   * ... this.passengerCapacity ... -- int
   * ... this.fare ...              -- double
   * ... this.fuelCapacity ...      -- int
   * 
   * METHODS:
   * ... this.totalRevenue() ...              -- double
   * ... this.fuelCost() ...                  -- double
   * ... this.perPassengerProfit ...          -- double
   * ... this.format() ...                    -- String
   * ... this.sameVehicle(IVehicle that) ...  -- boolean
   * ... this.sameAirplane(Airplane that) ... -- boolean
   * ... this.sameTrain(Train that) ...       -- boolean
   * ... this.sameBus(Bus that) ...           -- boolean
   */
  
  // computes the total revenue of this Vehicle
  public double totalRevenue() {
    return this.passengerCapacity * this.fare;
  }
  
  // computes the cost of fully fueling this Vehicle
  public double fuelCost() {
    return this.fuelCapacity * 2.55;
  }
  
  // computes the per-passenger profit of this Vehicle
  public double perPassengerProfit() {
    return (this.totalRevenue() - this.fuelCost()) / this.passengerCapacity;
  }
  
  // produce a String that shows the name and passenger capacity of this Vehicle
  public String format() {
    return this.name + ", " + this.passengerCapacity + ".";
  }
  
  // is this IVehicle the same as that one?
  public abstract boolean sameVehicle(IVehicle that);
  
  // is this Airplane the same as that one?
  public boolean sameAirplane(Airplane that) {
    return false;
  }
  
  // is this Train the same as that one?
  public boolean sameTrain(Train that) {
    return false;
  }
  
  // is this Bus the same as that one?
  public boolean sameBus(Bus that) {
    return false;
  }
}

class Airplane extends AVehicle {
  String code; // ICAO type designator
  boolean isWideBody; // twin-aisle aircraft

  Airplane(String name, int passengerCapacity, double fare, int fuelCapacity, String code,
      boolean isWideBody) {
    super(name, passengerCapacity, fare, fuelCapacity);
    this.code = code;
    this.isWideBody = isWideBody;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.name ...              -- String
   * ... this.passengerCapacity ... -- int
   * ... this.fare ...              -- double
   * ... this.fuelCapacity ...      -- int 
   * ... this.code ...              -- String
   * ... this.isWideBody ...        -- boolean
   * 
   * METHODS:
   * ... this.totalRevenue() ...              -- double
   * ... this.fuelCost() ...                  -- double
   * ... this.perPassengerProfit() ...        -- double
   * ... this.format() ...                    -- String
   * ... this.sameVehicle(IVehicle that) ...  -- boolean
   * ... this.sameAirplane(Airplane that) ... -- boolean
   * ... this.sameTrain(Train that) ...       -- boolean
   * ... this.sameBus(Bus that) ...           -- boolean
   */

  // computes the cost of fully fueling this Airplane
  @Override
  public double fuelCost() {
    return this.fuelCapacity * 1.94;
  }
  
  // is this Vehicle the same as that one?
  public boolean sameVehicle(IVehicle that) {
    return that.sameAirplane(this);
  }
  
  // is this Airplane the same as that Airplane?
  @Override
  public boolean sameAirplane(Airplane that) {
    return this.name.equals(that.name)
        && this.passengerCapacity == that.passengerCapacity
        && this.fare == that.fare
        && this.fuelCapacity == that.fuelCapacity
        && this.code == that.code
        && this.isWideBody == that.isWideBody;
  }
}

class Train extends AVehicle {
  int numberOfCars; // cars per trainset
  int gauge; // track gauge in millimeters

  Train(String name, int passengerCapacity, double fare, int fuelCapacity, int numberOfCars,
      int gauge) {
    super(name, passengerCapacity, fare, fuelCapacity);
    this.numberOfCars = numberOfCars;
    this.gauge = gauge;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.name ...              -- String
   * ... this.passengerCapacity ... -- int
   * ... this.fare ...              -- double
   * ... this.fuelCapacity ...      -- int 
   * ... this.numberOfCars ...      -- int
   * ... this.gauge ...             -- int
   * 
   * METHODS:
   * ... this.totalRevenue() ...              -- double
   * ... this.fuelCost() ...                  -- double
   * ... this.perPassengerProfit() ...        -- double
   * ... this.format() ...                    -- String
   * ... this.sameVehicle(IVehicle that) ...  -- boolean
   * ... this.sameAirplane(Airplane that) ... -- boolean
   * ... this.sameTrain(Train that) ...       -- boolean
   * ... this.sameBus(Bus that) ...           -- boolean
   */
  
  // is this Vehicle the same as that one?
  public boolean sameVehicle(IVehicle that) {
    return that.sameTrain(this);
  }
  
  // is this Train the same as that Train?
  @Override
  public boolean sameTrain(Train that) {
    return this.name.equals(that.name)
        && this.passengerCapacity == that.passengerCapacity
        && this.fare == that.fare
        && this.fuelCapacity == that.fuelCapacity
        && this.numberOfCars == that.numberOfCars
        && this.gauge == that.gauge;
  }
}

class Bus extends AVehicle {
  int length; // length in feet

  Bus(String name, int passengerCapacity, double fare, int fuelCapacity, int length) {
    super(name, passengerCapacity, fare, fuelCapacity);
    this.length = length;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.name ...              -- String
   * ... this.passengerCapacity ... -- int
   * ... this.fare ...              -- double
   * ... this.fuelCapacity ...      -- int 
   * ... this.length ...            -- int
   * 
   * METHODS:
   * ... this.totalRevenue() ...              -- double
   * ... this.fuelCost() ...                  -- double
   * ... this.perPassengerProfit() ...        -- double
   * ... this.format() ...                    -- String
   * ... this.sameVehicle(IVehicle that) ...  -- boolean
   * ... this.sameAirplane(Airplane that) ... -- boolean
   * ... this.sameTrain(Train that) ...       -- boolean
   * ... this.sameBus(Bus that) ...           -- boolean
   */
  
  // is this Vehicle the same as that one?
  public boolean sameVehicle(IVehicle that) {
    return that.sameBus(this);
  }
  
  // is this Bus the same as that Bus?
  @Override
  public boolean sameBus(Bus that) {
    return this.name.equals(that.name)
        && this.passengerCapacity == that.passengerCapacity
        && this.fare == that.fare
        && this.fuelCapacity == that.fuelCapacity
        && this.length == that.length;
  }
}

class ExamplesVehicle {
  IVehicle dreamliner = new Airplane("Boeing 787", 242, 835.0, 33340, "B788", false);
  IVehicle dreamlifter = new Airplane("Boeing 747", 366, 250.0, 45000, "B742", true);
  IVehicle commuterRail = new Train("MPI HSP46", 500, 11.50, 2000, 6, 1435);
  IVehicle acela = new Train("Acela Express", 386, 130.0, 3500, 10, 1435);
  IVehicle silverLine = new Bus("Neoplan AN460LF", 77, 1.70, 100, 60);
  IVehicle newFlyer = new Bus("XDE40 Xcelsior", 76, 2.75, 150, 41);

  // testing total revenue method
  boolean testTotalRevenue(Tester t) {
    return t.checkInexact(this.dreamliner.totalRevenue(), 242 * 835.0, .0001)
        && t.checkInexact(this.dreamlifter.totalRevenue(), 366 * 250.0, .0001)
        && t.checkInexact(this.commuterRail.totalRevenue(), 500 * 11.5, .0001)
        && t.checkInexact(this.acela.totalRevenue(), 386 * 130.0, 0.0001)
        && t.checkInexact(this.silverLine.totalRevenue(), 77 * 1.7, 0.001)
        && t.checkInexact(this.newFlyer.totalRevenue(), 76 * 2.75, 0.0001);
  }
  
  // testing fuelCost method
  boolean testFuelCost(Tester t) {
    return t.checkInexact(this.dreamliner.fuelCost(), 33340 * 1.94, 0.0001)
        && t.checkInexact(this.dreamlifter.fuelCost(), 45000 * 1.94, 0.0001)
        && t.checkInexact(this.commuterRail.fuelCost(), 2000 * 2.55, 0.0001)
        && t.checkInexact(this.acela.fuelCost(), 3500 * 2.55, 0.0001)
        && t.checkInexact(this.silverLine.fuelCost(), 100 * 2.55, 0.0001)
        && t.checkInexact(this.newFlyer.fuelCost(), 150 * 2.55, 0.0001);
  }
  
  // testing perPassengerProfit method
  boolean testPerPassengerProfit(Tester t) {
    return t.checkInexact(this.dreamliner.perPassengerProfit(),
        (this.dreamliner.totalRevenue() - this.dreamliner.fuelCost()) / 242, 0.0001)
        && t.checkInexact(this.dreamlifter.perPassengerProfit(),
            (this.dreamlifter.totalRevenue() - this.dreamlifter.fuelCost()) / 366, 0.0001)
        && t.checkInexact(this.commuterRail.perPassengerProfit(),
            (this.commuterRail.totalRevenue() - this.commuterRail.fuelCost()) / 500, 0.0001)
        && t.checkInexact(this.acela.perPassengerProfit(),
            (this.acela.totalRevenue() - this.acela.fuelCost()) / 386, 0.0001)
        && t.checkInexact(this.silverLine.perPassengerProfit(),
            (this.silverLine.totalRevenue() - this.silverLine.fuelCost()) / 77, 0.0001)
        && t.checkInexact(this.newFlyer.perPassengerProfit(),
            (this.newFlyer.totalRevenue() - this.newFlyer.fuelCost()) / 76, 0.0001);
  }
  
  // testing format method
  boolean testFormat(Tester t) {
    return t.checkExpect(this.dreamliner.format(), "Boeing 787, 242.")
        && t.checkExpect(this.dreamlifter.format(), "Boeing 747, 366.")
        && t.checkExpect(this.commuterRail.format(), "MPI HSP46, 500.")
        && t.checkExpect(this.acela.format(), "Acela Express, 386.")
        && t.checkExpect(this.silverLine.format(), "Neoplan AN460LF, 77.")
        && t.checkExpect(this.newFlyer.format(), "XDE40 Xcelsior, 76.");
  }
  
  // testing sameVehicle method
  boolean testSameVehicle(Tester t) {
    return t.checkExpect(this.dreamliner.sameVehicle(this.dreamliner), true)
        && t.checkExpect(this.commuterRail.sameVehicle(this.commuterRail), true)
        && t.checkExpect(this.silverLine.sameVehicle(new Bus("Neoplan AN460LF", 77, 1.70, 100, 60)),
            true)
        && t.checkExpect(this.dreamlifter.sameVehicle(new Airplane("Boeing 747", 366, 250.0, 1000,
            "B742", true)), false)
        && t.checkExpect(this.newFlyer.sameVehicle(this.silverLine), false)
        && t.checkExpect(this.dreamliner.sameVehicle(this.acela), false);
  }
  
  // testing sameAirplane method
  boolean testSameAirplane(Tester t) {
    return t.checkExpect(this.dreamliner.sameAirplane(new Airplane("Boeing 787", 242, 835.0, 33340,
        "B788", false)), true)
        && t.checkExpect(this.dreamlifter.sameAirplane(new Airplane("Boeing 746", 366, 250.0, 45000,
            "B742", true)), false)
        && t.checkExpect(this.commuterRail.sameAirplane(new Airplane("Boeing 787", 242, 835.0,
            33340, "B788", false)), false)
        && t.checkExpect(this.newFlyer.sameAirplane(new Airplane("Boeing 787", 242, 835.0, 33340,
        "B788", false)), false);
  }
  
  // testing sameTrain method
  boolean testSameTrain(Tester t) {
    return t.checkExpect(this.commuterRail.sameTrain(new Train("MPI HSP46", 500, 11.50, 2000, 6,
        1435)), true)
        && t.checkExpect(this.acela.sameTrain(new Train("Acela Express", 386, 130.0, 3500, 10,
            1000)), false)
        && t.checkExpect(this.dreamlifter.sameTrain(new Train("Acela Express", 386, 130.0, 3500, 10,
            1435)), false)
        && t.checkExpect(this.silverLine.sameTrain(new Train("Acela Express", 386, 130.0, 3500, 10,
            1435)), false);
  }
  
  // testing sameBus method
  boolean testSameBus(Tester t) {
    return t.checkExpect(this.silverLine.sameBus(new Bus("Neoplan AN460LF", 77, 1.70, 100, 60)),
        true)
        && t.checkExpect(this.newFlyer.sameBus(new Bus("XDE40 Xcelsior", 76, 2.75, 150, 40)),
            false)
        && t.checkExpect(this.dreamliner.sameBus(new Bus("XDE40 Xcelsior", 76, 2.75, 150, 41)),
            false)
        && t.checkExpect(this.acela.sameBus(new Bus("XDE40 Xcelsior", 76, 2.75, 150, 41)), false);
  }
}