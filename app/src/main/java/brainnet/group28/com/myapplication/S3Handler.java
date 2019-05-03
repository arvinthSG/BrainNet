package brainnet.group28.com.myapplication;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import android.content.Context;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import  com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.*;

import java.io.File;

public class S3Handler {
    public void uploadFile(String path_to_file, String fileName, Context context){
        //role-name : Cognito_BrainNetAuth_Role and Cognito_BrainNetUnauth_Role
        File FILE_TO_UPLOAD = new File(path_to_file);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-1:c290e010-ebaa-4037-9671-96000abd4bb3", // Identity pool ID
                Regions.US_EAST_1 // Region
        );

        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);

        TransferUtility transferUtility = new TransferUtility(s3Client,context.getApplicationContext());
        TransferObserver observer = transferUtility.upload(
                "mceeg2",           // The S3 bucket to upload to
                fileName,          // The key for the uploaded object
                FILE_TO_UPLOAD       // The location of the file to be uploaded
        );
    }
}
