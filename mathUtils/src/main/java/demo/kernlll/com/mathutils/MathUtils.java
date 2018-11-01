package demo.kernlll.com.mathutils;

import java.util.Arrays;

public class MathUtils {

    static int[] arrs = {
            42,20,17,13,28,14,23,15
    };

    static int[] arrs1 = {
            13,17,20,36,14,15,23,28
    };
    public static void main(String[] args ) {

        System.out.println(Arrays.toString(bubbleSort(arrs)));
        System.out.println(Arrays.toString(chooseSort(arrs)));
        System.out.println(Arrays.toString(insertSort(arrs)));
        System.out.println(Arrays.toString(mergeSort(bubbleSort(arrs),chooseSort(arrs1))));
    }

    //冒泡排序(on2)7个数只需要比较6次
    public static int[] bubbleSort(int[] args) {
        int temp;
        boolean flag = false;
        for(int i=0; i<args.length-1;i++) {
            flag = false;
            for(int j=args.length-1;j>i;j--) {//冒泡倒叙比较
                if (args[j]<args[j-1]) {
                    temp = args[j];
                    args[j] = args[j-1];
                    args[j-1] = temp;
                    flag = true;
                }
            }
            if (!flag) break;
        }
        return args;
    }

    //选择排序on2
    public static int[] chooseSort(int[] args) {

        for (int i=0;i<args.length-1;i++) {
            int minIndex = i;
            for(int j=i+1;j<args.length;j++) {//不是length-1
                if (args[j] < args[minIndex]) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                int temp = args[i];
                args[i] = args[minIndex];
                args[minIndex] = temp;
            }
        }
        return args;
    }

    //插入排序
    public static int[] insertSort(int[] args) {
        int temp = 0;
        for(int i=0; i<args.length-1;i++) {
            for(int j=i+1;j>0;j--) {
                if(args[j]<args[j-1]) {
                    temp = args[j-1];
                    args[j-1] = args[j];
                    args[j] = temp;
                } else {
                    break;
                }
            }
        }
        return args;
    }

    //归并排序
    static int[] mergeSort(int[] args1, int[] args2) {
        int args3[] = new int[args1.length + args2.length];
        int i,j,k;
        i=j=k=0;
        while(i<args1.length & j<args2.length) {
            if (args1[i]<args2[j]) {
                args3[k++] = args1[i++];
            }else if (args1[i]>args2[j]){
                args3[k++] = args2[j++];
            } else if (args1[i] == args2[j]) {
                args3[k++]= args1[i];
                i++;
                j++;
            }
        }

        while(i<args1.length) {
            args3[k++] = args1[i++];
        }
        while(j<args2.length) {
            args3[k++] = args1[j++];
        }
        return args3;
    }
}
