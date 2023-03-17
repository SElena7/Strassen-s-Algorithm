package mpjExpressTesting;

	import java.util.Random;
	import java.util.Scanner;
	import java.util.concurrent.*;

	 class ParallelStrassen1 {


	     public static void main(String[] args) throws ExecutionException, InterruptedException {
	         Random rd = new Random();
	         Scanner scan = new Scanner(System.in);
	         ParallelStrassen1 s = new ParallelStrassen1();
	         long startTime = System.nanoTime();
	         System.out.println("enter the size of the matrix:");
	         int len = scan.nextInt();
	         double A[][] = new double[len][len];
	         double B[][] = new double[len][len];
	         //First matrix;

	         for (int i = 0; i < len; i++)
	             for (int j = 0; j < len; j++)
	                 A[i][j] = rd.nextInt(10) + 1;
             //second matrix 
	         for (int i = 0; i < len; i++)
	             for (int j = 0; j < len; j++)
	                 B[i][j] = rd.nextInt(10) + 1;
	         
	         System.out.println("matrix A:");
	         System.out.print("[");
	         for (int i = 0; i < len; i++) {
	             System.out.print("[");
	             for (int j = 0; j < len; j++) {
	                 System.out.print(A[i][j] + " " + ",");

	             }
	             System.out.println("]");
	         }
	         System.out.println("]");

	         System.out.println("matrix B:");
	         System.out.print("[");
	         for (int i = 0; i < len; i++) {
	             System.out.print("[");
	             for (int j = 0; j < len; j++) {
	                 System.out.print(B[i][j] + " " + ",");

	             }
	             System.out.println("]");
	         }
	         System.out.println("]");

	         //The result of multiplication of A and B

	         ParallelMM t1 = new ParallelMM(A, B, len);
	         t1.multiply();
	         long endTime = System.nanoTime();
	         double C[][] = t1.getRes();

	         System.out.println("Result of parallel multiplication of A and B:");
	         System.out.print("[");
	         for (int i = 0; i < len; i++) {
	             System.out.print("[");
	             for (int j = 0; j < len; j++) {
	                 System.out.print(C[i][j] + " " + ",");

	             }
	             System.out.println("]");
	         }
	         System.out.println("]");
	         // parallel program execution time in nanoseconds
	         long totalTime = endTime - startTime;
	         System.out.println("Parallel program execution time in nanoseconds: " + totalTime);
	     }
	 }


	    class ParallelMM {
	        //Free processors for executor to create threads (compatible from machine to machine)
	        private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
	        private final ExecutorService exec = Executors.newFixedThreadPool(POOL_SIZE);
	        private double A[][];
	        private double B[][];
	        private double C[][];

	        ParallelMM(double A[][], double B[][], int len) {
	            this.A = A;
	            this.B = B;
	            this.C = new double[len][len];	        }
	        public void multiply() {
	            Future f = exec.submit(new Mul(A, B, C, 0, 0, 0, 0, 0, 0, A.length));
	            try {
	                f.get();
	                exec.shutdown();
	            } catch (Exception e) { System.out.println(e); }}
	        public double[][] getRes() {
	            return C;}
	        class Mul implements Runnable {
	            private double[][] A;
	            private double[][] B;
	            private double[][] C;
	            private int a_i, a_j, b_i, b_j, c_i, c_j, n;
	    public Mul(double A[][], double B[][], double[][] C, int a_i, int a_j, int b_i, int b_j, int c_i, int c_j, int n) {
	                this.A = A;
	                this.B = B;
	                this.C = C;
	                this.a_i = a_i;
	                this.a_j = a_j;
	                this.b_i = b_i;
	                this.b_j = b_j;
	                this.c_i = c_i;
	                this.c_j = c_j;
	                this.n = n;
	            }

	            public void multiply(double[][] A, double[][] B, double[][] C, int a_i, int a_j, int b_i, int b_j, int c_i, int c_j, int n) {


	                if (n == 1) {
	                    C[c_i][c_j] = A[a_i][a_j] * B[b_i][b_j];

	                } else {
	                    double[][] A11 = copy(A, a_i, a_i + n / 2, a_j, a_j + n / 2);
	                    double[][] A12 = copy(A, a_i + n / 2, a_i + n, a_j, a_j + n / 2);
	                    double[][] A21 = copy(A, a_i, a_i + n / 2, a_j + n / 2, a_j + n);
	                    double[][] A22 = copy(A, a_i + n / 2, a_i + n, a_j + n / 2, a_j + n);

	                    double[][] B11 = copy(B, b_i, b_i + n / 2, b_j, b_j + n / 2);
	                    double[][] B12 = copy(B, b_i + n / 2, b_i + n, b_j, b_j + n / 2);
	                    double[][] B21 = copy(B, b_i, b_i + n / 2, b_j + n / 2, b_j + n);
	                    double[][] B22 = copy(B, b_i + n / 2, b_i + n, b_j + n / 2, b_j + n);


	                    double[][] p = new double[n / 2][n / 2];
	                    double[][] q = new double[n / 2][n / 2];
	                    double[][] r = new double[n / 2][n / 2];
	                    double[][] s = new double[n / 2][n / 2];
	                    double[][] t = new double[n / 2][n / 2];
	                    double[][] u = new double[n / 2][n / 2];
	                    double[][] v = new double[n / 2][n / 2];

	                    multiply(add(A11, A22, n / 2), add(B11, B22, n / 2), p, 0, 0, 0, 0, 0, 0, n / 2);
	                    multiply(add(A21, A22, n / 2), B11, q, 0, 0, 0, 0, 0, 0, n / 2);
	                    multiply(A11, sub(B12, B22, n / 2), r, 0, 0, 0, 0, 0, 0, n / 2);
	                    multiply(A22, sub(B21, B11, n / 2), s, 0, 0, 0, 0, 0, 0, n / 2);
	                    multiply(add(A11, A12, n / 2), B22, t, 0, 0, 0, 0, 0, 0, n / 2);
	                    multiply(sub(A21, A11, n / 2), add(B11, B12, n / 2), u, 0, 0, 0, 0, 0, 0, n / 2);
	                    multiply(sub(A12, A22, n / 2), add(B21, B22, n / 2), v, 0, 0, 0, 0, 0, 0, n / 2);

	                    join(C, add(sub(add(p, s, n / 2), t, n / 2), v, n / 2),
	                            add(r, t, n / 2),
	                            add(q, s, n / 2),
	                            add(sub(add(p, r, n / 2), q, n / 2), u, n / 2),
	                            c_i, c_j, n);
	                }
	            }

	            public void join(double[][] C, double[][] c11, double[][] c12, double[][] c21, double[][] c22, int c_i, int c_j, int n) {
	                int m = n / 2;

	                for (int i = 0; i < m; i++) {
	                    for (int j = 0; j < m; j++) {
	                        C[c_i + i][c_j + j] += c11[i][j];
	                    }
	                }

	                for (int i = m; i < 2 * m; i++) {
	                    for (int j = 0; j < m; j++) {
	                        C[c_i + i][c_j + j] += c12[i - m][j];
	                    }
	                }

	                for (int i = 0; i < m; i++) {
	                    for (int j = m; j < 2 * m; j++) {
	                        C[c_i + i][c_j + j] += c21[i][j - m];
	                    }
	                }

	                for (int i = m; i < 2 * m; i++) {
	                    for (int j = m; j < 2 * m; j++) {
	                        C[c_i + i][c_j + j] += c22[i - m][j - m];
	                    }
	                }
	            }

	            public double[][] add(double[][] A, double[][] B, int n) {
	                double[][] C = new double[n][n];
	                for (int i = 0; i < n; i++) {
	                    for (int j = 0; j < n; j++) {
	                        C[i][j] = A[i][j] + B[i][j];
	                    }
	                }
	                return C;
	            }

	            public double[][] sub(double[][] A, double[][] B, int n) {
	                double[][] C = new double[n][n];
	                for (int i = 0; i < n; i++) {
	                    for (int j = 0; j < n; j++) {
	                        C[i][j] = A[i][j] - B[i][j];
	                    }
	                }
	                return C;
	            }

	            public double[][] copy(double[][] A, int n1, int n2, int p1, int p2) {
	                double[][] C = new double[n2 - n1][p2 - p1];
	                for (int i = n1; i < n2; i++) {
	                    for (int j = p1; j < p2; j++) {
	                        C[i - n1][j - p1] = A[i][j];
	                    }
	                }
	                return C;
	            }


	            public void run() {
	                int h = n / 2;
	                if (n <= 64) {
	                    multiply(A, B, C, a_i, a_j, b_i, b_j, c_i, c_j, n);
	                } else {
	                    Mul[] tasks = {
	                            new Mul(A, B, C, a_i, a_j, b_i, b_j, c_i, c_j, h),
	                            new Mul(A, B, C, a_i, a_j + h, b_i + h, b_j, c_i, c_j, h),

	                            new Mul(A, B, C, a_i, a_j, b_i, b_j + h, c_i, c_j + h, h),
	                            new Mul(A, B, C, a_i, a_j + h, b_i + h, b_j + h, c_i, c_j + h, h),

	                            new Mul(A, B, C, a_i + h, a_j, b_i, b_j, c_i + h, c_j, h),
	                            new Mul(A, B, C, a_i + h, a_j + h, b_i + h, b_j, c_i + h, c_j, h),

	                            new Mul(A, B, C, a_i + h, a_j, b_i, b_j + h, c_i + h, c_j + h, h),
	                            new Mul(A, B, C, a_i + h, a_j + h, b_i + h, b_j + h, c_i + h, c_j + h, h)
	                    };
	                    FutureTask[] fs = new FutureTask[tasks.length / 2];
	                    for (int i = 0; i < tasks.length; i += 2) {
	                        fs[i / 2] = new FutureTask(new Seq(tasks[i], tasks[i + 1]), null);
	                        exec.execute(fs[i / 2]);
	                    }
	                    for (int i = 0; i < fs.length; ++i) {
	                        fs[i].run();
	                    }
	                    try {
	                        for (int i = 0; i < fs.length; ++i) {
	                            fs[i].get();
	                        }
	                    } catch (Exception e) {
	                        System.out.println(e);
	                    }
	                }
	            }
	        }


	    class Seq implements Runnable {
	        private Mul first, second;

	        Seq(Mul first, Mul second) {
	            this.first = first;
	            this.second = second;
	        }

	        public void run() {
	            first.run();
	            second.run();
	        }
	    }
	}







