package clique.model.util;

import org.hibernate.*;
import org.hibernate.cfg.*;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;
   
    static {
       
        try {
    
            sessionFactory = new AnnotationConfiguration()
                    .configure().buildSessionFactory();

        } catch (Throwable ex) {
            
            System.out.println("SessionFactory doesn't work.");
            
            // Log exception!
            throw new ExceptionInInitializerError(ex);
        
        }

    }

    public static Session openSession()
            throws HibernateException {

        return sessionFactory.openSession();
    
    }
}

