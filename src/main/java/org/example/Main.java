package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        List<Instance> instances = new ArrayList<>();
        for(int i = 2 ; i <= 5 ; i++){
            instances.add(new Instance(i, 1, 1));
        }
        instances.add(new Instance(1,1,-1));
        instances.add(new Instance(1,2,1));
        instances.add(new Instance(1,3,1));
        for(int i = 2 ; i <= 5 ; i++){
            instances.add(new Instance(i, 2, -1));
        }

        instances.add(new Instance(2,3,-1));

        List<Double> splitsX1 = Arrays.asList(0.5, 1.5, 2.5, 3.5, 4.5);
        List<Double> splitsX2 = Arrays.asList(0.5, 1.5, 2.5);

        List<Split> decisions =  AdaBoost.run(1000, splitsX1, splitsX2, instances);
        int error = 0;
        List<Instance> wrongInstances = new ArrayList<>();
        for(var i : instances){
            double sum=0;
            for(var d : decisions){
                boolean add = d.h()<0;
                if(d.isX1()){
                    if(add){
                       sum+= d.alfa()*getSign(i.getX1() + d.h());
                    }else{
                        sum+=d.alfa()*getSign(d.h()-i.getX1());
                    }
                }
                if(d.isX2()){
                    if(add){
                        sum+= d.alfa()*getSign(i.getX2() + d.h());
                    }else{
                        sum+=d.alfa()*getSign(d.h()-i.getX2());
                    }
                }
            }
            if(getSign(sum) != i.getY()){
                error +=1;
                wrongInstances.add(i);
            }
        }
        System.out.println();
        System.out.println("Error: " + error);
        System.out.println("Instances:");
        for(var i : wrongInstances){
            System.out.println("Instance: (" + i.getX1() + "," + i.getX2() + "," + i.getY() + ")");
        }
    }

    private static int getSign(double x){
        return x < 0 ? -1 : 1;
    }
}