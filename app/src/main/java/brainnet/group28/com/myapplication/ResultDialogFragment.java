package brainnet.group28.com.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import brainnet.group28.com.myapplication.Common.Constants;

public class ResultDialogFragment extends DialogFragment {

    //UI
    private TextView tvUser;
    private TextView tvClassifier;
    private TextView tvServer;
    private TextView tvExecutionTimr;
    private TextView tvLatency;

    private static final String LOG_TAG = "Result_Dialog";

    public static ResultDialogFragment newInstance(Bundle bundle) {
        ResultDialogFragment fragment = new ResultDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_result_dialog, container, false);
        tvUser = rootView.findViewById(R.id.tv_user);
        tvClassifier = rootView.findViewById(R.id.tv_lassifier);
        tvServer = rootView.findViewById(R.id.tv_server);
        tvExecutionTimr = rootView.findViewById(R.id.tv_execution_time);
        tvLatency = rootView.findViewById(R.id.tv_latency);
        rootView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        tvUser.setText("User          : " + bundle.getString(Constants.USER));
        tvClassifier.setText("Classifier   : " + bundle.getString(Constants.CLASSIFIER));
        tvServer.setText("Server         : " + bundle.getString(Constants.SERVER));
        tvExecutionTimr.setText("Execution time: " + bundle.getInt(Constants.EXECUTION_TIME));
        tvLatency.setText("Latency       : " + bundle.getInt(Constants.LATENCY));

    }


}
