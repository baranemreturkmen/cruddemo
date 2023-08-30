package com.javaet.cruddemo.dao;

import com.javaet.cruddemo.entity.Student;
import com.javaet.cruddemo.utils.HibernateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class StudentDAOImpl implements StudentDAO{

    @PersistenceContext
    private EntityManager entityManager;

    //@Autowired
    /*public StudentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }*/
    //Remember the autowired annotation is optional if we only have one constructor!

    @Override
    @Transactional
    public void save(Student student) {
        entityManager.persist(student);
    }

    @Override
    public Student findById(Integer id) {
        return entityManager.find(Student.class,id);
    }

    @Override
    public List<Student> findAll() {
        //create query
        TypedQuery<Student> theQuery = entityManager.createQuery ("FROM Student order by lastName desc", Student.class);
        //return query results
        return theQuery.getResultList();
    }

    @Override
    public List<Student> findByLastName(String theLastName) {
        TypedQuery<Student> theQuery = entityManager.createQuery (
                "FROM Student WHERE lastName=:theData", Student.class);
        //set query parameters
        theQuery.setParameter ( "theData", theLastName);
        //return query results
        return theQuery.getResultList();
    }

    @Override
    @Transactional
    public void update(Student theStudent) {
        entityManager.merge(theStudent);
    }

    @Override
    @Transactional
    public int updateLastNameWithLastName(String lastName,String newLastName) {
        /*TypedQuery<Student> typedQuery = entityManager.createQuery(
                        "UPDATE Student SET lastName=:newLastName WHERE lastName=:lastName",Student.class);
        typedQuery.setParameter("lastName",lastName);
        typedQuery.setParameter("newLastName",newLastName);

        return typedQuery.executeUpdate();

        org.hibernate.query.IllegalQueryOperationException: Result type given for a non-SELECT Query
        Whoops! Hibernate is right we have choose different approach. We need to session factory unfortunately :(*/

        //Session session = HibernateUtils.getSessionFactory().openSession();
        //I don't want to control manually session close I want to control it automatically with @Transactional annotation.
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        int result = 0;
        try{
            transaction = session.beginTransaction();

            String queryString="UPDATE Student SET lastName=:newLastName WHERE lastName=:lastName";
            Query query=session.createQuery(queryString);

            query.setParameter("lastName",lastName);
            query.setParameter("newLastName",newLastName);
            result = query.executeUpdate();
        }
        catch(RuntimeException runtimeException){
            System.out.println(runtimeException.getMessage());
        }
        finally {
            transaction.commit();//whoops! Don't forget to commit if you want to change database values.
            //session.close();
            //HibernateUtils.closeSessionFactory();
            /*
              Factory and Session Close
              You should not close your SessionFactory on every query.
              Your SessionFactory should be initialised only once per application.

              The main contract here is the creation of Session instances. Usually an application has a single
              SessionFactory instance and threads servicing client requests obtain Session instances from this factory.
              The internal state of a SessionFactory is immutable. Once it is created this internal state is set.
              This internal state includes all of the metadata about Object/Relational Mapping.
              Implementors must be threadsafe.
              https://stackoverflow.com/questions/33236407/should-session-and-factory-be-closed

              It all depends on how you obtain the session.

              if you use sessionFactory.getCurrentSession(), you'll obtain a "current session" which is bound to the
              lifecycle of the transaction and will be automatically flushed and closed when the transaction ends
              (commit or rollback).
              if you decide to use sessionFactory.openSession(), you'll have to manage the session yourself and to flush
              and close it "manually".
              https://stackoverflow.com/questions/4040761/control-the-hibernate-sessionwhen-to-close-it-manually
              */
        }
        /*
        It's a good practice to add a try and catch block while using Hibernate.

        There may be several times when you miss one step like starting the database or connection error which can result
        in the stopping of the web application all at once, if that happens you'll have to find the error first and restart
        the server all over again. So if you add a try and catch block the, application goes on running and you'll be able
        to pinpoint the error very easily too.
        https://www.quora.com/Do-we-really-need-a-try-and-catch-block-with-Hibernate
        */
        return result;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Student theStudent = entityManager.find(Student.class, id);
        entityManager.remove(theStudent);
    }

    @Override
    public int deleteStudentWithName(String firstName) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        int result = 0;
        try{
            transaction = session.beginTransaction();

            String queryString="DELETE Student WHERE firstName=:firstName";
            Query query=session.createQuery(queryString);

            query.setParameter("firstName",firstName);
            result = query.executeUpdate();
        }
        catch(RuntimeException runtimeException){
            System.out.println(runtimeException.getMessage());
        }
        finally {
            transaction.commit();//whoops! Don't forget to commit if you want to change database values.
        }
        return result;
    }

    @Override
    @Transactional
    public int deleteAll() {
        int numRowsDeleted = entityManager.createQuery ("DELETE FROM Student").executeUpdate();
        return numRowsDeleted;
    }
}
