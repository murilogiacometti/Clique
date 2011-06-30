package clique.model.core;

import java.util.*;
import java.io.*;
import javax.persistence.*;
import org.hibernate.*;
import java.sql.*;

import clique.model.util.*;

@Entity
@Table(name = "People")
@SequenceGenerator(name = "seqId", sequenceName = "seqPersonId")
@Inheritance(strategy = InheritanceType.JOINED)

@NamedQueries({

    @NamedQuery(
        name = "findByName", 
        query = "SELECT person FROM Person person WHERE person.name = :name"
    ) 

})

public class Person implements Serializable { 

    @Id
    @Column(name = "personId", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqId")
    protected Integer id;

    @Column
    protected String name;

    @Column
    protected Long lastUpdated;


    @OneToMany(mappedBy = "person")
    protected Set<PersonWord> personWords = new HashSet<PersonWord>();



    public Person() {}


    // GET's and SET's

    public Integer getId() { return this.id; }
    protected void setId(Integer id) { this.id = id; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public Long getLastUpdated() { return this.lastUpdated; }
    public void setLastUpdated(Long lastUpdated) { 
        this.lastUpdated = lastUpdated; 
    }


    // PERSISTENCE

    public void save(Session context) {
  
        context.beginTransaction();

        context.save(this);

        context.getTransaction().commit();
  
    }

    public void merge(Session context) {
 
        context.beginTransaction();

        context.merge(this);

        context.getTransaction().commit();
    
    }

    public void remove(Session context) {
 
        context.beginTransaction();

        context.delete(this);

        context.getTransaction().commit();
    
    }


    // QUERIES

    public static Person findById(Integer id, Session context) {
        
        context.beginTransaction();
        
        Person person = (Person) context.get(Person.class, id);
        
        context.getTransaction().commit();

        return person;
    }

    public static ArrayList<Person> findByName(String name, Session context) {
    
        ArrayList<Person> people = new ArrayList<Person>();

        context.beginTransaction();

        org.hibernate.Query query = context.getNamedQuery("findByName");

        query.setParameter("name", name);

        for (Iterator it = query.iterate(); it.hasNext(); ) {
            
            people.add((Person) it.next());
        
        }

        context.getTransaction().commit();

        return people;

    }

    // RELATIONS

    public void add(Word word, Float score, Session context) {
    
        PersonWord personWord = new PersonWord();
        personWord.setWord(word);
        personWord.setPerson(this);
        personWord.setScore(score);

        personWord.save(context);
    
    }


    // TESTS

    private static void unitTest1() {
       
        Session context = HibernateUtil.openContext();
        
        Person person = new Person();
        person.setName("Name");
        person.save(context);

        HibernateUtil.closeContext(context);

    }


    public static void main(String args[]) {

        unitTest1();

    }

}
