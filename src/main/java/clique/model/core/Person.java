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
    ),
    
    @NamedQuery(
        name = "getWords", 
        query = "SELECT word FROM PersonWord word JOIN word.person person WHERE person.id = :personId ORDER BY word.score DESC"
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


    public ArrayList<PersonWord> getWords(int maxResults, Session context) {
    
        ArrayList<PersonWord> words = new ArrayList<PersonWord>();

        context.beginTransaction();

        org.hibernate.Query query = context.getNamedQuery("getWords");
        query.setParameter("personId", this.id);
        query.setMaxResults(maxResults);

        for (Iterator it = query.iterate(); it.hasNext(); ) {
            words.add((PersonWord) it.next());
        }

        context.getTransaction().commit();

        return words;

    }

    // RELATIONS

    public void add(Word word, Integer score, Session context) {
    
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

    private static void unitTest2() {
    
        Session context = HibernateUtil.openContext();

        Person person = new Person();
        person.setName("Name");
        person.save(context);

        Word word1 = new Word("word1");
        Word word2 = new Word("word2");
        Word word3 = new Word("word3");
        Word word4 = new Word("word4");
        Word word5 = new Word("word5");

        word1.save(context);
        word2.save(context);
        word3.save(context);
        word4.save(context);
        word5.save(context);
        
        person.add(word1, new Integer(2), context);
        person.add(word2, new Integer(4), context);
        person.add(word3, new Integer(5), context);
        person.add(word4, new Integer(1), context);
        person.add(word5, new Integer(54), context);

        ArrayList<PersonWord> words = person.getWords(3, context);

        for(int word = 0; word < words.size(); word++) {
            System.out.println(words.get(word).getWord().getWord() + " : " + words.get(word).getScore());                   
        }


        HibernateUtil.closeContext(context);
    
    }


    public static void main(String args[]) {

        unitTest2();

    }

}
