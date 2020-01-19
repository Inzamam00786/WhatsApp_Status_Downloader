package com.whats.status.downloader.saver.ui.main.saved;

import com.whats.status.downloader.saver.data.model.ImageModel;
import com.whats.status.downloader.saver.ui.base.MvpView;

import java.util.List;

/**
 * Created by shaz on 6/3/17.
 */

public interface SavedPicsView extends MvpView{
    void displayLoadingAnimation(boolean status);
    void displaySavedImages(List<ImageModel> images);
    void displayNoImagesInfo();
    void displayImage(int position, ImageModel imageModel);
    void displayDeleteSuccessMsg();
    void displayDeleteConfirm(List<ImageModel> imageModels);
}
