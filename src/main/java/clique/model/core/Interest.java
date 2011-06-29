package clique.model.core;

import java.util.*;
import java.io.*;
import javax.persistence.*;
import org.hibernate.*;
import java.sql.*;

@Entity
@Table(name = "Interests")
@SequenceGenerator(name = "seqId", sequenceName = "seqInterestId")
public class Interest implements Serializable {

    @Id
    @Column(name = "interestId", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqId")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String affix;

    @OneToMany(mappedBy = "interest")
    private Set<InterestPerson> interestPersons = new HashSet();

    public Interest() {}

    public String getAffix() { return this.affix; }

    public void setAffix(String affix) { this.affix = affix; }

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


}
