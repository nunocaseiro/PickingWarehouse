package ipleiria.estg.dei.ei.gui;

import ipleiria.estg.dei.ei.model.Environment;
import ipleiria.estg.dei.ei.model.EnvironmentListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class PanelSimulation extends JPanel implements EnvironmentListener {

    public static final int PANEL_SIZE = 250;
    public static final int CELL_SIZE = 15;
    public static final int GRID_TO_PANEL_GAP = 15;
    private MainFrame mainFrame;
    //private CatchState environment; //TODO uncomment and build set method
    private Image image;
    JPanel environmentPanel = new JPanel();
    final PanelInformation panelInformation = new PanelInformation();
    final JButton buttonSimulate = new JButton("Simulate");

    SwingWorker worker;

    public PanelSimulation(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        environmentPanel.setPreferredSize(new Dimension(PANEL_SIZE,PANEL_SIZE));
        setLayout(new BorderLayout());

        add(environmentPanel,BorderLayout.NORTH);
        add(panelInformation,BorderLayout.CENTER);
        add(buttonSimulate,BorderLayout.SOUTH);
        buttonSimulate.addActionListener(new SimulationPanel_buttonSimulate_actionAdapter(this));

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(1,1,1,1)
        ));
    }

    public JButton getButtonSimulate() {
        return buttonSimulate;
    }

    //    @Override
//    public void environmentUpdated() {
        //TODO
      /*  int n = environment.getSize();
        Graphics g = image.getGraphics();

        //Fill the cells color
        for(int y = 0; y < n; y++){
            for (int x = 0; x < n; x++){
                g.setColor(environment.getCellColor(y,x));
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE , CELL_SIZE, CELL_SIZE);
            }
        }

        //Draw the grid lines
        g.setColor(Color.BLACK);
        for(int i = 0; i <= n; i++){
            g.drawLine(0, i * CELL_SIZE, n * CELL_SIZE, i * CELL_SIZE);
            g.drawLine(i * CELL_SIZE, 0,n * CELL_SIZE, i * CELL_SIZE);
        }

        g = environmentPanel.getGraphics();
        g.drawImage(image, GRID_TO_PANEL_GAP, GRID_TO_PANEL_GAP, null);
        panelInformation.getTextFieldBox().setText(Integer.toString(mainFrame.getAgentSearch().getInitialBox().size()-environment.getNumBox()));
        panelInformation.getTextFieldSteps().setText(Integer.toString(environment.getSteps()));

        try{
            Thread.sleep(100);
        } catch (InterruptedException ignore) {
        }*/
//    }

    public void setJButtonSimulateEnabled(boolean enabled){
        if(worker != null){
            worker.cancel(true);
            worker = null;
            panelInformation.textFieldSteps.setText("0");
            panelInformation.textFieldBox.setText("0");
            environmentPanel.repaint();
        }
        buttonSimulate.setEnabled(enabled);
    }


    public void buttonSimulate_actionPerformed(ActionEvent actionEvent) {
        if(worker != null){
            worker.cancel(true);
            worker = null;
            panelInformation.textFieldSteps.setText("0");
            panelInformation.textFieldBox.setText("0");
            //environment.removeEnvironmentListener(this);
            environmentPanel.repaint();
            return;

        }
        createEnvironment();

        final PanelSimulation simulationPanel = this;

        //TODO
       /* worker = new SwingWorker<Void,Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try{
                    mainFrame.getAgentSearch().setEnvironment(environment);
                    if(mainFrame.getAgentSearch().hasSolution())
                        mainFrame.getAgentSearch().executeSolution();
                    else
                        JOptionPane.showMessageDialog(mainFrame, "There is no solution", "Error!", JOptionPane.ERROR_MESSAGE);
                }catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                environment.removeEnvironmentListener(simulationPanel);
            }
        };

        worker.execute();
        */


    }

    public void createEnvironment() {
        //TODO
//        environment = new State(mainFrame.getState().getMatrix());
        Environment.getInstance().addEnvironmentListener(this);

        buildImage(Environment.getInstance().getMatrix());
        environmentUpdated();
    }

    //TODO
    public void buildImage(int[][] matrix){
        image = new BufferedImage(matrix[0].length * CELL_SIZE +1, matrix.length * CELL_SIZE + 1, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void environmentUpdated() {

        int n = Environment.getInstance().getMatrix().length;
        Graphics g = image.getGraphics();


        //Fill the cells color
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                g.setColor(Environment.getInstance().getCellColor(y, x));
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        //Draw the grid lines
        g.setColor(Color.BLACK);
        for (int i = 0; i <= n; i++) {
            g.drawLine(0, i * CELL_SIZE, n * CELL_SIZE, i * CELL_SIZE);
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, n * CELL_SIZE);
        }

        g = environmentPanel.getGraphics();
        g.drawImage(image, GRID_TO_PANEL_GAP, GRID_TO_PANEL_GAP, null);
//        panelInformation.getTextFieldBox().setText(Integer.toString(mainFrame.getAgentSearch().getInitialBox().size()-environment.getNumBox()));
////        panelInformation.getTextFieldSteps().setText(Integer.toString(environment.getSteps()));

        try {
            Thread.sleep(100);
        } catch (InterruptedException ignore) {
        }
    }
}

class SimulationPanel_buttonSimulate_actionAdapter implements ActionListener{

    final private PanelSimulation adaptee;

    public SimulationPanel_buttonSimulate_actionAdapter(PanelSimulation adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        adaptee.buttonSimulate_actionPerformed(actionEvent);
    }
}

class PanelInformation extends PanelAtributesValue{

    public static final int TEXT_FIELD_LENGHT = 3;
    JTextField textFieldBox = new JTextField("0", TEXT_FIELD_LENGHT);
    JTextField textFieldSteps = new JTextField("0", TEXT_FIELD_LENGHT);

    public PanelInformation(){
        title = "Simulation information";

        labels.add(new JLabel("Objects: "));
        valueComponents.add(textFieldBox);
        labels.add(new JLabel("Steps: "));
        valueComponents.add(textFieldSteps);
        configure();

    }

    public JTextField getTextFieldBox() {
        return textFieldBox;
    }

    public JTextField getTextFieldSteps() {
        return textFieldSteps;
    }
}
