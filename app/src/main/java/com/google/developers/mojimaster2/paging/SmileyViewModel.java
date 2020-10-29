package com.google.developers.mojimaster2.paging;

import android.media.ImageReader;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.developers.mojimaster2.data.Smiley;
import com.google.developers.mojimaster2.data.DataRepository;

import java.util.List;

/**
 * Store and manage data for SmileyListActivity.
 */
public class SmileyViewModel extends ViewModel {

    private final DataRepository mRepository;
    public static int PAGE_SIZE = 30;
    public static boolean PLACEHOLDERS = true;
    public LiveData<PagedList<Smiley>> smileys_list_pager;

    public SmileyViewModel(DataRepository repository) {
        mRepository = repository;
        smileys_list_pager=new LivePagedListBuilder<>(mRepository.getSmileys(),PAGE_SIZE).build();
    }

    public void save(Smiley smiley) {
       //to cache deleted smily

        mRepository.insert(smiley);
    }



    public void delete(Smiley smiley) {
        mRepository.delete(smiley);
    }

}
