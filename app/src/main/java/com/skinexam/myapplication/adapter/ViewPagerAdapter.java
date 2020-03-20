package com.skinexam.myapplication.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

//    private ArrayList<Integer> IMAGES;
//    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> imageUrls;



    public ViewPagerAdapter(Context context, ArrayList<String> imageUrls) {
        this.context = context;
        this.imageUrls=imageUrls;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Picasso.with(context)
//                .load(imageUrls[position])
                .load(new File(imageUrls.get(position)))
                .fit()
                .centerCrop()
                .into(imageView);

        container.addView(imageView);



        return imageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
//        return view.equals(object);
        return view == object;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
