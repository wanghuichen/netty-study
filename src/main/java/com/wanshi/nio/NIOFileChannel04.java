package com.wanshi.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {

    public static void main(String[] args) throws Exception{
//        //创建输入输出流
//        FileInputStream fileInputStream = new FileInputStream("d:\\a.jpg");
//        FileOutputStream fileOutputStream = new FileOutputStream("d:\\a2.jpg");
//
//        //获取相关的通道
//        FileChannel sourceCh = fileInputStream.getChannel();
//        FileChannel destCh = fileOutputStream.getChannel();
//
//        destCh.transferFrom(sourceCh, 0, sourceCh.size());
//        //关闭通道和流
//        sourceCh.close();
//        destCh.close();
//        fileInputStream.close();
//        fileOutputStream.close();

        //二分查找
        //4
        //1 2 3 4 5 9
        int[] nums = {1, 3, 4, 5, 9};
        int search = search(nums, 2);
        System.out.println(search);
    }

    public static int search(int[] nums, int target) {
        int n = nums.length;
        int left = 0, right = n - 1, ans = n;
        while (left <= right) {
            int mid = ((right - left) >> 1) + left;
            if (target <= nums[mid]) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }
}
