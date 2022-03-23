package com.wanshi.bio;

import java.util.Arrays;

public class LeetCodeDemo {

    public static void main(String[] args) {
        //题目1：
        //给你一个按 非递减顺序 排序的整数数组 nums，返回 每个数字的平方 组成的新数组，要求也按 非递减顺序 排序。
//        int[] nums = {-4,-1,0,3,10};
//        int[] resultArr = sortNum(nums);
//        System.out.println(Arrays.toString(resultArr));

        //题目2
        //给你一个数组，将数组中的元素向右轮转 k 个位置，其中 k 是非负数。
        int[] nums = {1, 2, 3, 4, 5, 6, 7};
        rotate(nums, 2);
        System.out.println(Arrays.toString(nums));
    }

    public static void rotate(int[] nums, int k) {
        int n = nums.length;
        int[] newArr = new int[n];
        for (int i = 0; i < n; i++) {
            newArr[(i + k) % n] = nums[i];
        }
        System.arraycopy(newArr, 0, nums, 0, n);
    }

    public static int[] sortNum(int[] nums) {
        int[] nums2 = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            nums2[i] = nums[i]*nums[i];
        }

        Arrays.sort(nums2);

        return nums2;
    }


}
