package edu.oregonstate.studentlife.ihcv2.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import edu.oregonstate.studentlife.ihcv2.data.Product;

/**
 * Created by beerad on 1/17/18.
 * NOTE: Class not used; can be deleted
 */

public class GridViewAdapter extends ArrayAdapter<Product> {
    public GridViewAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
    }
    /*@NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_item, null);
        }

        Product product = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) v.findViewById(R.id.txtDescription);

        img.setImageResource(product.getImageId());
        txtTitle.setText(product.getTitle());
        txtDescription.setText(product.getDescription());

        return v;
    }*/
}
