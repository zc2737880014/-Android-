package com.byted.camp.todolist.beans;

import android.graphics.Color;

/**
 * Created on 2019/1/23.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
//public enum Priority {
//    High(2, Color.RED),
//    Medium(1, Color.GREEN),
//    Low(0, Color.WHITE);
//
//    public final int intValue;
//    public final int color;
//
//    Priority(int intValue, int color) {
//        this.intValue = intValue;
//        this.color = color;
//    }
//
//    public static Priority from(int intValue) {
//        for (Priority priority : Priority.values()) {
//            if (priority.intValue == intValue) {
//                return priority;
//            }
//        }
//        return Priority.Low; // default
//    }
//}

//重写Priority


public class Priority{
    public static final int valueHigh = 2;
    public static final int valueMedium = 1;
    public static final int valueLow = 0;
    public static final int colorHigh = Color.BLUE;
    public static final int colorMedium = Color.GREEN;
    public static final int colorLow = Color.WHITE;


    private int value;
    private int color;

    //初始化，只需用value值初始化，调用setColorbyValue()函数来设置color值
    public Priority(int value) {
        if ((value == valueHigh) || (value == valueMedium) || (value == valueLow)) {
            this.value = value;
            setColorbyValue(value);
        }
        else{
            this.value = valueLow;
            setColorbyValue(value);
        }
    }
    //获取和设置Priority内容
    //首先颜色要根据Priority的值来确定，向外不提供直接设置颜色的接口
    private void setColorbyValue(int value){
        switch (value){
            case valueHigh:
                this.color = colorHigh;
                break;
            case valueMedium:
                this.color = colorMedium;
                break;
            case valueLow:
                this.color = colorLow;
                break;
            default:
                this.color = colorLow;
                break;
        }
    }
    //设置Value可于更改优先级时使用（本次作业未用到）
    public void setValue(int value){
        this.value = value;
        setColorbyValue(value);
    }
    //取出Value和Color
    public int getValue(){
        return this.value;
    }
    public int getColor(){
        return this.color;
    }
}
