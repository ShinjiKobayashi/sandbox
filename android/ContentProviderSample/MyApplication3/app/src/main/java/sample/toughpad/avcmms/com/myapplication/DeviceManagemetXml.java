
package sample.toughpad.avcmms.com.myapplication;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.util.List;
import java.util.Properties;

/**
 * Created by skobayashi on 14/08/21.
 */
public class DeviceManagemetXml {
    private final int MAX_FILTER_RULES_NUM = 5;

    private final static String XMLTAG_DEVICEMANAGEMENT = "DeviceManagement";
    private final static String XMLTAG_SETTINGITEMS = "SettingItems";
    private final static String XMLTAG_SETTINGITEMS_WIFI = "WiFi";
    private final static String XMLTAG_SETTINGITEMS_BLUETOOTH = "Bluetooth";
    private final static String XMLTAG_SETTINGITEMS_GPS = "GPS";
    private final static String XMLTAG_SETTINGITEMS_WWAN = "WWAN";
    private final static String XMLTAG_SETTINGITEMS_SDCARD = "SDCard";
    private final static String XMLTAG_SETTINGITEMS_CAMERA = "CAMERA";
    private final static String XMLTAG_SETTINGITEMS_CRADLEUSB = "CradleUSB";
    private final static String XMLTAG_SETTINGITEMS_ADB = "ADB";
    private final static String XMLTAG_SETTINGITEMS_USBSTORAGE = "USBStorage";
    private final static String XMLTAG_ADDRESSFILTER = "AddressFilter";
    private final static String XMLTAG_ADDRESSFILTER_USEFILTER = "UseFilter";
    private final static String XMLTAG_ADDRESSFILTER_RULE = "Rule";
    private final static String XMLTAG_ADDRESSFILTER_RULE_RULEUSE = "RuleUse";
    private final static String XMLTAG_ADDRESSFILTER_RULE_INTERFACE = "Interface";
    private final static String XMLTAG_ADDRESSFILTER_RULE_ADDRESS = "Address";
    private final static String XMLTAG_USBSTORAGEWHITELIST = "USBStorageWhiteList";
    private final static String XMLTAG_USBSTORAGEWHITELIST_DEVICE = "Device";
    private final static String XMLTAG_USBSTORAGEWHITELIST_DEVICE_VENDORID = "VendorID";
    private final static String XMLTAG_USBSTORAGEWHITELIST_DEVICE_PRODUCTID = "ProductID";
    private final static String XMLTAG_USBSTORAGEWHITELIST_DEVICE_SERIALNUMBER = "SerialNumber";
    private final static String XMLTAG_USBSTORAGEWHITELIST_DEVICE_DEVICENAME = "DeviceName";

    private final static String XML_NODE_TEXT_ON = "ON";
    private final static String XML_NODE_TEXT_OFF = "OFF";
    private final static String XML_NODE_TEXT_WHITELIST = "WhiteList";

    private Document mDocument;

    DeviceManagemetXml() {
    }

    public boolean loadXmlFile(String path){
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            mDocument = documentBuilder.parse(new File(path));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return false;
        } catch (SAXException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean saveXmlFile(String path){
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            // 出力propertiesの設定
            Properties outFormat = new Properties();
            outFormat.setProperty(OutputKeys.INDENT, "yes");
            outFormat.setProperty(OutputKeys.METHOD, "xml");
            outFormat.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            outFormat.setProperty(OutputKeys.VERSION, "1.0");
            outFormat.setProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperties(outFormat);

            DOMSource domSource = new DOMSource(mDocument.getDocumentElement());
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(domSource, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return false;
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void createInitXml() {
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        mDocument = documentBuilder.newDocument();

        Element root = appendRoot(XMLTAG_DEVICEMANAGEMENT);
        createSettingsItemXml(appendChild(root, XMLTAG_SETTINGITEMS));
        addTextNode(
                appendChild(appendChild(root, XMLTAG_ADDRESSFILTER), XMLTAG_ADDRESSFILTER_USEFILTER),
                XML_NODE_TEXT_OFF);

    }

    private void createSettingsItemXml(Element settingItemElement) {
        addTextNode(appendChild(settingItemElement, XMLTAG_SETTINGITEMS_WIFI),
                XML_NODE_TEXT_OFF);
        addTextNode(appendChild(settingItemElement, XMLTAG_SETTINGITEMS_BLUETOOTH),
                XML_NODE_TEXT_OFF);
        addTextNode(appendChild(settingItemElement, XMLTAG_SETTINGITEMS_GPS),
                XML_NODE_TEXT_OFF);
        addTextNode(appendChild(settingItemElement, XMLTAG_SETTINGITEMS_WWAN),
                XML_NODE_TEXT_OFF);
        addTextNode(appendChild(settingItemElement, XMLTAG_SETTINGITEMS_SDCARD),
                XML_NODE_TEXT_OFF);
        addTextNode(appendChild(settingItemElement, XMLTAG_SETTINGITEMS_CAMERA),
                XML_NODE_TEXT_OFF);
        addTextNode(appendChild(settingItemElement, XMLTAG_SETTINGITEMS_CRADLEUSB),
                XML_NODE_TEXT_OFF);
        addTextNode(appendChild(settingItemElement, XMLTAG_SETTINGITEMS_ADB),
                XML_NODE_TEXT_OFF);
        addTextNode(appendChild(settingItemElement, XMLTAG_SETTINGITEMS_USBSTORAGE),
                XML_NODE_TEXT_OFF);
    }

    public String getXmlDataInString() {
        String ret = null;
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            // 出力propertiesの設定
            Properties outFormat = new Properties();
            outFormat.setProperty(OutputKeys.INDENT, "yes");
            outFormat.setProperty(OutputKeys.METHOD, "xml");
            outFormat.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            outFormat.setProperty(OutputKeys.VERSION, "1.0");
            outFormat.setProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperties(outFormat);

            DOMSource domSource = new DOMSource(mDocument.getDocumentElement());
            OutputStream output = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(output);

            transformer.transform(domSource, result);
            ret = output.toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void readXml() {

    }

    public void changeStateIpFilteringAll(int state) {
        if (state > 0 || 1 < state) {
            return;
        }
        NodeList nodeList = mDocument.getElementsByTagName(XMLTAG_ADDRESSFILTER_USEFILTER);
        nodeList.item(0).getChildNodes().item(0)
                .setNodeValue(state == 0 ? XML_NODE_TEXT_OFF : XML_NODE_TEXT_ON);
    }

    /**
     * Addは一番後ろにAddするのみ。index指定のAddには対応しない。
     * 
     * @param data
     */
    public void addIpFilteringRule(IpFilteringSettingsData data) {
        NodeList nodeList = mDocument.getElementsByTagName(XMLTAG_ADDRESSFILTER);
        Element rule = appendChild((Element) nodeList.item(0), XMLTAG_ADDRESSFILTER_RULE);

        // userule
        addTextNode(appendChild(rule, XMLTAG_ADDRESSFILTER_RULE_RULEUSE),
                data.isToggle() ? XML_NODE_TEXT_ON : XML_NODE_TEXT_OFF);

        // interface
        addTextNode(appendChild(rule, XMLTAG_ADDRESSFILTER_RULE_INTERFACE),
                String.valueOf(data.getInterfaceCode()));

        // address
        List<String> addrList = data.getWhiteList();
        for (String addr : addrList) {
            addTextNode(appendChild(rule, XMLTAG_ADDRESSFILTER_RULE_ADDRESS),
                    addr);
        }
    }

    /**
     * stateを変更するのみ。UI上で変更処理は存在しないので、APIとしてもそれに合わせる
     * 
     * @param ruleNum 0 origin
     * @param state
     */
    public void changeStateIpFilteringRule(int ruleNum, int state) {
        if (state > 0 || 1 < state) {
            return;
        }
        NodeList nodeList = mDocument.getElementsByTagName(XMLTAG_ADDRESSFILTER_RULE_RULEUSE);
        nodeList.item(ruleNum).getChildNodes().item(0).setNodeValue(state == 0 ? XML_NODE_TEXT_OFF : XML_NODE_TEXT_ON);
    }

    public void deleteIpFilteringRule(int ruleNum) {
        NodeList nodeList = mDocument.getElementsByTagName(XMLTAG_ADDRESSFILTER_RULE);
        NodeList parentList = mDocument.getElementsByTagName(XMLTAG_ADDRESSFILTER);
        parentList.item(0).removeChild(nodeList.item(ruleNum));
    }

    private Element appendRoot(String elemName) {
        Element e = mDocument.createElement(elemName);
        mDocument.appendChild(e);
        return e;
    }

    private Element appendChild(Element to, String elemName) {
        Element e = mDocument.createElement(elemName);
        to.appendChild(e);
        return e;
    }

    private void addTextNode(Element to, String text) {
        to.appendChild(mDocument.createTextNode(text));
    }

}
