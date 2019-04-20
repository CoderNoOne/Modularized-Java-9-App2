module convertersapp {
  requires mockneat;
  requires modelapp;
  requires gson;
  requires java.sql;
  requires exceptionsapp;
  requires validatorsapp;
  exports converters.json.generator to mainapp;
  exports converters.others to serviceapp;
  opens converters.json.generator to mockneat;
}