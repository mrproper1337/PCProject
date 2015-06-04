    package tableModels;

    import org.hibernate.HibernateException;
    import org.hibernate.Session;
    import org.hibernate.SessionFactory;
    import org.hibernate.Transaction;
    import org.hibernate.cfg.AnnotationConfiguration;
    import pojo.Group;
    import pojo.SportNorm;
    import pojo.SportNormName;
    import pojo.Student;

    import java.util.List;

    public final class ConnectHibernate {

        private static final SessionFactory factory = new AnnotationConfiguration()
                .configure()
                .addAnnotatedClass(Group.class)
                .addAnnotatedClass(SportNorm.class)
                .addAnnotatedClass(SportNormName.class)
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(ResultModel.class)
                .buildSessionFactory();
        private ConnectHibernate(){}

        public static List loadTable(String request){
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

        public static <T> int addToTable(T item){
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

        public static <T>void updateInTable(T item){
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

        public static <T>void deleteFromTable(T item){
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
        public static void sqlQuery(String query){
            Session session = factory.openSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.createSQLQuery(query).executeUpdate();
            } catch (HibernateException ex){
                if (transaction != null) transaction.rollback();
                ex.printStackTrace();
            } finally {
                session.close();
            }
        }
        public static List sqlRequest(String request){
            List resultList = null;

            Session session = factory.openSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                resultList = session.createSQLQuery(request).list();
            } catch (HibernateException ex){
                if (transaction != null) transaction.rollback();
                ex.printStackTrace();
            } finally {
                session.close();
            }
            return resultList;
        }
    }
