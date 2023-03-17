package mpjExpressTesting;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Strassen {
	public int [][] multiply(int A[][], int B[][]) {
		
	int n = A.length;
	int res [][]= new int [n][n];
	if (n==1) {	
		res[0][0]=A[0][0]*B[0][0];

	}
	else {
		int A11[][]=new int[n/2][n/2];
		int A12[][]=new int[n/2][n/2];
		int A21[][]=new int[n/2][n/2];
		int A22[][]=new int[n/2][n/2];
		
		int B11[][]=new int[n/2][n/2];
		int B12[][]=new int[n/2][n/2];
		int B21[][]=new int[n/2][n/2];
		int B22[][]=new int[n/2][n/2];
	
	    split(A,A11,0,0);
	    split(A,A12,0,n/2);
	    split(A,A21,n/2,0);
	    split(A,A22,n/2,n/2);
	    
	    split(B,B11,0,0);
	    split(B,B12,0,n/2);
	    split(B,B21,n/2,0);
	    split(B,B22,n/2,n/2);
	    
	    
	    int p [][]=multiply(add(A11,A22),add(B11,B22));
	    int q [][]=multiply(add(A21,A22),B11);
	    int r [][]=multiply(A11,sub(B12,B22));
	    int s [][]=multiply(A22,sub(B21,B11));
	    int t [][]=multiply(add(A11,A12),B22);
	    int u [][]=multiply(sub(A21,A11),add(B11,B12));
	    int v [][]=multiply(sub(A12,A22),add(B21,B22));
	    
	    int C11[][]=add(sub(add(p,s),t),v);
	    int C12[][]=add(r,t);
	    int C21[][]=add(q,s);
	    int C22[][]=add(sub(add(p,r),q),u);
	
	    join(C11,res,0,0);
	    join(C12,res,0,n/2);
	    join(C21,res,n/2,0);
	    join(C22,res,n/2,n/2);

	}
	return res;
	}
	
	public int [][] add(int a[][], int b[][]) {
		int r[][]=new int [a.length][a.length]; 	
		 for(int i=0; i<a.length; i++) {
		   for(int j=0; j<a.length; j++) {
			   r[i][j]=a[i][j]+b[i][j];}   }
		return r;}
	
	public int [][] sub(int a[][], int b[][]) {
		
	int r[][]=new int [a.length][a.length]; 	
		
	   for(int i=0; i<a.length; i++) {
		   for(int j=0; j<a.length; j++) {
			   r[i][j]=a[i][j]-b[i][j];}  }
	return r;}
	
	public void split(int[][] parent, int [][] child, int x , int y) {
		for(int  i1=0, i2=x;i1<child.length; i1++, i2++ ) 
			for(int j1=0, j2 =y;j1<child.length; j1++, j2++ )
             child[i1][j1]=parent[i2][j2];				
	}

	public void join(int[][] child, int [][] parent, int x , int y) {
		for(int  i1=0, i2=x;i1<child.length; i1++, i2++ ) 
			for(int j1=0, j2 =y;j1<child.length; j1++, j2++ )
             parent[i2][j2]=child[i1][j1];				
	}

	public static void main (String [] args) throws ExecutionException, InterruptedException {
		Strassen s = new Strassen();
		 long startTime = System.nanoTime();
		Random rd = new Random(); 
		Scanner scan=new Scanner(System.in);

	
		System.out.println("enter the size of the matrix:");
		int len = scan.nextInt();
		int A[][]=new int [len][len];
		int B[][]=new int [len][len];
		//First matrix;
		for(int i=0;i<len;i++)  
			for(int j=0;j<len;j++) 
				A[i][j]=rd.nextInt(10)+1;
				//A[i][j]=scan.nextInt();
		//Second matrix;
		for(int i=0;i<len;i++) 
			for(int j=0;j<len;j++)
				//B[i][j]=scan.nextInt();
				B[i][j]=rd.nextInt(10)+1;
	    int C[][]=s.multiply(A, B);

	    System.out.println("matrix A:");
	    System.out.print("[");
	    for(int i=0;i<len;i++) {
	    	System.out.print("[");
	    	for(int j=0;j<len;j++) {
				System.out.print(A[i][j]+" "+",");	
				
			}
	       System.out.println("]");
		}System.out.println("]");
		System.out.println("matrix B:");
	    System.out.print("[");
	    for(int i=0;i<len;i++) {
	    	System.out.print("[");
	    	for(int j=0;j<len;j++) {
				System.out.print(B[i][j]+" "+",");	
				
			}
	       System.out.println("]");
		}System.out.println("]");
	    
	    //The result of multiplication of A and B 
	    
	    System.out.println("Result of sequential multiplication of A and B:");
	    System.out.print("[");
	    for(int i=0;i<len;i++) {
	    	System.out.print("[");
	    	for(int j=0;j<len;j++) {
				System.out.print(C[i][j]+" "+",");	
				
			}
	       System.out.println("]");
		}System.out.println("]");
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println("Sequential program execution time in nanoseconds: "+totalTime);
	}
}
