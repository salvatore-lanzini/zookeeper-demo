package slanzini.zookeeper.demo.util;

import slanzini.zookeeper.demo.model.Person;

import java.util.ArrayList;
import java.util.List;

/** @author "Salvatore Lanzinil" 11/02/23 */
public final class DataStorage {

  private static List<Person> personList = new ArrayList<>();

  public static List<Person> getPersonListFromStorage() {
    return personList;
  }

  public static void setPerson(Person person) {
    personList.add(person);
  }

  private DataStorage() {}
}
