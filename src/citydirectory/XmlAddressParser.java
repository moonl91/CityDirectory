package citydirectory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class XmlAddressParser implements AddressParser {

    @Override
    public List<AddressRecord> parse(Path path) throws IOException {
        List<AddressRecord> result = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);


            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(path.toFile());
            document.getDocumentElement().normalize();

            NodeList nodes = document.getElementsByTagName("item");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);

                String city = element.getAttribute("city").trim();
                String street = element.getAttribute("street").trim();
                String house = element.getAttribute("house").trim();
                String floorText = element.getAttribute("floor").trim();

                if (city.isEmpty() || street.isEmpty() || house.isEmpty() || floorText.isEmpty()) {
                    System.out.println("Пропущена XML-запись №" + (i + 1) + ": не хватает атрибутов.");
                    continue;
                }

                try {
                    int floor = Integer.parseInt(floorText);
                    result.add(new AddressRecord(city, street, house, floor));
                } catch (NumberFormatException ex) {
                    System.out.println("Пропущена XML-запись №" + (i + 1) + ": неверная этажность.");
                }
            }

            return result;
        } catch (Exception e) {
            throw new IOException("Не удалось прочитать XML: " + e.getMessage(), e);
        }
    }
}
