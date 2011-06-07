package pl.softwaremill.demo.impl.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.joda.time.DateTime;
import pl.softwaremill.demo.entity.Message;
import pl.softwaremill.demo.service.MessageAdder;

/**
 * User: szimano
 */
public class HibernateMessageAdder implements MessageAdder {

    private SessionFactory sessionFactory;

    public HibernateMessageAdder(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addMessage(Message msg) {
        if (msg.getSaveDate() == null)
            msg.setSaveDate(new DateTime());

        Session session = sessionFactory.openSession();

        session.beginTransaction();

        session.merge(msg);

        session.getTransaction().commit();
        session.close();
    }
}
