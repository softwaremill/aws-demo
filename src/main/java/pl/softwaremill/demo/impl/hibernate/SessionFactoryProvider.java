package pl.softwaremill.demo.impl.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * User: szimano
 */
public class SessionFactoryProvider {

    public SessionFactory getSessionFactory() {
        return new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();
    }
}
