package com.whats.status.downloader.saver.ui.base;

import android.support.v4.app.Fragment;

import com.whats.status.downloader.saver.TheApplication;

public class BaseFragment extends Fragment {

    public TheApplication getTheApplication() {
        return ((TheApplication) getActivity().getApplication());
    }

}