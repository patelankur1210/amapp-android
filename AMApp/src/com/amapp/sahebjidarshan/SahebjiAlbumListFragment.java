package com.amapp.sahebjidarshan;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amapp.R;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.smart.customviews.SmartRecyclerView;
import com.smart.customviews.SmartTextView;
import com.smart.framework.Constants;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartApplication;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartUtils;

import java.util.ArrayList;

/**
 * Created by tasol on 21/8/15.
 */
public class SahebjiAlbumListFragment extends SmartFragment {

    private SmartRecyclerView mRecyclerView;
    private AlbumAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ContentValues> albums;
    private SmartTextView emptyView;

    public SahebjiAlbumListFragment() {

    }

    @Override
    public int setLayoutId() {
        return R.layout.sahebji_album_list_fragment;
    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public void initComponents(View currentView) {

        mRecyclerView = (SmartRecyclerView) currentView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        emptyView= (SmartTextView) currentView.findViewById(R.id.txtEmpty);
    }

    @Override
    public void prepareViews(View currentView) {
        mAdapter = new AlbumAdapter();
    }

    @Override
    public void setActionListeners(View currentView) {

    }

    public void setAlbums(ArrayList<ContentValues> albums, boolean isCachedDataDisplayed) {

        this.albums = albums;

        if(isCachedDataDisplayed && mRecyclerView.getChildCount()>0){
            mAdapter.notifyDataSetChanged();
        }else{
            mRecyclerView.setAdapter(mAdapter);
            emptyView.setText("No Temples Found");
            mRecyclerView.setEmptyView(emptyView);
        }
    }

    class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> implements Constants {

        public class ViewHolder extends RecyclerView.ViewHolder {

            public View view;
            public ImageView imgAlbum;
            public SmartTextView txtName;

            public ViewHolder(View view) {
                super(view);

                this.view = view;
                imgAlbum = (ImageView) view.findViewById(R.id.imgAlbum);
                txtName = (SmartTextView) view.findViewById(R.id.txtName);
            }
        }

        @Override
        public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sahebji_list_item, parent, false);

            final ViewHolder vh = new ViewHolder(v);
            if(SmartUtils.isOSPreLollipop()){
                ((CardView)v.findViewById(R.id.cardView)).setPreventCornerOverlap(false);
            }
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                int index = mRecyclerView.getChildAdapterPosition(view);
                Intent intent = new Intent(getActivity(), SahebjiGalleryActivity.class);
                intent.putExtra(SahebjiGalleryActivity.ALBUM_DETAIL, albums.get(index));
                Pair<View, String> p1 = Pair.create(view.findViewById(R.id.imgAlbum), "image");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1);
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            ContentValues temple= albums.get(position);

            SmartApplication.REF_SMART_APPLICATION.getAQuery().id(holder.imgAlbum).image(temple.getAsString("mainImage"),true,true,((SmartActivity)getActivity()).getDeviceWidth(),0,new BitmapAjaxCallback(){
                @Override
                protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                    super.callback(url, iv, bm, status);

                }
            });

            holder.txtName.setText(temple.getAsString("albumName"));
        }

        @Override
        public int getItemCount() {
            return albums.size();
        }
    }
}
