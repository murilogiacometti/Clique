package clique.model.core;

import clique.model.util.*;
import clique.IR.*;

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
        name = "findWord", 
        query = "SELECT word FROM Word word WHERE word.word = :word"
    ),

    @NamedQuery(
        name = "getPeople", 
        query = "SELECT association FROM PersonWord association JOIN association.word word JOIN association.person person WHERE word.id = :wordId ORDER BY association.score DESC"
    ),

    @NamedQuery(
        name = "getPeopleDisjoint", 
        query = "SELECT association FROM PersonWord association JOIN association.word word JOIN association.person person WHERE word.id = :wordId AND person.isUser = :isUser ORDER BY association.score DESC"
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
    @JoinColumn(name = "stemId")
    private Stem stem;

    
    
    public Word() { }

    public Word(String word, Session context) { setWord(word, context); }


    
    public Integer getId() { return this.id; }
    private void setId(Integer id) { this.id = id; }

    public String getWord() { return this.word; }
    public void setWord(String word, Session context) { 

        this.word = word; 
        
        Porter porterAlgorithm = new Porter();
       
        // Calculate stem
        String stemString = porterAlgorithm.stripAffixes(word);
        
        // Verify if stem is in database
        Stem stem = Stem.findByStem(stemString, context);
        
        if (stem != null) {

            // If yes, add to word
            this.stem = stem;

        } else {
            
            // If not, persist stem and add to word
            stem = new Stem(stemString);
            stem.save(context);
            this.stem = stem;

        }
    }

    public Stem getStem() { return this.stem; }

    public String toString() {
        return this.word;
    }


    
    
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

    public static Word findById(Integer id, Session context) {
        
        Word word = (Word) context.get(Word.class, id);
        return word;
    }

    public static Word findByWord(String wordString, Session context) {

        org.hibernate.Query query = context.getNamedQuery("findByWord");
        query.setParameter("word", wordString);

        Word word = (Word) query.uniqueResult();

        return word;
    }



    public ArrayList<PersonWord> getPeople(int maxResults, Session context) {
        
        ArrayList<PersonWord> people = new ArrayList<PersonWord>();

        org.hibernate.Query query = context.getNamedQuery("getPeople");
        query.setParameter("wordId", this.id);
        query.setMaxResults(maxResults);

        for(Iterator it = query.iterate(); it.hasNext(); ) {
            people.add((PersonWord) it.next());
        }

        return people;
    
    }

    public ArrayList<PersonWord> getPeople(int maxResults, boolean isUser, Session context) {
        
        ArrayList<PersonWord> people = new ArrayList<PersonWord>();

        org.hibernate.Query query = context.getNamedQuery("getPeopleDisjoint");
        query.setParameter("wordId", this.id);
        query.setParameter("isUser", new Boolean(isUser));
        query.setMaxResults(maxResults);

        for(Iterator it = query.iterate(); it.hasNext(); ) {
            people.add((PersonWord) it.next());
        }

        return people;
    
    }



    public static ArrayList<Word> match(String pattern, int maxResults, Session context) {
        
        ArrayList<Word> words = new ArrayList<Word>();
        
        org.hibernate.Query query = context.getNamedQuery("match");
        query.setParameter("pattern", pattern + "%");
        query.setMaxResults(maxResults);

        for (Iterator it = query.iterate(); it.hasNext(); ) {
            words.add((Word) it.next());
        }

        return words;

    }


    private static void unitTest1() {
    
        Session context = HibernateUtil.openContext();

        Word word = new Word();
        word.setWord("word", context);
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
        
        word1.setWord("teste", context);
        word2.setWord("testando", context);
        word3.setWord("testudo", context);
        word4.setWord("intestavel", context);
        word5.setWord("amora", context);
        
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
        word.setWord("testing", context);
        word.save(context);
   
        Word word2 = new Word();
        word2.setWord("testable", context);
        word2.save(context);
        
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

        person1.add(word, new Integer(21), context);
        person2.add(word, new Integer(2), context);
        person3.add(word, new Integer(3), context);
        person4.add(word, new Integer(4), context);
        person5.add(word, new Integer(15), context);

        word.merge(context);

        ArrayList<PersonWord> people = word.getPeople(10, context);

        for (int person = 0; person < people.size(); person++) {
            System.out.println(people.get(person).getPerson().getName());
        }

        HibernateUtil.closeContext(context);
    
    }
    
    private static void unitTest4() {
    
        Session context = HibernateUtil.openContext();

        Word word1 = new Word();
        word1.setWord("testing", context);
        word1.save(context);
   
        Word word2 = new Word();
        word2.setWord("testable", context);
        word2.save(context);
        
        Word word3 = Word.findById(new Integer(1), context);

        System.out.println(word3);
        
        HibernateUtil.closeContext(context);

    }

    private static void unitTest5() {
    
        Session context = HibernateUtil.openContext();

        Word word = new Word();
        word.setWord("testing", context);
        word.save(context);
   
        Person person1 = new Person();
        Person person2 = new Person();
        Person person3 = new Person();
        User user1 = new User();
        User user2 = new User();

        person1.setName("Person 1");
        person2.setName("Person 2");
        person3.setName("Person 3");
        user1.setName("User 1");
        user1.setEmail("etc@etc");
        user1.setPassword("password");
        user2.setName("User 2");
        user2.setEmail("bla@bla");
        user2.setPassword("password");

        person1.save(context);
        person2.save(context);
        person3.save(context);
        user1.save(context);
        user2.save(context);

        person1.add(word, new Integer(21), context);
        person2.add(word, new Integer(2), context);
        person3.add(word, new Integer(3), context);
        user1.add(word, new Integer(4), context);
        user2.add(word, new Integer(15), context);

        word.merge(context);

        ArrayList<PersonWord> all = word.getPeople(20, context);
        ArrayList<PersonWord> users = word.getPeople(20, true, context);
        ArrayList<PersonWord> people = word.getPeople(20, false, context);

        System.out.println("ALL:");
        for (int person = 0; person < all.size(); person++) {
            System.out.println(all.get(person).getPerson().getName());        
        }
        
        System.out.println("USERS:");
        for (int person = 0; person < users.size(); person++) {
            System.out.println(users.get(person).getPerson().getName());        
        }
        
        System.out.println("PEOPLE:");
        for (int person = 0; person < people.size(); person++) {
            System.out.println(people.get(person).getPerson().getName());        
        }

        HibernateUtil.closeContext(context);
    
    
    }

    public static void main(String args[]) {
    
        //unitTest4();

        unitTest5();
    
    }

}

