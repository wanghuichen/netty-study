package com.wanshi.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception {
//        //创建文件输入流
//        File file = new File("d:\\01.txt");
//        FileInputStream fileInputStream = new FileInputStream(file);
//        //获取文件对应的FileChannel
//        FileChannel fileChannel = fileInputStream.getChannel();
//
//        //创建ByteBuffer缓冲区
//        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
//
//        //将文件中的内容读取到缓冲区
//        fileChannel.read(byteBuffer);
//
//        //输出文件内容
//        System.out.println(new String(byteBuffer.array()));
//        fileInputStream.close();

//        int[] nums = {-4, -1, 0, 3, 10};
//        int[] ints = sortedSquares(nums);
//        System.out.println(Arrays.toString(ints));
        int[] nums = {1, 2, 3, 4, 5, 6, 7};
        rotate(nums, 2);
    }

    public static int[] sortedSquares(int[] nums) {
        int[] nums2 = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            nums2[i] = nums[i]*nums[i];
        }
        Arrays.sort(nums2);
        return nums2;
    }

    public static void rotate(int[] nums, int k) {
        int n = nums.length;
        int[] newArr = new int[n];
        for (int i = 0; i < n; i++) {
            newArr[(i + k) % n] = nums[i];
        }
        System.arraycopy(newArr, 0, nums, 0, n);
        System.out.println(Arrays.toString(newArr));
    }

    /**
     * 搜索插入的位置
     * @param nums
     * @param target
     * @return 返回下标
     */
    public int searchInsert(int[] nums, int target) {
        //得到数组的长度
        int n = nums.length;
        //计算左右两边位置和中间位
        int left = 0, right = n - 1, ans = n;
        //循环遍历查询
        while (left <= right) {
            //计算中间位置
            int mid = ((right - left) >> 1) + left;
            //目标值小于中间数字
            if (target <= nums[mid]) {
                //更新中间值
                ans = mid;
                //左移一位
                right = mid - 1;
            } else {
                //反之右移一位
                left = mid + 1;
            }
        }
        //返回下标所在处
        return ans;
    }
}

