package com.google.developers.mojimaster2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.widget.EmojiAppCompatTextView;
import androidx.emoji.widget.EmojiTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.developers.mojimaster2.data.Smiley;
import com.google.developers.mojimaster2.game.AnswersView;
import com.google.developers.mojimaster2.game.GameViewModel;
import com.google.developers.mojimaster2.game.GameViewModelFactory;
import com.google.developers.mojimaster2.game.Result;

import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private EmojiTextView mQuestionView;
    private AnswersView mAnswersView;
    private TextView mResult;
    private GameViewModel mGameViewModel;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAnswersView=new AnswersView(this);

        GameViewModelFactory viewModelFactory = GameViewModelFactory.createFactory(this);
        mGameViewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel.class);

        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mQuestionView = findViewById(R.id.question);
        mResult = findViewById(R.id.result);
        linearLayout=findViewById(R.id.linearLayout);

        mGameViewModel.getCurrentAnswer().observe(this, this::updateContent);
        mGameViewModel.getResults().observe(this, this::showResults);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::loadNewGame);


        mGameViewModel.setUpGame().observe(this, new Observer<List<Smiley>>() {
            @Override
            public void onChanged(List<Smiley> smilies) {
                loadRound(smilies);



            }
        });
}

    /**
     * Listener on answer selection.
     */
    private void onAnswersChange(String answer) {

        mAnswersView.setOnAnswerListener(new AnswersView.OnAnswerListener() {
            @Override
            public void onAnswerSelected(String answer) {
                mGameViewModel.updateResult(answer);

            }
        });
    }

    /**
     * Loads new game and updates observer.
     */
    private void loadNewGame(@Nullable View view) {

        mResult.setText(null);
        mGameViewModel.resetGame();
        mGameViewModel.setUpGame().observe(this, new Observer<List<Smiley>>() {
            @Override
            public void onChanged(List<Smiley> smilies) {
                loadRound(smilies);



            }
        });

    }

    /**
     * Load answers for the next round.
     */
    private void loadRound(List<Smiley> smileys) {

        mGameViewModel.startNewGameRound();
        mAnswersView.loadAnswers(smileys);

        linearLayout.removeView(mAnswersView);
        linearLayout.addView(mAnswersView);

    }

    /**
     * Show results of each round.
     */
    private void showResults(Result result) {
        if (result == null) {
            return;
        }
        mResult.setTextColor(getColor(result.getColor()));
        mResult.setText(getString(result.getResult()));

        if (!result.getEnableAnswersView()) {
            mAnswersView.setEnabled(false);
        }
    }

    private void updateContent(Smiley smiley) {
        if (smiley == null) {
            return;
        }
        mQuestionView.setText(smiley.getEmoji());
        onAnswersChange(smiley.getName());




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_list:
                Intent listIntent = new Intent(this, SmileyListActivity.class);
                startActivity(listIntent);
                return true;
            case R.id.action_add:
                Intent addIntent = new Intent(this, AddSmileyActivity.class);
                startActivity(addIntent);
                return true;
            case R.id.action_settings:
                Intent settingIntent = new Intent(this, SettingActivity.class);
                startActivity(settingIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
