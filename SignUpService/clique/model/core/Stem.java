package clique.model.core;

import java.util.*;
import java.io.*;
import javax.persistence.*;
import org.hibernate.*;
import java.sql.*;

@Entity
@Table(name = "Stems")
@SequenceGenerator(name = "seqId", sequenceName = "seqStemId")

@NamedQueries({

    @NamedQuery(
        name = "findByStem", 
        query = "SELECT stem FROM Stem stem WHERE stem.stem = :stem"
    ),

    @NamedQuery(
        name = "Stem.getWords", 
        query = "SELECT word FROM Word word JOIN word.stem stem WHERE stem.id = :stemId"
    ) 

})


public class Stem implements Serializable {

    @Id
    @Column(name = "stemId", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqId")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String stem;

    @OneToMany(mappedBy = "stem")
    private Set<Word> words = new HashSet();


    public Stem() { }
    public Stem(String stem) { this.stem = stem; }

    public Integer getId() { return this.id; }
    private void setId(Integer id) { this.id = id; }

    public String getStem() { return this.stem; }
    public void setStem(String stem) { this.stem = stem; }

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

    public static Stem findById(Integer id, Session context) {
    
        Stem stem = (Stem) context.get(Stem.class, id);

        return stem;

    }

    public static Stem findByStem(String stemString, Session context) {
    
        org.hibernate.Query query = context.getNamedQuery("findByStem");
        query.setParameter("stem", stemString);
        Stem stem = (Stem) query.uniqueResult();

        return stem;

    }


    public ArrayList<Word> getWords(int maxResults, Session context) {
    
        ArrayList<Word> words = new ArrayList<Word>();
    
        org.hibernate.Query query = context.getNamedQuery("Stem.getWords");
        query.setParameter("stemId", this.id);
        query.setMaxResults(maxResults);

        for(Iterator it = query.iterate(); it.hasNext(); ) {
            words.add((Word) it.next());
        }

        return words;

    }

    
}
