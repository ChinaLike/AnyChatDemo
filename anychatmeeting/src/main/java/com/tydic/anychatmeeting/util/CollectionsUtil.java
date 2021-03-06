package com.tydic.anychatmeeting.util;

import java.util.Collections;
import java.util.List;

/**
 * 集合工具类
 * 向前移动问题：把第一个元素与第三个元素调换一下
 * 向后移动问题：把第三个元素与第一个元素调换一下
 * Created by like on 2017-10-09
 */

public class CollectionsUtil {
    /**
     * 调换集合中两个指定位置的元素, 若两个元素位置中间还有其他元素，需要实现中间元素的前移或后移的操作。
     *
     * @param list        集合
     * @param oldPosition 需要调换的元素
     * @param newPosition 被调换的元素
     * @param <T>
     */
    public static <T> void swap1(List<T> list, int oldPosition, int newPosition) {
        if (null == list) {
            throw new IllegalStateException("The list can not be empty...");
        }
        T tempElement = list.get(oldPosition);

        // 向前移动，前面的元素需要向后移动
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                list.set(i, list.get(i + 1));
            }
            list.set(newPosition, tempElement);
        }
        // 向后移动，后面的元素需要向前移动
        if (oldPosition > newPosition) {
            for (int i = oldPosition; i > newPosition; i--) {
                list.set(i, list.get(i - 1));
            }
            list.set(newPosition, tempElement);
        }
    }

    /**
     * 调换集合中两个指定位置的元素, 若两个元素位置中间还有其他元素，需要实现中间元素的前移或后移的操作。
     *
     * @param list        集合
     * @param oldPosition 需要调换的元素
     * @param newPosition 被调换的元素
     * @param <T>
     */
    public static <T> void swap2(List<T> list, int oldPosition, int newPosition) {
        if (null == list) {
            throw new IllegalStateException("The list can not be empty...");
        }

        // 向前移动，前面的元素需要向后移动
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        }

        // 向后移动，后面的元素需要向前移动
        if (oldPosition > newPosition) {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
    }

    /**
     * 只交换指定位置数据
     *
     * @param list
     * @param oldPosition
     * @param newPosition
     * @param <T>
     */
    public static <T> void swap3(List<T> list, int oldPosition, int newPosition) {
        if (null == list) {
            throw new IllegalStateException("The list can not be empty...");
        }
        try {
            Collections.swap(list, oldPosition, newPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 把oldPosition位置的数据插入到newPosition位置，数据依次后移
     *
     * @param list
     * @param oldPosition
     * @param newPosition
     * @param <T>
     */
    public static <T> void insertion(List<T> list, int oldPosition, int newPosition) {
        if (null == list) {
            throw new IllegalStateException("The list can not be empty...");
        }
        try {
            T bean = list.get(oldPosition);
            list.remove(oldPosition);
            list.add(newPosition, bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
