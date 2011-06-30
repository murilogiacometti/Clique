package clique.model.core;

import clique.model.util.*;

import org.hibernate.*;
import java.util.*;
import java.io.*;
import javax.persistence.*;
import java.sql.*;

@Entity
@Table(name = "Words")
@SequenceGenerator(name = "seqId", sequenceName = "seqWordId")

@NamedQueries({

    @NamedQuery(
        name = "getPeople", 
        query = "SELECT person FROM Person person JOIN person.personWords association JOIN association.word word WHERE word.id = :wordId"
    ),

    @NamedQuery(
        name = "match", 
        query = "SELECT word FROM Word word WHERE word.word LIKE :pattern"
    ) 

})

public class Word implements Serializable {

    @Id
    @Column(name = "wordId", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqId")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String word;

    @OneToMany(mappedBy = "word")
    private Set<PersonWord> personWords = new HashSet();
    
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "affixId")
    private Affix affix;

    public Word() { }

    public Integer getId() { return this.id; }
    private void setId(Integer id) { this.id = id; }

    public String getWord() { return this.word; }
    public void setWord(String word) { 

        this.word = word; 
        
        Affix affix = null;

        // Calculate affix
        // Verify if affix is in database
        // If yes, load and add
        // If not, just add and wait for this word to be persisted, so that this affix will be persisted too (CascadeType.Persiste)

    }

    public Affix getAffix() { return this.affix; }

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

    public ArrayList<Person> getPeople(int maxResults, Session context) {
        
        ArrayList<Person> people = new ArrayList<Person>();

        context.beginTransaction();

        org.hibernate.Query query = context.getNamedQuery("getPeople");

        query.setParameter("wordId", this.id);
        query.setMaxResults(maxResults);

        for(Iterator it = query.iterate(); it.hasNext(); ) {
            
            people.add((Person) it.next());
        
        }

        context.getTransaction().commit();

        return people;
    
    }


    public static ArrayList<Word> match(String pattern, int maxResults, Session context) {
        
        ArrayList<Word> words = new ArrayList<Word>();
        
        context.beginTransaction();

        org.hibernate.Query query = context.getNamedQuery("match");
        
        query.setParameter("pattern", pattern + "%");
        query.setMaxResults(maxResults);

        for (Iterator it = query.iterate(); it.hasNext(); ) {
            words.add((Word) it.next());
        }

        context.getTransaction().commit();

        return words;

    }


    private static void unitTest1() {
    
        Session context = HibernateUtil.openContext();

        Word word = new Word();
        word.setWord("word");
        word.save(context);
    
        HibernateUtil.closeContext(context);
    
    }

    private static void unitTest2() {
        
        Session context = HibernateUtil.openContext();

        Word word1 = new Word();
        Word word2 = new Word();
        Word word3 = new Word();
        Word word4 = new Word();
        Word word5 = new Word();
        
        word1.setWord("teste");
        word2.setWord("testando");
        word3.setWord("testudo");
        word4.setWord("intestavel");
        word5.setWord("amora");
        
        word1.save(context);
        word2.save(context);
        word3.save(context);
        word4.save(context);
        word5.save(context);
    
        ArrayList<Word> words = Word.match("test", 10, context);

        for (int word = 0; word < words.size(); word++) {
            System.out.println(words.get(word).getWord());
        }

        HibernateUtil.closeContext(context);
    
    }

    private static void unitTest3() {
    
        Session context = HibernateUtil.openContext();

        Word word = new Word();
        word.setWord("teste");
        word.save(context);
   
        Person person1 = new Person();
        Person person2 = new Person();
        Person person3 = new Person();
        Person person4 = new Person();
        Person person5 = new Person();

        person1.setName("Person 1");
        person2.setName("Person 2");
        person3.setName("Person 3");
        person4.setName("Person 4");
        person5.setName("Person 5");

        person1.save(context);
        person2.save(context);
        person3.save(context);
        person4.save(context);
        person5.save(context);

        person1.add(word, new Float(1.0), context);
        person2.add(word, new Float(1.0), context);
        person3.add(word, new Float(1.0), context);
        person4.add(word, new Float(1.0), context);
        person5.add(word, new Float(1.0), context);

        word.merge(context);

        ArrayList<Person> people = word.getPeople(10, context);

        for (int person = 0; person < people.size(); person++) {
            System.out.println(people.get(person).getName());
        }

        HibernateUtil.closeContext(context);
    
    }
    
    private static void unitTest4() {}
    private static void unitTest5() {}

    public static void main(String args[]) {
    
        //unitTest1();
        //unitTest2();
        unitTest3();
        //unitTest4();
        //unitTest5();
    
    }

}

