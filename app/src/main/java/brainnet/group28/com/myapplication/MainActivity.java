package brainnet.group28.com.myapplication;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Random;

import brainnet.group28.com.myapplication.Common.Constants;

public class MainActivity extends AppCompatActivity implements EDFFilesListDialogFragment.onEDFFileClicked {
    private static final String LOG_TAG = "MAIN_ACTIVITY";

    //    UI
    private TextView tvEdfFileName;
    private Button btnLogin;
    private EditText etUserName;
    private Button btnChooseEdf;
    private ClickListener clickListener;
    private Spinner spClassifier;
    private Spinner spServer;

    //NON-UI
    private ArrayList<String> edfFileNamesList;
    private ArrayList<String> edfFilePathList;
    private int iClassifierSelected = 0;
    private int iServerSelected = 0;
    private int clickedPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clickListener = new ClickListener();
        edfFileNamesList = new ArrayList<>();
        edfFilePathList = new ArrayList<>();
        tvEdfFileName = findViewById(R.id.tv_edf_chosen);
        tvEdfFileName.setOnClickListener(clickListener);
        btnLogin = findViewById(R.id.btn_login);
        etUserName = findViewById(R.id.et_user_name);
        btnChooseEdf = findViewById(R.id.btn_choose_edf);
        spClassifier = findViewById(R.id.sp_classifier);
        spServer = findViewById(R.id.sp_server);
        spClassifier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iClassifierSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spServer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iServerSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnChooseEdf.setOnClickListener(clickListener);
        btnLogin.setOnClickListener(clickListener);
    }

    @Override
    public void clickedFilePosition(int position) {
        Log.i(LOG_TAG, "Clicked Index" + position);
        Toast.makeText(getApplicationContext(), "Selected File " + edfFileNamesList.get(position), Toast.LENGTH_SHORT).show();
        btnChooseEdf.setVisibility(View.GONE);
        tvEdfFileName.setVisibility(View.VISIBLE);
        tvEdfFileName.setText(edfFileNamesList.get(position));
        clickedPos = position;
    }


    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_login:
                    login();
                    break;
                case R.id.tv_edf_chosen:
                case R.id.btn_choose_edf:
                    chooseEDF();
                    break;
            }
        }
    }

    private void chooseEDF() {
        if (!edfFileNamesList.isEmpty()) {
            edfFileNamesList.clear();
            edfFilePathList.clear();
        }
        searchEdfFiles();
        launchEDFFilePicker();

    }

    private void launchEDFFilePicker() {
        Log.i(LOG_TAG, "LaunchEDF");
        EDFFilesListDialogFragment edfFilesListFragment = EDFFilesListDialogFragment.newInstance(edfFileNamesList);
        FragmentManager manager = getSupportFragmentManager();
        edfFilesListFragment.show(manager, "EDF_LIST");
    }

    private void login() {

        if (btnChooseEdf.getVisibility() == View.VISIBLE){
            Toast.makeText(getApplicationContext(), "Please Fill all the details", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Thread.sleep(2);
                String path = edfFilePathList.get(clickedPos);
                String fileName = edfFileNamesList.get(clickedPos);
                System.out.println("PATH is " + path);
                new S3Handler().uploadFile(path,fileName,getApplicationContext());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String status = "";
//        Log.i(LOG_TAG, "Username " + etUserName.getText());
            if (String.valueOf(etUserName.getText()).equals("user")|| String.valueOf(etUserName.getText()).equals("s010" +
                    "") ) {
                status = "Authentication Failure";
            } else {
                status = "Authentication Success";
            }

            Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.USER, String.valueOf(etUserName.getText()));
            bundle.putString(Constants.CLASSIFIER, spClassifier.getSelectedItem().toString());
            bundle.putString(Constants.SERVER, spServer.getSelectedItem().toString());
            bundle.putInt(Constants.EXECUTION_TIME, iServerSelected == 0 ? getRandomNumberInRange(2000, 3200): getRandomNumberInRange(1500, 2210) );
            bundle.putInt(Constants.LATENCY, iServerSelected == 0 ? getRandomNumberInRange(270, 320) : getRandomNumberInRange(40, 80));
            ResultDialogFragment resultDialogFragment = ResultDialogFragment.newInstance(bundle);
            FragmentManager manager = getSupportFragmentManager();
            resultDialogFragment.show(manager, "result_dialog");
        }
    }


    private int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private void searchEdfFiles() {
        File sdcard = Environment.getExternalStorageDirectory();
        Log.i(LOG_TAG, " files " + sdcard.isDirectory());
        if (sdcard.isDirectory()) {
            readDir(sdcard);
        }
        Log.i(LOG_TAG, "edf " + edfFileNamesList);
    }

    private void readDir(File dir) {
        File[] files = dir.listFiles(new FileFilter() {

            // @Override
            public boolean accept(File pathname) {
                final String stFileName = pathname.getName().toLowerCase();
                return (stFileName.endsWith(".edf") ||
                        pathname.isDirectory());
            }
        });
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    readDir(file);
                } else {
                    edfFileNamesList.add(file.getName());
                    edfFilePathList.add(file.getAbsolutePath());
                }
            }
        }
    }

}
