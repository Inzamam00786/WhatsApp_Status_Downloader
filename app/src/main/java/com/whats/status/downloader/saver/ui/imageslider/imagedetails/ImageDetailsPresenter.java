package com.whats.status.downloader.saver.ui.imageslider.imagedetails;

import com.whats.status.downloader.saver.data.local.FileHelper;
import com.whats.status.downloader.saver.data.model.ImageModel;
import com.whats.status.downloader.saver.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by shaz on 14/2/17.
 */

public class ImageDetailsPresenter extends BasePresenter<ImageDetailsView> {

    private static final String TAG = ImageDetailsPresenter.class.getSimpleName();
    private final FileHelper fileHelper;

    @Inject
    public ImageDetailsPresenter(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    void setLoadingAnimation(boolean status) {
        getMvpView().displayLoadingAnimation(status);
    }

    void saveMedia(ImageModel imageModel) {
        boolean status = fileHelper.saveMediaToLocalDir(imageModel);
        if (status) {
            getMvpView().displayImageSavedMsg();
        }
    }

    void deleteLocalImage(ImageModel imageModel) {
        boolean status = fileHelper.deleteImageFromLocalDir(imageModel);
        if (status) {
            getMvpView().displayDeleteSuccessMsg();
        }
    }

}
