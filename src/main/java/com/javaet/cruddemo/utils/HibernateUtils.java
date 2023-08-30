package com.javaet.cruddemo.utils;

import com.javaet.cruddemo.entity.Student;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtils {

    public static SessionFactory getSessionFactory(){
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();

        return factory;
    }

    public static void closeSessionFactory(){
        getSessionFactory().close();
    }

}
