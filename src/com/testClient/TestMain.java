package com.testClient;

import com.jsonLib.json.JsonLib;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiragjain on 8/6/20.
 */
public class TestMain {

  public static void main(String[] args) {
    JsonLib j=new JsonLib();
    Address address=new Address("Gurgaon",123456,"street 23");
    /*List<Project> projects=new ArrayList();
    projects.add(new Project("Insurance",123));
    projects.add(new Project("Recharge",456));*/
    Employee employee=new Employee("sumit",30,address);
    System.out.println(j.stringToJson(employee));
  }

}
