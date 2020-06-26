package com.testClient;

import java.util.List;

/**
 * Created by chiragjain on 8/6/20.
 */
public class Employee {

  String empName;
  int age;
  Address address;
  List<Project> projects;

  public Employee(String name, int age, Address address,List<Project> projects) {
    this.empName = name;
    this.age = age;
    this.address = address;
    this.projects=projects;
  }

  public Employee(String name, int age, Address address) {
    this.empName = name;
    this.age = age;
    this.address = address;
  }
}
