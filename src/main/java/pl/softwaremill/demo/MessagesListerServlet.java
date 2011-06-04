package pl.softwaremill.demo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class MessagesListerServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MessagesDomainProvider messagesDomainProvider = new MessagesDomainProvider(awsAccessKeys);
        MessagesLister messagesLister = new MessagesLister(messagesDomainProvider);
        List<Message> messages = messagesLister.listRecentMessages("confitura");

        MessageListSerializer messageListSerializer = new MessageListSerializer();
        String serialized = messageListSerializer.serialize(messages);

        resp.setContentType("application/json");
        resp.getWriter().print(serialized);
    }
}
