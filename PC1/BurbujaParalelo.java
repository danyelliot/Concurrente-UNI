import java.util.Arrays;
public class BurbujaParalelo{
    static int N = 10000;
    static int H = 8;
    static int[] arr = new int[N];    
    static int[] arrParallel = new int[N];    
    static int[] arrParallelSort = new int[N];

    public static void GenerateArray(){
        System.out.println("Generando arreglo...");
        for(int i = 0; i < N; i++){
            arr[i] = (int)(Math.random()*100);
            arrParallel[i] = arr[i];
            arrParallelSort[i] = arr[i];
        }
        System.out.println("Arreglo generado");
    }
    public static void ImprimirArray(int[] arrToPrint){
        for(int i = 0; i < N; i++){
            System.out.print(arrToPrint[i] + " ");
        }
        System.out.println();
    }
    public static void main(String[] args) {
        GenerateArray();
        long startTime = System.currentTimeMillis();
        try{
            while(true){
                Manager manager = new Manager(N, H);
                manager.start();
                manager.join();
                if(H == 1){
                    break;
                }
                H = H/2;
            }
        }catch(InterruptedException e){
            System.out.println("Error");
        }
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Tiempo: " + estimatedTime + "ms");
        startTime = System.currentTimeMillis();
        Arrays.parallelSort(arrParallelSort);
        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Tiempo: " + estimatedTime + "ms");
    }
}

class Burbuja extends Thread{
    int id, inicio, fin;
    public Burbuja(int id, int inicio, int fin){
        this.id = id;
        this.inicio = inicio;
        this.fin = fin;
    }
    public void run(){
        //System.out.println("Hilo " + id + " creado" + " inicio: " + inicio + " fin: " + fin);
        int len = fin - inicio;
        int k = 0;
        for(int i = 0; i < len-1; i++){
            for(int j = 0; j < len-1-i; j++){
                k = inicio + j;
                if(BurbujaParalelo.arrParallel[k] > BurbujaParalelo.arrParallel[k+1]){
                    int aux = BurbujaParalelo.arrParallel[k];
                    BurbujaParalelo.arrParallel[k] = BurbujaParalelo.arrParallel[k+1];
                    BurbujaParalelo.arrParallel[k+1] = aux;
                }
            }
        }
        //System.out.println("Hilo " + id + " terminado");
    }
}

class Manager extends Thread{
    int N, H;
    public Manager(int N, int H){
        this.N = N;
        this.H = H;
    }
    public void run(){
        Burbuja[] hilos = new Burbuja[H];
        for(int i = 0; i < H; i++){
            Burbuja t = new Burbuja(i, i*N/H, (i+1)*N/H);
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
                    flag = true;
                }
            }
        }
    }
}