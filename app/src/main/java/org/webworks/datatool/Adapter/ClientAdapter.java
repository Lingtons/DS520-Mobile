package org.webworks.datatool.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.webworks.datatool.Model.ClientForm;
import org.webworks.datatool.Model.Contact;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.FacilityRepository;

import java.util.ArrayList;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyHolder> {
    private ArrayList<ClientForm> clientForms;
    private Context context;

    public ClientAdapter(Context _context, ArrayList<ClientForm> _clientForms) {
        clientForms = _clientForms;
        context = _context;
    }

    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_single_layout,parent,false);
        MyHolder holder=new MyHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        ClientForm form = clientForms.get(position);
        holder.name.setText(form.getClientName() +" "+form.getClientLastname());
        holder.code.setText(form.getClientIdentifier());
    }

    @Override
    public int getItemCount() {
        return clientForms.size();
    }

    /*
ADD DATA TO ADAPTER
 */
    public void add(ArrayList<ClientForm> clientForms) {
        clientForms.addAll(clientForms);
        notifyDataSetChanged();
    }

    /*
    CLEAR DATA FROM ADAPTER
     */
    public void clear() {
        clientForms.clear();
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private View relativeLayer;
        private TextView name, code, referredTo, toOrFrom;
        private CheckedTextView uploaded;

        public MyHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            code = itemView.findViewById(R.id.code);
            referredTo = itemView.findViewById(R.id.referred);
            relativeLayer = itemView.findViewById(R.id.lay2);
            toOrFrom = itemView.findViewById(R.id.to_or_from);
            uploaded = itemView.findViewById(R.id.uploaded);
        }
    }
}
