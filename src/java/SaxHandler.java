
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
class SaxHandler extends DefaultHandler {
    
    //int idx = 0;
    boolean word = false;
    boolean POS = false;
    boolean NER = false;
    boolean dependencies = false;
    boolean dep = false;
    boolean begin = false;
    boolean end = false;
    Tokens tok;
    String s="";
    String tag="";
    @Override
    public void startDocument() throws SAXException {
        CoreNLP.edges.clear();
        CoreNLP.tokens.clear();
    }

    @Override
    public void endDocument() throws SAXException {
       
        
    }

    @Override
    public void startElement(String uri, String localName,String qName, Attributes attributes)throws SAXException {
        if(qName.equalsIgnoreCase("token")){
            tok = new Tokens();
            tok.index = Integer.parseInt(attributes.getValue("id"));
        }
        if(qName.equalsIgnoreCase("word")){
            word = true;
        }
        if(qName.equalsIgnoreCase("CharacterOffsetBegin")){
            begin = true;
        }
        if(qName.equalsIgnoreCase("CharacterOffsetEnd")){
            end = true;
        }
        if(qName.equalsIgnoreCase("POS")){
            POS = true;
        }
        if(qName.equalsIgnoreCase("NER")){
            NER = true;
        }
        if(qName.equalsIgnoreCase("dependencies")){
            if(attributes.getValue("type").equalsIgnoreCase("collapsed-dependencies")){
                dependencies = true;
            }
        }
        if(qName.equalsIgnoreCase("dep")){
            if(dependencies){
                tag = attributes.getValue("type");
                if(tag.equalsIgnoreCase("nsubj"))
                    CoreNLP.flag=true;
                //CoreNLP.tag.add(attributes.getValue("type"));
            }
        }
        if(qName.equalsIgnoreCase("governor")){
            if(dependencies){
                CoreNLP.edges.add(Integer.parseInt(attributes.getValue("idx")));
                s += attributes.getValue("idx");
            }
        }
        if(qName.equalsIgnoreCase("dependent")){
           if(dependencies){
                CoreNLP.edges.add(Integer.parseInt(attributes.getValue("idx")));
                s +=","+attributes.getValue("idx");
                CoreNLP.tags.put(s, tag);
                String str[] = s.split(",");
                CoreNLP.tags.put(str[1]+","+str[0],tag);
                s="";
            }
        }
        
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equalsIgnoreCase("token")){
            CoreNLP.tokens.add(tok);
        }
        if(qName.equalsIgnoreCase("dependencies"))
            dependencies = false;
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if(word){
            tok.str = new String(ch,start,length);
            word = false;
        }
        if(begin){
            tok.begin = Integer.parseInt(new String(ch,start,length));
            begin = false;
        }
        if(end){
            tok.end = Integer.parseInt(new String(ch,start,length));
            end = false;
        }
        if(POS){
            tok.POS = new String(ch,start,length);
            POS = false;
        }
        if(NER){
            tok.NER = new String(ch,start,length);
            NER = false;
        }
        
    }
    
}
