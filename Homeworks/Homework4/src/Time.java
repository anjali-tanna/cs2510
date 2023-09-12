import tester.*;

// to represent the time
class Time {
  int hour;
  int minute;
  int second;

  // Time constructor
  Time(int hour, int minute, int second) {
    this.hour = new Constraints().determineInterval(hour, 0, 23, "Invalid hour: " + hour);
    this.minute = new Constraints().determineInterval(minute, 0, 59, "Invalid minute: " + minute);
    this.second = new Constraints().determineInterval(second, 0, 59, "Invalid second: " + second);
  }

  /* TEMPLATE
   * FIELDS:
   * ... this.hour ...              -- int
   * ... this.minute ...            -- int
   * ... this.second ...            -- int
   * 
   * METHODS:
   * ... this.sameTime() ...        -- boolean
   * 
   */
  
  // convenience constructor to provide a default value of 
  // zero seconds for the time taking in just hours and minutes
  Time(int hour, int minute) {
    this(hour, minute, 0);
  }

  // to represent regular time, not military
  Time(int hour, int minute, boolean isAM) {
    this(new Constraints().militaryHour(hour, isAM), minute);
  }
  
  
  // determines if this time the same as the given time
  public boolean sameTime(Time time) {
    return time.hour == this.hour 
        && this.minute == time.minute 
        && time.second == this.second;
  }
}

// to represent constraints
class Constraints {
  
  /* TEMPLATE
   * FIELDS:
   * 
   * METHODS:
   * ... this.determineInterval() ...       -- int
   * ... this.militaryHour() ...            -- int
   * 
   */
  
  // determines if the given number is within a certain interval
  int determineInterval(int x, int min, int max, String error) {
    if (x <= max && x >= min) {
      return x;
    }
    else {
      throw new IllegalArgumentException(error);
    }
  }

  // determines the military time hour of the regular time hour
  int militaryHour(int hour, boolean isAM) {
    if (isAM) {
      return this.determineInterval(hour, 1, 12, "Invalid hour: " + hour) % 12;
    }
    else {
      return (this.determineInterval(hour, 1, 12, "Invalid hour: " + hour) % 12) + 12;
    }
  }
}

// examples for Times
class ExamplesTime {
  Time exTime1 = new Time(12, 25, true); // 0 25
  Time exTime2 = new Time(12, 15, true); // 0 15
  Time exTime3 = new Time(0, 25, 0);
  Time exTime4 = new Time(0, 25);
  Time exTime5 = new Time(12, 15, false); // 12 15
  Time exTime6 = new Time(2, 45, false); // 14 45
  
  // tests for new Times
  boolean testNewTimes(Tester t) {
    return t.checkConstructorException(new IllegalArgumentException("Invalid hour: -12"),
        "Time", -12, 60, 100)
        && t.checkConstructorException(new IllegalArgumentException("Invalid minute: 65"), 
            "Time", 12, 65, 100)
        && t.checkConstructorException(new IllegalArgumentException("Invalid second: 200"), 
            "Time", 12, 59, 200);
  }
  
  // tests for determineInterval
  boolean testerDetermineInterval(Tester t) {
    return t.checkExpect(new Constraints().determineInterval(8, 0, 23, "Invalid hour: 8"), 8)
        && t.checkException(new IllegalArgumentException("Invalid hour: -12"), 
            new Constraints(), "determineInterval", -12, 0, 12, "Invalid hour: -12")
        && t.checkException(new IllegalArgumentException("Invalid minute: 80"), 
            new Constraints(), "determineInterval", 80, 0, 59, "Invalid minute: 80")
        && t.checkException(new IllegalArgumentException("Invalid second: -10"), 
            new Constraints(), "determineInterval", -10, 0, 59, "Invalid second: -10");
  }
  
  
  // tests for sameTime
  boolean testerSameTime(Tester t) {
    return t.checkExpect(this.exTime1.sameTime(this.exTime6), false)
        && t.checkExpect(this.exTime1.sameTime(this.exTime1), true)
        && t.checkExpect(this.exTime4.sameTime(this.exTime3), true)
        && t.checkExpect(this.exTime2.sameTime(this.exTime5), false)
        && t.checkExpect(this.exTime3.sameTime(this.exTime1), true);
  }
}