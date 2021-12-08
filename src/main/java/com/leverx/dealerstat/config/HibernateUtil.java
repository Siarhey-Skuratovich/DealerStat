package com.leverx.dealerstat.config;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;


public class HibernateUtil {
  private static SessionFactory sessionFactory;
  public static SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      try {
        Configuration configuration = new Configuration();

        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "org.postgresql.Driver");
        settings.put(Environment.URL, "***REMOVED***");
        settings.put(Environment.USER, "***REMOVED***");
        settings.put(Environment.PASS, "***REMOVED***");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL10Dialect");

        settings.put(Environment.SHOW_SQL, "true");

        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

        settings.put(Environment.HBM2DDL_AUTO, "create-drop");

        configuration.setProperties(settings);

        configuration.addAnnotatedClass(com.leverx.dealerstat.model.Game.class);
        configuration.addAnnotatedClass(com.leverx.dealerstat.model.Post.class);
        configuration.addAnnotatedClass(com.leverx.dealerstat.model.GameObject.class);
        configuration.addAnnotatedClass(com.leverx.dealerstat.model.Comment.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return sessionFactory;
  }
}