package Minimize;

import java.util.Random;

public class Minimize {
    static final int MAX_ITR = 100;
    static final double LOWER_BOUND = -50.0;
    static final double UPPER_BOUND = 50.0;
    static final int POPULATION_SIZE = 30;
    static final int SENSOR_COUNT = 6;

    static Random rnd = new Random();

    // Tek bir sensörü temsil eden Wolf sınıfı
    static class Wolf {
        double x, y;

        public Wolf() {
            this.x = LOWER_BOUND + (UPPER_BOUND - LOWER_BOUND) * rnd.nextDouble();
            this.y = LOWER_BOUND + (UPPER_BOUND - LOWER_BOUND) * rnd.nextDouble();
        }

        public Wolf(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Wolf(Wolf other) {
            this.x = other.x;
            this.y = other.y;
        }

        // Sınır kontrolü
        public void checkBounds() {
            if (x < LOWER_BOUND) x = LOWER_BOUND;
            if (x > UPPER_BOUND) x = UPPER_BOUND;
            if (y < LOWER_BOUND) y = LOWER_BOUND;
            if (y > UPPER_BOUND) y = UPPER_BOUND;
        }

        // İki wolf arasındaki mesafe
        public double distanceTo(Wolf other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public String toString() {
            return String.format("(%.2f, %.2f)", x, y);
        }
    }

    // 6 sensörden oluşan bir çözümü temsil eden sınıf
    static class Solution {
        Wolf[] sensors;
        double fitness;

        public Solution() {
            sensors = new Wolf[SENSOR_COUNT];
            for (int i = 0; i < SENSOR_COUNT; i++) {
                sensors[i] = new Wolf();
            }
            calculateFitness();
        }

        public Solution(Solution other) {
            sensors = new Wolf[SENSOR_COUNT];
            for (int i = 0; i < SENSOR_COUNT; i++) {
                sensors[i] = new Wolf(other.sensors[i]);
            }
            this.fitness = other.fitness;
        }

        // Fitness hesaplama: Tüm sensör çiftleri arasındaki ters-kare mesafelerin toplamı
        public void calculateFitness() {
            double sum = 0.0;
            for (int i = 0; i < SENSOR_COUNT - 1; i++) {
                for (int j = i + 1; j < SENSOR_COUNT; j++) {
                    double distance = sensors[i].distanceTo(sensors[j]);
                    // Sıfıra bölme hatası önlemi
                    if (distance < 0.001) {
                        distance = 0.001;
                    }
                    sum += 1.0 / (distance * distance);
                }
            }
            this.fitness = sum;
        }

        // Sınır kontrolü
        public void checkBounds() {
            for (Wolf sensor : sensors) {
                sensor.checkBounds();
            }
        }

        // Ortalama mesafe hesaplama
        public double getAverageDistance() {
            double totalDistance = 0;
            int pairCount = 0;
            for (int i = 0; i < SENSOR_COUNT - 1; i++) {
                for (int j = i + 1; j < SENSOR_COUNT; j++) {
                    totalDistance += sensors[i].distanceTo(sensors[j]);
                    pairCount++;
                }
            }
            return totalDistance / pairCount;
        }

        // Sonuçları yazdırma
        public void printResults() {
            System.out.printf("En düşük parazit seviyesi: %.6f\n", fitness);
            System.out.println("\nOptimal sensör konumları:");

            for (int i = 0; i < SENSOR_COUNT; i++) {
                System.out.printf("Sensör %d: %s\n", i + 1, sensors[i]);
            }

            System.out.println("\nSensörler arası mesafeler:");
            for (int i = 0; i < SENSOR_COUNT - 1; i++) {
                for (int j = i + 1; j < SENSOR_COUNT; j++) {
                    double distance = sensors[i].distanceTo(sensors[j]);
                    System.out.printf("Sensör %d - Sensör %d: %.2f birim\n",
                            i + 1, j + 1, distance);
                }
            }

            System.out.printf("\nOrtalama sensör mesafesi: %.2f birim\n", getAverageDistance());
        }
    }

    public static void main(String[] args) {
        // Popülasyon oluştur
        Solution[] population = new Solution[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = new Solution();
        }

        // İlk alfa, beta, delta seçimi
        Solution alpha = new Solution(population[0]);
        Solution beta = new Solution(population[1]);
        Solution delta = new Solution(population[2]);

        // En iyi 3'ü bul
        for (Solution solution : population) {
            if (solution.fitness < alpha.fitness) {
                delta = new Solution(beta);
                beta = new Solution(alpha);
                alpha = new Solution(solution);
            } else if (solution.fitness < beta.fitness) {
                delta = new Solution(beta);
                beta = new Solution(solution);
            } else if (solution.fitness < delta.fitness) {
                delta = new Solution(solution);
            }
        }


        System.out.printf("Başlangıç fitness: %.6f\n", alpha.fitness);
        System.out.println();

        // GWO ana döngüsü
        for (int iteration = 0; iteration < MAX_ITR; iteration++) {
            double a = 2.0 - (double) iteration * (2.0 / MAX_ITR);

            for (Solution solution : population) {
                for (int sensorIndex = 0; sensorIndex < SENSOR_COUNT; sensorIndex++) {
                    Wolf currentSensor = solution.sensors[sensorIndex];

                    // X koordinatı için GWO güncellemesi
                    double r1 = rnd.nextDouble();
                    double r2 = rnd.nextDouble();
                    double A1 = 2 * a * r1 - a;
                    double C1 = 2 * r2;
                    double dAlpha_x = Math.abs(C1 * alpha.sensors[sensorIndex].x - currentSensor.x);
                    double X1_x = alpha.sensors[sensorIndex].x - A1 * dAlpha_x;

                    r1 = rnd.nextDouble();
                    r2 = rnd.nextDouble();
                    double A2 = 2 * a * r1 - a;
                    double C2 = 2 * r2;
                    double dBeta_x = Math.abs(C2 * beta.sensors[sensorIndex].x - currentSensor.x);
                    double X2_x = beta.sensors[sensorIndex].x - A2 * dBeta_x;

                    r1 = rnd.nextDouble();
                    r2 = rnd.nextDouble();
                    double A3 = 2 * a * r1 - a;
                    double C3 = 2 * r2;
                    double dDelta_x = Math.abs(C3 * delta.sensors[sensorIndex].x - currentSensor.x);
                    double X3_x = delta.sensors[sensorIndex].x - A3 * dDelta_x;

                    currentSensor.x = (X1_x + X2_x + X3_x) / 3.0;

                    // Y koordinatı için GWO güncellemesi
                    r1 = rnd.nextDouble();
                    r2 = rnd.nextDouble();
                    A1 = 2 * a * r1 - a;
                    C1 = 2 * r2;
                    double dAlpha_y = Math.abs(C1 * alpha.sensors[sensorIndex].y - currentSensor.y);
                    double X1_y = alpha.sensors[sensorIndex].y - A1 * dAlpha_y;

                    r1 = rnd.nextDouble();
                    r2 = rnd.nextDouble();
                    A2 = 2 * a * r1 - a;
                    C2 = 2 * r2;
                    double dBeta_y = Math.abs(C2 * beta.sensors[sensorIndex].y - currentSensor.y);
                    double X2_y = beta.sensors[sensorIndex].y - A2 * dBeta_y;

                    r1 = rnd.nextDouble();
                    r2 = rnd.nextDouble();
                    A3 = 2 * a * r1 - a;
                    C3 = 2 * r2;
                    double dDelta_y = Math.abs(C3 * delta.sensors[sensorIndex].y - currentSensor.y);
                    double X3_y = delta.sensors[sensorIndex].y - A3 * dDelta_y;

                    currentSensor.y = (X1_y + X2_y + X3_y) / 3.0;
                }

                // Sınır kontrolü ve fitness güncelleme
                solution.checkBounds();
                solution.calculateFitness();
            }

            // Alfa, beta, delta güncelleme
            for (Solution solution : population) {
                if (solution.fitness < alpha.fitness) {
                    delta = new Solution(beta);
                    beta = new Solution(alpha);
                    alpha = new Solution(solution);
                } else if (solution.fitness < beta.fitness) {
                    delta = new Solution(beta);
                    beta = new Solution(solution);
                } else if (solution.fitness < delta.fitness) {
                    delta = new Solution(solution);
                }
            }

            // İlerleme raporu
            if (iteration % 10 == 0 || iteration == MAX_ITR - 1) {
                System.out.printf("İterasyon %3d: En iyi fitness = %.6f (Parazit seviyesi)\n",
                        iteration, alpha.fitness);
            }
        }

        // Final sonuçları
        System.out.println("\n=== SONUÇLAR ===");
        alpha.printResults();

        // Basit görselleştirme
        System.out.println("\n=== SENSÖR HARİTASI ===");
        System.out.println("(Koordinat sistemi: -50 ile +50 arası)");

        char[][] grid = new char[21][21];
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                grid[i][j] = '.';
            }
        }

        // Sensörleri grid'e yerleştir
        for (int i = 0; i < SENSOR_COUNT; i++) {
            Wolf sensor = alpha.sensors[i];
            int gridX = (int) ((sensor.x + 50) / 100 * 20);
            int gridY = (int) ((sensor.y + 50) / 100 * 20);
            gridX = Math.max(0, Math.min(20, gridX));
            gridY = Math.max(0, Math.min(20, gridY));
            grid[gridY][gridX] = (char) ('1' + i);
        }

        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("\n1-6: Sensör numaraları, .: Boş alan");
    }
}