package de.wwu.nwa.gui;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.NestedWordAutomaton;
import de.wwu.nwa.automata.NondeterministicNestedWordAutomaton;
import de.wwu.nwa.automata.items.Alphabet;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolAlreadyExistsException;
import de.wwu.nwa.automata.items.exceptions.NestedWordSymbolNotFoundException;
import de.wwu.nwa.automata.items.state.HierarchyState;
import de.wwu.nwa.automata.items.state.LinearState;
import de.wwu.nwa.automata.items.transition.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// todo: list transition clicked

/**
 * View for creating automatas in gui
 *
 * @author Allan Grunert
 */
public class CreateAutomataView extends Pane {

    public ComboBox createdAutomataBox;
    // for deterministic Automata
    CallTransitionFunctions ctfs;
    ReturnTransitionFunctions rtfs;
    InternalTransitionFunctions itfs;
    // for nondeterministic automata
    CallTransitionRelations ctrs;
    InternalTransitionRelations itrs;
    ReturnTransitionRelations rtrs;
    ArrayList<LinearState> Q;
    ArrayList<HierarchyState> P;
    ArrayList<LinearState> Qf;
    ArrayList<HierarchyState> Pf;
    private Alphabet alphabet;
    private ComboBox sourceQ;
    private ComboBox inputSymbol;
    private ComboBox targetQ;
    private ComboBox sourceP;
    private ComboBox targetP;
    private ComboBox automatTypeBox;
    private TextField transitionName;
    private TextField automataName;
    private ListView callListView;
    private ListView internalListView;
    private ListView returnListView;
    private ListView linearStateListView;
    private ListView acceptingLinearStatesListView;
    private ListView hierarchyStateListView;
    private ListView acceptingHierarchyListView;
    private ListView alphabetListView;
    private Main main;

    private Label setQ0Label;
    private Label setP0Label;


    /**
     * Window for creating new Nested-word automata and alphabets
     *
     * @param main Main Class in which this view is embedded
     */
    public CreateAutomataView(Main main) {

        this.main = main;
        alphabet = new Alphabet();
        ArrayList<SaveAutomata> saveAutomataList = new ArrayList<SaveAutomata>();

        Q = new ArrayList<LinearState>();
        P = new ArrayList<HierarchyState>();
        Qf = new ArrayList<LinearState>();
        Pf = new ArrayList<HierarchyState>();

        itfs = new InternalTransitionFunctions(null);
        ctfs = new CallTransitionFunctions(null);
        rtfs = new ReturnTransitionFunctions(null);

        // automata
        automataName = new TextField("");
        createdAutomataBox = new ComboBox();
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Common NWA",
                        "Linear Accepting NWA" //,
                        // "Nondeterministic NWA"
                );

        automatTypeBox = new ComboBox(options);

        Button saveButton = new Button("Automaten speichern");
        Button newButton = new Button("Neuer Automat");
        Button infoBtn = new Button("Info (Konsole)");

        // Alphabet
        Label alphabetLabel = new Label("Alphabet:");
        alphabetListView = new ListView();
        alphabetListView.setPrefHeight(100.0);
        Label symbolLabel = new Label("Symbol");
        TextField symbolTextField = new TextField();
        Button addSymbolButton = new Button("hinzufügen");
        Button delSymbolButton = new Button("löschen");

        ObservableList<String> optionsAlpha =
                FXCollections.observableArrayList(
                        "Internal",
                        "Call",
                        "Return"
                );


        ComboBox alphaComboBox = new ComboBox(optionsAlpha);

        // Transitions
        Label automataNameLabel = new Label("Name des Automaten: ");
        Label automataTypeLabel = new Label(" Typ des Automaten: ");
        Label transitionNameLabel = new Label("Transition Name:");
        transitionName = new TextField();
        Label sourceQLabel = new Label("q (Source)");
        sourceQ = new ComboBox();
        Label targetQLabel = new Label("q (Target)");
        targetQ = new ComboBox();
        Label sourcePLabel = new Label("p (Source)");
        sourceP = new ComboBox();
        Label targetPLabel = new Label("p (Target)");
        targetP = new ComboBox();
        Label inputSymbolLabel = new Label("input symbol");
        inputSymbol = new ComboBox();

        Button callTransitionBtn = new Button("Call Transition hinzufügen");
        Button returnTransitionBtn = new Button("Return Transition hinzufügen");
        Button internalTransitionBtn = new Button("Internal Transition hinzufügen");

        Button callTransitionEditBtn = new Button("Call Transition bearbeiten");
        Button returnTransitionEditBtn = new Button("Return Transition bearbeiten");
        Button internalTransitionEditBtn = new Button("Internal Transition berarbeiten");

        Button callTransitionRMBtn = new Button("Call Transition löschen");
        Button returnTransitionRMBtn = new Button("Return Transition löschen");
        Button internalTransitionRMBtn = new Button("Internal Transition löschen");

        // Third column
        Label callLabel = new Label("Call Transitionen");
        callListView = new ListView();
        callListView.setPrefHeight(50.0);

        Label returnLabel = new Label("Return Transition");
        returnListView = new ListView();
        returnListView.setPrefHeight(50.0);

        Label internalLabel = new Label("Internal Transition");
        internalListView = new ListView();
        internalListView.setPrefHeight(50.0);

        Label acceptingLinearStatesLabel = new Label("Akzeptierende lin. Zustände");
        acceptingLinearStatesListView = new ListView();
        acceptingLinearStatesListView.setPrefHeight(50.0);

        Label acceptingHierarchyStateLabel = new Label("Akzeptierende hierarchische Zust.");
        acceptingHierarchyListView = new ListView();
        acceptingHierarchyListView.setPrefHeight(50.0);

        // Linear State
        Label linearStatesLabel = new Label("Linear States:");
        linearStateListView = new ListView();
        linearStateListView.setPrefHeight(100.0);
        Label linearStateLabel = new Label("Linearer Zustand");
        TextField lineareStateTextField = new TextField();
        Button linearStateButton = new Button("hinzufügen");

        //  Hierarchy State
        Label hierachyStatesLabel = new Label("Hierarchical States:");
        hierarchyStateListView = new ListView();
        hierarchyStateListView.setPrefHeight(100.0);
        Label hierarchyStateLabel = new Label("Hierarchischer Zustand");
        TextField hierarchyStateTextField = new TextField();
        Button hierarchyStateButton = new Button("hinzufügen");

        // Accepting States
        Button addQfButton = new Button("zu Qf hinzufügen");
        Button delQfButton = new Button("von Qf löschen");
        Button addPfButton = new Button("zu Pf hinzufügen");
        Button delPfButton = new Button("von Pf löschen");


        // Start State
        setQ0Label = new Label("");
        setP0Label = new Label("");
        Button setQ0Button = new Button("q0 setzen");
        Button setP0Button = new Button("p0 setzen");
        Button delQButton = new Button("löschen");
        Button delPButton = new Button("löschen");


        // events ----------------------------

        infoBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (createdAutomataBox.getSelectionModel().getSelectedIndex() < 0) {
                    errorNoSelection();
                } else {
                    printInfo();
                }
            }
        });


        // add Automata
        newButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (automataName.getText().equals("") || createdAutomataBox.getItems().contains(automataName.getText())) {
                    main.errorDialog("Fehler Erstellung eines Automaten", "Entweder wurde keine Bezeichnung oder eine bereits vorhandene Bezeichnung für den Automaten eingegeben");

                } else {
                    if (automatTypeBox.getSelectionModel().getSelectedIndex() < 0) {
                        errorAutomatonTypNotSelected();
                    } else {
                        if (automataName.getText().equals("")) {
                            errorAutomatonNameNotGiven();
                        } else {
                            if (automatTypeBox.getSelectionModel().getSelectedIndex() == 0) {
                                CommonNestedWordAutomaton cnwa = new CommonNestedWordAutomaton();
                                cnwa.setName(automataName.getText());
                                // common nwa
                                main.newAutomaton(cnwa, automataName.getText());
                                alphabet = new Alphabet();
                                cnwa.setAlphabet(alphabet);
                                // createdAutomataBox.getItems().add((String) automataName.getText());
                                // createdAutomataBox.getSelectionModel().select(createdAutomataBox.getItems().size() - 1);
                            }
                            if (automatTypeBox.getSelectionModel().getSelectedIndex() == 1) {
                                // lanwa
                                LinearAcceptingNestedWordAutomaton lanwa = new LinearAcceptingNestedWordAutomaton();
                                lanwa.setName(automataName.getText());
                                main.newAutomaton(lanwa, automataName.getText());
                                alphabet = new Alphabet();
                                lanwa.setAlphabet(alphabet);
                                // createdAutomataBox.getItems().add(automataName.getText());
                                // createdAutomataBox.getSelectionModel().select(createdAutomataBox.getItems().size() - 1);
                            }

                            if (automatTypeBox.getSelectionModel().getSelectedIndex() == 2) {
                                // nnwa
                                NondeterministicNestedWordAutomaton nnwa = new NondeterministicNestedWordAutomaton();
                                nnwa.setName(automataName.getText());
                                // common nwa
                                main.newAutomaton(nnwa, automataName.getText());
                                alphabet = new Alphabet();
                                nnwa.setAlphabet(alphabet);
                                // createdAutomataBox.getItems().add(automataName.getText());
                                createdAutomataBox.getSelectionModel().select(createdAutomataBox.getItems().size() - 1);
                            }
                        }
                    }
                }
            }
        });

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // createdAutomataBox.getItems().add(automataName.getText());

                // create new file
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter("automata.xml"));
                    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                            "<NestedWordAutomata>");
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                // append alll automata to file
                for (int i = 0; i < main.saveAutomataList.size(); i++) {
                    NestedWordAutomaton nwa = main.saveAutomataList.get(i);
                    if (nwa.getClass().equals(CommonNestedWordAutomaton.class)) {
                        main.saveAutomata((CommonNestedWordAutomaton) nwa, (String) main.automataComboBox.getItems().get(i));
                    }
                    if (nwa.getClass().equals(NondeterministicNestedWordAutomaton.class)) {
                        main.saveAutomata((NondeterministicNestedWordAutomaton) nwa, (String) main.automataComboBox.getItems().get(i));
                    }
                    if (nwa.getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {
                        main.saveAutomata((LinearAcceptingNestedWordAutomaton) nwa, (String) main.automataComboBox.getItems().get(i));
                    }
                }

                writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter("automata.xml", true));
                    writer.write("</NestedWordAutomata>");
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // TODO: onlick transition

        addSymbolButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    if (alphaComboBox.getSelectionModel().getSelectedIndex() < 0 ||
                            alphaComboBox.getSelectionModel().getSelectedItem() == null ||
                            createdAutomataBox.getSelectionModel().getSelectedIndex() < 0) {
                        errorNoSelection();
                    } else {
                        alphabet = main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getAlphabet();
                        alphabet.addSymbol(symbolTextField.getText(), alphaComboBox.getSelectionModel().getSelectedIndex());
                        alphabetListView.getItems().add(symbolTextField.getText());
                        inputSymbol.getItems().add(symbolTextField.getText() + "(" + alphaComboBox.getSelectionModel().getSelectedItem() + ")");
                        main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).setAlphabet(alphabet);

                    }

                } catch (NestedWordSymbolAlreadyExistsException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Symbol von Alphabet löschen
        delSymbolButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    if (alphabetListView.getSelectionModel().getSelectedIndex() < 0) {
                        errorNoSelection();
                    } else {
                        alphabet.removeSymbol(((String) alphabetListView.getSelectionModel().getSelectedItem()), alphabet.getTypeOfSymbol((String) alphabetListView.getSelectionModel().getSelectedItem()));
                        alphabetListView.getItems().remove(alphabetListView.getSelectionModel().getSelectedIndex());
                    }
                } catch (NestedWordSymbolNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

        linearStateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (createdAutomataBox.getSelectionModel().getSelectedIndex() < 0) {
                    errorNoSelection();
                } else {
                    linearStateListView.getItems().add(lineareStateTextField.getText());
                    sourceQ.getItems().add(lineareStateTextField.getText());
                    targetQ.getItems().add(lineareStateTextField.getText());
                    Q.add(new LinearState(lineareStateTextField.getText()));
                    main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).addStateToQ(Q.get(Q.size() - 1));
                }
            }
        });

        hierarchyStateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (createdAutomataBox.getSelectionModel().getSelectedIndex() < 0) {
                    errorNoSelection();
                } else {

                    hierarchyStateListView.getItems().add(hierarchyStateTextField.getText());
                    sourceP.getItems().add(hierarchyStateTextField.getText());
                    targetP.getItems().add(hierarchyStateTextField.getText());
                    P.add(new HierarchyState(hierarchyStateTextField.getText()));
                    main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).addHierarchyStateToP(P.get(P.size() - 1));
                }
            }
        });

        callTransitionRMBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (callListView.getSelectionModel().getSelectedIndex() > -1) {
                    ((CommonNestedWordAutomaton) main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex())).getDeltaC().getDeltaC().remove(callListView.getSelectionModel().getSelectedIndex());
                    callListView.getItems().remove(callListView.getSelectionModel().getSelectedIndex());
                } else {
                    errorNoSelection();
                }

            }
        });


        returnTransitionRMBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (returnListView.getSelectionModel().getSelectedIndex() > -1) {
                    ((CommonNestedWordAutomaton) main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex())).getDeltaR().getDeltaR().remove(returnListView.getSelectionModel().getSelectedIndex());
                    returnListView.getItems().remove(returnListView.getSelectionModel().getSelectedIndex());
                } else {
                    errorNoSelection();
                }

            }
        });

        internalTransitionRMBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (internalListView.getSelectionModel().getSelectedIndex() > -1) {
                    ((CommonNestedWordAutomaton) main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex())).getDeltaI().getDeltaI().remove(internalListView.getSelectionModel().getSelectedIndex());
                    internalListView.getItems().remove(internalListView.getSelectionModel().getSelectedIndex());
                } else {
                    errorNoSelection();
                }
            }
        });

        callTransitionEditBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (callListView.getSelectionModel().getSelectedIndex() > -1) {

                    if (sourceQ.getSelectionModel().getSelectedIndex() < 0 ||
                            targetQ.getSelectionModel().getSelectedIndex() < 0 ||
                            targetP.getSelectionModel().getSelectedIndex() < 0) {
                        errorWrongStateTransition();

                    } else {
                        if (inputSymbol.getSelectionModel().getSelectedIndex() < 0 || alphabet.getTypeOfSymbol((String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex())) != Alphabet.CALL_POSITION) {
                            errorSymbolAlphabetNoType();
                        } else {

                            if (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(CommonNestedWordAutomaton.class)) {

                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().getDeltaC().get(callListView.getSelectionModel().getSelectedIndex()).setP(P.get(targetP.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().getDeltaC().get(callListView.getSelectionModel().getSelectedIndex()).setqSource(Q.get(sourceQ.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().getDeltaC().get(callListView.getSelectionModel().getSelectedIndex()).setqTarget(Q.get(targetQ.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().getDeltaC().get(callListView.getSelectionModel().getSelectedIndex()).setSymbol((String) inputSymbol.getSelectionModel().getSelectedItem());

                            }

                            if (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().getDeltaC().get(callListView.getSelectionModel().getSelectedIndex()).setP(P.get(targetP.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().getDeltaC().get(callListView.getSelectionModel().getSelectedIndex()).setqSource(Q.get(sourceQ.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().getDeltaC().get(callListView.getSelectionModel().getSelectedIndex()).setqTarget(Q.get(targetQ.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().getDeltaC().get(callListView.getSelectionModel().getSelectedIndex()).setSymbol((String) inputSymbol.getSelectionModel().getSelectedItem());
                            }

                        }
                    }
                } else {
                    errorNoSelection();
                }

            }
        });


        returnTransitionEditBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (returnListView.getSelectionModel().getSelectedIndex() > -1) {

                    if (sourceQ.getSelectionModel().getSelectedIndex() < 0 ||
                            targetQ.getSelectionModel().getSelectedIndex() < 0 ||
                            sourceP.getSelectionModel().getSelectedIndex() < 0) {
                        errorWrongStateTransition();

                    } else {
                        if (inputSymbol.getSelectionModel().getSelectedIndex() < 0 || alphabet.getTypeOfSymbol((String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex())) != Alphabet.RETURN_POSITION) {
                            errorSymbolAlphabetNoType();
                        } else {


                            if (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(CommonNestedWordAutomaton.class)) {

                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().getDeltaR().get(returnListView.getSelectionModel().getSelectedIndex()).setP(P.get(sourceP.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().getDeltaR().get(returnListView.getSelectionModel().getSelectedIndex()).setqSource(Q.get(sourceQ.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().getDeltaR().get(returnListView.getSelectionModel().getSelectedIndex()).setqTarget(Q.get(targetQ.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().getDeltaR().get(returnListView.getSelectionModel().getSelectedIndex()).setSymbol((String) inputSymbol.getSelectionModel().getSelectedItem());

                            }

                            if (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().getDeltaR().get(returnListView.getSelectionModel().getSelectedIndex()).setP(P.get(sourceP.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().getDeltaR().get(returnListView.getSelectionModel().getSelectedIndex()).setqSource(Q.get(sourceQ.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().getDeltaR().get(returnListView.getSelectionModel().getSelectedIndex()).setqTarget(Q.get(targetQ.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().getDeltaR().get(returnListView.getSelectionModel().getSelectedIndex()).setSymbol((String) inputSymbol.getSelectionModel().getSelectedItem());
                            }
                        }
                    }
                } else {
                    errorNoSelection();
                }

            }
        });

        internalTransitionEditBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (sourceQ.getSelectionModel().getSelectedIndex() < 0 ||
                        targetQ.getSelectionModel().getSelectedIndex() < 0) {
                    errorWrongStateTransition();

                } else {
                    if (inputSymbol.getSelectionModel().getSelectedIndex() < 0 || alphabet.getTypeOfSymbol((String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex())) != Alphabet.INTERNAL_POSITION) {
                        errorSymbolAlphabetNoType();
                    } else {

                        if (internalListView.getSelectionModel().getSelectedIndex() > -1) {
                            if (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(CommonNestedWordAutomaton.class)) {

                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaI().getDeltaI().get(internalListView.getSelectionModel().getSelectedIndex()).setqSource(Q.get(sourceQ.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaI().getDeltaI().get(internalListView.getSelectionModel().getSelectedIndex()).setqTarget(Q.get(targetQ.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaI().getDeltaI().get(internalListView.getSelectionModel().getSelectedIndex()).setSymbol((String) inputSymbol.getSelectionModel().getSelectedItem());

                            }

                            if (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaI().getDeltaI().get(internalListView.getSelectionModel().getSelectedIndex()).setqSource(Q.get(sourceQ.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaI().getDeltaI().get(internalListView.getSelectionModel().getSelectedIndex()).setqTarget(Q.get(targetQ.getSelectionModel().getSelectedIndex()));
                                ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaI().getDeltaI().get(internalListView.getSelectionModel().getSelectedIndex()).setSymbol((String) inputSymbol.getSelectionModel().getSelectedItem());
                            }
                        } else {
                            errorNoSelection();
                        }
                    }
                }
            }
        });

        addQfButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (linearStateListView.getSelectionModel().getSelectedItem() != null) {
                    acceptingLinearStatesListView.getItems().add(linearStateListView.getSelectionModel().getSelectedItem());
                    Qf.add(Q.get(linearStateListView.getSelectionModel().getSelectedIndex()));
                    main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).addStateToQf(Qf.get(Qf.size() - 1));

                } else {
                    main.errorDialog("Fehler akzeptierende Zustände", "Es wurde kein Zustand ausgewählt.");
                }
            }
        });


        delQfButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (acceptingLinearStatesListView.getSelectionModel().getSelectedItem() != null) {
                    main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).removeLinearStateFromQf(
                            main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getLinearStateByName(
                                    Qf.get(acceptingLinearStatesListView.getSelectionModel().getSelectedIndex()).getStateName()
                            )
                    );

                    // Qf.remove(acceptingLinearStatesListView.getSelectionModel().getSelectedIndex());
                    acceptingLinearStatesListView.getItems().remove(acceptingLinearStatesListView.getSelectionModel().getSelectedIndex());

                } else {
                    main.errorDialog("Fehler akzeptierende Zustände", "Es wurde kein Zustand ausgewählt.");
                }
            }
        });

        delPfButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (acceptingHierarchyListView.getSelectionModel().getSelectedItem() != null) {
                    main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).removeHierarchyStateFromPf(
                            main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getHierarchyStateByName(
                                    Pf.get(acceptingHierarchyListView.getSelectionModel().getSelectedIndex()).getStateName()
                            )
                    );

                    // Pf.remove(acceptingHierarchyListView.getSelectionModel().getSelectedIndex());
                    acceptingHierarchyListView.getItems().remove(acceptingHierarchyListView.getSelectionModel().getSelectedIndex());

                } else {
                    main.errorDialog("Fehler akzeptierende Zustände", "Es wurde kein Zustand ausgewählt.");
                }
            }
        });


        delQButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (linearStateListView.getSelectionModel().getSelectedItem() != null) {
                    main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).removeLinearStateFromQ(
                            main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getLinearStateByName(
                                    Q.get(linearStateListView.getSelectionModel().getSelectedIndex()).getStateName()
                            )
                    );

                    // Qf.remove(acceptingLinearStatesListView.getSelectionModel().getSelectedIndex());
                    linearStateListView.getItems().remove(linearStateListView.getSelectionModel().getSelectedIndex());
                    acceptingLinearStatesListView.getItems().clear();
                    for (LinearState q : Qf) {
                        acceptingLinearStatesListView.getItems().add(q.getStateName());
                    }

                } else {
                    main.errorDialog("Fehler akzeptierende Zustände", "Es wurde kein Zustand ausgewählt.");
                }
            }
        });

        delPButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (hierarchyStateListView.getSelectionModel().getSelectedItem() != null) {
                    main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).removeHierarchyStateFromP(
                            main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getHierarchyStateByName(
                                    P.get(hierarchyStateListView.getSelectionModel().getSelectedIndex()).getStateName()
                            )
                    );

                    // Pf.remove(acceptingHierarchyListView.getSelectionModel().getSelectedIndex());
                    hierarchyStateListView.getItems().remove(hierarchyStateListView.getSelectionModel().getSelectedIndex());
                    acceptingHierarchyListView.getItems().clear();
                    for (HierarchyState p : Pf) {
                        acceptingHierarchyListView.getItems().add(p.getStateName());
                    }

                } else {
                    main.errorDialog("Fehler akzeptierende Zustände", "Es wurde kein Zustand ausgewählt.");
                }
            }
        });

        setQ0Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                // TODO: Fehler
                if (linearStateListView.getSelectionModel().getSelectedItem() != null) {
                    setQ0Label.setText("q0: " + (String) linearStateListView.getSelectionModel().getSelectedItem());
                    main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).setQ0(Q.get(linearStateListView.getSelectionModel().getSelectedIndex()));
                } else {
                    main.errorDialog("Fehler linearer Anfangszustand", "Es wurde kein linearer Anfangszustand ausgewählt");
                }
            }
        });

        setP0Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (hierarchyStateListView.getSelectionModel().getSelectedItem() != null) {
                    setP0Label.setText("p0: " + (String) hierarchyStateListView.getSelectionModel().getSelectedItem());
                    main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).setP0(P.get(hierarchyStateListView.getSelectionModel().getSelectedIndex()));
                } else {
                    main.errorDialog("Fehler hierarchischer Anfangszustand", "Es wurde kein hierarchischer Zustand ausgewählt.");
                }
            }
        });

        addPfButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (hierarchyStateListView.getSelectionModel().getSelectedItem() != null) {
                    acceptingHierarchyListView.getItems().add(hierarchyStateListView.getSelectionModel().getSelectedItem());
                    Pf.add(P.get(hierarchyStateListView.getSelectionModel().getSelectedIndex()));
                    main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).addHierarchyStateToPf(Pf.get(Pf.size() - 1));

                } else {
                    main.errorDialog("Fehler akzeotierender hierarchischer Zustand", "Es wurde kein hierarchischischer Zustand ausgewählt.");
                }
            }
        });


        callTransitionBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    if (createdAutomataBox.getSelectionModel().getSelectedIndex() < 0) {
                        errorNoSelection();
                    } else {

                        if (sourceQ.getSelectionModel().getSelectedIndex() < 0 ||
                                targetQ.getSelectionModel().getSelectedIndex() < 0 ||
                                targetP.getSelectionModel().getSelectedIndex() < 0) {
                            errorWrongStateTransition();

                        } else {
                            if (inputSymbol.getSelectionModel().getSelectedIndex() < 0 || alphabet.getTypeOfSymbol((String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex())) != Alphabet.CALL_POSITION) {
                                errorSymbolAlphabetNoType();
                            } else {

                                callListView.getItems().add(transitionName.getText());
                                ctfs.addTransitionFunction(Q.get(sourceQ.getSelectionModel().getSelectedIndex()), (String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex()), Q.get(targetQ.getSelectionModel().getSelectedIndex()), P.get(targetP.getSelectionModel().getSelectedIndex()), transitionName.getText());
                                if (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(CommonNestedWordAutomaton.class)) {
                                    ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().addTransitionFunction(Q.get(sourceQ.getSelectionModel().getSelectedIndex()), (String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex()), Q.get(targetQ.getSelectionModel().getSelectedIndex()), P.get(targetP.getSelectionModel().getSelectedIndex()), transitionName.getText());
                                }

                                if (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {
                                    ((LinearAcceptingNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().addTransitionFunction(Q.get(sourceQ.getSelectionModel().getSelectedIndex()), (String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex()), Q.get(targetQ.getSelectionModel().getSelectedIndex()), P.get(targetP.getSelectionModel().getSelectedIndex()), transitionName.getText());

                                }
                            }
                        }
                    }

                } catch (NestedWordSymbolAlreadyExistsException e1) {
                    e1.printStackTrace();
                }
            }
        });

        returnTransitionBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    if (createdAutomataBox.getSelectionModel().getSelectedIndex() < 0) {
                        errorNoSelection();
                    } else {
                        if (sourceQ.getSelectionModel().getSelectedIndex() < 0 ||
                                targetQ.getSelectionModel().getSelectedIndex() < 0 ||
                                sourceP.getSelectionModel().getSelectedIndex() < 0) {
                            errorWrongStateTransition();

                        } else {
                            if (inputSymbol.getSelectionModel().getSelectedIndex() < 0 || alphabet.getTypeOfSymbol((String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex())) != Alphabet.RETURN_POSITION) {
                                errorSymbolAlphabetNoType();
                            } else {

                                returnListView.getItems().add(transitionName.getText());
                                rtfs.addTransitionFunction(Q.get(sourceQ.getSelectionModel().getSelectedIndex()), P.get(sourceP.getSelectionModel().getSelectedIndex()), (String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex()), Q.get(targetQ.getSelectionModel().getSelectedIndex()), transitionName.getText());

                                if (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(CommonNestedWordAutomaton.class)) {
                                    ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().addTransitionFunction(Q.get(sourceQ.getSelectionModel().getSelectedIndex()), P.get(sourceP.getSelectionModel().getSelectedIndex()), (String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex()), Q.get(targetQ.getSelectionModel().getSelectedIndex()), transitionName.getText());
                                }

                                if (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {
                                    ((LinearAcceptingNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().addTransitionFunction(Q.get(sourceQ.getSelectionModel().getSelectedIndex()), P.get(sourceP.getSelectionModel().getSelectedIndex()), (String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex()), Q.get(targetQ.getSelectionModel().getSelectedIndex()), transitionName.getText());

                                }
                            }
                        }
                    }

                } catch (NestedWordSymbolAlreadyExistsException e1) {
                    e1.printStackTrace();
                }
            }
        });

        internalTransitionBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (createdAutomataBox.getSelectionModel().getSelectedIndex() < 0) {
                    errorNoSelection();
                } else {
                    if (sourceQ.getSelectionModel().getSelectedIndex() < 0 ||
                            targetQ.getSelectionModel().getSelectedIndex() < 0) {
                        errorWrongStateTransition();

                    } else {
                        if (inputSymbol.getSelectionModel().getSelectedIndex() < 0 || alphabet.getTypeOfSymbol((String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex())) != Alphabet.INTERNAL_POSITION) {
                            errorSymbolAlphabetNoType();
                        } else {
                            try {

                                internalListView.getItems().add(transitionName.getText());
                                itfs.addTransitionFunction(Q.get(sourceQ.getSelectionModel().getSelectedIndex()), (String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex()), Q.get(targetQ.getSelectionModel().getSelectedIndex()), transitionName.getText());

                                if (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(CommonNestedWordAutomaton.class)) {
                                    ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaI().addTransitionFunction(Q.get(sourceQ.getSelectionModel().getSelectedIndex()), (String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex()), Q.get(targetQ.getSelectionModel().getSelectedIndex()), transitionName.getText());
                                }

                                if (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {
                                    ((LinearAcceptingNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaI().addTransitionFunction(Q.get(sourceQ.getSelectionModel().getSelectedIndex()), (String) alphabetListView.getItems().get(inputSymbol.getSelectionModel().getSelectedIndex()), Q.get(targetQ.getSelectionModel().getSelectedIndex()), transitionName.getText());

                                }
                            } catch (NestedWordSymbolAlreadyExistsException e1) {
                                e1.printStackTrace();
                            }

                        }
                    }
                }

            }
        });

        createdAutomataBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                NestedWordAutomaton nwa = main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex());
                if (nwa.getAlphabet() != null) {
                    clearAll();

                    // Automaten Typ
                    if (nwa.getClass().equals(CommonNestedWordAutomaton.class))
                        automatTypeBox.getSelectionModel().select(0);
                    if (nwa.getClass().equals(LinearAcceptingNestedWordAutomaton.class))
                        automatTypeBox.getSelectionModel().select(1);
                    if (nwa.getClass().equals(NondeterministicNestedWordAutomaton.class))
                        automatTypeBox.getSelectionModel().select(2);

                    // Alphabete
                    for (String sym : nwa.getAlphabet().getAlphabet(Alphabet.CALL_POSITION)
                            ) {
                        alphabetListView.getItems().add(sym);
                        inputSymbol.getItems().add(sym + " (c)");
                    }
                    for (String sym : nwa.getAlphabet().getAlphabet(Alphabet.INTERNAL_POSITION)
                            ) {
                        alphabetListView.getItems().add(sym);
                        inputSymbol.getItems().add(sym + " (i)");
                    }
                    for (String sym : nwa.getAlphabet().getAlphabet(Alphabet.RETURN_POSITION)
                            ) {
                        alphabetListView.getItems().add(sym);
                        inputSymbol.getItems().add(sym + " (r)");
                    }

                    alphabet = nwa.getAlphabet().clone();

                    // Lin Zust
                    Q = nwa.getQ();
                    Qf = nwa.getQf();

                    for (LinearState q : Q) {
                        sourceQ.getItems().add(q.getStateName());
                        targetQ.getItems().add(q.getStateName());
                        linearStateListView.getItems().add(q.getStateName());
                    }

                    for (LinearState q : Qf) {
                        acceptingLinearStatesListView.getItems().add(q.getStateName());
                    }

                    // hier Zust
                    P = nwa.getP();
                    Pf = nwa.getPf();
                    for (HierarchyState p : P) {
                        sourceP.getItems().add(p.getStateName());
                        targetP.getItems().add(p.getStateName());
                        hierarchyStateListView.getItems().add(p.getStateName());
                    }

                    for (HierarchyState p : Pf) {
                        acceptingHierarchyListView.getItems().add(p.getStateName());
                    }


                    if (nwa.getQ0() != null)
                        setQ0Label.setText("q0: " + nwa.getQ0().getStateName());
                    if (nwa.getP0() != null)
                        setP0Label.setText("p0: " + nwa.getP0().getStateName());


                    if (nwa.getClass().equals(CommonNestedWordAutomaton.class) || nwa.getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {

                        // c trans
                        for (CallTransitionFunction ctf : ((CommonNestedWordAutomaton) nwa).getDeltaC().getDeltaC()
                                ) {
                            callListView.getItems().add(ctf.getName());
                            ctfs.getDeltaC().add(ctf);
                        }

                        // i trans
                        for (InternalTransitionFunction itf : ((CommonNestedWordAutomaton) nwa).getDeltaI().getDeltaI()
                                ) {
                            internalListView.getItems().add(itf.getName());
                            itfs.getDeltaI().add(itf);
                        }
                        // r trans
                        for (ReturnTransitionFunction rtf : ((CommonNestedWordAutomaton) nwa).getDeltaR().getDeltaR()
                                ) {
                            returnListView.getItems().add(rtf.getName());
                            rtfs.getDeltaR().add(rtf);
                        }
                    }

                    if (nwa.getClass().equals(NondeterministicNestedWordAutomaton.class)) {
                        // TODO: implement
                    }

                }
            }
        });


// TODO: Fehler!!!
        internalListView.setOnMouseClicked(event -> {
            sourceQ.getSelectionModel().select(
                    ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaI().getDeltaI().get(internalListView.getSelectionModel().getSelectedIndex()).getqSource().getStateName());
            targetQ.getSelectionModel().select(((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaI().getDeltaI().get(internalListView.getSelectionModel().getSelectedIndex()).getqTarget().getStateName());
            inputSymbol.getSelectionModel().select(((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaI().getDeltaI().get(internalListView.getSelectionModel().getSelectedIndex()).getSymbol());

        });

        callListView.setOnMouseClicked(event -> {
            sourceQ.getSelectionModel().select(
                    ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().getDeltaC().get(callListView.getSelectionModel().getSelectedIndex()).getqSource().getStateName());
            targetQ.getSelectionModel().select(((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().getDeltaC().get(callListView.getSelectionModel().getSelectedIndex()).getqTarget().getStateName());
            inputSymbol.getSelectionModel().select(((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().getDeltaC().get(callListView.getSelectionModel().getSelectedIndex()).getSymbol());
            targetP.getSelectionModel().select(((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaC().getDeltaC().get(callListView.getSelectionModel().getSelectedIndex()).getP().getStateName());

        });

        returnListView.setOnMouseClicked(event -> {
            sourceQ.getSelectionModel().select(
                    ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().getDeltaR().get(returnListView.getSelectionModel().getSelectedIndex()).getqSource().getStateName());
            targetQ.getSelectionModel().select(((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().getDeltaR().get(returnListView.getSelectionModel().getSelectedIndex()).getqTarget().getStateName());
            inputSymbol.getSelectionModel().select(((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().getDeltaR().get(returnListView.getSelectionModel().getSelectedIndex()).getSymbol());
            sourceP.getSelectionModel().select(
                    ((CommonNestedWordAutomaton) (main.saveAutomataList.get(createdAutomataBox.getSelectionModel().getSelectedIndex()))).getDeltaR().getDeltaR().get(returnListView.getSelectionModel().getSelectedIndex()).getP().getStateName());

        });


        // Layout

        HBox generalConfig = new HBox();
        generalConfig.getChildren().addAll(automataNameLabel, automataName, automataTypeLabel, automatTypeBox, newButton, createdAutomataBox, infoBtn, saveButton);
        HBox addSymbolBox = new HBox();
        addSymbolBox.getChildren().addAll(symbolLabel, symbolTextField, alphaComboBox, addSymbolButton, delSymbolButton);
        VBox automataParameters = new VBox();
        automataParameters.getChildren().addAll(alphabetLabel, alphabetListView, addSymbolBox);

        HBox linearStateBox = new HBox();
        linearStateBox.getChildren().addAll(linearStateLabel, lineareStateTextField, linearStateButton, delQButton);

        automataParameters.getChildren().addAll(linearStateLabel, linearStateListView, linearStateBox);

        HBox hierarchyStateBox = new HBox();
        hierarchyStateBox.getChildren().addAll(hierarchyStateLabel, hierarchyStateTextField, hierarchyStateButton, delPButton);

        automataParameters.getChildren().addAll(hierarchyStateLabel, hierarchyStateListView, hierarchyStateBox);

        HBox qButtons = new HBox();
        qButtons.getChildren().addAll(addQfButton, delQfButton);

        HBox pButtons = new HBox();
        pButtons.getChildren().addAll(addPfButton, delPfButton);

        HBox zeroButtons = new HBox();
        zeroButtons.getChildren().addAll(setQ0Button, setP0Button);

        automataParameters.getChildren().addAll(qButtons, pButtons, zeroButtons);

        VBox transitionBox = new VBox();
        transitionBox.getChildren().addAll(transitionNameLabel, transitionName, sourceQLabel, sourceQ, targetQLabel, targetQ, sourcePLabel, sourceP, targetPLabel, targetP, inputSymbolLabel, inputSymbol, callTransitionBtn, callTransitionEditBtn, callTransitionRMBtn, internalTransitionBtn, internalTransitionEditBtn, internalTransitionRMBtn, returnTransitionBtn, returnTransitionEditBtn, returnTransitionRMBtn);


        VBox transitionViewBox = new VBox();
        transitionViewBox.getChildren().addAll(callLabel, callListView, internalLabel, internalListView, returnLabel, returnListView, acceptingLinearStatesLabel, acceptingLinearStatesListView, acceptingHierarchyStateLabel, acceptingHierarchyListView, setQ0Label, setP0Label);

        HBox body = new HBox();
        transitionBox.setPadding(new Insets(5, 3, 3, 3));
        body.getChildren().addAll(automataParameters, transitionBox, transitionViewBox);

        VBox mainBox = new VBox();
        mainBox.getChildren().addAll(generalConfig, body);

        mainBox.setPadding(new Insets(3, 3, 3, 3));
        this.getChildren().add(mainBox);
    }

    /**
     * Print information of automaton to console
     */
    public void printInfo() {
        System.out.println("####################################################################");
        System.out.println("Name des Automats: " + createdAutomataBox.getSelectionModel().getSelectedItem());
        System.out.println("####################################################################");

        main.saveAutomataList.get(this.createdAutomataBox.getSelectionModel().getSelectedIndex()).getAlphabet().printAll();
        if (main.saveAutomataList.get(this.createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(CommonNestedWordAutomaton.class)) {
            ((CommonNestedWordAutomaton) (main.saveAutomataList.get(this.createdAutomataBox.getSelectionModel().getSelectedIndex()))).printAll();

        }

        if (main.saveAutomataList.get(this.createdAutomataBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {
            ((LinearAcceptingNestedWordAutomaton) (main.saveAutomataList.get(this.createdAutomataBox.getSelectionModel().getSelectedIndex()))).printAll();

        }
        System.out.println("####################################################################");
    }

    /**
     * Clear all intereface compontents and configuration for modifying nwa
     */
    public void clearAll() {
        Q = new ArrayList<LinearState>();
        P = new ArrayList<HierarchyState>();
        Qf = new ArrayList<LinearState>();
        Pf = new ArrayList<HierarchyState>();
        alphabetListView.getItems().clear();
        alphabet.clearAlphabet();
        sourceQ.getItems().clear();
        targetQ.getItems().clear();
        sourceP.getItems().clear();
        targetP.getItems().clear();
        callListView.getItems().clear();
        internalListView.getItems().clear();
        returnListView.getItems().clear();
        linearStateListView.getItems().clear();
        acceptingLinearStatesListView.getItems().clear();
        hierarchyStateListView.getItems().clear();
        acceptingHierarchyListView.getItems().clear();
        ctfs = new CallTransitionFunctions(null);
        itfs = new InternalTransitionFunctions(null);
        rtfs = new ReturnTransitionFunctions(null);
        ctrs = new CallTransitionRelations(null);
        rtrs = new ReturnTransitionRelations(null);
        itrs = new InternalTransitionRelations(null);
        setP0Label.setText("");
        setQ0Label.setText("");
        inputSymbol.getItems().clear();
    }

    /**
     * Error message
     */
    public void errorAutomatonTypNotSelected() {
        main.errorDialog("Fehler Neuer Automat", "Für neuen Automaten muss ein Automatentyp ausgewählt werden.");
    }

    /**
     * Error message
     */
    public void errorAutomatonNameNotGiven() {
        main.errorDialog("Fehler Neuer Automat", "Es muss ein Name für den Automaten eingegeben weredn");

    }

    public void errorWrongSymbolTransitoin() {
        main.errorDialog("Fehler Transition", "Für die Transition muss ein passendes Symbol gewählt werden.");
    }

    public void errorWrongStateTransition() {
        main.errorDialog("Fehler Transition", "Für die Transition müssen alle notwenidgen Transitionen gewählt werden.");
    }

    public void errorSymbolAlphabetNoType() {
        main.errorDialog("Fehler hinzufügen eines Symbols zum Alphabet", "Bitte geben Sie einen Typ beim Hinzufügen des Symbols an.");
    }


    public void errorNoSelection() {
        main.errorDialog("Fehler Auswahl", "Es wurde nichts ausgewählt.");
    }

}