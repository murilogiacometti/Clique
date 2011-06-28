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
@PrimaryKeyJoinColumn(name = "personId")

@NamedQueries({

    @NamedQuery(
        name = "findByEmailPassword", 
        query = "SELECT user FROM User user WHERE user.email = :email AND user.password = :password"
    ) 

})

public class User extends Person { 

    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;

    @Column
    private String address;

    @Column
    private Boolean facebook;


    private static org.hibernate.Session userManager;

    static {
        userManager = HibernateUtil.openSession();
    }

    public User() { }


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

    public String getAddress() { return this.address; }
    public void setAddress(String address) { this.address = address; }
    
    public Boolean getFacebook() { return this.facebook; }
    public void setFacebook(Boolean facebook) { this.facebook = facebook; }

    public void save() {
  
        userManager.beginTransaction();

        userManager.save(this);

        userManager.getTransaction().commit();
  
    }

    public void merge() {
 
        userManager.beginTransaction();

        userManager.merge(this);

        userManager.getTransaction().commit();
    
    }


    /**
      * Retrieve an user from database, checking its email and password.
      *
      * @param email    User's mail string.
      * @param password User's password string.
      * @return User corresponding to given email and password.
      */
    public static User findByEmailPassword(String email, String password) {

        String hashedPassword = null;

        try {

            // Get a message digestor implementing the SHA-1 algorithm 
            MessageDigest sha = MessageDigest.getInstance("SHA");

            // Make a hash of password and store it as a String
            hashedPassword = new String(sha.digest(password.getBytes()), "UTF-8");

        } catch (Exception exception) {
            // Ignore exception as they will never happen
        }

        // Get database connection
        userManager.beginTransaction();

        // Load query as specified in annotations by its name
        org.hibernate.Query query = userManager.getNamedQuery("findByEmailPassword");

        // Prepare query with parameters
        query.setParameter("email", email);
        query.setParameter("password", hashedPassword);

        // Execute query returning a single result: the user we are looking for
        User user = (User) query.uniqueResult();
        
        // Commit transaction
        userManager.getTransaction().commit();
        
        return user;
    }


    private static void unitTest1() {
    
        User user = new User();
        user.setName("Name");
        user.setPassword("password");
        user.setEmail("student@usp.com");
        user.save();
    
    }

    private static void unitTest2() {
    
        User user = new User();
        user.setName("Name");
        user.setPassword("password");
        user.setEmail("student@usp.com");
        user.save();

        user.setName("NewName");

        user.merge();

    }

    private static void unitTest3() {
    
        User user1 = new User();
        user1.setName("Name");
        user1.setPassword("password");
        user1.setEmail("student@usp.com");
        user1.save();

        User user2 = User.findByEmailPassword("student@usp.com", "password");
        
        if (user2 != null) {
        
            user2.setName("NewName");
            user2.merge();
            System.out.println(user1.getName()); // Imprime NewName
        
        }

    }


    public static void main(String args[]) {
    
        unitTest3();

    }

}
