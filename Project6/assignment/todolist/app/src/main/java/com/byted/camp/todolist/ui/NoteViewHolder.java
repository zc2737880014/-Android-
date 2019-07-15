package com.byted.camp.todolist.ui;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.byted.camp.todolist.NoteOperator;
import com.byted.camp.todolist.R;
import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.beans.State;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created on 2019/1/23.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
//由于重写了db的相关类和函数，因此需要改写一些接口调用语句
public class NoteViewHolder extends RecyclerView.ViewHolder {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
            new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);

    private final NoteOperator operator;

    private CheckBox checkBox;
    private TextView contentText;
    private TextView dateText;
    private View deleteBtn;

    public NoteViewHolder(@NonNull View itemView, NoteOperator operator) {
        super(itemView);
        this.operator = operator;

        checkBox = itemView.findViewById(R.id.checkbox);
        contentText = itemView.findViewById(R.id.text_content);
        dateText = itemView.findViewById(R.id.text_date);
        deleteBtn = itemView.findViewById(R.id.btn_delete);
    }

    public void bind(final Note note) {
        contentText.setText(note.getContent());
        dateText.setText(SIMPLE_DATE_FORMAT.format(note.getDate()));
        //由于之后我改变了完成任务后的dateText的颜色加一行dateText颜色设置
        dateText.setTextColor(Color.GRAY);
        checkBox.setOnCheckedChangeListener(null);
//        checkBox.setChecked(note.getState() == State.DONE);
        //由于State类重写需要更改判定条件
        checkBox.setChecked(note.getState().getValue()==State.Done);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                note.setState(isChecked ? State.DONE : State.TODO);
                //由于State类重写需要改变setState（）的参数
                note.setState(isChecked ? (new State(State.Done)):(new State(State.Undo)));
                operator.updateNote(note);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator.deleteNote(note);
            }
        });

        if (/*note.getState() == State.DONE
            由于重写了State类，需要更改判定条件*/
            note.getState().getValue()==State.Done) {
            contentText.setTextColor(Color.GRAY);
            dateText.setText("Finish!");
            dateText.setTextColor(Color.RED);
            contentText.setPaintFlags(contentText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            contentText.setTextColor(Color.BLACK);
            contentText.setPaintFlags(contentText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

//        itemView.setBackgroundColor(note.getPriority().color);
        //由于重写了Priority类，color为一个private项，需要调用函数获得color
        itemView.setBackgroundColor(note.getPriority().getColor());
    }
}
