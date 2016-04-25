
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
@SuppressWarnings("uncheked")
class SaxAspect extends DefaultHandler {
    
    boolean aspect = false;
    @Override
    public void startDocument() throws SAXException {
        
    }

    @Override
    public void endDocument() throws SAXException {
       
        
    }

    @Override
    public void startElement(String uri, String localName,String qName, Attributes attributes)throws SAXException {
        if(qName.equalsIgnoreCase("aspectTerm")){
            NewServlet.aspectTerm.put(attributes.getValue("term"), attributes.getValue("polarity"));
        }
        if(qName.equalsIgnoreCase("aspectCategory")){
            NewServlet.aspectCat.put(attributes.getValue("category"), attributes.getValue("polarity"));
        }
            
        
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        aspect = false;
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        
    }
    
}
