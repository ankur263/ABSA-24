/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author CRYPTOK
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import nu.xom.Serializer;
import edu.stanford.nlp.pipeline.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import nu.xom.Element;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import nu.xom.Document;

/**
 *
 * @author CRYPTOK
 */

public class SimpleSentence {
    static boolean flag=false;
    static List<Tokens> tokens = new ArrayList<>();
    static List<Integer> edges = new ArrayList<>();
    static List<Integer> starting_points = new ArrayList<>();         //load each time
    static LinkedHashMap<String,String> tags= new LinkedHashMap<>();  //load each time
    int graphDIR[][];
    int graphUNDIR[][];
    
    public void main(String string) throws IOException, SAXException, ParserConfigurationException, TransformerConfigurationException, TransformerException {
        //CoreNLP core = new CoreNLP();
        System.out.println("In simple---------------");
        PrintWriter out = null;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(new File("C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\output\\output.xml"));
        //root element of output file
        org.w3c.dom.Document doc = docBuilder.newDocument();
        org.w3c.dom.Element rootElement = doc.createElement("sentences");
        doc.appendChild(rootElement);
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //while(true){
            flag=false;
            tags.clear();
            starting_points.clear();
            //System.out.println("Enter Sentence");
            String text = string;// br.readLine();
            text = text.replace(".", "");
            text = text.replace("!", "");
            org.w3c.dom.Element sentence = doc.createElement("sentence");
            rootElement.appendChild(sentence);
            sentence.setAttribute("id","1543");
            org.w3c.dom.Element content = doc.createElement("text");
            content.appendChild(doc.createTextNode(text));
            sentence.appendChild(content);
            System.out.println(CoreNLP.auxillary.isEmpty());      
            SentiWordNet sentiwordnet = new SentiWordNet("C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\Files\\Senti.txt");
            Annotation document = new Annotation(text);
            System.out.println(CoreNLP.pipeline);
            CoreNLP.pipeline.annotate(document);
            Document xmldoc = XMLOutputter.annotationToDoc(document, CoreNLP.pipeline);
            ByteArrayOutputStream sw = new ByteArrayOutputStream();
            Serializer ser = new Serializer(sw);
            ser.setIndent(0);
            ser.setLineSeparator("\n");
            ser.write(xmldoc);
            ser.flush();
            String xmlstr = sw.toString();
            xmlstr = xmlstr.replace("\n", "");
            out = new PrintWriter(new File("C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\out.xml"), "UTF-8");
            out.write(xmlstr);
            out.close();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser   saxParser = factory.newSAXParser();
            SaxHandlerSimple handler = new SaxHandlerSimple();
            InputStream    file  = new FileInputStream("C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\out.xml");
            saxParser.parse(file, handler);
                       
            int tolerance = 3;
            int size = tokens.size();
            
            CreateDependencyGraph(size) ;
            FindAspectTerms();
            
            Set<Integer> set = new HashSet<>();
            for(int i=0;i<starting_points.size();i++){
                set.add(starting_points.get(i));
            }
            for(String s:tags.keySet()){
                if(tags.get(s).contains("prep")){
                    String temp[] = s.split(",");
                    if(!CoreNLP.stopWords.containsKey(tokens.get(Integer.parseInt(temp[0])-1).str.toLowerCase()) && tokens.get(Integer.parseInt(temp[0])-1).POS.contains("NN") && (tokens.get(Integer.parseInt(temp[0])-1).NER.equalsIgnoreCase("O") || tokens.get(Integer.parseInt(temp[0])-1).NER.equalsIgnoreCase("Misc"))){
                        set.add(Integer.parseInt(temp[0]));
                    }
                   if(!CoreNLP.stopWords.containsKey(tokens.get(Integer.parseInt(temp[1])-1).str) && tokens.get(Integer.parseInt(temp[1])-1).POS.contains("NN") && (tokens.get(Integer.parseInt(temp[1])-1).NER.equalsIgnoreCase("O") || tokens.get(Integer.parseInt(temp[1])-1).NER.equalsIgnoreCase("Misc"))){
                        set.add(Integer.parseInt(temp[1]));
                    }
                }
                
                if(tags.get(s).contains("nn")){
                    String temp[] = s.split(",");
                    if(!CoreNLP.stopWords.containsKey(tokens.get(Integer.parseInt(temp[0])-1).str.toLowerCase()) && tokens.get(Integer.parseInt(temp[0])-1).POS.contains("NN") && (tokens.get(Integer.parseInt(temp[0])-1).NER.equalsIgnoreCase("O") || tokens.get(Integer.parseInt(temp[0])-1).NER.equalsIgnoreCase("Misc"))){
                        set.add(Integer.parseInt(temp[0]));
                    }
                    if(!CoreNLP.stopWords.containsKey(tokens.get(Integer.parseInt(temp[1])-1).str.toLowerCase()) && tokens.get(Integer.parseInt(temp[1])-1).POS.contains("NN") && (tokens.get(Integer.parseInt(temp[1])-1).NER.equalsIgnoreCase("O") || tokens.get(Integer.parseInt(temp[1])-1).NER.equalsIgnoreCase("Misc"))){
                        set.add(Integer.parseInt(temp[1]));
                    }
                }
                if(tags.get(s).contains("amod")){
                    String temp[] = s.split(",");
                    if(!CoreNLP.stopWords.containsKey(tokens.get(Integer.parseInt(temp[0])-1).str.toLowerCase()) && tokens.get(Integer.parseInt(temp[0])-1).POS.contains("NN") && (tokens.get(Integer.parseInt(temp[0])-1).NER.equalsIgnoreCase("O") || tokens.get(Integer.parseInt(temp[0])-1).NER.equalsIgnoreCase("Misc"))){
                        set.add(Integer.parseInt(temp[0]));
                    }
                    if(!CoreNLP.stopWords.containsKey(tokens.get(Integer.parseInt(temp[1])-1).str.toLowerCase()) && tokens.get(Integer.parseInt(temp[1])-1).POS.contains("NN") && (tokens.get(Integer.parseInt(temp[1])-1).NER.equalsIgnoreCase("O") || tokens.get(Integer.parseInt(temp[1])-1).NER.equalsIgnoreCase("Misc"))){
                        set.add(Integer.parseInt(temp[1]));
                    }
                }
                if(tags.get(s).contains("conj")){
                    String temp[] = s.split(",");
                    if(!CoreNLP.stopWords.containsKey(tokens.get(Integer.parseInt(temp[0])-1).str.toLowerCase()) && tokens.get(Integer.parseInt(temp[0])-1).POS.contains("NN") && (tokens.get(Integer.parseInt(temp[0])-1).NER.equalsIgnoreCase("O") || tokens.get(Integer.parseInt(temp[0])-1).NER.equalsIgnoreCase("Misc"))){
                        set.add(Integer.parseInt(temp[0]));
                    }
                   if(!CoreNLP.stopWords.containsKey(tokens.get(Integer.parseInt(temp[1])-1).str.toLowerCase()) && tokens.get(Integer.parseInt(temp[1])-1).POS.contains("NN") && (tokens.get(Integer.parseInt(temp[1])-1).NER.equalsIgnoreCase("O") || tokens.get(Integer.parseInt(temp[1])-1).NER.equalsIgnoreCase("Misc"))){
                        set.add(Integer.parseInt(temp[1]));
                    }
                }
            }
            
            Object arr[] = set.toArray();
            System.out.println("before traversal");
            List<objects> word = traversal(arr,size,tolerance,sentiwordnet);
            Collections.sort(word, new comp());
            System.out.println(word.size());  
            System.out.println("after traversal");
            if(word.size()>0){
                int last=word.get(0).x;
                int to = 0;
                int current;
                StringBuilder sb = new StringBuilder();
                sb.append(tokens.get(word.get(0).x).str);
                sb.append(" ");
                double score=word.get(0).score;
                org.w3c.dom.Element aspecterms = doc.createElement("aspectTerms");
                sentence.appendChild(aspecterms);
                
                for(int x=1;x<word.size();x++){
                    current = word.get(x).x;
                    System.out.println("current"+current);
                    if(current == last+1){
                        score += word.get(x).score;
                    }
                    else{
                        if(score>0){
                            org.w3c.dom.Element aspectTerm = doc.createElement("aspectTerm");
                            aspecterms.appendChild(aspectTerm);
                            aspectTerm.setAttribute("term", sb.toString().trim());
                            aspectTerm.setAttribute("polarity", "positive");
                            aspectTerm.setAttribute("from", Integer.toString(tokens.get(word.get(to).x).begin).trim());
                            aspectTerm.setAttribute("to", Integer.toString(tokens.get(word.get(x-1).x).end).trim());
                            //System.out.print(sb.toString()+" "+"positive  "+tokens.get(word.get(x-1).x).begin+" "+tokens.get(word.get(x-1).x).end+",");
                        }
                        else if(score<0){
                            org.w3c.dom.Element aspectTerm = doc.createElement("aspectTerm");
                            aspecterms.appendChild(aspectTerm);
                            aspectTerm.setAttribute("term", sb.toString().trim());
                            aspectTerm.setAttribute("polarity", "negative");
                            aspectTerm.setAttribute("from", Integer.toString(tokens.get(word.get(to).x).begin).trim());
                            aspectTerm.setAttribute("to", Integer.toString(tokens.get(word.get(x-1).x).end).trim());
                            //System.out.print(sb.toString()+" "+"negative "+tokens.get(word.get(x-1).x).begin+" "+tokens.get(word.get(x-1).x).end+",");
                        }
                        else{
                            org.w3c.dom.Element aspectTerm = doc.createElement("aspectTerm");
                            aspecterms.appendChild(aspectTerm);
                            aspectTerm.setAttribute("term", sb.toString().trim());
                            aspectTerm.setAttribute("polarity", "neutral");
                            aspectTerm.setAttribute("from", Integer.toString(tokens.get(word.get(to).x).begin).trim());
                            aspectTerm.setAttribute("to", Integer.toString(tokens.get(word.get(x-1).x).end).trim());
                            //System.out.print(sb.toString()+" "+"neutral "+tokens.get(word.get(x-1).x).begin+" "+tokens.get(word.get(x-1).x).end+",");
                        }
                        score = word.get(x).score;
                        sb.setLength(0);
                        to = x;
                    }
                    sb.append(tokens.get(word.get(x).x).str);
                    sb.append(" ");
                    last = current;
                }
                if(score>0){
                    org.w3c.dom.Element aspectTerm = doc.createElement("aspectTerm");
                    aspecterms.appendChild(aspectTerm);
                    aspectTerm.setAttribute("term", sb.toString().trim());
                    aspectTerm.setAttribute("polarity", "positive");
                    aspectTerm.setAttribute("from", Integer.toString(tokens.get(word.get(to).x).begin).trim());
                    aspectTerm.setAttribute("to", Integer.toString(tokens.get(word.get(word.size()-1).x).end).trim());
                    //System.out.print(sb.toString()+" "+"positive  "+tokens.get(word.get(word.size()-1).x).begin+" "+tokens.get(word.get(word.size()-1).x).end);
                }
                else if(score<0){
                    org.w3c.dom.Element aspectTerm = doc.createElement("aspectTerm");
                    aspecterms.appendChild(aspectTerm);
                    aspectTerm.setAttribute("term", sb.toString().trim());
                    aspectTerm.setAttribute("polarity", "negative");
                    aspectTerm.setAttribute("from", Integer.toString(tokens.get(word.get(to).x).begin).trim());
                    aspectTerm.setAttribute("to", Integer.toString(tokens.get(word.get(word.size()-1).x).end).trim());
                    //System.out.print(sb.toString()+" "+"negative "+tokens.get(word.get(word.size()-1).x).begin+" "+tokens.get(word.get(word.size()-1).x).end);
                }
                else{
                    org.w3c.dom.Element aspectTerm = doc.createElement("aspectTerm");
                    aspecterms.appendChild(aspectTerm);
                    aspectTerm.setAttribute("term", sb.toString().trim());
                    aspectTerm.setAttribute("polarity", "neutral");
                    aspectTerm.setAttribute("from", Integer.toString(tokens.get(word.get(to).x).begin).trim());
                    aspectTerm.setAttribute("to", Integer.toString(tokens.get(word.get(word.size()-1).x).end).trim());
                    //System.out.print(sb.toString()+" "+"neutral "+tokens.get(word.get(word.size()-1).x).begin+" "+tokens.get(word.get(word.size()-1).x).end);
                }
                //System.out.println("");
            }
            System.out.println("out");
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            out.close();
            //insert here 
            File f = new File("C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\out.xml");
            //f.delete();
    }

    private void FindAspectTerms() {
        if(flag){
            //Rule 1 starts
            for(int i=1;i<=tokens.size();i++){
                for(int j=1;j<=tokens.size();j++){
                    if(graphDIR[i][j] == 1){
                        String s = i+","+j;
                        if(tags.get(s).equalsIgnoreCase("nsubj") || tags.get(s).equalsIgnoreCase("dobj")){
                            if(tokens.get(i-1).POS.contains("NN"))
                                starting_points.add(i);
                            if(tokens.get(j-1).POS.contains("NN"))
                                starting_points.add(j);
                            for(int k=1;k<=tokens.size();k++){
                                if(graphDIR[j][k] == 1 && k!=i && k!=j && (tokens.get(k-1).POS.contains("RB") || tokens.get(k-1).POS.contains("JJ"))){
                                    if(CoreNLP.senticnet.containsKey(tokens.get(k-1).str))
                                        starting_points.add(j);
                                }
                            }
                        }
                    }
                }
            }
            //System.out.println(starting_points.size()+" Before rule2");
            //Rule 2 starts
            boolean aux_flag=false;
            for(int i=0;i<tokens.size();i++){
                if(CoreNLP.auxillary.containsKey(tokens.get(i).str.toLowerCase().trim())){
                    aux_flag=true;
                    break;
                }
            }

            //Rule 2.1
            if(!aux_flag){
                for(int i=1;i<=tokens.size();i++){
                    for(int j=1;j<=tokens.size();j++){
                        if(graphDIR[tokens.get(i-1).index][tokens.get(j-1).index] == 1){
                            String s = Integer.toString(i)+","+Integer.toString(j);
                            if(tags.get(s).equalsIgnoreCase("nsubj") || tags.get(s).equalsIgnoreCase("dobj")){
                                if(tokens.get(i-1).POS.contains("NN"))
                                    starting_points.add(i);
                                if(tokens.get(j-1).POS.contains("NN"))
                                    starting_points.add(j);
                                if(tokens.get(j-1).POS.contains("VB")){
                                    for(int k=1;k<=tokens.size();k++){
                                        if(graphDIR[j][k] == 1 && (tokens.get(k-1).POS.contains("RB") || tokens.get(k-1).POS.contains("JJ"))){
                                            starting_points.add(j);
                                            starting_points.add(i);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //Rule 2.2
                for(int i=1;i<=tokens.size();i++){
                    for(int j=1;j<=tokens.size();j++){
                        if(graphDIR[tokens.get(i-1).index][tokens.get(j-1).index] == 1){
                            String s = Integer.toString(i)+","+Integer.toString(j);
                            if(tags.get(s).equalsIgnoreCase("nsubj") || tags.get(s).equalsIgnoreCase("dobj")){
                                if(tokens.get(i-1).POS.contains("NN"))
                                    starting_points.add(i);
                                if(tokens.get(j-1).POS.contains("NN"))
                                    starting_points.add(j);
                                for(int k=1;k<=tokens.size();k++){
                                    String temp = j+","+k;
                                    if(graphDIR[j][k] == 1 && tags.get(temp).equalsIgnoreCase("dobj") && tokens.get(k-1).POS.contains("NN") &&!CoreNLP.senticnet.containsKey(tokens.get(k-1).str.toLowerCase().trim())){
                                        starting_points.add(k);
                                    }
                                }
                               
                            }
                        }
                    }
                }

                //Rule2.3
                for(int i=1;i<=tokens.size();i++){
                    for(int j=1;j<=tokens.size();j++){
                        if(graphDIR[tokens.get(i-1).index][tokens.get(j-1).index] == 1){
                            String s = Integer.toString(i)+","+Integer.toString(j);
                            if(tags.get(s).equalsIgnoreCase("nsubj") || tags.get(s).equalsIgnoreCase("dobj")){
                                if(tokens.get(i-1).POS.contains("NN"))
                                    starting_points.add(i);
                                if(tokens.get(j-1).POS.contains("NN"))
                                    starting_points.add(j);
                                for(int k=1;k<=tokens.size();k++){
                                    String temp = j+","+k;
                                    if(graphDIR[j][k] == 1 && tags.get(temp).equalsIgnoreCase("dobj") && tokens.get(k-1).POS.contains("NN") && CoreNLP.senticnet.containsKey(tokens.get(k-1).str.toLowerCase().trim())){
                                        starting_points.add(k);
                                        for(int m=1;m<=tokens.size();m++){
                                            if(graphDIR[k][m] == 1 && tokens.get(m-1).POS.contains("NN")){
                                                starting_points.add(m);
                                            }
                                        }
                                    }
                                }
                               
                            }
                        }
                    }
                }

                //Rule2.4
                for(int i=1;i<=tokens.size();i++){
                    for(int j=1;j<=tokens.size();j++){
                        if(graphDIR[tokens.get(i-1).index][tokens.get(j-1).index] == 1){
                            String s = Integer.toString(i)+","+Integer.toString(j);
                            if(tags.get(s).equalsIgnoreCase("nsubj") || tags.get(s).equalsIgnoreCase("dobj")){
                                if(tokens.get(i-1).POS.contains("NN"))
                                    starting_points.add(i);
                                if(tokens.get(j-1).POS.contains("NN"))
                                    starting_points.add(j);
                                for(int k=1;k<=tokens.size();k++){
                                    String temp = j+","+k;
                                    String temp1 = tokens.get(j-1).str.toLowerCase().trim() + "-" +  tokens.get(k-1).str.toLowerCase().trim();
                                    if(graphDIR[j][k] == 1 && tags.get(temp).equalsIgnoreCase("xcomp")){
                                        if((CoreNLP.positiveLex.containsKey(temp1) || CoreNLP.negativeLex.containsKey(temp1))){
                                            starting_points.add(j);
                                            starting_points.add(k);
                                        }
                                    }
                                    for(int m=1;m<=tokens.size();m++){
                                        if(graphDIR[k][m] == 1 && tokens.get(m-1).POS.contains("NN")){// && nsubj_flag && xcomp_flag){
                                            starting_points.add(m);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //Rule 3
            for(int i=1;i<=tokens.size();i++){
                for(int j=1;j<=tokens.size();j++){
                    if(graphDIR[i][j] == 1){
                        String s = i+","+j;
                        if(tags.get(s).equalsIgnoreCase("nsubj") || tags.get(s).equalsIgnoreCase("dobj")){
                            if(tokens.get(i-1).POS.contains("NN"))
                                starting_points.add(i);
                            if(tokens.get(j-1).POS.contains("NN"))
                                starting_points.add(j);
                            for(int k=1;k<=tokens.size();k++){
                                String temp = j+","+k;
                                if(graphDIR[j][k] == 1 && tags.get(temp).equalsIgnoreCase("cop") && tokens.get(k-1).POS.contains("VB")){
                                    starting_points.add(j);
                                }
                            }
                        }
                    }
                }
            }

            //Rule 4
            for(int i=1;i<=tokens.size();i++){
                for(int j=1;j<=tokens.size();j++){
                    if(graphDIR[tokens.get(i-1).index][tokens.get(j-1).index] == 1){
                        String s = Integer.toString(i)+","+Integer.toString(j);
                        if((tags.get(s).equalsIgnoreCase("nsubj") || tags.get(s).equalsIgnoreCase("dobj")) && tokens.get(i-1).POS.contains("NN")){
                            if(tokens.get(i-1).POS.contains("NN"))
                                starting_points.add(i);
                            if(tokens.get(j-1).POS.contains("NN"))
                                starting_points.add(j);
                            for(int k=1;k<=tokens.size();k++){
                                String temp = j+","+k;
                                if(graphDIR[j][k] == 1 && tags.get(temp).equalsIgnoreCase("cop") && tokens.get(k-1).POS.contains("VB")){
                                    starting_points.add(i);
                                }
                            }
                        }
                    }
                }
            }

            //Rule5
            for(int i=1;i<=tokens.size();i++){
                for(int j=1;j<=tokens.size();j++){
                    if(graphDIR[tokens.get(i-1).index][tokens.get(j-1).index] == 1){
                        String s = Integer.toString(i)+","+Integer.toString(j);
                        if(tags.get(s).equalsIgnoreCase("nsubj") || tags.get(s).equalsIgnoreCase("dobj")){
                            if(tokens.get(i-1).POS.contains("NN"))
                                starting_points.add(i);
                            if(tokens.get(j-1).POS.contains("NN"))
                                starting_points.add(j);
                            for(int k=1;k<=tokens.size();k++){
                                String temp = j+","+k;
                                if(graphDIR[j][k] == 1 && tags.get(temp).equalsIgnoreCase("cop") && tokens.get(k-1).POS.contains("VB")){
                                    for(int m=1;m<=tokens.size();m++){
                                        if(graphDIR[j][m] == 1 && m!=k && tokens.get(m-1).POS.contains("VB")){
                                            starting_points.add(j);
                                            starting_points.add(m);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private  void CreateDependencyGraph(int size) {
        graphDIR = new int[size+1][size+1];
        graphUNDIR = new int[size+1][size+1];
        for(int i=2;i<edges.size();i+=2){
            int j = edges.get(i);
            int k = edges.get(i+1);
            graphDIR[j][k] = 1;// graph[k][j] = 1;
            graphUNDIR[j][k] = graphUNDIR[k][j] = 1;
        }
    }
    
    private List<objects> traversal(Object starting_points[], int size, int tolerance, SentiWordNet sentiwordnet) throws IOException {
        List<objects> list = new ArrayList<>();
        for(int i=0;i<starting_points.length;i++){
            List<Tokens> temp = new ArrayList<>();
            int start = (int)starting_points[i];
            int distance[] = new int[size+1];
            for(int j=0;j<=size;j++)
                distance[j] = -1;
            BFS(start,graphUNDIR,size,distance);
            int min=100000;
            int index=-1;
            String tag1 = null,tag2 = null;
            for(int j=1;j<=size;j++){
                if(distance[j]!=-1 && distance[j]<=tolerance && ( tokens.get(j-1).POS.contains("JJ") || tokens.get(j-1).POS.contains("RB"))){
                    if(distance[j]<min ){
                        min = distance[j];
                        index = j;
                        temp.add(tokens.get(j-1));
                    }
                    else if(distance[j] == min){
                        if(tokens.get(index-1).POS.contains("JJ"))
                            tag1 = "a";
                        if(tokens.get(index-1).POS.contains("RB"))
                            tag1 = "r";
                        if(tokens.get(j-1).POS.contains("JJ"))
                            tag2 = "a";
                        if(tokens.get(j-1).POS.contains("RB"))
                            tag2 = "r";
                        try{
                            if(Math.abs(sentiwordnet.extract(tokens.get(index-1).str, tag1)) < Math.abs(sentiwordnet.extract(tokens.get(j-1).str, tag2))){
                                index = j;
                            }
                        }
                        catch(Exception e){e.printStackTrace();}
                        tag1 = "";
                        tag2 = "";
                        //index = -11;
                    }
                }
            }
            String tag;
            objects o = null;
            if(index!=-1 && tokens.get(index-1).POS.contains("JJ")){
                tag = "a";
                try{
                    if(!tokens.get((int)starting_points[i]-1).POS.contains("JJ") && (tokens.get((int)starting_points[i]-1).NER.equalsIgnoreCase("Misc")|| tokens.get((int)starting_points[i]-1).NER.equalsIgnoreCase("O")) && !CoreNLP.stopWords.containsKey(tokens.get((int)starting_points[i]-1).str.toLowerCase())){
                        o = new objects();
                        o.x = (int)starting_points[i]-1;
                        o.score = sentiwordnet.extract(tokens.get(index-1).str.toLowerCase(), tag);
                        list.add(o);
                        //System.out.println(CoreNLP.tokens.get((int)starting_points[i]-1).str+"-->"+CoreNLP.tokens.get(index-1).str+"  "+sentiwordnet.extract(CoreNLP.tokens.get(index-1).str, tag));
                    }
                }
                catch(Exception e){o.score = 0.0;list.add(o);e.printStackTrace();}
            }   
            else if(index!=-1 && tokens.get(index-1).POS.contains("RB")){
                tag = "r";
                try{
                    if(!tokens.get((int)starting_points[i]-1).POS.contains("RB") && (tokens.get((int)starting_points[i]-1).NER.equalsIgnoreCase("Misc")|| tokens.get((int)starting_points[i]-1).NER.equalsIgnoreCase("O")) && !CoreNLP.stopWords.containsKey(tokens.get((int)starting_points[i]-1).str.toLowerCase())){
                        o = new objects();
                        o.x = (int)starting_points[i]-1;
                        o.score = sentiwordnet.extract(tokens.get(index-1).str.toLowerCase(), tag);
                        list.add(o);
                        //System.out.println(CoreNLP.tokens.get((int)starting_points[i]-1).str+"-->"+CoreNLP.tokens.get(index-1).str+"  "+sentiwordnet.extract(CoreNLP.tokens.get(index-1).str, tag));
                    }
                }
                catch(Exception e){o.score = 0.0;list.add(o);e.printStackTrace();}
            }
            else{
                try{
                    o = new objects();
                    o.x = (int)starting_points[i]-1;
                    o.score = 0.00;
                    for(int x=1;x<=tokens.size();x++){
                        if(graphUNDIR[x][(int)starting_points[i]] == 1 && tags.get(x+","+starting_points[i]).contains("dobj")){
                            if(tokens.get(x-1).POS.contains("VB")){
                                o.score = sentiwordnet.extract(tokens.get(x-1).str.toLowerCase(), "v");
                            }
                        }
                    }
                    list.add(o);
                    //System.out.println(CoreNLP.tokens.get((int)starting_points[i]-1).str+"-->No JJ or RB near by  " +o.score);
                }
                catch(Exception e){o.score = 0.0;list.add(o);e.printStackTrace();}
            }
        }
        return list;
    }

    private  void BFS(int v, int[][] graph, int size, int distance[]) {
        Queue<Integer> q = new LinkedList<>();
        q.add(v);
        distance[v] = 0;
        while(!q.isEmpty()){
            v = q.remove();
            for(int i=1;i<=size;i++){
                if(graph[i][v] == 1 && distance[i] == -1){
                    q.add(i);
                    distance[i] = distance[v]+1;
                }
            }
        }
    }
}
