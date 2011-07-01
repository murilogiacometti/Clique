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
        name = "getMostPopularWords", 
        query = "SELECT word FROM PersonWord word JOIN word.person person WHERE person.id = :personId ORDER BY word.score DESC"
    ),

    @NamedQuery(
        name = "countPeople",
        query = "SELECT MAX(person.id) FROM Person person"
    ),

    @NamedQuery(
        name = "findAssociationByWord",
        query = "SELECT association FROM PersonWord association JOIN association.person WHERE association.word = :word"
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

    @Column
    protected Boolean isUser;

    @OneToMany(mappedBy = "person")
    protected Set<PersonWord> personWords = new HashSet<PersonWord>();



    public Person() {
        this.isUser = false;
    }


    // GET's and SET's

    public Integer getId() { return this.id; }
    protected void setId(Integer id) { this.id = id; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public Long getLastUpdated() { return this.lastUpdated; }
    public void setLastUpdated(Long lastUpdated) { 
        this.lastUpdated = lastUpdated; 
    }

    public Boolean isUser() { return this.isUser; }


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

    // RELATIONS

    public void add(Word word, Integer score, Session context) {
    
        PersonWord personWord = new PersonWord();
        personWord.setWord(word);
        personWord.setPerson(this);
        personWord.setScore(score);

        personWord.save(context);
    
    }


    // QUERIES

    public static Person findById(Integer id, Session context) {
        
        Person person = (Person) context.get(Person.class, id);
        return person;
    }

    public static ArrayList<Person> findByName(String name, Session context) {
    
        ArrayList<Person> people = new ArrayList<Person>();

        org.hibernate.Query query = context.getNamedQuery("findByName");
        query.setParameter("name", name);

        for (Iterator it = query.iterate(); it.hasNext(); ) {
            people.add((Person) it.next());
        }

        return people;

    }

    /**
     * Find tuples with Person-Word-Score whose Person is self and tuples 
     * ordered by highest scores.
     *
     * @param maxResults Maximum number of results retrieved.
     */
    public ArrayList<PersonWord> getMostPopularWords(
            int maxResults, Session context) {
    
        ArrayList<PersonWord> words = new ArrayList<PersonWord>();

        org.hibernate.Query query = context.getNamedQuery("getMostPopularWords");
        query.setParameter("personId", this.id);
        query.setMaxResults(maxResults);

        for (Iterator it = query.iterate(); it.hasNext(); ) {
            words.add((PersonWord) it.next());
        }

        return words;

    }


    /**
     * Find tuples with Person-Word-Score whose Person is self and tuples 
     * ordered by highest scores.
     *
     * @param pageNumber k-th set of words (page).
     * @param pageSize Maximum number of results retrieved per page.
     **/
    public ArrayList<PersonWord> getMostPopularWords(
            int pageNumber, int pageSize, Session context) {
        
        ArrayList<PersonWord> words = new ArrayList();

        // Calculate initial element of page based on total size
        int initialElement = pageNumber * pageSize;
        
        org.hibernate.Query query = context.getNamedQuery("getMostPopularWords");
        query.setParameter("categoryId", this.id);

        // STARTING FROM initialElement WITH pageSize ELEMENTS
        // Set first element of the list
        query.setFirstResult(initialElement);
        // Limit retrieval to only last maxResults results
        query.setMaxResults(pageSize);
        
        for (Iterator iterator = query.iterate(); iterator.hasNext(); ) {
            words.add((PersonWord) iterator.next());
        }

        return words;
    }

   
    /**
     * Find a set of people that have similar score for the set of words given.
     *
     * @param words A list of word to calculate similarity.
     * @param maxResults Maximum results to be retrieved.
     */
    public ArrayList<Person> findMostSimilarByWords(ArrayList<Word> words, 
            int maxResults, Session context) {


        int nWords = words.size();
      
        org.hibernate.Query queryCount = context.getNamedQuery("countPeople");
        int nPeople = ((Integer) queryCount.uniqueResult()).intValue();

        // Set vector space for these words
        
        // "people" keeps track of found people
        Person people[] = new Person[nPeople + 1];
        // "vectors" stands for each person's vector
        int vectors[][] = new int[nPeople + 1][nWords];
        
        for (int person = 1; person <= nPeople; person++) {
            people[person] = null;
            for (int word = 0; word < nWords; word++) {
                vectors[person][word] = 0;
            }
        }
        
    
        // For each word
        for (int word = 0; word < nWords; word++) {
        
            // Find all people that have this word and its associated score
            org.hibernate.Query queryFind = context.getNamedQuery(
                    "findAssociationByWord");
            queryFind.setParameter("word", words.get(word));

            PersonWord personWord = null;
         // For each person with that word
            for(Iterator it = queryFind.iterate(); it.hasNext(); ) {
                personWord = (PersonWord) it.next();
                
                // Save person and its score for this word
                people[personWord.getPerson().getId().intValue()] = 
                        personWord.getPerson();
                vectors[personWord.getPerson().getId().intValue()][word] = 
                        personWord.getScore().intValue();
            
            }

        }

        // Just meant to print vector of each person        
        for (int person = 1; person <= nPeople; person++) {
            System.out.print(person + ": ");
            for (int word = 0; word < nWords; word++) {
                System.out.print(vectors[person][word] + "  ");
            }
            System.out.println();
        }
       
        // Find similarity between this user and all other
        ArrayList<Similarity> similarities = new ArrayList(nPeople + 1);
        Similarity similarity = null; 
        
        for (int person = 1; person <= nPeople; person++) {
            
            // Check if person has one of given words and if it is not himself
            if (people[person] != null) {
                if (people[person].getId() != this.id) {

                    for (int word = 0; word < nWords; word++) {
                        
                        similarity = new Similarity();
                        similarity.setValue(
                                vectors[person][word] * vectors[this.id][word]);
                        similarity.setPerson(people[person]);

                        similarities.add(similarity);
                    
                    }
            
                    // Just meant to print similarity between people
                    System.out.println("Similarity " + person + ": " + 
                            similarity.getValue());
                
                }
            }
        }

        // Sort by most similar and retrieve the first "maxResults" people
        Collections.sort(similarities);

        ArrayList<Person> similarPeople = new ArrayList<Person>();
        
        if (similarities.size() < maxResults) {
            for (int person = 0; person < similarities.size(); person++) {
                similarPeople.add(similarities.get(person).getPerson());
            }
        } else {
            for (int person = 0; person < maxResults; person++) {
                similarPeople.add(similarities.get(person).getPerson());
            }
        }
       
        return similarPeople;        
    
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

        Word word1 = new Word("word1", context);
        Word word2 = new Word("word2", context);
        Word word3 = new Word("word3", context);
        Word word4 = new Word("word4", context);
        Word word5 = new Word("word5", context);

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

        ArrayList<PersonWord> words = person.getMostPopularWords(1, 4, context);

        for(int word = 0; word < words.size(); word++) {
            System.out.println(words.get(word).getWord().getWord() + " : " + words.get(word).getScore());                   
        }

        ArrayList<Person> array = person.findMostSimilarByWords(new ArrayList<Word>(), 20, context);

        HibernateUtil.closeContext(context);
    
    }


    private static void unitTest3() {
    
        Session context = HibernateUtil.openContext();

        Person person1 = new Person();
        person1.setName("Fabio");
        person1.save(context);

        Person person2 = new Person();
        person2.setName("Murilo");
        person2.save(context);
        
        Person person3 = new Person();
        person3.setName("Rafael");
        person3.save(context);
        
        Person person4 = new Person();
        person4.setName("Renato");
        person4.save(context);
        
        Word word1 = new Word("Linux", context);
        Word word2 = new Word("Mac", context);
        Word word3 = new Word("Windows", context);
        Word word4 = new Word("IE", context);
        Word word5 = new Word("Firefox", context);
        Word word6 = new Word("Safari", context);
        Word word7 = new Word("Chrome", context);
        
        word1.save(context);
        word2.save(context);
        word3.save(context);
        word4.save(context);
        word5.save(context);
        word6.save(context);
        word7.save(context);
        
        person1.add(word1, new Integer(20), context);
        person1.add(word2, new Integer(20), context);
        person1.add(word3, new Integer(30), context);
        person1.add(word5, new Integer(50), context);

        person2.add(word1, new Integer(40), context);
        person2.add(word2, new Integer(20), context);
        person2.add(word3, new Integer(15), context);
        person2.add(word5, new Integer(10), context);
        person2.add(word7, new Integer(40), context);

        person3.add(word1, new Integer(50), context);
        person3.add(word2, new Integer(100), context);
        person3.add(word3, new Integer(5), context);
        person3.add(word6, new Integer(20), context);
        person3.add(word7, new Integer(20), context);

        person4.add(word2, new Integer(50), context);
        
        ArrayList<Word> words = new ArrayList<Word>();
        words.add(word2);

        ArrayList<Person> similarPeople = person3.findMostSimilarByWords(words, 20, context);
        
        ArrayList<PersonWord> popularWords = person1.getMostPopularWords(3, context);
        for(int word = 0; word < popularWords.size(); word++) {
            System.out.println(popularWords.get(word).getWord());
        }


        HibernateUtil.closeContext(context);
    
    }


    public static void unitTest4() {
        
        Session context = HibernateUtil.openContext();

        Person person1 = new Person();
        person1.setName("Fabio");
        person1.save(context);

        Person person2 = new Person();
        person2.setName("Murilo");
        person2.save(context);
        
        Person person3 = new Person();
        person3.setName("Rafael");
        person3.save(context);
        
        Person person4 = new Person();
        person4.setName("Renato");
        person4.save(context);
        
        Word word1 = new Word("Linux", context);
        Word word2 = new Word("Mac", context);
        Word word3 = new Word("Windows", context);
        Word word4 = new Word("IE", context);
        Word word5 = new Word("Firefox", context);
        Word word6 = new Word("Safari", context);
        Word word7 = new Word("Chrome", context);
        
        word1.save(context);
        word2.save(context);
        word3.save(context);
        word4.save(context);
        word5.save(context);
        word6.save(context);
        word7.save(context);
        
        person1.add(word1, new Integer(20), context);
        person1.add(word2, new Integer(20), context);
        person1.add(word3, new Integer(30), context);
        person1.add(word5, new Integer(50), context);

        person2.add(word1, new Integer(80), context);
        person2.add(word2, new Integer(20), context);
        person2.add(word3, new Integer(15), context);
        person2.add(word5, new Integer(10), context);
        person2.add(word7, new Integer(40), context);

        person3.add(word1, new Integer(80), context);
        person3.add(word2, new Integer(100), context);
        person3.add(word3, new Integer(5), context);
        person3.add(word6, new Integer(20), context);
        person3.add(word7, new Integer(20), context);

        person4.add(word1, new Integer(50), context);
        person4.add(word2, new Integer(50), context);
        
       
        
    
    }

    public static void main(String args[]) {

        unitTest3();

    }

}
