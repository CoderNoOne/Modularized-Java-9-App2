module serviceapp {
  requires exceptionsapp;
  exports service to mainapp;
  requires modelapp;
  requires convertersapp;
  requires org.eclipse.collections.impl;
  requires org.eclipse.collections.api;
  requires java.sql;
}