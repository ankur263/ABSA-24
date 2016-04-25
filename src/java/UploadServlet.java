// Import required java libraries
import java.io.*;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

@WebServlet(name = "UploadServlet", urlPatterns = {"/UploadServlet"})
public class UploadServlet extends HttpServlet {
    
    private boolean isMultipart;
    private String filePath = "C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\upload";
    private int maxFileSize = 1024 * 1024 *1024;
    private int maxMemSize = 1024 * 1024 *1024;
    private File file ;
    private File outFile;
    
    public void init( ){
        filePath = "C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\upload\\";
    }
    
    public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, java.io.IOException {
        isMultipart = ServletFileUpload.isMultipartContent(request);
        response.setContentType("application/octet-stream"); // for making the user download file
        OutputStream outStream = response.getOutputStream();
        FileInputStream inStream;
        if( !isMultipart ){
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);
        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File("C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\upload\\"));
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // maximum file size to be uploaded.
        upload.setSizeMax( maxFileSize );
        
        try{
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);
            // Process the uploaded file items
            Iterator i = fileItems.iterator();
            while ( i.hasNext () ) {
                FileItem fi = (FileItem)i.next();
                if ( !fi.isFormField () ){
                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();
                    String actualFileName;
                    if( fileName.lastIndexOf("\\") >= 0 ){
                        actualFileName = fileName.substring( fileName.lastIndexOf("\\"));
                        file = new File (filePath + actualFileName);
                    }
                    else{
                        actualFileName = fileName.substring(fileName.lastIndexOf("\\")+1);
                        file = new File( filePath + actualFileName) ;
                    }
                    fi.write( file ) ;
                    SAXParserFactory fac = SAXParserFactory.newInstance();
                    SAXParser   saxParser = fac.newSAXParser();
                    SAXhandlerParse handler = new SAXhandlerParse();
                    InputStream    f  = new FileInputStream("C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\upload\\"+fileName);
                    //System.out.println("C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\upload\\"+fileName);
                    saxParser.parse(f, handler);
                    CoreNLP core = new CoreNLP();
                    core.main("C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\download\\Reviews.txt");
                    
                    Process p=Runtime.getRuntime().exec("cmd /c start /wait C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\output\\run.bat");
                    int exitVal = p.waitFor();
                    response.setHeader("Content-Disposition","inline; filename=" + "FO1.xml" );
                    outFile = new File("C:\\Users\\CRYPTOK\\Documents\\NetBeansProjects\\WebApplication1\\output\\FO1.xml");
                    inStream = new FileInputStream(outFile);
                    byte[] buffer = new byte[4096*4];
                    int length;
                    while ((length = inStream.read(buffer)) > 0){
                        outStream.write(buffer, 0, length);
                    }
                    inStream.close();
                    outStream.flush();
                }
            }
//            out.println("</body>");
//            out.println("</html>");
        }catch(Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
        
    }
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, java.io.IOException {
        
        throw new ServletException("GET method used with " +
                getClass( ).getName( )+": POST method required.");
    }
}