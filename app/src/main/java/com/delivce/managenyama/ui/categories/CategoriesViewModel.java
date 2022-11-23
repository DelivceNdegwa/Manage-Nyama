package com.delivce.managenyama.ui.categories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CategoriesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
//    private final MutableLiveData<List<Object>> salesList;

    public CategoriesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
//        salesList = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }

}