package cl.ucn.disc.dsm.ffarias.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


/**
 * The Adapter.
 */
public final class Adapter extends BaseAdapter {

  Context context;
  List<Contact> contactList;

  Adapter(Context context, List<Contact> contactList) {
    this.context = context;
    this.contactList = contactList;
  }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        @SuppressLint("ViewHolder") View view = View.inflate(context, R.layout.contacts_list, null);
        TextView tv_cl_name = view.findViewById(R.id.tv_cl_name);
        TextView tv_cl_number = view.findViewById(R.id.tv_cl_number);
        ImageView iv_cl_image = view.findViewById(R.id.iv_cl_image);

        tv_cl_name.setText(contactList.get(position).name);
        tv_cl_number.setText(contactList.get(position).number);

        if (contactList.get(position).image != null)
            iv_cl_image.setImageBitmap(contactList.get(position).image);
        else{
            iv_cl_image.setImageResource(R.drawable.ic_launcher_foreground);
        }
        view.setTag(contactList.get(position).name);
        return view;
    }
}