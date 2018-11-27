package com.flyaudio.base.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Xu
 */
public final class ArrayUtils {

    public static <T> boolean isEmpty(T[] arrays) {
        return arrays == null || arrays.length == 0;
    }

    public static <T> boolean isEmpty(Collection<T> list) {
        return list == null || list.size() == 0;
    }

    public static <T> int indexOf(T[] list, T item) {
        if (isEmpty(list) || item == null) {
            return -1;
        }
        return Arrays.asList(list).indexOf(item);
    }

    /**
     * @param from the initial index of the range to be copied, inclusive
     * @param to   the final index of the range to be copied, exclusive.
     */
    public static <T, U> T[] copyOfRange(U[] original, int from, int to, Class<? extends T[]> newType, CopyArrayConvert<U, T> convert) {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        T[] copy = ((Object) newType == (Object) Object[].class)
                ? (T[]) new Object[newLength]
                : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        for (int i = 0; i < newLength; i++) {
            U u = original[i];
            copy[i] = convert.convert(u);
        }
        return copy;
    }

    /**
     * @param from the initial index of the range to be copied, inclusive
     * @param to   the final index of the range to be copied, exclusive.
     */
    public static <T, U> void copyOfRange(U[] original, int from, int to, T[] dest, int destFrom, CopyArrayConvert<U, T> convert) {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        for (int i = 0; i < newLength; i++) {
            U u = original[i];
            dest[destFrom++] = convert.convert(u);
        }
    }

    public static <T, U> void copyOfRange(List<U> src, int from, int to, List<T> dest, int destFrom, CopyArrayConvert<U, T> convert) {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        for (int i = from; i < to; i++) {
            U u = src.get(i);
            dest.add(destFrom + i, convert.convert(u));
        }
    }

    public static <U, T> List<T> copyOfRange(List<U> src, int from, int to, CopyArrayConvert<U, T> convert) {
        if (src == null) {
            return Collections.emptyList();
        }
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        ArrayList<T> dest = new ArrayList<>(newLength);
        for (int i = from; i < to; i++) {
            U u = src.get(i);
            dest.add(convert.convert(u));
        }
        return dest;
    }

    /**
     * @param from the initial index of the range to be copied, inclusive
     * @param to   the final index of the range to be copied, exclusive.
     */
    public static <T, U> T[] copyOfRange(List<U> original, int from, int to, Class<? extends T[]> newType, CopyArrayConvert<U, T> convert) {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        T[] copy = ((Object) newType == (Object) Object[].class)
                ? (T[]) new Object[newLength]
                : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        for (int i = 0; i < newLength; i++) {
            U u = original.get(i);
            copy[i] = convert.convert(u);
        }
        return copy;
    }

    /**
     * 在数组末尾添加元素
     * @param origin
     * @param element
     * @param <T>
     * @return
     */
    public static<T> T[] insertElement(T[] origin, T element) {
        T[] result = Arrays.copyOf(origin, origin.length + 1);
        result[origin.length] = element;
        return result;
    }

    /**
     * @return
     */
    public static<T> T[] concat(Class<? extends T[]> newType, T[]... list) {
        int newLength = 0;
        for (T[] a : list) {
            newLength += a.length;
        }
        T[] c = ((Object) newType == (Object) Object[].class)
                ? (T[]) new Object[newLength]
                : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        int curIndex = 0;
        for (T[] a : list) {
            System.arraycopy(a, 0, c, curIndex, a.length);
            curIndex += a.length;
        }
        return c;
    }

    public interface CopyArrayConvert<U, T> {
        T convert(U u);
    }


}
