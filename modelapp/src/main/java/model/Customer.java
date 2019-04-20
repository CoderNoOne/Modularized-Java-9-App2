package model;

import java.util.Objects;

public class Customer {

  private String name;
  private String surname;
  private int age;
  private String email;

  public Customer(String name, String surname, int age, String email) {
    this.name = name;
    this.surname = surname;
    this.age = age;
    this.email = email;
  }

  public Customer() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Customer customer = (Customer) o;
    return age == customer.age &&
            Objects.equals(name, customer.name) &&
            Objects.equals(surname, customer.surname) &&
            Objects.equals(email, customer.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, surname, age, email);
  }

  @Override
  public String toString() {
    return "Customer{" +
            "name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", age=" + age +
            ", email='" + email + '\'' +
            '}';
  }
}
