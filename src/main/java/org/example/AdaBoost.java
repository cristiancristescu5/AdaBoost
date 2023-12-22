package org.example;

import java.util.*;

public class AdaBoost {
    private static double eps1 = 0, eps2 = 0;
    private static double epsilon1 = Double.MAX_VALUE, epsilon2 = Double.MAX_VALUE;
    private static double epsilon = 0.5;
    private static double split1 = 0, split2 = 0, split = 0;
    private static boolean flag = false;

    public static List<Split> run(int T, List<Double> splitsX1, List<Double> splitsX2, List<Instance> instances) {
        int num = instances.size();

        List<Split> decisions = new ArrayList<>();

        instances.forEach(instance -> {
            instance.setDistribution(1.0 / num);
        });

        for (int i = 0; i < T; i++) {
            System.out.println("Iteration: " + i);
            epsilon = 0.500000;
            splitsX1.forEach(s -> {
                instances.forEach(j -> {
                    if ((j.getX1() < s && j.getY() != 1)) {
                        eps1 += j.getDistribution();
                    } else if (j.getX1() > s && j.getY() != -1) {
                        eps1 += j.getDistribution();
                    }
                    if ((j.getX1() >= s && j.getY() != 1)) {
                        eps2 += j.getDistribution();
                    } else if (j.getX1() < s && j.getY() != -1) {
                        eps2 += j.getDistribution();
                    }
                });
                if (eps1 < epsilon1) {
                    epsilon1 = eps1;
                    split1 = s;
                }
                if (eps2 < epsilon1) {
                    epsilon1 = eps2;
                    split1 = -s;
                }
                eps1 = 0;
                eps2 = 0;
            });
            eps1 = 0;
            eps2 = 0;
            splitsX2.forEach(s -> {
                instances.forEach(j -> {
                    if ((j.getX2() < s && j.getY() != 1) || (j.getX2() > s && j.getY() != -1)) {
                        eps1 += j.getDistribution();
                    }
                    if ((j.getX2() >= s && j.getY() != 1) || (j.getX2() < s && j.getY() != -1)) {
                        eps2 += j.getDistribution();
                    }
                });

                if (eps1 < epsilon2) {
                    epsilon2 = eps1;
                    split2 = s;
                }
                if (eps2 < epsilon2) {
                    epsilon2 = eps2;
                    split2 = -s;
                }
                eps1 = 0;
                eps2 = 0;
            });
            if (epsilon1 < epsilon2) {
                epsilon = epsilon1;
                split = split1;
                flag = true;

            } else {
                epsilon = epsilon2;
                split = split2;
                flag = false;
            }
            if ((float) epsilon == (float)0.5 || epsilon == 0){
                return decisions;
            }
            double alfa = 0.5 * Math.log((1 - epsilon) / epsilon);
            Split s = new Split(split, alfa, flag, !flag);
            decisions.add(s);
            updateDistribution(s, instances, epsilon);

            System.out.println("Alfa: "+alfa);
            System.out.println("Epsilon: "+ epsilon);

            eps1 = 0;
            eps2 = 0;
            split1 = 0;
            split2 = 0;
            epsilon1 = Double.MAX_VALUE;
            epsilon2 = Double.MAX_VALUE;

        }
        return decisions;
    }

    private static void updateDistribution(Split split, List<Instance> instances, double epsilon) {
        boolean add = (split.h() < 0);
        for (Instance i : instances) {
            if (split.isX1()) {
                if (add) {
                    if ((i.getX1() + split.h() > 0 && i.getY() == 1) || (i.getX1() + split.h() < 0 && i.getY() == -1)) {
                        i.setDistribution(i.getDistribution() / (2 * (1 - epsilon)));//correct classification
                    } else {
                        i.setDistribution(i.getDistribution() / (2 * epsilon));//wrong classification
                    }
                }
                if (!add) {
                    if ((split.h() - i.getX1() > 0 && i.getY() == 1) || (split.h() - i.getX1() < 0 && i.getY() == -1)) {
                        i.setDistribution(i.getDistribution() / (2 * (1 - epsilon)));
                    } else {
                        i.setDistribution(i.getDistribution() / (2 * epsilon));
                    }
                }
            }
            if (split.isX2()) {
                if (add) {
                    if ((i.getX2() + split.h() > 0 && i.getY() == 1) || (i.getX2() + split.h() < 0 && i.getY() == -1)) {
                        i.setDistribution(i.getDistribution() / (2 * (1 - epsilon)));//correct classification
                    } else {
                        i.setDistribution(i.getDistribution() / (2 * epsilon));//wrong classification
                    }
                }
                if (!add) {
                    if ((split.h() - i.getX2() > 0 && i.getY() == 1) || (split.h() - i.getX2() < 0 && i.getY() == -1)) {
                        i.setDistribution(i.getDistribution() / (2 * (1 - epsilon)));
                    } else {
                        i.setDistribution(i.getDistribution() / (2 * epsilon));
                    }
                }
            }
        }
    }
}
