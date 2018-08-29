package de.wwu.nwa.gui;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.NondeterministicNestedWordAutomaton;
import de.wwu.nwa.automata.items.Alphabet;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import de.wwu.nwa.automata.items.transition.CallTransitionFunction;
import de.wwu.nwa.automata.items.transition.InternalTransitionFunction;
import de.wwu.nwa.automata.items.transition.ReturnTransitionFunction;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Class for storing and reading Nested-word automata
 *
 * @author Allan Grunert
 */
public class NWAStorage {

    /**
     * Save common Nested-word automata to file
     *
     * @param cnwa name of common Nested-word automata we save to file
     */
    public void saveDNWA(CommonNestedWordAutomaton cnwa) {
        try {

            cnwa.printAll();
            cnwa.getAlphabet().printAll();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("NestedWordAutomaton");
            doc.appendChild(rootElement);

            // name elements
            Element name = doc.createElement("Name");
            name.appendChild(doc.createTextNode(cnwa.getName()));
            rootElement.appendChild(name);

            // name elements
            Element type = doc.createElement("Type");
            type.appendChild(doc.createTextNode(cnwa.getClass().getSimpleName()));
            rootElement.appendChild(type);

            // state elements
            Element states = doc.createElement("States");
            rootElement.appendChild(states);

            // state elements
            Element alphabet = doc.createElement("Alphabet");
            rootElement.appendChild(alphabet);

            for (String sym : cnwa.getAlphabet().getAlphabet(Alphabet.CALL_POSITION)
                    ) {
                Element symbol = doc.createElement("Symbol");

                alphabet.appendChild(symbol);

                Attr attr = doc.createAttribute("type");
                attr.setValue("" + Alphabet.CALL_POSITION);
                symbol.setAttributeNode(attr);
                symbol.appendChild(doc.createTextNode(sym));

            }

            for (String sym : cnwa.getAlphabet().getAlphabet(Alphabet.RETURN_POSITION)
                    ) {
                Element symbol = doc.createElement("Symbol");

                alphabet.appendChild(symbol);

                Attr attr = doc.createAttribute("type");
                attr.setValue("" + Alphabet.RETURN_POSITION);
                symbol.setAttributeNode(attr);
                symbol.appendChild(doc.createTextNode(sym));

            }

            for (String sym : cnwa.getAlphabet().getAlphabet(Alphabet.INTERNAL_POSITION)
                    ) {
                Element symbol = doc.createElement("Symbol");

                alphabet.appendChild(symbol);

                Attr attr = doc.createAttribute("type");
                attr.setValue("" + Alphabet.INTERNAL_POSITION);
                symbol.setAttributeNode(attr);
                symbol.appendChild(doc.createTextNode(sym));

            }

            // state elements
            Element linearestates = doc.createElement("LinearStates");
            states.appendChild(linearestates);

            // linear state elements
            for (LinearState q : cnwa.getQ()
                    ) {
                Element linearstate = doc.createElement("LinearState");
                linearstate.appendChild(doc.createTextNode(q.getStateName()));
                linearestates.appendChild(linearstate);
            }

            // linear accepting state elements
            Element linearacceptingstates = doc.createElement("LinearAcceptingStates");
            states.appendChild(linearacceptingstates);

            // linear accepting state elements
            Element q0 = doc.createElement("q0");
            if (cnwa.getQ0() != null) {
                q0.appendChild(doc.createTextNode(cnwa.getQ0().getStateName()));
            }
            states.appendChild(q0);

            // linear accepting state elements
            Element p0 = doc.createElement("p0");
            if (cnwa.getP0() != null) {
                p0.appendChild(doc.createTextNode(cnwa.getP0().getStateName()));
            }
            states.appendChild(p0);

            // linear accepting state elements
            for (LinearState q : cnwa.getQf()
                    ) {
                Element linearacceptingstate = doc.createElement("LinearAcceptingState");
                linearacceptingstate.appendChild(doc.createTextNode(q.getStateName()));
                linearacceptingstates.appendChild(linearacceptingstate);
            }

            // hiearchy states
            Element hierarchystates = doc.createElement("HierarchyStates");
            states.appendChild(hierarchystates);


            for (HierarchyState p : cnwa.getP()
                    ) {
                // hiearchy states
                Element hierarchystate = doc.createElement("HierarchyState");
                hierarchystate.appendChild(doc.createTextNode(p.getStateName()));
                hierarchystates.appendChild(hierarchystate);
            }

            // hiearchy states
            Element hierarchyacceptingstates = doc.createElement("HierarchyAcceptingStates");
            states.appendChild(hierarchyacceptingstates);

            // hiearchy states
            for (HierarchyState p : cnwa.getPf()) {
                Element hierarchyacceptingstate = doc.createElement("HierarchyAcceptingState");
                hierarchyacceptingstate.appendChild(doc.createTextNode(p.getStateName()));
                hierarchyacceptingstates.appendChild(hierarchyacceptingstate);
            }

            Element transistions = doc.createElement("Transitions");
            rootElement.appendChild(transistions);

            Element calltransitions = doc.createElement("CallTransitions");
            transistions.appendChild(calltransitions);

            for (CallTransitionFunction ctf : cnwa.getDeltaC().getDeltaC()
                    ) {
                Element calltransition = doc.createElement("CallTransition");

                calltransitions.appendChild(calltransition);

                Attr attr = doc.createAttribute("sourceq");
                attr.setValue(ctf.getqSource().getStateName());
                calltransition.setAttributeNode(attr);

                Attr attr2 = doc.createAttribute("targetq");
                attr2.setValue(ctf.getqTarget().getStateName());
                calltransition.setAttributeNode(attr2);

                Attr attr3 = doc.createAttribute("p");
                attr3.setValue(ctf.getP().getStateName());
                calltransition.setAttributeNode(attr3);

                Attr attr4 = doc.createAttribute("name");
                attr4.setValue(ctf.getName());
                calltransition.setAttributeNode(attr4);

                calltransition.appendChild(doc.createTextNode(ctf.getSymbol()));

            }

            Element returntransitions = doc.createElement("ReturnTransitions");
            transistions.appendChild(returntransitions);

            for (ReturnTransitionFunction rtf : cnwa.getDeltaR().getDeltaR()) {
                Element returntransition = doc.createElement("ReturnTransition");
                returntransitions.appendChild(returntransition);

                Attr attr = doc.createAttribute("sourceq");
                attr.setValue(rtf.getqSource().getStateName());
                returntransition.setAttributeNode(attr);


                Attr attr2 = doc.createAttribute("targetq");
                attr2.setValue(rtf.getqTarget().getStateName());
                returntransition.setAttributeNode(attr2);

                Attr attr3 = doc.createAttribute("p");
                attr3.setValue(rtf.getP().getStateName());
                returntransition.setAttributeNode(attr3);

                Attr attr4 = doc.createAttribute("name");
                attr4.setValue(rtf.getName());
                returntransition.setAttributeNode(attr4);

                returntransition.appendChild(doc.createTextNode(rtf.getSymbol()));

            }

            Element internaltransitions = doc.createElement("InternalTransitions");
            transistions.appendChild(internaltransitions);

            for (InternalTransitionFunction itf : cnwa.getDeltaI().getDeltaI()) {
                Element interaltransition = doc.createElement("InternalTransition");
                internaltransitions.appendChild(interaltransition);

                Attr attr = doc.createAttribute("sourceq");
                attr.setValue(itf.getqSource().getStateName());
                interaltransition.setAttributeNode(attr);


                Attr attr2 = doc.createAttribute("targetq");
                attr2.setValue(itf.getqTarget().getStateName());
                interaltransition.setAttributeNode(attr2);

                Attr attr4 = doc.createAttribute("name");
                attr4.setValue(itf.getName());
                interaltransition.setAttributeNode(attr4);

                interaltransition.appendChild(doc.createTextNode(itf.getSymbol()));
            }


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream("automata.xml", true));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveNNWA(NondeterministicNestedWordAutomaton nnwa) {
        // Not implemented yet
    }

    /**
     * Save linear-accepting Nested-Word automata
     *
     * @param lnwa linear-accepting nested-word automata to be stored
     */
    public void saveLNWA(LinearAcceptingNestedWordAutomaton lnwa) {
        this.saveDNWA((CommonNestedWordAutomaton) lnwa);
    }


    /**
     * load Nested-word automata from list
     *
     * @return list of Nested-word automata
     */
    public ArrayList<NestedWordAutomaton> loadFromFile() {
        ArrayList<NestedWordAutomaton> list = new ArrayList<NestedWordAutomaton>();

        try {

            File fXmlFile = new File("automata.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("NestedWordAutomaton");

            for (int i = 0; i < nodeList.getLength(); i++) {
                NestedWordAutomaton nwa = null;
                Node an = nodeList.item(i);
                Element e = (Element) an;
                if (e.getElementsByTagName("Type").item(0).getTextContent().equals("CommonNestedWordAutomaton")) {
                    nwa = new CommonNestedWordAutomaton();
                }
                if (e.getElementsByTagName("Type").item(0).getTextContent().equals("LinearAcceptingNestedWordAutomaton")) {
                    nwa = new LinearAcceptingNestedWordAutomaton();
                }
                nwa.setName(e.getElementsByTagName("Name").item(0).getTextContent());

                nwa.setAlphabet(new Alphabet());

                for (int j = 0; j < e.getElementsByTagName("Symbol").getLength(); j++) {
                    String type = e.getElementsByTagName("Symbol").item(j).getAttributes().getNamedItem("type").getTextContent();
                    String symbol = e.getElementsByTagName("Symbol").item(j).getTextContent();
                    nwa.getAlphabet().addSymbol(symbol, Integer.valueOf(type));
                }

                for (int j = 0; j < e.getElementsByTagName("LinearState").getLength(); j++) {
                    nwa.addStateToQ(new LinearState(e.getElementsByTagName("LinearState").item(j).getTextContent()));
                }

                for (int j = 0; j < e.getElementsByTagName("LinearAcceptingState").getLength(); j++) {
                    nwa.addStateToQf(nwa.getLinearStateByName(e.getElementsByTagName("LinearAcceptingState").item(j).getTextContent()));
                }

                nwa.setQ0(nwa.getLinearStateByName(e.getElementsByTagName("q0").item(0).getTextContent()));


                for (int j = 0; j < e.getElementsByTagName("HierarchyState").getLength(); j++) {
                    nwa.addHierarchyStateToP(new HierarchyState(e.getElementsByTagName("HierarchyState").item(j).getTextContent()));
                }
                nwa.setP0(nwa.getHierarchyStateByName(e.getElementsByTagName("p0").item(0).getTextContent()));


                for (int j = 0; j < e.getElementsByTagName("HierarchyAcceptingState").getLength(); j++) {
                    nwa.addHierarchyStateToPf(nwa.getHierarchyStateByName(e.getElementsByTagName("HierarchyAcceptingState").item(j).getTextContent()));
                }

                for (int j = 0; j < e.getElementsByTagName("CallTransition").getLength(); j++) {
                    HierarchyState p = nwa.getHierarchyStateByName(e.getElementsByTagName("CallTransition").item(j).getAttributes().getNamedItem("p").getTextContent());
                    LinearState qSource = nwa.getLinearStateByName(e.getElementsByTagName("CallTransition").item(j).getAttributes().getNamedItem("sourceq").getTextContent());
                    LinearState qTarget = nwa.getLinearStateByName(e.getElementsByTagName("CallTransition").item(j).getAttributes().getNamedItem("targetq").getTextContent());
                    String name = e.getElementsByTagName("CallTransition").item(j).getAttributes().getNamedItem("name").getTextContent();
                    String symbol = e.getElementsByTagName("CallTransition").item(j).getTextContent();

                    ((CommonNestedWordAutomaton) nwa).getDeltaC().addTransitionFunction(qSource, symbol, qTarget, p, name);
                }

                for (int j = 0; j < e.getElementsByTagName("ReturnTransition").getLength(); j++) {
                    HierarchyState p = nwa.getHierarchyStateByName(e.getElementsByTagName("ReturnTransition").item(j).getAttributes().getNamedItem("p").getTextContent());
                    LinearState qSource = nwa.getLinearStateByName(e.getElementsByTagName("ReturnTransition").item(j).getAttributes().getNamedItem("sourceq").getTextContent());
                    LinearState qTarget = nwa.getLinearStateByName(e.getElementsByTagName("ReturnTransition").item(j).getAttributes().getNamedItem("targetq").getTextContent());
                    String symbol = e.getElementsByTagName("ReturnTransition").item(j).getTextContent();
                    String name = e.getElementsByTagName("ReturnTransition").item(j).getAttributes().getNamedItem("name").getTextContent();

                    ((CommonNestedWordAutomaton) nwa).getDeltaR().addTransitionFunction(qSource, p, symbol, qTarget, name);
                }

                for (int j = 0; j < e.getElementsByTagName("InternalTransition").getLength(); j++) {
                    LinearState qSource = nwa.getLinearStateByName(e.getElementsByTagName("InternalTransition").item(j).getAttributes().getNamedItem("sourceq").getTextContent());
                    LinearState qTarget = nwa.getLinearStateByName(e.getElementsByTagName("InternalTransition").item(j).getAttributes().getNamedItem("targetq").getTextContent());
                    String symbol = e.getElementsByTagName("InternalTransition").item(j).getTextContent();
                    String name = e.getElementsByTagName("InternalTransition").item(j).getAttributes().getNamedItem("name").getTextContent();

                    ((CommonNestedWordAutomaton) nwa).getDeltaI().addTransitionFunction(qSource, symbol, qTarget, name);
                }

                ((CommonNestedWordAutomaton) nwa).printAll();
                list.add(nwa);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
