import java.util.Arrays;
public class CountingParalelo{
    static int N;
    static int H;
    static int[] arr;    
    static int[] arrParallel;
    static int[] arrParallelJava;
    
    public static void GenerateArray(){
        //System.out.println("Generando arreglo...");
        arr = new int[N];
        arrParallel = new int[N];
        arrParallelJava = new int[N];
        for(int i = 0; i < N; i++){
            int value = (int)(Math.random()*100);
            arr[i] = value;
            arrParallel[i] = value;
            arrParallelJava[i] = value;
        }
        //System.out.println("Arreglo generado");
    }

    public static void main(String[] args){
        for(int i = 4; i < 8; i++){
            N = (int) Math.pow(10, i);
            for(int j = 0; j < 10; j+=2) {
                H = (int) 2 + j;
                Main();
            }
        }
    }

    static void Main(){
        GenerateArray();
        for (int i = 0; i < N; i++) {
            //System.out.print(arr[i] + " ");
        }
        //System.out.println();
        System.out.println("Datos: " + N + " Hilos: " + H);
        System.out.println("Secuencial");
        //mide el tiempo de ejecucion
        long startTime = System.nanoTime();
        Sequential();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Tiempo: " + (double)duration/1_000_000_000 + " segundos");
        for (int i = 0; i < N; i++) {
            //System.out.print(arr[i] + " ");
        }
        //System.out.println();
        CountingManager manager = new CountingManager(N, H);
        System.out.println("Paralelo");
        startTime = System.nanoTime();
        manager.start();
        try{
            manager.join();
            endTime = System.nanoTime();
            duration = (endTime - startTime);
            System.out.println("Tiempo: " + (double)duration/1_000_000_000 + " segundos");
        }catch(InterruptedException e){
            System.out.println("Error");
        }
        for (int i = 0; i < N; i++) {
            //System.out.print(arrParallel[i] + " ");
        }
        //System.out.println();
        System.out.println("Paralelo Java");
        startTime = System.nanoTime();
        Arrays.parallelSort(arrParallelJava);
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        System.out.println("Tiempo: " + (double)duration/1_000_000_000 + " segundos");
    }

    public static void Sequential() {
        int max = Arrays.stream(arr).max().getAsInt();
        int min = Arrays.stream(arr).min().getAsInt();
        int k = max - min + 1;
        int[] count = new int[k];
        int[] out = new int[N];
        for (int i = 0; i < N; i++) {
            count[arr[i] - min]++;
        }
        for (int i = 1; i < k; i++) {
            count[i] += count[i - 1];
        }
        for (int i = N - 1; i >= 0; i--) {
            out[count[arr[i] - min] - 1] = arr[i];
            count[arr[i] - min]--;
        }
        for (int i = 0; i < N; i++) {
            arr[i] = out[i];
        }
    }
}

class Counting extends Thread{
    int id, start, end;
    public Counting(int id, int start, int end){
        this.id = id;
        this.start = start;
        this.end = end;
    }
    public void run() {
        int N = this.end - this.start;
        int[] arr = new int[N];
        for (int i = 0; i < N; i++) {
            arr[i] = CountingParalelo.arrParallel[i + this.start];
        }
        int min = Arrays.stream(arr).min().getAsInt();
        int max = Arrays.stream(arr).max().getAsInt();
        int k = max - min + 1;
        int[] count = new int[k];
        int[] out = new int[N];

        for (int i = 0; i < N; i++) {
            count[arr[i] - min]++;
        }
        for (int i = 1; i < k; i++) {
            count[i] += count[i - 1];
        }
        for (int i = N - 1; i >= 0; i--) {
            out[count[arr[i] - min] - 1] = arr[i];
            count[arr[i] - min]--;
        }

        for (int i = 0; i < N; i++) {
            CountingParalelo.arrParallel[i + this.start] = out[i];
        }
    }
}

class CountingManager extends Thread{
    int N, H;
    public CountingManager(int N, int H){
        this.N = N;
        this.H = H;
    }
    public void run(){
        //System.out.println("Manager empezado");
        Counting[] hilos = new Counting[H];
        for(int i = 0; i < H; i++){
            Counting t = new Counting(i, i*N/H, (i+1)*N/H);
            hilos[i] = t;
        }
        for(int i = 0; i < H; i++){
            hilos[i].start();
        }
        boolean flag = true;
        while(flag){
            flag = false;
            for(int i = 0; i < H; i++){
                if(hilos[i].isAlive()){
                    //System.out.println("Hilo " + i + " sigue vivo");
                    flag = true;
                }
            }
        }
        Counting t = new Counting(0, 0, CountingParalelo.N);
        t.start();
        try{
            t.join();
        }catch(InterruptedException e){
            System.out.println("Error");
        }
    }
}