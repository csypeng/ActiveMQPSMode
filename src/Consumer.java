import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


public class Consumer {

	private String user = ActiveMQConnection.DEFAULT_USER;  
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;  
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;  
    private String subject = "mytopic";  
    private Destination[] destinations = null;  
    private Connection connection = null;  
    private Session session = null;  
    
    public Consumer() throws JMSException {  
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);  
        connection = factory.createConnection();  
        connection.start();  
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  
    }
    
    
    public static void main(String[] args) throws JMSException {  
        Consumer consumer = new Consumer(); 
        String[] stocks = new String[2];
        stocks[0] = "s1";
        stocks[1] = "s2";
        for (String stock : stocks) {  
        	Destination destination = consumer.getSession().createTopic("STOCKS." + stock);  
        	MessageConsumer messageConsumer = consumer.getSession().createConsumer(destination);  
        	messageConsumer.setMessageListener(new Listener());  
        }  
    }  
          
    public Session getSession() {  
        return session;  
    }

}
