import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import pojo.*;

import java.util.List;

public class ConnectHibernate {

    private static SessionFactory factory;
    public ConnectHibernate(){
        try{
            //noinspection deprecation
            factory = new AnnotationConfiguration()
                    .configure()
                    .addAnnotatedClass(Group.class)
                    .addAnnotatedClass(SportNorm.class)
                    .addAnnotatedClass(Student.class)
                    .addAnnotatedClass(Result.class)
                    .buildSessionFactory();
        }catch (Throwable ex){
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

    }

    public List loadTable(String request){
        List resultList = null;

        Session session = factory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            resultList = session.createQuery(request).list();
        } catch (HibernateException ex){
            if (transaction != null) transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return resultList;
    }

    public <T> int addToTable(T item){
        int id = 0;
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            id = (Integer) session.save(item);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return id;
    }

    public <T>void updateInTable(T item){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(item);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    public <T>void deleteFromTable(T item){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.delete(item);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
}
