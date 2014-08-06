import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


public class Publisher {
    private String user = ActiveMQConnection.DEFAULT_USER;  
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;  
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;  
    private String subject = "mytopic";  
    private Destination[] destinations = null;  
    private Connection connection = null;  
    private Session session = null;  
    private MessageProducer producer = null;
	
    public Publisher() throws JMSException {
    	ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);  
        connection = factory.createConnection();  
        connection.start();  
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  
        producer = session.createProducer(null); 
    }
    
    protected void setTopics(String[] stocks) throws JMSException {  
        destinations = new Destination[stocks.length];  
        for(int i = 0; i < stocks.length; i++) {  
            destinations[i] = session.createTopic("STOCKS." + stocks[i]);  
        }  
    }  
    
    protected void sendMessage(String[] stocks) throws JMSException {  
        for(int i = 0; i < stocks.length; i++) {  
            Message message = createStockMessage(stocks[i], session);  
            System.out.println("Sending: " + ((TextMessage)message).getText() + " on destination: " + destinations[i]);  
            producer.send(destinations[i], message);  
        }  
    }  
      
    protected Message createStockMessage(String stock, Session session) throws JMSException {  
        TextMessage message = session.createTextMessage("hehe");  
        return message;  
    } 
    
    public void close() throws JMSException {  
        if (connection != null) {  
            connection.close();  
         }  
    }  
    
    public static void main(String[] args) throws JMSException {
    	// Create publisher       
        Publisher publisher = new Publisher();  
         
        String[] stocks = new String[2];
        stocks[0] = "s1";
        stocks[1] = "s2";
        
        // Set topics  
        publisher.setTopics(stocks);  
          
        for(int i = 0; i < 10; i++) {  
	        publisher.sendMessage(stocks);  
	        System.out.println("Publisher '" + i + " messages");  
	        try {  
	            Thread.sleep(1000);  
	        } catch(InterruptedException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    // Close all resources  
	    publisher.close(); 
	    }
}
