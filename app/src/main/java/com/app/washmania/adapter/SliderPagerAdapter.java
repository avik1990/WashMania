package com.app.washmania.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.app.washmania.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avik on 13-08-2017.
 */

public class SliderPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Activity activity;
    List<String> image_arraylist;

    public SliderPagerAdapter(Activity activity, List<String> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
        try {
            SimpleDraweeView im_slider = (SimpleDraweeView) view.findViewById(R.id.im_slider);
            final ProgressBar progressView = (ProgressBar) view.findViewById(R.id.progressbar);
            im_slider.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
            //progressView.setVisibility(View.VISIBLE);
       /*Picasso.with(activity.getApplicationContext())
                .load(image_arraylist.get(position))
                //.placeholder(R.mipmap.ic_launcher) // optional
                //.error(R.mipmap.ic_launcher)         // optional
                .into(im_slider);*/
        /*Picasso.with(activity.getApplicationContext())
                .load(image_arraylist.get(position))
                .into(im_slider, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressView.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError() {
                    }
                });*/
       /* Glide.with(activity.getApplicationContext() )
                .load(image_arraylist.get(position))
                .thumbnail(0.1f) //rate of the full resoultion to show while loading
                .into(im_slider);*/

            com.facebook.imagepipeline.request.ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(image_arraylist.get(position)))
                    .setProgressiveRenderingEnabled(true)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .build();

           /* im_slider.setController(Fresco.newDraweeControllerBuilder().setControllerListener(new ControllerListener<ImageInfo>() {
                @Override
                public void onSubmit(String id, Object callerContext) {

                }

                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    progressView.setVisibility(View.GONE);
                }

                @Override
                public void onIntermediateImageSet(String id, ImageInfo imageInfo) {

                }

                @Override
                public void onIntermediateImageFailed(String id, Throwable throwable) {

                }

                @Override
                public void onFailure(String id, Throwable throwable) {

                }

                @Override
                public void onRelease(String id) {

                }
            }).build());*/

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(im_slider.getController())
                    .build();
            im_slider.setController(controller);


            container.addView(view);
        } catch (Exception e) {

        }
        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}