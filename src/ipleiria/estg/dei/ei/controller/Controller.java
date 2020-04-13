package ipleiria.estg.dei.ei.controller;

import ipleiria.estg.dei.ei.gui.MainFrame;
import ipleiria.estg.dei.ei.model.Environment;
import ipleiria.estg.dei.ei.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.model.geneticAlgorithm.Individual;
import ipleiria.estg.dei.ei.model.search.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Random;

public class Controller {

    private MainFrame view;
    private SwingWorker<Void, Void> worker;

    public Controller(MainFrame view) {
        this.view = view;
    }


    public void initController() {
        view.getMenuBarHorizontal().getMenuItemImportLayout().addActionListener(e -> loadWarehouseLayout());
        view.getMenuBarHorizontal().getMenuItemImportPicks().addActionListener(e -> loadPicks());
        view.getMenuBarHorizontal().getExit().addActionListener(e->closeApplication());

        view.getMenuBarHorizontal().getMenuItemRunSearch().addActionListener(e -> search());
        view.getMenuBarHorizontal().getMenuItemRunGA().addActionListener(e -> runGA());
        view.getMenuBarHorizontal().getMenuItemStopGA().addActionListener(e -> stop());
        view.getMenuBarHorizontal().getMenuItemRunSimulate().addActionListener(e -> simulate());

        view.getToolBarHorizontal().getLoadLayout().addActionListener(e-> loadWarehouseLayout());
        view.getToolBarHorizontal().getLoadPicks().addActionListener(e-> loadPicks());
        view.getToolBarHorizontal().getaStarRun().addActionListener(e-> search());
        view.getToolBarHorizontal().getGaRun().addActionListener(e-> runGA());
        view.getToolBarHorizontal().getStopGaRun().addActionListener(e-> stop());
        view.getToolBarHorizontal().getSimulateRun().addActionListener(e-> simulate());

        view.getMenuBarHorizontal().getMenuItemSearchPanel().addActionListener(e->showProblemData());
        view.getMenuBarHorizontal().getMenuItemGAPanel().addActionListener(e->showGaPanel());
        view.getMenuBarHorizontal().getMenuItemSimulationPanel().addActionListener(e->showSimulatePanel());

        view.getToolBarVertical().getProblemData().addActionListener(e->showProblemData());
        view.getToolBarVertical().getGa().addActionListener(e->showGaPanel());
        view.getToolBarVertical().getSimulate().addActionListener(e->showSimulatePanel());
        view.getToolBarVertical().getGaHistory().addActionListener(e->showGaHistory());

        view.getMenuBarHorizontal().getMenuItemWelcome().addActionListener(e->showMainPage());

    }

    private void showGaHistory() {
        view.showPanel(4);
    }

    private void showSimulatePanel() {
    view.showPanel(3);
    }

    private void showGaPanel() {
        view.showPanel(2);
    }

    private void showProblemData() {
        view.showPanel(1);
    }

    private void showMainPage() {
        view.showPanel(0);
    }

    private void simulate() {
        worker = new SwingWorker<>() {
            @Override
            public Void doInBackground() {
                try {
                    view.manageButtons(false,false,false,false,false,false);
                    Environment.getInstance().executeSolution();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                return null;
            }

            @Override
            public void done() {
                view.manageButtons(true,false,false,true,false,true);

            }
        };
        worker.execute();
    }

    private void stop() {
        worker.cancel(true);
    }

    private void runGA() {

        view.setBestIndividualPanelText("");
        view.getGaPanel().getSeriesBestIndividual().clear();
        view.getGaPanel().getSeriesAverage().clear();

        view.manageButtons(true,false,true,true,true,false);
        Random random = new Random(Integer.parseInt(view.getGaPanel().getPanelParameters().getTextFieldSeed().getText()));

        GeneticAlgorithm ga = new GeneticAlgorithm(
                Integer.parseInt(view.getGaPanel().getPanelParameters().getTextFieldN().getText()),
                Integer.parseInt(view.getGaPanel().getPanelParameters().getTextFieldGenerations().getText()),
                view.getGaPanel().getPanelParameters().getSelectionMethod(),
                view.getGaPanel().getPanelParameters().getRecombinationMethod(),
                view.getGaPanel().getPanelParameters().getMutationMethod(),
                Environment.getInstance().getNumberOfAgents(),
                Environment.getInstance().getNumberOfPicks(), random);

        ga.addGAListener(view);

        worker = new SwingWorker<>() {
            @Override
            public Void doInBackground() {
                try {
                    Individual bestInRun = ga.run();
                    Environment.getInstance().setBestInRun(bestInRun);
                    view.getGaHistoryPanel().appendParametersOfRun(bestInRun,view.getGaPanel().getPanelParameters());
                    System.out.println(Environment.getInstance().getPicks());
                    System.out.println(bestInRun);

                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                return null;
            }

            @Override
            public void done() {
                view.manageButtons(true,false,false,true,false,true);

            }
        };
        worker.execute();
    }

    private void search() {

            worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    try {
                        AStar aStar = new AStar();
                        for (Pair pair : Environment.getInstance().getPairs()) {
                            List<Node> pathNodes = aStar.search(Environment.getInstance().getNode(pair.getNode1()), Environment.getInstance().getNode(pair.getNode2()));
                            pair.setValue(pathNodes.get(pathNodes.size() - 1).getG());
                            pair.setPath(pathNodes);
                        }

                        Environment.getInstance().setPairsMap();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void done() {
                    List<Pair> pairs = Environment.getInstance().getPairs();
                    view.getProblemData().getModel().setRowCount(0);

                    for (int i = 0; i < Environment.getInstance().getPairs().size()-3; i++) {
                        view.getProblemData().addRow(pairs.get(i).toString(),pairs.get(i+1).toString(), pairs.get(i+2).toString(),pairs.get(i+3).toString());
                    }
                    view.manageButtons(true,false,true,true,false,false);
                }
            };
            worker.execute();
        }

    private void loadWarehouseLayout() {
        JFileChooser fc = new JFileChooser(new File("./src/ipleiria/estg/dei/ei/dataSets"));
        int returnVal = fc.showOpenDialog(view);
        Environment.getInstance().addEnvironmentListener(view.getSimulationPanel());

        try {
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File dataSet = fc.getSelectedFile();
                Environment.getInstance().readInitialStateFromFile(dataSet);
                view.manageButtons(true,true,false,false,false,false);
            }

        } catch (IOException e1) {
            e1.printStackTrace(System.err);
        } catch (java.util.NoSuchElementException e2) {
            JOptionPane.showMessageDialog(view, "Invalid file format", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPicks(){
        JFileChooser fc = new JFileChooser(new File("./src/ipleiria/estg/dei/ei/dataSets"));
        int returnVal = fc.showOpenDialog(view);
        try {

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File dataSetPick = fc.getSelectedFile();
                Environment.getInstance().loadPicksFromFile(dataSetPick);
                view.manageButtons(true,true,true,false,false,false);
            }

        }catch (IOException e1) {
            e1.printStackTrace(System.err);
        } catch (java.util.NoSuchElementException e2) {
            JOptionPane.showMessageDialog(view, "Invalid file format", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeApplication(){
        System.exit(0);
    }
}


