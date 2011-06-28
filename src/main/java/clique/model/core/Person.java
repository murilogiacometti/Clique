package clique.model.core;

import java.util.*;
import java.io.*;
import javax.persistence.*;
import org.hibernate.*;
import java.sql.*;

import clique.model.util.*;

@Entity
@Table(name = "Persons")
@SequenceGenerator(name = "seqId", sequenceName = "seqPersonId")
@Inheritance(strategy = InheritanceType.JOINED)

@NamedQueries({


})

public class Person implements Serializable { 

    @Id
    @Column(name = "personId", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqId")
    protected Integer id;

    @Column
    protected String name;

    public Integer getId() { return this.id; }
    protected void setId(Integer id) { this.id = id; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

}


