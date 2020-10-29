package com.google.developers.mojimaster2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.developers.mojimaster2.data.Smiley;
import com.google.developers.mojimaster2.paging.SmileyAdapter;
import com.google.developers.mojimaster2.paging.SmileyViewModel;
import com.google.developers.mojimaster2.paging.SmileyViewModelFactory;

/**
 * Activity lists all Smileys in RecyclerView using Paging library.
 */
public class SmileyListActivity extends AppCompatActivity {

    private SmileyViewModel mViewModel;
    private RecyclerView mRecycler;
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SmileyViewModelFactory smileyViewModelFactory = SmileyViewModelFactory.createFactory(this);
        mViewModel = ViewModelProviders.of(this, smileyViewModelFactory).get(SmileyViewModel.class);

        EmojiCompat.init(new BundledEmojiCompatConfig(this));
        setContentView(R.layout.activity_smileys);

        SmileyAdapter adapter = new SmileyAdapter();
        mRecycler=findViewById(R.id.rview_smileyitems);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);


        //do the adapter work here
        mViewModel.smileys_list_pager.observe(this, new Observer<PagedList<Smiley>>() {
            @Override
            public void onChanged(PagedList<Smiley> smilies) {
               adapter.submitList(smilies);
            }

        });

        mRecycler.setAdapter(adapter);

        initAction();

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddSmileyActivity.class);
            startActivity(intent);
        });
    }

    public void initAction() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                        @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Smiley smiley = ((SmileyAdapter.SmileyViewHolder) viewHolder).
                        getSmiley();
                mViewModel.delete(smiley);

                String text = getString(R.string.undo_deleted, smiley.getEmoji());
                Snackbar.make(mFab, text, Snackbar.LENGTH_LONG)
                        .setAction("Undo", view -> mViewModel.save(smiley)).show();
            }
        });

        itemTouchHelper.attachToRecyclerView(mRecycler);
    }

}
