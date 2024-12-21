public class Laba {
    public static void main(String[] args) {
        short[] w = new short[]{3, 5, 7, 9, 11, 13, 15, 17, 19, 21};
        double[] x;
        x = new double[12];
        for (int i = 0; i < x.length; i++) {
            x[i] = -5.0 + (Math.random() * (11.0 - (-5.0)));//random
        }
        mas(w,x);//создаём массив

    }
    public static void mas(short[] w,double[] x)
    {
        double[][] w1 = new double[10][12];
        for (int i = 0;i<10;i++){
            for (int j = 0;j<12;j++)
            {
                if (w[i] == 9)
                {
                    w1[i][j] = Math.cos(Math.log(Math.acos((x[j]+3)/16)));
                }
                else if (w[i] == 5 || w[i] ==7 || w[i]==11 || w[i]==17 || w[i] == 21) {
                    w1[i][j] = Math.log(Math.pow(Math.cos(Math.pow(( (double) 1 /3-Math.pow((x[j]/2)/Math.PI,3)), 2)),2));
                    
                }
                else{
                    w1[i][j]= Math.pow(Math.pow(4*(2+Math.cbrt(Math.pow((x[j]-3)/0.5,x[j]))),2),  0.25 * (4-Math.pow(Math.tan(Math.atan((x[j]+3)/16)), (Math.tan(Math.atan((x[j]+3)/16)))/1/2)));
                }
            }
        }
        fin(w1);//out
    }
    public static void fin(double[][] w1)
    {
        for (int i = 0; i < 10; i++) {  //идём по строкам
            System.out.println("-".repeat(169));
            for (int j = 0; j < 12; j++) {//идём по столбцам
                System.out.print("|");
                System.out.printf(" %12.3f", w1[i][j]); //вывод элемента
            }
            System.out.println("|");

        }
        System.out.println("-".repeat(169));
    }
}