package com.babkin.eljournal.entity.temporaly;

import com.babkin.eljournal.entity.working.Raspisanie;

import java.util.List;

public class QuickSort {
    public static void quickSort(List<Raspisanie> array, int low, int high) {
        if (array.size() == 0)
            return;//завершить выполнение, если длина массива равна 0

        if (low >= high)
            return;//завершить выполнение если уже нечего делить

        // выбрать опорный элемент
        int middle = low + (high - low) / 2;
        Raspisanie opora = array.get(middle);

        // разделить на подмассивы, который больше и меньше опорного элемента
        int i = low, j = high;
        while (i <= j) {
            while (array.get(i).getNumber() < opora.getNumber()) {
                i++;
            }

            while (array.get(j).getNumber() > opora.getNumber()) {
                j--;
            }

            if (i <= j) {//меняем местами
                Raspisanie tempI = array.get(i);
                int indexI = array.indexOf(tempI);
                Raspisanie tempJ = array.get(j);
                int indexJ = array.indexOf(tempJ);
                array.set(indexI, tempJ);
                array.set(indexJ, tempI);
                //array[i] = array[j];
                //array[j] = temp;
                i++;
                j--;
            }
        }

        // вызов рекурсии для сортировки левой и правой части
        if (low < j)
            quickSort(array, low, j);

        if (high > i)
            quickSort(array, i, high);
    }
//    public static void main(String[] args) {
//        int[] x = { 8, 0, 4, 7, 3, 7, 10, 12, -3 };
//        System.out.println("Было");
//        System.out.println(Arrays.toString(x));
//
//        int low = 0;
//        int high = x.length - 1;
//
//        quickSort(x, low, high);
//        System.out.println("Стало");
//        System.out.println(Arrays.toString(x));
//    }
}
