package org.example;

import org.example.model.Catalog;
import org.example.model.Plant;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final String TAG_COMMON = "COMMON";
    private static final String TAG_BOTANICAL = "BOTANICAL";
    private static final String TAG_ZONE = "ZONE";
    private static final String TAG_LIGHT = "LIGHT";
    private static final String TAG_PRICE = "PRICE";
    private static final String TAG_AVAILABILITY = "AVAILABILITY";
    private static final String path = "C:\\Users\\inkin\\OneDrive\\Рабочий стол\\Тестовое задание КРИСТА\\data\\"; //Путь к папке с файлами

    static void loadXmlCatalog(String name) {
        //Путь к папке с xml
        File file = new File(path + name);
        System.out.println("Выбран документ: " + path + name);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            doc = dbf.newDocumentBuilder().parse(file);
        } catch (Exception e) {
            System.out.println("Parsing error ");
            return;
        }
        Catalog catalog = new Catalog();
        NodeList catalogChildren = getCatalogAttributes(doc, catalog);
        List<Plant> plantList = parsCatalog(catalogChildren);


        fillingBD.fillingTheDatabase(catalog, plantList);
    }

    private static NodeList getCatalogAttributes(Document doc, Catalog catalog) {
        NodeList catalogChildren = null;
        Node catalogNode = doc.getFirstChild();
        String uuid = catalogNode.getAttributes().getNamedItem("uuid").getNodeValue();
        String date = catalogNode.getAttributes().getNamedItem("date").getNodeValue();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            java.util.Date parsedDate = dateFormat.parse(date);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            catalog.setDate(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String company = catalogNode.getAttributes().getNamedItem("company").getNodeValue();
        catalog.setUuid(uuid);
        catalog.setCompany(company);
        catalogChildren = catalogNode.getChildNodes();
        return catalogChildren;
    }


    private static List<Plant> parsCatalog(NodeList catalogChildren) {
        List<Plant> plantList = new ArrayList<>();
        for (int i = 0; i < catalogChildren.getLength(); i++) {
            if (catalogChildren.item(i).getNodeType() == Node.ELEMENT_NODE) { //Фильтрация

                Plant plant = parsPlant(catalogChildren.item(i));
                plantList.add(plant);
            }
        }
        return plantList;
    }

    private static Plant parsPlant(Node plantNode) {
        String common = "";
        String botanical = "";
        int zone = 0;
        String light = "";
        double price = 0.0;
        int availability = 0;
        NodeList plantNodeChild = plantNode.getChildNodes();
        for (int j = 0; j < plantNodeChild.getLength(); j++) {

            if (plantNodeChild.item(j).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            switch (plantNodeChild.item(j).getNodeName()) {
                case TAG_COMMON: {
                    common = plantNodeChild.item(j).getTextContent();
                    break;
                }
                case TAG_BOTANICAL: {
                    botanical = plantNodeChild.item(j).getTextContent();
                    break;
                }
                case TAG_ZONE: {
                    try {
                        zone = Integer.parseInt(plantNodeChild.item(j).getTextContent());
                    } catch (NumberFormatException e) {
                        zone = 1;
                    }
                    break;
                }
                case TAG_LIGHT: {
                    light = plantNodeChild.item(j).getTextContent();
                    break;
                }
                case TAG_PRICE: {
                    price = Double.parseDouble(plantNodeChild.item(j).getTextContent().replace("$", ""));
                    break;
                }
                case TAG_AVAILABILITY: {
                    availability = Integer.parseInt(plantNodeChild.item(j).getTextContent());
                    break;
                }

            }

        }
        return new Plant(common, botanical, zone, light, price, availability);
    }
}
