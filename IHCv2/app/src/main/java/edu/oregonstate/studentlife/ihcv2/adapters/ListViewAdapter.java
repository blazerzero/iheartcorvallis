package edu.oregonstate.studentlife.ihcv2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

import edu.oregonstate.studentlife.ihcv2.data.Product;

/**
 * Created by beerad on 1/17/18.
 * NOTE: Class not used; can be deleted
 */

public class ListViewAdapter extends ArrayAdapter<Product> {
    public ListViewAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
    }

    /*@NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.mylistviewevents, null);
        }

        Product product = getItem(position);
        TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) v.findViewById(R.id.txtDescription);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);


        txtTitle.setText(product.getTitle());
        txtDescription.setText(product.getDescription());
        img.setImageResource(product.getImageId());


        return v;
    }*/
}
