package com.google.developers.mojimaster2.paging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.emoji.widget.EmojiAppCompatEditText;
import androidx.emoji.widget.EmojiAppCompatTextView;
import androidx.emoji.widget.EmojiTextView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.developers.mojimaster2.R;
import com.google.developers.mojimaster2.data.Smiley;

import java.util.List;

public class SmileyAdapter extends PagedListAdapter<Smiley, SmileyAdapter.SmileyViewHolder> {
    public SmileyAdapter() {
        super(DIFF_CALLBACK);
    }



    @NonNull
    @Override
    public SmileyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rviewlistitem, parent, false);
        return new SmileyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SmileyViewHolder holder, int position) {
        Smiley item = getItem(position);
        holder.bindTo(item);
    }

    public class SmileyViewHolder extends RecyclerView.ViewHolder {

        private EmojiTextView mEmoji;
        private TextView mName;
        private TextView mUnicode;
        private Smiley mSmiley;

        SmileyViewHolder(View itemView) {
            super(itemView);
            mEmoji=itemView.findViewById(R.id.emj_listitem);
            mName=itemView.findViewById(R.id.name_listitem);
            mUnicode=itemView.findViewById(R.id.unicode_listitem);


        }

        public Smiley getSmiley() {
            return mSmiley;
        }

        void bindTo(Smiley smiley) {
            mSmiley = smiley;
            mEmoji.setText(smiley.getEmoji());
            mName.setText(smiley.getName());
            mUnicode.setText(smiley.getCode());
        }
    }

    private static final DiffUtil.ItemCallback<Smiley> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Smiley>() {
                @Override
                public boolean areItemsTheSame(@NonNull Smiley oldItem, @NonNull Smiley newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Smiley oldItem,
                                                  @NonNull Smiley newItem) {
                    return oldItem.getCode().equals(newItem.getCode());
                }
            };
}
