package com.byted.camp.todolist.beans;

/**
 * Created on 2019/1/23.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
//public enum State {
//    TODO(0), DONE(1);
//
//    public final int intValue;
//
//    State(int intValue) {
//        this.intValue = intValue;
//    }
//
//    public static State from(int intValue) {
//        for (State state : State.values()) {
//            if (state.intValue == intValue) {
//                return state;
//            }
//        }
//        return TODO; // default
//    }
//}

//重写State
public class State{
    public static final int Undo = 0;
    public static final int Done = 1;

    private int value;

    //初始化
    public State(int value){
        if(value == Done)
        this.value = Done;
        else this.value = Undo;
    }
    //设置和获取value的值来知道状态
    public void setValue(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
