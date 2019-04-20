module serviceapp {
  requires exceptionsapp;
  requires modelapp;
  requires convertersapp;
  requires org.eclipse.collections.impl;
  requires org.eclipse.collections.api;
  requires mail;
  requires j2html;
  exports service to mainapp;
}