


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
@SuppressWarnings("uncheked")
public class SAXhandlerParse extends DefaultHandler {
    
    //int idx = 0;
    boolean text = false;
    boolean sentence = false;
    String id=null;
    FileWriter fw;// = new FileWriter("");

    public void startDocument() throws SAXException {
        try {
            fw = new FileWriter("C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\download\\Reviews.txt");
        } catch (IOException ex) {
            Logger.getLogger(SAXhandlerParse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void endDocument() throws SAXException {
        try {
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(SAXhandlerParse.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void startElement(String uri, String localName,String qName, Attributes attributes)throws SAXException {
        if(qName.equalsIgnoreCase("sentence")){
           sentence = true;
           id = attributes.getValue("id");
        }
        if(qName.equalsIgnoreCase("text")){
           text = true;
        }
      
        
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        text = false;
        sentence = false;
    }


    public void characters(char ch[], int start, int length) throws SAXException {
        if(text){
            try {
                fw.write(id+"<-->"+new String(ch,start,length)+"\n");
                //fw.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(SAXhandlerParse.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        /*if(sentence){
            try {
                fw.write(new String(ch,start,length));
                fw.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(SaxHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }*/
    }
    
}
