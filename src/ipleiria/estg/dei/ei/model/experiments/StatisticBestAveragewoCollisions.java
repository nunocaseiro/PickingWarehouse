package ipleiria.estg.dei.ei.model.experiments;

import ipleiria.estg.dei.ei.model.Environment;
import ipleiria.estg.dei.ei.model.geneticAlgorithm.GAListener;
import ipleiria.estg.dei.ei.model.geneticAlgorithm.GeneticAlgorithm;
import ipleiria.estg.dei.ei.utils.FileOperations;
import ipleiria.estg.dei.ei.utils.Maths;

import java.io.File;
import java.util.Arrays;

public class StatisticBestAveragewoCollisions implements GAListener {

    private final double[] values;
    private int run;
    private final double[] allRunsCollisions;
    private GeneticAlgorithm geneticAlgorithm;

    public StatisticBestAveragewoCollisions(int numRuns, String experimentHeader) {
        run=0;
        values = new double[numRuns];
        allRunsCollisions = new double[numRuns];
        File file = new File("statistic_average_fitness_woCollisions.xls");
        if(!file.exists()){
            FileOperations.appendToTextFile("statistic_average_fitness_woCollisions.xls", experimentHeader + "\t" + "Average:" + "\t" + "StdDev:" + "\t" + "Collisions average" + "\t" + "Collisions stdDev" + "\r\n");
        }
    }


    @Override
    public void generationEnded(GeneticAlgorithm e) {

    }

    @Override
    public void runEnded(GeneticAlgorithm geneticAlgorithm) {
        this.geneticAlgorithm= geneticAlgorithm;
        values[run]=geneticAlgorithm.getBestInRun().getFitnesswofitness();
        allRunsCollisions[run++]=geneticAlgorithm.getBestInRun().getNumberOfCollisions();
    }


    @Override
    public void experimentEnded() {
        double average = Maths.average(values);
        double stdDeviation= Maths.standardDeviation(values,average);
        double collisionsAverage = Maths.average(allRunsCollisions);
        double collisionStdDeviation = Maths.standardDeviation(allRunsCollisions,collisionsAverage);

        FileOperations.appendToTextFile("statistic_average_fitness_woCollisions.xls", buildExperimentValues() + "\t" + average + "\t" + stdDeviation + "\t" + collisionsAverage + "\t" + collisionStdDeviation + "\r\n");
        Arrays.fill(values,0);
        Arrays.fill(allRunsCollisions,0);
        this.run=0;
    }

    private String buildExperimentValues() {
        StringBuilder sb = new StringBuilder();
        sb.append(geneticAlgorithm.getPopSize() + "\t");
        sb.append(geneticAlgorithm.getMaxGenerations() + "\t");
        sb.append(geneticAlgorithm.getSelection() + "\t");
        sb.append(geneticAlgorithm.getRecombination() + "\t");
        sb.append(geneticAlgorithm.getRecombination().getProbability() + "\t");
        sb.append(geneticAlgorithm.getMutation() + "\t");
        sb.append(geneticAlgorithm.getMutation().getProbability() + "\t");
        sb.append(Environment.getInstance().getTimeWeight() + "\t");
        sb.append(Environment.getInstance().getCollisionsWeight() + "\t");
        return sb.toString();
    }


}
