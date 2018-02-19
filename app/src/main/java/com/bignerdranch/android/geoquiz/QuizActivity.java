package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    //Тэг для поиска отладочной информации
    private static final String TAG = "QuizActivity";
    //Ключ для сохранения информации об номере вопроса при повороте экрана и переходе между активностями
    private static final String KEY_INDEX = "index";
    //ключ для передачи данных о подсмотре вопроса между активностями
    private static final String KEY_CHEATER = "cheater";
    private static final String KEY_QUESTIONS = "questions";
    //Код возврата данных из другой активности
    private static final int REQUEST_CODE_CHEAT = 0;

    //Описываем кнопки и тексты
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private int mAnswers = 0;
    private int mTrueAnswers = 0;

    //Инициализируем массив вопросов
    private Question[] mQuestionBank = new Question[]{new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    //Переопределяем метод вызываемый при создании активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Собственно создание активности
        super.onCreate(savedInstanceState);
        //Выводим информацию в лог
        Log.d(TAG, "onCreate(Bundle) called");
        //Задаём вызываемую разметку активности
        setContentView(R.layout.activity_quiz);

        //Если активность была открыта ранее, получаем индекс вопроса и узнаём, был ли подсмотрен ответ
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER, false);

        }

        //Связываем переменную с текстовым полем в активности
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);


        //Аналогично для кнопок
        mTrueButton = (Button) findViewById(R.id.true_button);
        //Задаём слушателя кнопки, т.е. реакцию на нажатие
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });
        //Обновляем вывод вопроса
        updateQuestion();
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Номер вопроса +1, переходим дальше и обновляем экран
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //То же, только переходим назад
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                if (mCurrentIndex < 0) mCurrentIndex = mQuestionBank.length - 1;
                updateQuestion();
            }
        });
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Задаём переменную с верным ответом
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                //Задаём новую активность
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                //Вызываем новую активность с возвратом результата
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
    }

    //Переопределяем метод, вызываемый при возврате активностью CheatActivity результата
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    //Просто куча переопределений с выводом действий в лог
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart() called");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy() called");
    }

    //Сохранение данных при уничтожении активности (при повороте экрана, например)
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_CHEATER, mIsCheater);
    }

    //Метод обновления данных вопроса на экране
    private void updateQuestion() {
        //Получаем идентификатор такста вопроса
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        //Задаём этот текст
        mQuestionTextView.setText(question);

        //ПРоверяем, если вопрос был отвечен, то блокируем кнопки
        if (mQuestionBank[mCurrentIndex].isAnswered()) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
        try {
            if (!mQuestionBank[mCurrentIndex].isAnswered()) {
                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);
            }
        } catch (Exception e) {
            Log.e(TAG, "Обнаружено исключение! " + e);
        }

    }

    //Метод для проверки на читера
    private void checkAnswer(boolean userPressedTrue) {
        //Получаем правильный ответ
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        //Задаём варианты всплывающего сообщения
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mTrueAnswers += 1;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        //Запоминаем количество отвеченных вопросов
        mAnswers += 1;
        //Отмечаем вопрос отвеченным
        mQuestionBank[mCurrentIndex].setAnswered(true);
        //Обновляем экран
        updateQuestion();
        //Выводим сообщение
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        //Если отвечены все вопросы - выводим строку с итогами
        if (mAnswers == mQuestionBank.length) {
            TextView mScoreTextView = findViewById(R.id.score);
            mScoreTextView.setText("Your score:" + mTrueAnswers + " of " + mAnswers);
        }
    }
}
