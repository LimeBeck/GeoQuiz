package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    //Здаём ключ для сохранения данных
    private static final String KEY_CHEATER = "cheater";

    //Задаём ключи для получения данных и передачи
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";

    //Определяем переменные
    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private TextView mVersion;
    private Button mShowAnswerButton;
    private boolean mIsCheater = false;

    //Переопреляем создание активности из другого класса
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        //Собственно создание активности
        Intent intent = new Intent(packageContext, CheatActivity.class);
        //Вывод данных в экстра
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    //Передаём данные из активности через экстра (был ли показан ответ)
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    //Переопределяем метод при создании активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Задаём разметку
        setContentView(R.layout.activity_cheat);
        //Проверяем сохранённые данные
        if (savedInstanceState != null) {
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER, false);
        }

        //Получаем данные из экстра
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        //Находим элементы интерфейса
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        //Задаём реакцию кнопки
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                //Сигналим, что кто-то читерит
                setAnswerShownResult(true);

                //Добавляем свистоперделки для новых версий андроид (начиная с 5)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    //Определяем сохранение инфы при повороте экрана
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_CHEATER, mIsCheater);
    }

    //Сигналим про читерство, пихая данные в экстра и сигнализируя об успешном выполнении активности
    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        mIsCheater = true;
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
