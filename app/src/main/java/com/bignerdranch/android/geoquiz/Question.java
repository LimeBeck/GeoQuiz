package com.bignerdranch.android.geoquiz;

/**
 * Created by metal on 22.01.2018.
 */

//Класс для хранения данных по вопросам
public class Question {
    //ID текста вопроса
    private int mTextResId;
    //Врено/Неверно
    private boolean mAnswerTrue;
    //Был ли отвечен
    private boolean mAnswered;


    //Конструктор, ID текста вопроса задаётся, верно/неверно - тоже, "отвечено" по-умолнчанию - нет
    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mAnswered = false;
    }

    //гетеры/сетеры для всех полей
    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isAnswered() {
        return mAnswered;
    }

    public void setAnswered(boolean answered) {
        mAnswered = answered;
    }

}
