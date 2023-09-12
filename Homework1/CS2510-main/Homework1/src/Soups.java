//to represent a soup
interface ISoup { }

class Broth implements ISoup {
  String type;
  
  Broth(String type) {
    this.type = type;
  }
}

class Ingredient implements ISoup {
  ISoup more;
  String name;
  
  Ingredient(ISoup more, String name) {
    this.more = more;
    this.name = name;
  }
}

//examples of Soups
class ExamplesSoup {
  ExamplesSoup() {}
  
  ISoup chicken = new Broth("chicken");
  ISoup carrots = new Ingredient(this.chicken, "carrots");
  ISoup celery = new Ingredient(this.carrots, "celery");
  ISoup yummy = new Ingredient(this.celery, "noodles");
  ISoup vanilla = new Broth("vanilla");
  ISoup horseradish = new Ingredient(this.vanilla, "horseradish");
  ISoup hotdogs = new Ingredient(this.horseradish, "hot dogs");
  ISoup noThankYou = new Ingredient(this.hotdogs, "plum sauce");   
}
