package ipleiria.estg.dei.ei.model;

import ipleiria.estg.dei.ei.model.search.Action;
import ipleiria.estg.dei.ei.model.search.Pair;
import ipleiria.estg.dei.ei.model.search.State;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import ipleiria.estg.dei.ei.utils.Properties;

public class Environment {

    private static Environment INSTANCE = new Environment();
    private int[][] matrix;
    private List<Action> actions;
    private List<State> picks;
    private List<Pair> pairs;
    private List<State> agents;
    private State offloadArea;

    private Environment() {
        this.actions = new LinkedList<>();
        this.actions.add(new Action(1, -1, 0)); // move up
        this.actions.add(new Action(1, 1, 0)); // move down
        this.actions.add(new Action(1, 0, -1)); // move left
        this.actions.add(new Action(1, 0, 1)); // move right

        this.pairs = new LinkedList<>();
        this.agents = new LinkedList<>();
    }

    public static Environment getInstance() {
        return INSTANCE;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public void readInitialStateFromFile(File file) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(file);

        int lines = scanner.nextInt();
        scanner.nextLine();
        System.out.println(lines);
        int columns = scanner.nextInt();
        scanner.nextLine();
        System.out.println(columns);

        int[][] matrix = new int[lines][columns];
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = scanner.nextInt();
                if (matrix[i][j] == Properties.agent) {
                    agents.add(new State(i,j));
                }
                if (matrix[i][j] == Properties.offloadArea) {
                    offloadArea = new State(i,j);
                }
            }
            scanner.nextLine();
        }

        this.matrix = matrix;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<State> getPicks() {
        return picks;
    }

    public void setPicks(List<State> picks) {
        this.picks = picks;
        //TODO create pairs list
    }
}
