package edu.oregonstate.studentlife.ihcv2Demo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.oregonstate.studentlife.ihcv2Demo.R;
import edu.oregonstate.studentlife.ihcv2Demo.data.Resource;

/**
 * Created by Omeed on 3/1/18.
 */

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder> {

    private Context context;
    private ArrayList<Resource> mResourceList;
    private OnResourceClickListener mOnResourceClickListener;

    public ResourceAdapter(Context context, OnResourceClickListener onResourceClickListener) {
        this.context = context;
        mOnResourceClickListener = onResourceClickListener;
        mResourceList = new ArrayList<Resource>();
    }

    public void addResource(Resource resource) {
        mResourceList.add(resource);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mResourceList.size();
    }

    public interface OnResourceClickListener {
        void onResourceClick(Resource resource);
    }

    class ResourceViewHolder extends RecyclerView.ViewHolder {

        private ImageView mResourceImageIV;
        private TextView mResourceTitleTV;
        private TextView mResourceDescriptionTV;
        private TextView mResourceLinkTV;

        public ResourceViewHolder(View itemView) {
            super(itemView);
            mResourceImageIV = (ImageView) itemView.findViewById(R.id.iv_resource);
            mResourceTitleTV = (TextView) itemView.findViewById(R.id.tv_resource_title);
            mResourceDescriptionTV = (TextView) itemView.findViewById(R.id.tv_resource_description);
            mResourceLinkTV = (TextView) itemView.findViewById(R.id.tv_resource_link);
            mResourceLinkTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnResourceClickListener.onResourceClick(mResourceList.get(getAdapterPosition()));
                }
            });
        }

        void bind(Resource resource) {
            mResourceTitleTV.setText(resource.getResourceTitle());
            mResourceDescriptionTV.setText(resource.getResourceDescription());
            if (TextUtils.isEmpty(resource.getResourceLink())) {
                mResourceLinkTV.setVisibility(View.GONE);
            }
            Picasso.with(context)
                    .load(resource.getResourceImagePath())
                    .into(mResourceImageIV);
        }
    }

    @Override
    public ResourceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.resource_item, viewGroup, false);
        ResourceViewHolder viewHolder = new ResourceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResourceViewHolder holder, int position) {
        Resource resource = mResourceList.get(position);
        holder.bind(resource);
    }
}
