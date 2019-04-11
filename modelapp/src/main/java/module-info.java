module modelapp {
  exports model to convertersapp, serviceapp, validatorsapp, mainapp, mockneat;
  opens model to gson, org.apache.commons.lang3;

}