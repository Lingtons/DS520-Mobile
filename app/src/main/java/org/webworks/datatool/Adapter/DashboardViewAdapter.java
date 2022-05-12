//package org.webworks.datatool.Adapter;
//
//import android.content.Context;
//import android.os.Build;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.CheckedTextView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;
//
//import org.webworks.datatool.Model.ClientForm;
//import org.webworks.datatool.Model.DashboardSorter;
//import org.webworks.datatool.Model.Facility;
//import org.webworks.datatool.R;
//import org.webworks.datatool.Repository.FacilityRepository;
//
//import java.util.ArrayList;
//
//public class DashboardViewAdapter extends BaseAdapter implements StickyGridHeadersBaseAdapter {
//
//    private Context context;
//    private ArrayList<ClientForm> forms;
//    private TextView name, code, referredTo, toOrFrom, uploadedTxt;
//    private View relativeLayer;
//    private CheckedTextView uploaded;
//    private int fragmentId;
//    private ArrayList<String> formGroupText;
//    private ArrayList<Integer> formGroups;
//    ArrayList<DashboardSorter> sorters;
//
//
//    public DashboardViewAdapter(Context _context, ArrayList<ClientForm> _forms, int _fragmentId) {
//        this.forms = _forms;
//        context = _context;
//        this.fragmentId = _fragmentId;
//    }
//
//    public DashboardViewAdapter(Context _context, ArrayList<ClientForm> _forms, ArrayList<DashboardSorter> _sorters, int _fragmentId) {
//        this.forms = _forms;
//        context = _context;
//        this.fragmentId = _fragmentId;
//        this.sorters = _sorters;
//    }
//
//    public DashboardViewAdapter(Context _context, ArrayList<ClientForm> _forms,
//                                ArrayList<Integer> _formGroups, ArrayList<String> _formGroupText,
//                                int _fragmentId) {
//        this.forms = _forms;
//        context = _context;
//        this.fragmentId = _fragmentId;
//        this.formGroups = _formGroups;
//        this.formGroupText = _formGroupText;
//    }
//
//    @Override
//    public int getCount() {
//        return forms.size();
//    }
//
//    @Override
//    public ClientForm getItem(int position) {
//        return forms.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        convertView = inflater.inflate(R.layout.grid_single_layout, null);
//     //   init(convertView);
//     //   assign(forms.get(position));
//        return convertView;
//    }
//
////    private void init(View convertView) {
////        name = (TextView)convertView.findViewById(R.id.name);
////        code = (TextView)convertView.findViewById(R.id.code);
////        referredTo = (TextView)convertView.findViewById(R.id.referred);
////        relativeLayer = (LinearLayout)convertView.findViewById(R.id.lay2);
////        toOrFrom = (TextView)convertView.findViewById(R.id.to_or_from);
////        uploaded = (CheckedTextView)convertView.findViewById(R.id.uploaded);
////        uploadedTxt = convertView.findViewById(R.id.uploadedtxt);
////    }
////
////    private void assign(ClientForm form) {
////
////        name.setText(form.getClientName());
////        code.setText(form.getClientIdentifier());
////        FacilityRepository repository = new FacilityRepository(context);
////        if (!(form.getRefferedTo() == 0)){
////            String facilityName = repository.getFacilityName(form.getRefferedTo());
////            if (facilityName != null)
////                referredTo.setText(facilityName);
////        }
////
////        if(form.getUploaded() == 0) {
////            if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)){
////                uploaded.setCheckMarkDrawable(R.drawable.ic_cancel_red);
////            }else{
////                uploadedTxt.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_red));
////                uploadedTxt.setText("Not Uploaded");
////            }
////
////            toOrFrom.setText("Reffered to");
////
////        }else if(form.getClientConfirmed() == 1){
////            uploadedTxt.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_green));
////            uploadedTxt.setText("Completed");
////        }else if(form.getUploaded() == 1 || form.getUploaded() == 2) {
////            if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)){
////                uploaded.setCheckMarkDrawable(R.drawable.ic_done_green);
////            }else{
////               uploadedTxt.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_blue));
////                uploadedTxt.setText("Uploaded");
////            }
////            toOrFrom.setText("Reffered to");
////        }
////        else if (form.getUploaded() == 3) {
////            uploaded.setVisibility(View.GONE);
////            toOrFrom.setText("Received from");
////           // Facility fac = repository.getReferralForm(form.getRefferedFrom());
////           // referredTo.setText(fac.getName());
////        }
////    }
//
//    @Override
//    public int getCountForHeader(int i) {
//        return sorters.get(i).getFormCount();
//        //return formGroups.get(i);
//    }
//
//    @Override
//    public int getNumHeaders() {
//        return sorters.size();
//        //return formGroups.size();
//    }
//
//    @Override
//    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
//        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        view = inflater.inflate(R.layout.grid_header, null);
//        TextView textView = (TextView)view.findViewById(R.id.grid_header_text);
//        //textView.setText(String.valueOf(formGroupText.get(i)));
//        textView.setText(String.valueOf(sorters.get(i).getFormDate()));
//        return view;
//    }
//}
