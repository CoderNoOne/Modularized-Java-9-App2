package main;


import converters.json.generator.DataGenerator;

public class App {

  public static void main(String[] args) {

    DataGenerator.generate(".\\modelapp\\example.json");
    new MenuService(".\\modelapp\\example.json").mainMenu();

  }
}