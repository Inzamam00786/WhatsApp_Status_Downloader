package com.whats.status.downloader.saver.ui.imageslider;

import com.whats.status.downloader.saver.data.local.FileHelper;
import com.whats.status.downloader.saver.data.model.ImageModel;
import com.whats.status.downloader.saver.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by shaz on 14/2/17.
 */

public class ImageSliderPresenter extends BasePresenter<ImageSliderView> {

    private static final String TAG = ImageSliderPresenter.class.getSimpleName();
    private final FileHelper fileHelper;

    @Inject
    public ImageSliderPresenter(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    void setLoadingAnimation(boolean status) {
        getMvpView().displayLoadingAnimation(status);
    }

    void loadRecentImageSlider(ImageModel imageModel) {

        // Get images
        List<ImageModel> items = fileHelper.getRecentImages();

        // Get position of image to be displayed in the list
        int position = items.indexOf(imageModel);

        if (position != -1) {
            getMvpView().displayImageSlider(items, position);
        }
    }

    void loadSavedImageSlider(ImageModel imageModel) {
        // Get images
        List<ImageModel> items = fileHelper.getSavedImages();

        // Get position of image to be displayed in the list
        int position = items.indexOf(imageModel);

        if (position != -1) {
            getMvpView().displayImageSlider(items, position);
        }
    }

}
