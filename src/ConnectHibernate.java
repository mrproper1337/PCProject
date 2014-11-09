
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.AnnotationConfiguration;
import pojo.*;

import java.util.Iterator;
import java.util.List;

public class ConnectHibernate {

    private static SessionFactory factory;
    public ConnectHibernate(){

    }

    public static void main(String[] args) {

//        try{
//            factory = new Configuration().configure().buildSessionFactory();
//        }catch (Throwable ex) {
//            System.err.println("Failed to create sessionFactory object." + ex);
//            throw new ExceptionInInitializerError(ex);
//        }

        try{
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

        ConnectHibernate cs=new ConnectHibernate();
        cs.listGroups();
    }

    public List loadTable(String request){
        List resultList = null;
//hibernate load
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

    public <T> int addTotable(T item){
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

    public void  listGroups( ){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            List<Group> groups = session.createQuery("FROM Group").list();

            for (int i=0;i<4;i++){
                Group group = groups.get(i);
                System.out.print("Id: " + group.getGroupId());
                System.out.println("  Name: " + group.getGroupName());
            }
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
}
