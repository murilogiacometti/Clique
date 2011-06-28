package clique.model.core;

import java.security.*;
import java.util.*;
import java.io.*;
import javax.persistence.*;
import org.hibernate.*;
import java.sql.*;

import clique.model.util.*;


@Entity
@Table(name = "Users")
@SequenceGenerator(name = "seqId", sequenceName = "seqUserId")

@NamedQueries({

    @NamedQuery(
        name = "findByEmailPassword", 
        query = "SELECT user FROM User user WHERE user.email = :email AND user.password = :password"
    ) 

})

public class User implements Serializable { 

    @Id
    @Column(name = "userId", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqId")
    private Integer id;

    @Column
    private String firstName;

    @Column
    private String surname;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column
    private Boolean admin;

    private static EntityManagerFactory userManagers;

    static {
        userManager = Persistence.createEntityManagerFactory("UserManager");
    }

    public User() { }

    
    public Integer getId() { return this.id; }
    private void setId(Integer id) { this.id = id; }


    public String getFirstName() { return this.firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }


    public String getSurname() { return this.surname; }
    public void setSurname(String surname) { this.surname = surname; }


    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }


    public String getPassword() { return this.password; }
    public void setPassword(String password) {

        try {

            // Get a message digestor implementing the SHA-1 algorithm 
            MessageDigest sha = MessageDigest.getInstance("SHA");

            // Make a hash of password and store it as a String
            this.password = new String(sha.digest(password.getBytes()), "UTF-8");

        } catch (Exception exception) {
            // Ignore exceptions as they will never happen
        }

    }


    public Boolean isAdmin() { return this.admin; }
    public void setAdmin(Boolean admin) { this.admin = admin; }

    public void persist() {
  
        EntityManager userManager = userManagers.createEntityManager();

        userManager.getTransaction().begin();

        userManager.persist(this);

        userManager.getTransaction().commit();
  
    }

    public void merge() {
    
    }

}
