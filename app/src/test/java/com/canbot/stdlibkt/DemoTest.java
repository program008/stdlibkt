package com.canbot.stdlibkt;

import org.junit.Test;

/**
 * Created by tao.liu on 2019/1/4.
 * description this is description
 */
public class DemoTest {
        @Test
        public void test(){
                byte[] mArmActionBuf_head = new byte[]{0x44,0x4A,0x00,0x29, (byte) 0xB1,0x00,
                        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
                        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
                        0x00,0x0D,0x0A};
                String str = "000017";
                byte[] actionBytes = str.getBytes();
                int index = 37;
                for (int i = 0; i < actionBytes.length; i++) {
                        mArmActionBuf_head[index] = actionBytes[(actionBytes.length-1) - i];
                        index--;
                }

                for (int i = 0; i < mArmActionBuf_head.length; i++) {
                        System.out.print(mArmActionBuf_head[i]+" ");
                }

                System.out.println("-------------");
                for (int i = 0; i < actionBytes.length; i++) {
                        System.out.print(actionBytes[i]+" ");
                }
                System.out.println("");
                System.out.println("length: "+actionBytes.length);
        }

}
