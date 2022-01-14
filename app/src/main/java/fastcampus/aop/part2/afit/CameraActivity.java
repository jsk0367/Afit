package fastcampus.aop.part2.afit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import soup.neumorphism.NeumorphCardView;


public class CameraActivity extends AppCompatActivity implements CallbackInterface {
    private TextureView mCameraTextureView;
    private Preview mPreview;
    public ImageButton mCameraCaptureButton;
    private Button mPickPic;
    private TextView mTextWait;
    private NeumorphCardView home3;

    private static final String TAG = "MAIN_ACTIVITY";

    static final int REQUEST_CAMERA = 1;
    static final int REQUEST_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


//        mPickPic = (Button) findViewById(R.id.pickPic);
        mTextWait = (TextView) findViewById(R.id.TextWait);
        mCameraCaptureButton = (ImageButton) findViewById(R.id.capture);
        home3 = (NeumorphCardView) findViewById(R.id.home3);
        mCameraTextureView = (TextureView) findViewById(R.id.cameraTextureView);
        mPreview = new Preview(this, mCameraTextureView, mCameraCaptureButton, mPickPic, mTextWait, home3);


        home3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        MenuActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent);
            }
        });



        mTextWait.setVisibility(View.INVISIBLE);
        mPreview.setOnCallbackListener(this);

        int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStorage == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CameraActivity.REQUEST_STORAGE);
        }

//        startActivity(new Intent(this, PopupActivity.class));

        CustomDialog dialog = new CustomDialog(this);
        dialog.callDialog();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult()");
        switch (requestCode) {
            case REQUEST_CAMERA:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.CAMERA)) {
                        Log.d(TAG, "CAMERA");
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            mPreview.openCamera();
                        } else {
                            Toast.makeText(this, "Should have camera permission to run", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
                break;
            case REQUEST_STORAGE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "REQUEST_STORAGE");
                            mPreview.openCamera();
                        } else {
                            Toast.makeText(this, "Should have storage permission to run", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.onPause();
    }

    @Override
    public void onSave(File filePath) {
        Log.d(TAG, "onSave()");
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(filePath));
        sendBroadcast(intent);
    }
}
