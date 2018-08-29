package de.wwu.nwa.gui;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.NondeterministicNestedWordAutomaton;
import de.wwu.nwa.automata.items.Alphabet;
import de.wwu.nwa.automata.items.NestedWord;
import de.wwu.nwa.automata.items.Run;
import de.wwu.nwa.automata.items.exceptions.InputWordNotInAlphabeth;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Main Class for creating, testing and using opertions on nested words
 *
 * @author Allan Grunert
 */
public class Main extends Application implements SaveAutomata {

    public ArrayList<NestedWordAutomaton> saveAutomataList;

    public NestedWordAutomaton selectedNestedWordAutomaton;

    public NestedWord nestedWord;
    public ComboBox automataComboBox;
    public ComboBox alphabetComboBox;
    public Label acceptingLabel;
    public OperationsView ov;
    CreateAutomataView cav;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        saveAutomataList = new ArrayList<NestedWordAutomaton>();
        ov = new OperationsView(this);
        cav = new CreateAutomataView(this);
        cav.setPrefHeight(400);
        cav.setPrefWidth(1000);
        DisplayNWView dv = new DisplayNWView(this);

        Button b1 = new Button("Auswerten");

        Label nestedWordLabel = new Label("Nested-Word");
        ListView nestedWordList = new ListView();
        nestedWordList.setPrefHeight(160);

        automataComboBox = new ComboBox();
        alphabetComboBox = new ComboBox();

        Label nestedEdgeLabel = new Label("Nested Edges");
        ListView nestedEdgeView = new ListView();
        nestedEdgeView.setPrefHeight(160);

        Button clearNW = new Button("Nested Word löschen");

        Button deleteSymbol = new Button("Lösche Symbol");
        Button deleteNestedEdge = new Button("Nesting Kante löschen");
        acceptingLabel = new Label("");

        Button addSymbol = new Button("Symbol zu NW hinzufügen");

        ComboBox from = new ComboBox();
        ComboBox to = new ComboBox();
        Button addNestedEdgeButton = new Button("Nested Edge hinzu");


        // events

        automataComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                selectedNestedWordAutomaton = saveAutomataList.get(automataComboBox.getSelectionModel().getSelectedIndex());
                nestedWord = new NestedWord();
                nestedWord.setAlphabet(selectedNestedWordAutomaton.getAlphabet());
                ObservableList observableList =
                        FXCollections.observableArrayList(
                                selectedNestedWordAutomaton.getAlphabet().getAlphabet()
                        );
                alphabetComboBox.setItems(observableList);

            }
        });

        addSymbol.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (alphabetComboBox.getSelectionModel().getSelectedIndex() < 0) {
                    errorDialog("Fehler Alphabet", "Kein Symbol ausgewält.");
                } else {
                    if (alphabetComboBox.getSelectionModel().getSelectedIndex() < 0) {
                        errorDialog("Fehler Alphabet", "Kein Typ ausgewält.");
                    } else {
                        try {
                            nestedWord.addSymbol((String) alphabetComboBox.getSelectionModel().getSelectedItem());
                            String st = "(i)";
                            if (selectedNestedWordAutomaton.getAlphabet().getTypeOfSymbol((String) alphabetComboBox.getSelectionModel().getSelectedItem()) == Alphabet.CALL_POSITION) {
                                st = "(c)";
                            }
                            if (selectedNestedWordAutomaton.getAlphabet().getTypeOfSymbol((String) alphabetComboBox.getSelectionModel().getSelectedItem()) == Alphabet.RETURN_POSITION) {
                                st = "(r)";
                            }
                            nestedWordList.getItems().add((nestedWord.length() - 1) + ". " + alphabetComboBox.getSelectionModel().getSelectedItem() + " " + st);

                            to.setItems(
                                    FXCollections.observableArrayList(nestedWord.getReturnPositions()));
                            to.getItems().add(-1);

                            from.setItems(
                                    FXCollections.observableArrayList(nestedWord.getCallPositions()));
                            from.getItems().add(-2);

                        } catch (InputWordNotInAlphabeth inputWordNotInAlphabeth) {
                            inputWordNotInAlphabeth.printStackTrace();
                        }
                    }
                }
            }
        });


        clearNW.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                nestedWordList.getItems().clear();
                nestedWord.clear();
            }
        });


        addNestedEdgeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (from.getSelectionModel().getSelectedIndex() > -1 && to.getSelectionModel().getSelectedIndex() > -1) {
                    nestedEdgeView.getItems().add("(" + from.getSelectionModel().getSelectedItem() + "," + to.getSelectionModel().getSelectedItem() + ")");
                    nestedWord.addNestedEdge((Integer) from.getSelectionModel().getSelectedItem(), (Integer) (to.getSelectionModel().getSelectedItem()));
                }
            }
        });

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (selectedNestedWordAutomaton == null) {
                    errorDialog("Error Lauf", "Kein Automat ausgewählt");
                } else {
                    if (selectedNestedWordAutomaton.getQ0() == null || selectedNestedWordAutomaton.getP0() == null) {
                        errorDialog("Error Lauf", "Automat besitzt keine Anfangszustände");
                    } else {


                        Run run = new Run(nestedWord);
                        run.setNwa(selectedNestedWordAutomaton);

                        dv.testNWA(run);
                        if (run.getNwa().isAcceptingStates()) {
                            acceptingLabel.setText("Wort akzeptiert");
                            acceptingLabel.setStyle("-fx-text-fill: white; -fx-background-color: green;");
                        } else {
                            acceptingLabel.setText("Wort nicht akzeptiert");
                            acceptingLabel.setStyle("-fx-text-fill: white;  -fx-background-color: red;");
                        }
                    }
                }
            }
        });


        deleteNestedEdge.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (nestedEdgeView.getSelectionModel().getSelectedIndex() > -1) {
                    nestedWord.removeNestingEdge(nestedEdgeView.getSelectionModel().getSelectedIndex());
                    nestedEdgeView.getItems().remove(nestedEdgeView.getSelectionModel().getSelectedIndex());
                } else {
                    cav.errorNoSelection();
                }
            }
        });

        deleteSymbol.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (nestedWordList.getSelectionModel().getSelectedIndex() > -1) {
                    nestedWord.removeSymbol(nestedWordList.getSelectionModel().getSelectedIndex());
                    nestedWordList.getItems().clear();
                    for (int i = 0; i < nestedWord.length(); i++) {
                        String st = "(i)";
                        if (selectedNestedWordAutomaton.getAlphabet().getTypeOfSymbol(nestedWord.getSymbol(i)) == Alphabet.CALL_POSITION) {
                            st = "(c)";
                        }
                        if (selectedNestedWordAutomaton.getAlphabet().getTypeOfSymbol(nestedWord.getSymbol(i)) == Alphabet.RETURN_POSITION) {
                            st = "(r)";
                        }
                        nestedWordList.getItems().add(i + ". " + nestedWord.getSymbol(i) + " " + st);

                    }

                    to.setItems(
                            FXCollections.observableArrayList(nestedWord.getReturnPositions()));
                    to.getItems().add(-1);

                    from.setItems(
                            FXCollections.observableArrayList(nestedWord.getCallPositions()));
                    from.getItems().add(-2);

                } else {
                    cav.errorNoSelection();
                }
            }
        });


        // Layout
        VBox nestedEdgesBox = new VBox();
        nestedEdgesBox.getChildren().addAll(nestedEdgeLabel, nestedEdgeView);

        VBox nestedWordBox = new VBox();
        nestedWordBox.getChildren().addAll(nestedWordLabel, nestedWordList);

        AnchorPane anchorpane = new AnchorPane();
        HBox hb = new HBox();
        hb.setPadding(new Insets(0, 10, 10, 10));
        hb.setSpacing(10);

        VBox nestedBox = new VBox();
        nestedBox.getChildren().addAll(from, to, addNestedEdgeButton);

        VBox buttonsBox = new VBox();
        buttonsBox.getChildren().addAll(automataComboBox, alphabetComboBox, addSymbol, nestedBox);


        VBox controllButtons = new VBox();
        controllButtons.getChildren().addAll(b1, deleteSymbol, deleteNestedEdge, acceptingLabel);


        hb.getChildren().addAll(nestedWordBox, nestedEdgesBox, buttonsBox, controllButtons);

        VBox content = new VBox();
        content.getChildren().addAll(dv, hb);


        // Load predefined automata
        NWAStorage nwaStorage = new NWAStorage();

        for (NestedWordAutomaton nwa : nwaStorage.loadFromFile()
                ) {
            try {
                ((CommonNestedWordAutomaton) nwa).printAll();
                ((CommonNestedWordAutomaton) nwa).getAlphabet().printAll();
                this.newAutomaton(nwa, nwa.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tab1 = new Tab();
        tab1.setText("Lauf darstellen");
        Tab tab2 = new Tab();
        tab2.setText("Automaten verwalten");
        Tab tab3 = new Tab();
        tab3.setText("Operationen");

        tab1.setContent(content);
        tab2.setContent(cav);
        tab3.setContent(ov);

        tabPane.getTabs().addAll(tab1, tab2, tab3);
        anchorpane.getChildren().addAll(tabPane);

        primaryStage.setTitle("Grafische Testumgebung für eine Bibliothek für Nested-Word-Automaten");
        primaryStage.setScene(new Scene(anchorpane, 1000, 600));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    /**
     * if there was created a new automata all necessary comboboxes and the list of automata has to be updated
     *
     * @param nwa  new created Nested-word automata
     * @param name name of the newly created Nested-word automata
     */
    public void newAutomaton(NestedWordAutomaton nwa, String name) {
        ((CommonNestedWordAutomaton) nwa).printAll();
        nwa.setName(name);
        this.saveAutomataList.add(nwa);
        this.automataComboBox.getItems().add(name);
        this.cav.clearAll();
        this.cav.createdAutomataBox.getItems().add(name);
        this.cav.createdAutomataBox.getSelectionModel().select(this.cav.createdAutomataBox.getItems().size() - 1);

        this.ov.automata2ComboBox.getItems().add(name);
        this.ov.automata1ComboBox.getItems().add(name);

    }

    @Override
    public void saveAutomata(CommonNestedWordAutomaton cnwa, String name) {
        NWAStorage nwaStorage = new NWAStorage();
        nwaStorage.saveDNWA(cnwa);
    }

    @Override
    public void saveAutomata(NondeterministicNestedWordAutomaton nnwa, String name) {
        NWAStorage nwaStorage = new NWAStorage();
        nwaStorage.saveNNWA(nnwa);
    }

    @Override
    public void saveAutomata(LinearAcceptingNestedWordAutomaton lanwa, String name) {
        NWAStorage nwaStorage = new NWAStorage();
        nwaStorage.saveLNWA(lanwa);
    }


    /**
     * Dialog for displaying error
     *
     * @param title   title of dialog
     * @param content text content of dialog
     */
    public void errorDialog(String title, String content) {
        createDialog(title, content, Alert.AlertType.ERROR);
    }

    /**
     * Dialog for displaying information
     *
     * @param title   title of dialog
     * @param content text content of dialog
     */
    public void infoDialog(String title, String content) {
        createDialog(title, content, Alert.AlertType.INFORMATION);
    }

    /**
     * Method for creating dialog
     *
     * @param title     title of dialog
     * @param content   text content of dialog
     * @param alertType Type of Dialog that should be created
     */
    public void createDialog(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);

        alert.setContentText(content);

        alert.showAndWait();

    }


    /*

    // this code was only for test purposes at beginning of implementation
    public NestedWord getNestedWord(Alphabet alpha) {
        NestedWord nw = new NestedWord();
        nw.setAlphabet(alpha);

        try {
            nw.addSymbol("1");
            nw.addSymbol("<0");
            nw.addSymbol("1");
            nw.addSymbol("<1");
            nw.addSymbol("0");
            nw.addSymbol("0");
            nw.addSymbol("1>");
            nw.addSymbol("0>");
            nw.addSymbol("0");
        } catch (InputWordNotInAlphabeth inputWordNotInAlphabeth) {
            inputWordNotInAlphabeth.printStackTrace();
        }

        nw.addNestedEdge(1, 7);
        nw.addNestedEdge(3, 6);

        return nw;
    }

    public CommonNestedWordAutomaton getTestDNWA() {
        CommonNestedWordAutomaton dnwa = new CommonNestedWordAutomaton();


        String one = "1";
        String zero = "0";

        String onebrac = "<1";

        String zerobrac = "<0";

        String onebrar = "1>";

        String zerobrar = "0>";


        Alphabet alpha = new Alphabet();

        try {
            alpha.addSymbol(one, Alphabet.INTERNAL_POSITION);
            alpha.addSymbol(onebrac, Alphabet.CALL_POSITION);
            alpha.addSymbol(onebrar, Alphabet.RETURN_POSITION);
            alpha.addSymbol(zero, Alphabet.INTERNAL_POSITION);
            alpha.addSymbol(zerobrac, Alphabet.CALL_POSITION);
            alpha.addSymbol(zerobrar, Alphabet.RETURN_POSITION);
        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        dnwa.setAlphabet(alpha);

        LinearState q0 = new LinearState("q0");
        LinearState q1 = new LinearState("q1");

        HierarchyState p = new HierarchyState("p");

        HierarchyState p0 = new HierarchyState("p0");
        HierarchyState p1 = new HierarchyState("p1");

        dnwa.setQ0(q0);

        dnwa.addStateToQ(q0);
        dnwa.addStateToQ(q1);

        dnwa.addStateToQf(q0);
        dnwa.addStateToQf(q1);

        dnwa.addHierarchyStateToP(p);
        dnwa.addHierarchyStateToP(p0);
        dnwa.addHierarchyStateToP(p1);

        dnwa.setP0(p);

        dnwa.addHierarchyStateToPf(p);

        CallTransitionFunctions deltaC = new CallTransitionFunctions(dnwa);
        ReturnTransitionFunctions deltaR = new ReturnTransitionFunctions(dnwa);
        InternalTransitionFunctions deltaI = new InternalTransitionFunctions(dnwa);

        try {


            deltaC.addTransitionFunction(q0, zerobrac, q1, p0);
            deltaC.addTransitionFunction(q0, onebrac, q0, p0);
            deltaC.addTransitionFunction(q1, zerobrac, q1, p1);
            deltaC.addTransitionFunction(q1, onebrac, q0, p1);

            deltaR.addTransitionFunction(q1, p0, zerobrar, q0);
            deltaR.addTransitionFunction(q0, p1, onebrar, q1);
            deltaR.addTransitionFunction(q1, p1, onebrar, q1);
            deltaR.addTransitionFunction(q1, p1, zerobrar, q1);

            deltaI.addTransitionFunction(q1, one, q1);
            deltaI.addTransitionFunction(q1, zero, q0);
            deltaI.addTransitionFunction(q0, one, q0);
            deltaI.addTransitionFunction(q0, zero, q1);

        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        dnwa.setDeltaC(deltaC);
        dnwa.setDeltaI(deltaI);
        dnwa.setDeltaR(deltaR);


        return dnwa;
    }


    public CommonNestedWordAutomaton getTestAutomata() {
        CommonNestedWordAutomaton dnwa = new CommonNestedWordAutomaton();

        String opensbrac = "{";
        String closesbrac = "}";

        String openbrac = "(";
        String closebrac = ")";

        String intw = "int";
        String booleanw = "boolean";

        String function = "function";

        String main = "main";
        String f1 = "psa_a38";
        String f2 = "tatltuae";

        String returnw = "return";

        String zero = "0";
        String one = "1";
        String two = "2";
        String three = "3";
        String four = "4";
        String five = "5";
        String six = "6";
        String seven = "7";
        String eight = "8";
        String nine = "9";
        String minus = "-";
        String plus = "+";
        String space = " ";
        String tab = "\t";
        String newline = "\n";
        String equal = "=";
        String truew = "true";
        String falsew = "false";

        String ix = "i";

        String vf = "visitFloor";
        String va = "visitedAll";

        String aao = "aao";

        String dot = ".";

        String forw = "for";
        String ifw = "if";
        String elsew = "else";

        String semi = ";";


        String lesser = "<";
        String greater = ">";

        String count = "count";
        String years = "years";

        String dt = "deepThought";
        String fa = "foundAnswer";

        LinearState q0 = new LinearState("q0");
        // LinearState q1= new LinearState("q1");

        HierarchyState p = new HierarchyState("p");

        HierarchyState p0 = new HierarchyState("()");
        HierarchyState p1 = new HierarchyState("{}");

        dnwa.setQ0(q0);

        dnwa.addStateToQ(q0);
        // dnwa.addStateToQ(q1);

        dnwa.addStateToQf(q0);
        //dnwa.addStateToQf(q1);

        dnwa.addHierarchyStateToP(p);
        dnwa.addHierarchyStateToP(p0);
        dnwa.addHierarchyStateToP(p1);

        dnwa.setP0(p);

        dnwa.addHierarchyStateToPf(p);

        CallTransitionFunctions deltaC = new CallTransitionFunctions(dnwa);
        ReturnTransitionFunctions deltaR = new ReturnTransitionFunctions(dnwa);
        InternalTransitionFunctions deltaI = new InternalTransitionFunctions(dnwa);

        try {

            deltaC.addTransitionFunction(q0, openbrac, q0, p0);
            deltaC.addTransitionFunction(q0, opensbrac, q0, p1);

            deltaR.addTransitionFunction(q0, p0, closebrac, q0);
            deltaR.addTransitionFunction(q0, p1, closesbrac, q0);

            deltaI.addTransitionFunction(q0, zero, q0);
            deltaI.addTransitionFunction(q0, one, q0);
            deltaI.addTransitionFunction(q0, two, q0);
            deltaI.addTransitionFunction(q0, three, q0);
            deltaI.addTransitionFunction(q0, four, q0);
            deltaI.addTransitionFunction(q0, five, q0);
            deltaI.addTransitionFunction(q0, six, q0);
            deltaI.addTransitionFunction(q0, seven, q0);
            deltaI.addTransitionFunction(q0, eight, q0);
            deltaI.addTransitionFunction(q0, nine, q0);

            deltaI.addTransitionFunction(q0, ifw, q0);
            deltaI.addTransitionFunction(q0, forw, q0);
            deltaI.addTransitionFunction(q0, elsew, q0);
            deltaI.addTransitionFunction(q0, returnw, q0);

            deltaI.addTransitionFunction(q0, dot, q0);
            deltaI.addTransitionFunction(q0, semi, q0);
            deltaI.addTransitionFunction(q0, newline, q0);
            deltaI.addTransitionFunction(q0, space, q0);
            deltaI.addTransitionFunction(q0, tab, q0);

            deltaI.addTransitionFunction(q0, function, q0);

            deltaI.addTransitionFunction(q0, intw, q0);
            deltaI.addTransitionFunction(q0, booleanw, q0);
            deltaI.addTransitionFunction(q0, f1, q0);
            deltaI.addTransitionFunction(q0, f2, q0);
            deltaI.addTransitionFunction(q0, main, q0);

            deltaI.addTransitionFunction(q0, equal, q0);
            deltaI.addTransitionFunction(q0, minus, q0);
            deltaI.addTransitionFunction(q0, plus, q0);

            deltaI.addTransitionFunction(q0, count, q0);
            deltaI.addTransitionFunction(q0, years, q0);
            deltaI.addTransitionFunction(q0, dt, q0);
            deltaI.addTransitionFunction(q0, aao, q0);

            deltaI.addTransitionFunction(q0, fa, q0);
            deltaI.addTransitionFunction(q0, vf, q0);
            deltaI.addTransitionFunction(q0, va, q0);

            deltaI.addTransitionFunction(q0, ix, q0);

            deltaI.addTransitionFunction(q0, lesser, q0);
            deltaI.addTransitionFunction(q0, greater, q0);

            deltaI.addTransitionFunction(q0, truew, q0);
            deltaI.addTransitionFunction(q0, falsew, q0);


        } catch (NestedWordSymbolAlreadyExistsException e) {
            e.printStackTrace();
        }

        dnwa.setDeltaC(deltaC);
        dnwa.setDeltaI(deltaI);
        dnwa.setDeltaR(deltaR);

        return dnwa;
    }


*/


}


