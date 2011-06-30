package clique.model.core;

import java.util.*;
import java.io.*;
import javax.persistence.*;
import org.hibernate.*;
import java.sql.*;

@Entity
@Table(name = "Affixes")
@SequenceGenerator(name = "seqId", sequenceName = "seqAffixId")
public class Affix implements Serializable {

    @Id
    @Column(name = "affixId", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seqId")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String affix;

    @OneToMany(mappedBy = "affix")
    private Set<Word> words = new HashSet();


    public Affix() {}

    public Integer getId() { return this.id; }
    private void setId(Integer id) { this.id = id; }

    public String getAffix() { return this.affix; }
    public void setAffix(String affix) { this.affix = affix; }

    public ArrayList<String> getWords() {
    
        ArrayList<String> words = new ArrayList<String>();
    
        for (Iterator it = this.words.iterator(); it.hasNext(); ) {
            
            words.add(((Word) it.next()).getWord());

        }

        return words;

    }

}
