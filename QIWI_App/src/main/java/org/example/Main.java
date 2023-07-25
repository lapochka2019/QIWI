package org.example;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.transform.stream.StreamResult;


public class Main {
    public static void main(String[] args) throws XPathExpressionException {
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        String[] strB = str.split(" ");
        String name = strB[1].split("=")[2];
        String date = strB[2].split("=")[2].replace("-","/");
        final String rootURL = "http://www.cbr.ru/scripts/XML_daily.asp";
        HttpResponse <String> httpResponse = Unirest.get(rootURL).queryString("date",date).asString();

        System.out.print(name + " ");
        search(convertStringToXML(httpResponse.getBody()),name);

    }
    public static Document convertStringToXML(String inputString){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(inputString)));
            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void search(Document document, String charCode) throws XPathExpressionException {
        XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression2 = "/ValCurs/Valute[CharCode=" + "'" + charCode + "']/Name";
        //System.out.println(expression);
        NodeList nodeList2 = (NodeList) xPath.compile(expression2).evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodeList2.getLength(); i++) {
            System.out.print(nodeList2.item(i).getFirstChild().getNodeValue() + ": ");
        }


        String expression1 = "/ValCurs/Valute[CharCode=" + "'" + charCode + "']/Value";
        //System.out.println(expression);
        NodeList nodeList1 = (NodeList) xPath.compile(expression1).evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodeList1.getLength(); i++) {
            System.out.print(nodeList1.item(i).getFirstChild().getNodeValue());
        }


    }
}