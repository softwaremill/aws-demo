package pl.softwaremill.demo;

import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class MessageAdderServlet extends HttpServlet {
    private AwsAccessKeys awsAccessKeys;

    @Override
    public void init() throws ServletException {
        try {
            awsAccessKeys = AwsAccessKeys.createFromResources();
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Message message = new Message(
                UUID.randomUUID(),
                req.getParameter("room"),
                req.getParameter("content"),
                new DateTime()
        );

        MessagesDomainProvider messagesDomainProvider = new MessagesDomainProvider(awsAccessKeys);
        MessageAdder messageAdder = new MessageAdder(messagesDomainProvider);
        messageAdder.addMessage(message);

        resp.getWriter().print("OK");
    }
}
