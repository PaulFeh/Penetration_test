package net.uncc.app.xpath_injection;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

@Controller
@RequestMapping("/xpath_injection")
public class XPath_injectionController {

    @Autowired
    ResourceLoader resourceLoader;

    @GetMapping("/")
    public ModelAndView xpath_inject() {
        String data = "";
        try {
            Resource resource = resourceLoader.getResource("classpath:files/customer.xml");
            File file = resource.getFile();
            data = new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ModelAndView mav = new ModelAndView("xpath_injection/index");
        mav.addObject("customerList", StringEscapeUtils.escapeHtml(data).replace("\n", "</br>").replace(" ", "&nbsp;"));
        return mav;
    }

    @PostMapping("/")
    @ResponseBody
    public Object xpath_injected(@RequestParam String email_address) {
        Map<String, String> response_data = new HashMap<String, String>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("src/main/resources/files/customer.xml");

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();

            List<String> id_list = new ArrayList<>();
            XPathExpression expression = xpath.compile("/customers/customer[email = '" + email_address + "']/id/text()");
            NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                id_list.add(nodes.item(i).getNodeValue());

            response_data.put("status", "success");
            response_data.put("msg", Arrays.toString(id_list.toArray()));
            return response_data;
        } catch (Exception e) {
            e.printStackTrace();
            response_data.put("status", "error");
            response_data.put("msg", "Successfully logged error");
            return response_data;
        }
    }

}
