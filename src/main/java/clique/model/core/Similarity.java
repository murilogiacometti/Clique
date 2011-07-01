package clique.model.core;

import java.util.*;

public class Similarity implements Comparable<Similarity> {

    private int value;
    private Person person;

    public int getValue() { return this.value; }
    public void setValue(int value) { this.value = value; }

    public Person getPerson() { return this.person; }
    public void setPerson(Person person) { this.person = person; }

    public int compareTo(Similarity s) {
        
        if (s.getValue() < this.value) { 
            return -1;
        } else if (s.getValue() > this.value) {
            return 1;
        }
        return 0;
    }

}
