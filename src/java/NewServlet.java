/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author CRYPTOK
 */
public class NewServlet extends HttpServlet {
    static HashMap<String,String> aspectTerm  = new HashMap<>();
    static HashMap<String,String> aspectCat  = new HashMap<>();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SAXException, ParserConfigurationException, TransformerException, InterruptedException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            System.out.println("called--------------------------");
            aspectTerm.clear();
            aspectCat.clear();
            out.println("<html>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\"/>");
            out.println("<link href=\"CSS/nlp.css\" rel=\"stylesheet\" type=\"text/css\" />");
            out.println("<title>ABSA</title>");
            out.println("<style type=\"text/css\">");
            out.println("<!--");
            out.println("#Footer {");
            out.println("position: relative;");
            out.println("bottom: 0px;");
            out.println("}");
            out.println("-->");
            out.println("</style>");
            
            out.println("<body>");
            out.println("<div>");
            out.println("<h1>ABSA-24</h1>");
            out.println("<FORM name=\"myform\" METHOD=\"POST\" ACTION=\"NewServlet\" accept-charset=\"UTF-8\">");
            out.println("<table>");
            out.println("<tr><td>");
            out.println("</td></tr>");
            out.println("<tr><td colspan=2>");
            out.println("<br>Please enter your text here:<br><br>");
            out.println("<textarea valign=top name=\"input\" style=\"width: 400px; height: 8em\" rows=31 cols=7></textarea>");
            out.println("</td></tr>");
            out.println("<tr><td align=left>");
            out.println("<input type=\"submit\" name=\"Process\"/>");
            out.println("<input type=\"button\" value=\"Clear\" onclick=\"this.form.elements['input'].value=''\"/>");
            out.println("</td></tr>");
            out.println("</table>");
            out.println("</FORM>");
            out.println("</div>");
            SimpleSentence s = new SimpleSentence();
            s.main(request.getParameter("input"));
            System.out.println("Before");
            Process p = Runtime.getRuntime().exec("cmd /c start /wait C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\output\\run.bat");
            int exitVal = p.waitFor();
            System.out.println("after");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser   saxParser = factory.newSAXParser();
            SaxAspect handler = new SaxAspect();
            InputStream    file  = new FileInputStream("C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\output\\FO1.xml");
            saxParser.parse(file, handler);
            System.out.println("Done");
            out.println("<br/><h2><b>"+request.getParameter("input")+"</b></h2>");
            
            out.println("<br/><h3>Aspect Terms and there polarity:</h3>");
            for(String key : aspectTerm.keySet()){
                out.println("<br/><b>Aspect Term:</b> "+key+"&nbsp;&nbsp;&nbsp;<b>Polarity: </b>"+aspectTerm.get(key));
            }
            out.println("<br/>");
            out.println("<br/><h3>Aspect categories</h3>");
            for(String key : aspectCat.keySet()){
                out.println("<br/><b>Aspect Category:</b> "+key);//+"&nbsp;&nbsp;&nbsp;<b>Polarity: </b>"+aspectCat.get(key));
            }
            out.println("</body>");
            out.println("</html>");
            
            
            
            
            
              
                



                

                
                  
                  
                

                
                  
                    
                
              
            
            

            
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SAXException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SAXException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(NewServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
