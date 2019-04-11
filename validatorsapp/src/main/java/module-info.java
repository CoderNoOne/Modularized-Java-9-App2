module validatorsapp {
  requires modelapp;
  requires commons.validator;
  requires java.sql;
  exports validator to convertersapp;
}
