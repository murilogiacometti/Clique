package clique.model.core;

import java.util.*;
import java.io.*;
import javax.persistence.*;
import org.hibernate.*;
import java.sql.*;

import clique.model.util.*;

@Entity
@Table(
    name = "InterestsPeople",
    uniqueConstraints = { @UniqueConstraint(columnNames = {"interestId", "personId"}) }
)
@SequenceGenerator(name = "seqId", sequenceName = "seqInterestPersonId")
public class InterestPerson implements Serializable {

    @Id
    @Column(name = "interestPersonId", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "interestId")
    private Interest interest;
    
    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;

    @Column
    private Float weight;

    public Long getId() { return this.id; }
    private void setId(Long id) { this.id = id; }

    public Interest getInterest() { return this.interest; }
    public void setInterest(Interest interest) { this.interest = interest; }
    
    public Float getWeight() { return this.weight; }
    public void setWeight(Float weight) { this.weight = weight; }

    public Person getPerson() { return this.person; }
    public void setPerson(Person person) { this.person = person; }

    public void save(Session context) {

        context.beginTransaction();

        context.save(this);

        context.getTransaction().commit();
    
    }

    private static void unitTest1() {
    
        Session context = HibernateUtil.openContext();

        Interest interest = new Interest();
        interest.setAffix("test");
        interest.save(context);
        
        Person person = new Person();
        person.setName("Name");
        person.save(context);

        person.add(interest, new Float(0.7), context);

        HibernateUtil.closeContext(context);
    
    }

    public static void main(String args[]) {
    
        unitTest1();
    }

}
