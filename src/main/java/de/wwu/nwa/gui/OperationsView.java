package de.wwu.nwa.gui;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.operations.closure.Complement;
import de.wwu.nwa.operations.closure.Intersection;
import de.wwu.nwa.operations.closure.Subtraction;
import de.wwu.nwa.operations.closure.Union;
import de.wwu.nwa.operations.decision.Emptiness;
import de.wwu.nwa.operations.decision.Equivalence;
import de.wwu.nwa.operations.decision.Inclusion;
import de.wwu.nwa.operations.transformation.CommonToLinearAcceptingNestedWordAutomaton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

// TODO: Fenster: Diese Operation steht noch nicht zur Verfügung
// TODO: Löschen etc bei Create Automata
// TODO: Vereinigung


/**
 * Window for applying operations to nested-word-automata
 *
 * @author Allan Grunert
 */
public class OperationsView extends Pane {
    public ComboBox automata1ComboBox;
    public ComboBox automata2ComboBox;
    private Main main;
    private ComboBox closureOperations;
    private ComboBox decistionOperations;
    private ComboBox transformationOperations;
    private Label statusLabel;
    private TextField automatonTextField;

    public OperationsView(Main main) {
        this.main = main;

        Label automatonLabel = new Label("Neuer Automat: ");
        automatonTextField = new TextField();

        Label automaton1Label = new Label(" Automat1: ");
        automata1ComboBox = new ComboBox();

        Label automaton2Label = new Label(" Automat2: ");
        automata2ComboBox = new ComboBox();

        Label closureLabel = new Label("Abgeschlossenheitsoperationen: ");
        closureOperations = new ComboBox();
        Label decisionLabel = new Label("Entscheidungsoperationen: ");
        decistionOperations = new ComboBox();
        Label transformationLabel = new Label("Transformtionsoperationen: ");
        transformationOperations = new ComboBox();

        ObservableList observableClosureList =
                FXCollections.observableArrayList(
                        "Vereinigung",
                        "Schnitt",
                        "Komplement" //,
                        // "Subtraktion" ,
                        // "Spiegelung",
                        //   "Konkatenation",
                        //              "Kleene Stern",

                );
        closureOperations.setItems(observableClosureList);

        ObservableList observableDecisionList =
                FXCollections.observableArrayList(
                        "Wortproblem",
                        "Leerheit",
                        "Äquivalenz",
                        "Inklusion"

                );
        decistionOperations.setItems(observableDecisionList);

        ObservableList observableTransformationList =
                FXCollections.observableArrayList(
                        "Normaler NWA zu Linear-Accepting NWA" //,
                        // "Linear-Accepting NWA zu Normaler NWA",
                        //  "Deterministischer NWA zu Linear-Accepting NWA"

                );
        transformationOperations.setItems(observableTransformationList);

        Button closureBut = new Button("Abgeschlossenheitsoperation ausführen");
        Button decisionBut = new Button("Entscheidungsoperation ausführen");
        Button transformationBut = new Button("Transformationsoperation ausführen");

        closureBut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Cases
                switch (closureOperations.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        union();
                        break;
                    case 1:
                        intersection();
                        break;
                    case 2:
                        complement();
                        break;
                    case 3:
                        subtraction();
                        break;
                    case 4:
                        reverse();
                        break;
                    case 5:
                        concat();
                        break;
                    case 6:
                        star();
                        break;
                }
            }
        });

        decisionBut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                switch (decistionOperations.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        membership();
                        break;
                    case 1:
                        emptyness();
                        break;
                    case 2:
                        equivalence();
                        break;
                    case 3:
                        inklusion();
                        break;

                }
            }
        });


        transformationBut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                switch (transformationOperations.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        cnwa2lanwa();
                        break;
                    case 1:
                        lanwa2cnwa();
                        break;
                    case 2:
                        nnwa2lanwa();
                        break;
                }
            }
        });

        statusLabel = new Label("");

        // Layout

        HBox automataBox = new HBox();
        automataBox.getChildren().addAll(automatonLabel, automatonTextField, automaton1Label, automata1ComboBox, automaton2Label, automata2ComboBox);
        automataBox.setPadding(new Insets(5, 3, 3, 3));

        HBox closureBox = new HBox();
        closureBox.getChildren().addAll(closureLabel, closureOperations, closureBut);
        closureBox.setPadding(new Insets(5, 3, 3, 3));

        HBox decisionBox = new HBox();
        decisionBox.getChildren().addAll(decisionLabel, decistionOperations, decisionBut);
        decisionBox.setPadding(new Insets(5, 3, 3, 3));

        HBox transformationBox = new HBox();
        transformationBox.getChildren().addAll(transformationLabel, transformationOperations, transformationBut);
        transformationBox.setPadding(new Insets(5, 3, 3, 3));

        VBox mainBox = new VBox();
        mainBox.getChildren().addAll(automataBox, closureBox, decisionBox, transformationBox, statusLabel);
        mainBox.setPadding(new Insets(5, 3, 3, 3));

        this.getChildren().add(mainBox);
    }

    // closure operations

    /**
     * run union operation
     */
    public void union() {
        // automata from list and main
        Union union = new Union();
        main.newAutomaton(union.Union(main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex()), main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex())), automatonTextField.getText());
    }

    /**
     * run subtraction operation
     */
    public void subtraction() {
        if (main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class) && main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {
            // automata from list and main
            Subtraction subtraction = new Subtraction();
            main.newAutomaton(subtraction.Subtract((LinearAcceptingNestedWordAutomaton) main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex()), (LinearAcceptingNestedWordAutomaton) main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex())), automatonTextField.getText());
        } else {
            this.errorAutomatatype();
        }
    }


    /**
     * run intersectioon
     */
    public void intersection() {
        Intersection intersection = new Intersection();
        main.newAutomaton((intersection.Intersection(main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex()), main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex()))), automatonTextField.getText());
    }

    /**
     * run complement
     */
    public void complement() {
        Complement complement = new Complement();

        main.newAutomaton(complement.Complement(main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex())), automatonTextField.getText());
    }


    public void reverse() {
        functionNotYetAvailable();
    }

    public void concat() {
        functionNotYetAvailable();
    }

    public void star() {
        functionNotYetAvailable();
    }


    // decision operations

    /**
     * information how to use membership
     */
    public void membership() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hinweis");

        alert.setContentText("Auf der ersten Seite kann getestet werden, ob ein Wort von einem Automaten gelesen werden kann.");

        alert.showAndWait();
    }

    /**
     * run emptyness operation
     */
    public void emptyness() {
        if (main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {

            LinearAcceptingNestedWordAutomaton lanwa1 = (LinearAcceptingNestedWordAutomaton) main.saveAutomataList.get(this.automata1ComboBox.getSelectionModel().getSelectedIndex());
            Emptiness emptiness = new Emptiness();
            if (emptiness.Emptiness(lanwa1))
                this.statusLabel.setText("Leerheit ist gegeben");
            else
                this.statusLabel.setText("Leerheit ist nicht gegeben");
        } else {
            errorAutomatatype();
        }
    }

    /**
     * run equivalence operation
     */
    public void equivalence() {
        if (main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class) && main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {
            LinearAcceptingNestedWordAutomaton lanwa1 = (LinearAcceptingNestedWordAutomaton) main.saveAutomataList.get(this.automata1ComboBox.getSelectionModel().getSelectedIndex());
            LinearAcceptingNestedWordAutomaton lanwa2 = (LinearAcceptingNestedWordAutomaton) main.saveAutomataList.get(this.automata2ComboBox.getSelectionModel().getSelectedIndex());
            Equivalence equivalence = new Equivalence();
            if (equivalence.Equivalence(lanwa1, lanwa2))
                this.statusLabel.setText("Äquivalenz ist gegeben");
            else
                this.statusLabel.setText("Äauivalenz ist nicht gegeben");
        } else {
            errorAutomatatype();
        }
    }

    /**
     * Run inklusion operartion
     */
    public void inklusion() {


        if (automata1ComboBox.getSelectionModel().getSelectedIndex() > -1 && automata2ComboBox.getSelectionModel().getSelectedIndex() > -1) {
            if (main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class) && main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex()).getClass().equals(LinearAcceptingNestedWordAutomaton.class)) {

                LinearAcceptingNestedWordAutomaton lanwa1 = (LinearAcceptingNestedWordAutomaton) main.saveAutomataList.get(this.automata1ComboBox.getSelectionModel().getSelectedIndex());
                LinearAcceptingNestedWordAutomaton lanwa2 = (LinearAcceptingNestedWordAutomaton) main.saveAutomataList.get(this.automata2ComboBox.getSelectionModel().getSelectedIndex());
                Inclusion inklusion = new Inclusion();
                if (inklusion.Inclusion(lanwa1, lanwa2))
                    this.statusLabel.setText("Inklusion ist gegeben");
                else
                    this.statusLabel.setText("Inklusion ist nicht gegeben");
            } else {
                errorAutomatatype();
            }
        }
    }

    // transformation operations
    public void lanwa2cnwa() {
        functionNotYetAvailable();
    }

    /**
     * transform common nesting-word automata to linear-accepting nested-word automata
     */
    public void cnwa2lanwa() {
        CommonToLinearAcceptingNestedWordAutomaton ctlanwa = new CommonToLinearAcceptingNestedWordAutomaton();

        main.newAutomaton(
                (ctlanwa.transform((CommonNestedWordAutomaton)
                        main.saveAutomataList.get(automata1ComboBox.getSelectionModel().getSelectedIndex()))),
                automatonTextField.getText());

    }

    public void nnwa2lanwa() {
        functionNotYetAvailable();
    }

    public void functionNotYetAvailable() {
        main.infoDialog("Operation noch nicht implementiert", "In dieser Version ist diese Methode nicht implementiert");
    }

    public void errorAutomatatype() {
        main.errorDialog("Fehler Operation", "Bei der Transformation muss ein Linear-akzeptierender Nested-Word-Automat gewählt werden. Erstellen sie ggf. einen neuen Automaten durch Transformation und wenden die Operation auf diesen an.");
    }
}