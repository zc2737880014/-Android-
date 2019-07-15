package com.byted.camp.todolist.beans;

import java.util.Date;

/**
 * Created on 2019/1/23.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public class Note {

//    public final long id;
//    private Date date;
//    private State state;
//    private String content;
//    private Priority priority;
//
//    public Note(long id) {
//        this.id = id;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
//
//    public State getState() {
//        return state;
//    }
//
//    public void setState(State state) {
//        this.state = state;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public Priority getPriority() {
//        return priority;
//    }
//
//    public void setPriority(Priority priority) {
//        this.priority = priority;
//    }

// 重写部分
    //定义note包含的内容（id、内容、时间、完成状态、优先级）
    private long id;
    private String content;
    private Date date;
    private State state;
    private Priority priority;

    //两个初始化函数
    public Note(long id){
        this.id = id;
    }
    public Note(long id,String content,Priority priority){
        this.id = id;
        this.content = content;
        this.priority = priority;
    }

    //从note类中获取内容（id、内容、时间、完成状态、优先级）
    public long getId(){
        return this.id;
    }
    public String getContent(){
        return this.content;
    }
    public Date getDate(){
        return this.date;
    }
    public State getState(){
        return this.state;
    }
    public Priority getPriority(){
        return this.priority;
    }

    //设定note类的内容（id、内容、时间、完成状态、优先级）
    public void setId(long id){
        this.id = id;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setDate(Date date){
        this.date = date;
    }
    public void setState(State state){
        this.state = state;
    }
    public void setPriority(Priority priority){
        this.priority = priority;
    }

}
