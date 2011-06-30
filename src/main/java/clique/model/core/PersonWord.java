package clique.model.core;

import java.util.*;
import java.io.*;
import javax.persistence.*;
import org.hibernate.*;
import java.sql.*;

import clique.model.util.*;

@Entity
@Table(
    name = "PeopleWords",
    uniqueConstraints = { @UniqueConstraint(columnNames = {"personId", "wordId"}) }
)
@SequenceGenerator(name = "seqId", sequenceName = "seqPersonWordId")

public class PersonWord implements Serializable {

    @Id
    @Column(name = "personWordId", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;
    
    @ManyToOne
    @JoinColumn(name = "wordId")
    private Word word;

    @Column
    private Float score;

    public Long getId() { return this.id; }
    private void setId(Long id) { this.id = id; }

    public Person getPerson() { return this.person; }
    public void setPerson(Person person) { this.person = person; }
    
    public Word getWord() { return this.word; }
    public void setWord(Word word) { this.word = word; }

    public Float getScore() { return this.score; }
    public void setScore(Float score) { this.score = score; }
    
    
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

    private static void unitTest1() {
    
        Session context = HibernateUtil.openContext();

        
        
        HibernateUtil.closeContext(context);
    
    }

    public static void main(String args[]) {
    
        unitTest1();

    }

}
