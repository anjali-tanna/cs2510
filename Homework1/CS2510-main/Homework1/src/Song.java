// to represent a song for Billboard's Top 100
class Song {
  String title;
  String artist;
  int year;
  double length;
  
  // the constructor
  Song(String title, String artist, int year, double length) {
    this.title = title;
    this.artist = artist;
    this.year = year;
    this.length = length;
  }
}

// examples for the class that represent songs
// in the Billboard's Top 100.
class ExamplesSong {
  ExamplesSong() {}
  
  Song levitating = new Song("Levitating", "Dua Lipa", 2020, 3.5);
  Song rhiannon = new Song("Rhiannon", "Fleetwood Mac", 1975, 4.25);
  Song driverslicense = new Song("Driver's License", "Olivia Rodrigo", 2021, 4.0);
}
