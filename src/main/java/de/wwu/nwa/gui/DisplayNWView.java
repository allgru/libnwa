package de.wwu.nwa.gui;

import de.wwu.nwa.automata.CommonNestedWordAutomaton;
import de.wwu.nwa.automata.LinearAcceptingNestedWordAutomaton;
import de.wwu.nwa.automata.NondeterministicNestedWordAutomaton;
import de.wwu.nwa.automata.items.Run;
import de.wwu.nwa.automata.items.exceptions.NestedEdgeSharePositionException;
import de.wwu.nwa.automata.items.exceptions.NestedEdgesCrossedException;
import de.wwu.nwa.automata.items.node.CallNode;
import de.wwu.nwa.automata.items.node.Node;
import de.wwu.nwa.automata.items.node.ReturnNode;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.Stack;

/**
 * Display that generates with the information of a run on a Nested-word a graphical representation
 *
 * @author Allan Grunert
 */
public class DisplayNWView extends ScrollPane {
    private final int RADIUS = 30;
    private final int SPACE = (int) (5f * RADIUS);
    private final int YAXIS = 1000;
    Stack<Integer> HierarchyStack = new Stack<Integer>();
    private int hierarchy;
    private int step = 0;
    private int internal;
    private Pane pane;
    private Run run;

    private CommonNestedWordAutomaton dnwa = null;
    private NondeterministicNestedWordAutomaton nnwa = null;
    private LinearAcceptingNestedWordAutomaton lnwa = null;
    private Main main;

    public DisplayNWView(Main main) {
        this.main = main;
        pane = new Pane();
        this.setContent(pane);
        this.setPrefSize(1000, 400);
        this.setVvalue(1.0);

    }

    /**
     * With each call Display one Step
     *
     * @return number of step
     */
    public int displayStep() {
        // if Node = hierarchical then connect
        step++;

        switch (run.getStep(step).getType()) // .getNodeAt(step-1).getType())
        {
            case Node.InternalNode:
                this.drawCircle("a");
                break;

            case Node.CallNode:
                this.HierarchyStack.push(step);
                break;

            case Node.ReturnNode:
                this.connectHierarchicalEdge(this.HierarchyStack.pop(), step, 10, "h");
                break;
        }

        return step;
    }

    /**
     * method for going step by step through the runs iteration
     *
     * @param duration time between the steps
     */
    public void displayAllSteps(int duration) {
        while (displayStep() != run.length()) {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * draw hierarchical edge
     *
     * @param pos1    start position
     * @param pos2    end position
     * @param radiusY radius of (edge) arc
     * @param text    label of edge
     */
    public void connectHierarchicalEdge(float pos1, float pos2, float radiusY, String text) {
        // if (text.equals("p0")) return;
        // System.out.println("h/" + text +  ": p1" + pos1 + " p2" + pos2 + " p12-p1" + (pos2-pos1) + " cx" + (pos1 + ((pos2-pos1)/2)));
        Arc arc = new Arc();
        arc.setStroke(Color.BLACK);
        arc.setFill(Color.TRANSPARENT);
        arc.setCenterX(pos1 + ((pos2 - pos1) / 2));
        arc.setCenterY(YAXIS - this.RADIUS);
        arc.setRadiusX((pos2 - pos1) / 2);
        arc.setRadiusY(radiusY);
        arc.setStartAngle(0.0f);
        arc.setLength(180);
        arc.setType(ArcType.OPEN);
        arc.getStrokeDashArray().addAll(2d);
        // System.out.println("arc" + arc.getRadiusX() + " " + arc.getCenterX() + " " +arc.getRadiusY());

        this.pane.getChildren().addAll(arc, new Text(pos1 + ((pos2 - pos1) / 2) - (text.length() * 5), YAXIS - this.RADIUS - radiusY - 5, text));
    }

    /**
     * Draw line connecting (linear state)
     *
     * @param pos1 start position
     * @param pos2 end position
     * @param text label of edge
     */
    public void connectNormalEdge(float pos1, float pos2, String text) {
        Line line = new Line();
        line.setStartX(pos1 + this.RADIUS);
        line.setStartY(YAXIS);
        line.setEndX(pos2 - this.RADIUS);
        line.setEndY(YAXIS);

        this.pane.getChildren().addAll(line, new Text(pos1 + (SPACE / 2) - text.length() * 5, YAXIS - 5, text));
    }

    /**
     * Draw circle with text for nodes containing the input symbols
     *
     * @param text input symbol
     */
    public void drawCircle(String text) {
        Circle circle = new Circle(this.RADIUS + (this.step * this.SPACE), this.YAXIS, this.RADIUS);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.TRANSPARENT);
        this.pane.getChildren().addAll(circle, new Text(this.RADIUS + (this.step * this.SPACE) - text.length() * 4 + 5, this.YAXIS, text));
//+ (this.RADIUS+(this.step*this.SPACE))

    }

    public int getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(int hierarchy) {
        this.hierarchy = hierarchy;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getInternal() {
        return internal;
    }

    public void setInternal(int internal) {
        this.internal = internal;
    }


    public void setAutomata(CommonNestedWordAutomaton dnwa) {
        this.dnwa = dnwa;
    }

    public void setAutomata(NondeterministicNestedWordAutomaton nnwa) {
        this.nnwa = nnwa;
    }

    public void setAutomata(LinearAcceptingNestedWordAutomaton lnwa) {
        this.lnwa = lnwa;
    }

    /**
     * Mehtod for drawing run of automata
     *
     * @param run run which should be drawn
     */
    public void testNWA(Run run) {
        this.pane.getChildren().clear();
        this.step = 0;
        run.getNw().print();

        try {
            run.getInputWordAccepted();
        } catch (NestedEdgeSharePositionException e) {
            e.printStackTrace();
        } catch (NestedEdgesCrossedException e) {
            e.printStackTrace();
        }

        // draw linear states
        for (int i = 1; i <= run.size(); i++) {
            Node node = run.getStep(i);

            System.out.println((i) + "." + node.getState().getStateName() + ": " + node.getSymbol());
            connectNormalEdge(this.RADIUS + ((this.step) * this.SPACE), this.RADIUS + ((this.step + 1) * this.SPACE), node.getState().getStateName());
            if (i > 1) drawCircle(node.getSymbol());
            this.step++;
        }

        float nestedEdgesCount = run.getNw().getNestedEdges().size();
        float ystep = this.YAXIS / (nestedEdgesCount / 1.5f);
        ystep = 50.0f;
        int distance = 0;
        float stackCnt = run.size();
        float ges = ((float) ((SPACE + 100) / stackCnt));
        float radiusY = 1.0f * ges;
        float pos1 = 0;
        String hierarchyStateName = "";

        // draw matched edges + pending calls
        for (int i = 1; i <= run.size(); i++) {
            Node node = run.getStep(i);
            pos1 = 0;
            String pending = "";

            if (node.getType() == Node.CallNode) {
                hierarchyStateName = ((CallNode) node).getHierarchyState().getStateName();

                ReturnNode rn = ((CallNode) node).getReturnNode();

                pos1 = (node.getPosition()) * this.SPACE + this.RADIUS;
                if (rn == null) {
                    // pending call
                    pending = " (pending)";
                    distance = run.size() - node.getPosition();
                } else {
                    distance = rn.getPosition() - node.getPosition();
                }
            }

            if (pos1 != 0) {
                radiusY = distance * ystep;
                connectHierarchicalEdge(pos1, pos1 + distance * this.SPACE, radiusY, hierarchyStateName + pending);
            }
        }

        // draw pending returns
        for (int i = 1; i <= run.size(); i++) {
            Node node = run.getStep(i);
            if (node.getType() == Node.ReturnNode) {
                ReturnNode rn = ((ReturnNode) node);
                hierarchyStateName = rn.getHierarchyState().getStateName();

                if (rn.isPending()) {
                    // pending call
                    distance = i - 1;
                    radiusY = distance * ystep;
                    connectHierarchicalEdge(0, distance * this.SPACE + this.RADIUS, radiusY, hierarchyStateName + " (pending)");
                }
            }
        }
    }
}
