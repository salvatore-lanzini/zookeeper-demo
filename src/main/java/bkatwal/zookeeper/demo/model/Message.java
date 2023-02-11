package bkatwal.zookeeper.demo.model;

import java.util.List;

/** @author "Bikas Katwal" 26/03/19 */
public class Message {

  private String message;
  List<Person> persons;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<Person> getPersons() {
    return persons;
  }

  public void setPersons(List<Person> persons) {
    this.persons = persons;
  }
}
