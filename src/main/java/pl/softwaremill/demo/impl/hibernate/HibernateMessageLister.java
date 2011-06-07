package pl.softwaremill.demo.impl.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import pl.softwaremill.demo.entity.Message;
import pl.softwaremill.demo.service.MessagesLister;

import java.util.List;

/**
 * User: szimano
 */
public class HibernateMessageLister implements MessagesLister {

    private SessionFactory sessionFactory;

    public HibernateMessageLister(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Message> listRecentMessages(String room) {
        Session session = sessionFactory.openSession();

        return session.createQuery("select m from Message m where m.room = :room order by m.date desc")
                .setParameter("room", room).setMaxResults(100).list();
    }
}
