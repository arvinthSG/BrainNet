package brainnet.group28.com.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class EDFFilesListDialogFragment extends DialogFragment {

    private static final String LOG_TAG = "EDF_FRAG";

    private ArrayList<String> fileNamesList;
    private onEDFFileClicked listener;

    private ListView lvEDFList;

    public static EDFFilesListDialogFragment newInstance(ArrayList<String> filesName) {

        Log.i(LOG_TAG, "edf frag loading");
        Bundle args = new Bundle();
        EDFFilesListDialogFragment fragment = new EDFFilesListDialogFragment();
        args.putStringArrayList("FILES", filesName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_edf_files, container, false);
        lvEDFList = rootView.findViewById(R.id.lv_edf_files);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (onEDFFileClicked) context;
        } catch (Exception e) {

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fileNamesList = getArguments().getStringArrayList("FILES");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, fileNamesList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }
        };
        lvEDFList.setAdapter(adapter);
        Log.i(LOG_TAG, "edf list " + lvEDFList);
        lvEDFList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.clickedFilePosition(i);
                dismiss();
            }
        });
    }

    public interface onEDFFileClicked {
        void clickedFilePosition(int position);
    }
}
