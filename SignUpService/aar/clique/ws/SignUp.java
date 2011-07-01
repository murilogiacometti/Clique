package clique.ws;

import clique.model.util.*;
import clique.model.core.*;
import org.hibernate.*;

public class SignUp {

    public void directSignUp(String name, String email, String password, String address) {

        User user = new User();
        
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setAddress(address);

        Session context = HibernateUtil.openContext();
        user.save(context);
        HibernateUtil.closeContext(context);

    }

    public void indirectSignUp(String name) {

        Person person = new Person();
        person.setName(name);
        
        Session context = HibernateUtil.openContext();
        person.save(context);
        HibernateUtil.closeContext(context);
        
    }


}
