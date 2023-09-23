import org.apache.commons.math3.complex.Complex;

public class ParallelFFT {
    private static class FFTThread extends Thread {
        private Complex[] data;
        private Complex[] result;

        public FFTThread(Complex[] data) {
            this.data = data;
        }

        public Complex[] getResult() {
            return result;
        }

        @Override
        public void run() {
            try {
                result = fft(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static Complex[] fft(Complex[] data) throws InterruptedException {
        if (data.length == 1) {
            return data;
        }

        Complex[] even = new Complex[data.length / 2];
        Complex[] odd = new Complex[data.length - even.length];
        for (int i = 0; i < data.length; i++) {
            if (i % 2 == 0) {
                even[i / 2] = data[i];
            } else {
                odd[i / 2] = data[i];
            }
        }

        FFTThread threadEven = new FFTThread(even);
        FFTThread threadOdd = new FFTThread(odd);

        threadEven.start();
        threadOdd.start();

        threadEven.join();
        threadOdd.join();

        Complex[] evenFFT = threadEven.getResult();
        Complex[] oddFFT = threadOdd.getResult();

        Complex[] output = new Complex[data.length];
        for (int i = 0; i < data.length / 2; i++) {
            output[i] = evenFFT[i].add(oddFFT[i].multiply(new Complex(Math.cos(-2 * Math.PI * i / data.length), Math.sin(-2 * Math.PI * i / data.length))));
            output[i + data.length / 2] = evenFFT[i].subtract(oddFFT[i].multiply(new Complex(Math.cos(-2 * Math.PI * i / data.length), Math.sin(-2 * Math.PI * i / data.length))));
        }

        return output;
    }

    public static void main(String[] args) throws InterruptedException {
       /* Complex[] data = new Complex[]{
                new Complex(-4.0, 0.0),
                new Complex(-4.0, 0.0),
                new Complex(-12.0, 0.0),
                new Complex(-16.0, 0.0)
        };*/
        Complex[] data = new Complex[]{
                new Complex(-1.0, 0.0),
                new Complex(2.0, 0.0),
                new Complex(0, 0.0),
                new Complex(0, 0.0),
                new Complex(4.0, 0.0),
                new Complex(0, 0.0),
                new Complex(-3.0, 0.0),
                new Complex(-1.0, 0.0)
        };
        Complex[] fft = fft(data);

        for (Complex c : fft) {
            System.out.println(c);
        }
    }
}