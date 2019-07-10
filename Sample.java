
import javax.xml.parsers.DocumentBuilderFactory; 

  

import java.io.BufferedReader; 

import java.io.File; 

import java.io.FileReader; 

import java.io.IOException; 

import java.io.Reader; 

import java.io.StringReader; 

import java.util.ArrayList; 

import java.util.HashMap; 

import java.util.List; 

  

import javax.swing.plaf.synth.SynthSeparatorUI; 

import javax.xml.parsers.DocumentBuilder; 

  

import javax.xml.parsers.DocumentBuilderFactory; 

  

import javax.xml.parsers.ParserConfigurationException; 

  

import org.w3c.dom.Attr; 

import org.w3c.dom.Document; 

  

import org.w3c.dom.Element; 

import org.w3c.dom.NamedNodeMap; 

import org.w3c.dom.Node; 

import org.w3c.dom.NodeList; 

  

import org.xml.sax.InputSource; 

  

import org.xml.sax.SAXException; 

  

import jxl.Cell; 

import jxl.CellType; 

import jxl.Sheet; 

import jxl.Workbook; 

import jxl.read.biff.BiffException; 

public class meter { 

     

    static List<String> columnHeaders = new ArrayList<>(); 

    static HashMap<String, String> mapping = new HashMap<>(); 

     

    static List<String> xmlData=new ArrayList<>(); 

    static HashMap<String, List<String>> linkedExcel= new HashMap<String,List<String>>(); 
    static HashMap<String, List<String>> linkedXML= new HashMap<String,List<String>>();

     

     

    public static void main(String[] args) throws IOException, BiffException { 

          

        // TODO Auto-generated method stub 

        read("Automation_TestData.xls"); 

        mapping(); 

         

        File xmlFile = new File("log.xml"); // Let's get XML file as String using BufferedReader // FileReader uses platform's default character encoding // if you need to specify a different encoding, use InputStreamReader  

        Reader fileReader = new FileReader(xmlFile); 

        BufferedReader bufReader = new BufferedReader(fileReader);  

        StringBuilder sb = new StringBuilder(); String line = bufReader.readLine();  

        while( line != null){  

            sb.append(line).append("\n"); line = bufReader.readLine(); 

            } 

        String xml = sb.toString(); 

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 

        DocumentBuilder builder; 

        try { 

              builder = factory.newDocumentBuilder(); 

              Document document = builder.parse(new InputSource(new StringReader(xml))); 

              Element rootElement = document.getDocumentElement(); 
              String meter="";
              

             /* String list = rootElement.getAttribute("ID"); 

              System.out.println("Attr"+ list);*/ 

              for (String data : columnHeaders) { 
            	
                
                  if(data.equals("DeviceID")){ 

                      NodeList list = rootElement.getElementsByTagName("ID"); 

                      Node node=list.item(0); 

                      Element e=(Element) node; 

                     String meterId= e.getAttribute("schemeAgencyID"); 
                     meter=meterId;
                    //  System.out.println(meterId); 

                  } 

                  else{ 

                   String requestQueueName = getString(mapping.get(data),rootElement); 
                   xmlData.add(requestQueueName);
                       }
                  linkedXML.put(meter, xmlData);
                 
                  
                
                  }
             System.out.println(linkedXML);
           //   System.out.println(xmlData);

              //} 

              /*String requestQueueName = getString("Note",rootElement); 

              System.out.println(requestQueueName);*/ 

              bufReader.close(); 
              test();

        } catch (ParserConfigurationException e) { 

              // TODO Auto-generated catch block 

              e.printStackTrace(); 

        } catch (SAXException e) { 

              // TODO Auto-generated catch block 

              e.printStackTrace(); 

        } catch (IOException e) { 

              // TODO Auto-generated catch block 

            e.printStackTrace(); 

        }        
  }    

 private static void test() {
		// TODO Auto-generated method stub
	 for(String key:linkedXML.keySet()) {
		 if(linkedExcel.containsKey(key)) {
			 if(linkedExcel.get(key).equals(linkedXML.get(key))) {
			 		 
			 System.out.println("Pass");
			 }else {
				 System.out.println("fail");
			 }
		 }
		 //else
	 }
	 }
	

// Method to map TAg names of XML and Excel 

    protected static void mapping(){ 

        mapping.put(columnHeaders.get(0), "schemeAgencyID"); 

        mapping.put(columnHeaders.get(1),"ReferenceUUID"); 

        mapping.put(columnHeaders.get(2), "ID"); 

        mapping.put(columnHeaders.get(3), "BusinessDocumentProcessingResultCode"); 

        mapping.put(columnHeaders.get(4), "MaximumLogItemSeverityCode"); 

        mapping.put(columnHeaders.get(5), "TypeID"); 

        mapping.put(columnHeaders.get(6), "SeverityCode"); 

        mapping.put(columnHeaders.get(7), "Note"); 
   } 

// Method to get particular element according to its name 

  protected static String getString(String tagName, Element element) { 

         NodeList list = element.getElementsByTagName(tagName); 

      

    if (list != null && list.getLength() > 0) { 

         

        NodeList subList = list.item(0).getChildNodes(); 

        if (subList != null && subList.getLength() > 0) { 

             

            // System.out.println( nnmp.getNamedItem("schemeAgencyID")); 

          /* String namedItem = subList.item(0).getAttributes().toString(); 

            System.out.println(namedItem);*/ 

            return subList.item(0).getNodeValue(); 

        } 

    } 

    return null; 

} 

public static void read(String inputFile) throws IOException, BiffException { 

      File inputWorkbook = new File(inputFile); 

      // ReadExcel test1 = new ReadExcel(); 

      Cell cell = null; 

      Workbook w; 


      try { 

           w = Workbook.getWorkbook(inputWorkbook); 

           Sheet sheet = w.getSheet(0); 

           for (int j = 2; j < sheet.getColumns(); j++) { 

                //for (int i = 0; i < sheet.getRows(); i++) { 

                      cell = sheet.getCell(j, 0); 

                      CellType type = cell.getType(); 

                      columnHeaders.add(cell.getContents()); 

           } 

          /* for (int i = 1; i < sheet.getRows(); i++) { 

               cell= sheet.getCell(2, i); 
               meterIDS.add(cell.getContents()); 

           } */

            

          // System.out.println(sheet.getColumns() + " " + sheet.getRows()); 

               for(int i=1;i<sheet.getRows();i++){ 

                   String key = ""; 

                   ArrayList< String > arr = new ArrayList<String>(); 

                   for(int j= 2;j<sheet.getColumns();j++){ 

                   cell=sheet.getCell(j , i); 

                   if(j == 2) 

                   { 

                       key = cell.getContents(); 

                   } 

                   else 

                   { 

                  arr.add(cell.getContents()); 

                   } 
            linkedExcel.put(key, arr); 

               } 
         

           } 

            

           //System.out.println(columnHeaders); 
       

      } catch (IOException ie) { 

           ie.printStackTrace(); 

      } 

  } 

  

} 

  
