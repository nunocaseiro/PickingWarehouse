package ipleiria.estg.dei.ei.model.geneticAlgorithm.geneticOperators;

import ipleiria.estg.dei.ei.model.geneticAlgorithm.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RecombinationCX extends Recombination {

    public RecombinationCX(double probability) { super(probability); }

    @Override
    public void recombine(Individual ind1, Individual ind2) {
        List<Integer> cycle1 = new Vector<Integer>();
        List<Integer> cycle2 = new ArrayList<Integer>();
/*
        int[] genome1= new int[]{1,2,3,4,5,6,7,8};
        int[] genome2= new int[]{2,4,6,8,7,5,3,1};
        ind1.setGenome(genome1);
        ind2.setGenome(genome2);
*/

        int gene1P1 = 0;
        int gene1P2 = 0;
        int start= 0;

        //cycle 1
        gene1P1= ind1.getGene(start);
        gene1P2= ind2.getGene(start);

        int gene2P1= ind1.getGene(ind1.getIndexOf(gene1P2));
        cycle1.add(gene2P1);

        while(gene1P1!=gene2P1){
            gene1P2 = ind2.getGene(ind1.getIndexOf(gene2P1));
            gene2P1 = ind1.getGene(ind1.getIndexOf(gene1P2));
            cycle1.add(gene2P1);
        }

        //cycle 2
        start++;
        gene1P1= ind1.getGene(start);
        gene1P2= ind2.getGene(start);
        while(cycle1.contains(gene1P1)){
            start++;
            if(start>=ind1.getNumGenes()){
                break;
            }
            gene1P1= ind1.getGene(start);
            gene1P2= ind2.getGene(start);
        }

        gene2P1= ind1.getGene(ind1.getIndexOf(gene1P2));
        cycle2.add(gene2P1);

        while(gene1P1!=gene2P1){
            gene1P2 = ind2.getGene(ind1.getIndexOf(gene2P1));
            gene2P1 = ind1.getGene(ind1.getIndexOf(gene1P2));
            cycle2.add(gene2P1);
        }

        //Add cycle 1
        List<Integer> genomeOfChild1 = new Vector<>(ind1.getNumGenes());
        for (int i = 0; i < ind1.getNumGenes(); i++) {
            genomeOfChild1.add(i,0);
        }

        cycle(ind1,ind2,genomeOfChild1,cycle1);

        //Add cycle 2
        List<Integer> genomeOfChild2 = new Vector<>(ind1.getNumGenes());
        for (int i = 0; i < ind1.getNumGenes(); i++) {
            genomeOfChild2.add(i,0);
        }

        cycle(ind1,ind2,genomeOfChild2,cycle2);
        replaceParentsByChild(ind1,genomeOfChild1);
        replaceParentsByChild(ind2,genomeOfChild2);

        /*System.out.println("--------");
        System.out.println(ind1.toString());
        System.out.println(ind2.toString());
        System.out.println("--1-");
        System.out.println(cycle1.toString());
        System.out.println("--2-");
        System.out.println(cycle2.toString());
        System.out.println(genomeOfChild1.toString());
        System.out.println(genomeOfChild2.toString());
        System.out.println("--------");
         */

        cycle1.clear();
        cycle2.clear();
        genomeOfChild1.clear();
        genomeOfChild2.clear();
    }

    private void cycle(Individual ind1,Individual ind2, List<Integer> genomeOfChild,List<Integer> cycle1 ){
        for (int i = 0; i < cycle1.size(); i++) {
            int valueOfCycle=cycle1.get(i);
            int indexOfValueInParent1 = ind1.getIndexOf(valueOfCycle);
            genomeOfChild.remove(indexOfValueInParent1);
            genomeOfChild.add(indexOfValueInParent1,valueOfCycle);
        }

        for (int i = 0; i < ind2.getGenome().length; i++) {
            if(!genomeOfChild.contains(ind2.getGene(i))){
                genomeOfChild.remove(i);
                genomeOfChild.add(i,ind2.getGene(i));
            }
        }
    }

    private void replaceParentsByChild(Individual ind, List<Integer> genomeOfChild){
        for (int i = 0; i < ind.getNumGenes(); i++) {
            ind.setGene(i,genomeOfChild.get(i));
        }

    }

    @Override
    public String toString() {
        return "CX";
    }
}
