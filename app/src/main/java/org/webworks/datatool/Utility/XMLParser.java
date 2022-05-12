package org.webworks.datatool.Utility;

/**
 * Created by Charlie on 12/01/2015.
 */

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLParser {
    // constructor
    public XMLParser() {

    }

    /**
     * Getting XML from URL making HTTP request
     * @param link string
     * */
    public String getXmlFromUrl(String link) {
        String xml = null;

        try {
            // defaultHttpClient
            URL url=new URL(link);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/xml");

           /* DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();*/
            xml =readInputStreamToString(conn); //EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }

    /**
     * Getting XML DOM element
     * @param xml string
     * */
    public Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);
            /*byte[] bytes= Charset.forName("UTF-8").encode(xml).array();
            InputStream inputStream=new ByteArrayInputStream(bytes);
            doc=db.parse(inputStream);*/

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        catch (NullPointerException e){
            //Log.e("Error: ", e.getMessage());
            return null;
        }

        return doc;
    }

    /** Getting node value
     * @param elem element
     */
    private String getElementValue(Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    /**
     * Getting node value
     * @param item node
     * @param str string
     * */
    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public String getAttribute(Element item, String attr){
        try{
            return item.getAttributes().getNamedItem(attr).getNodeValue();
        }
        catch (Exception e){
            return "";
        }

    }

    private String TAG = "eCourseApp";

    private String readInputStreamToString(HttpURLConnection connection) {
        String result = null;
        StringBuilder sb = new StringBuilder();
        InputStream is = null;

        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        }
        catch (Exception e) {
            Log.i(TAG, "Error reading InputStream");
            result = null;
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    Log.i(TAG, "Error closing InputStream");
                }
            }
        }
        return result;
    }
}
