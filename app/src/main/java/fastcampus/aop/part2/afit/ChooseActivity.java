package fastcampus.aop.part2.afit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.ExifInterface;
import android.net.Uri;
import java.io.File;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//import androidx.databinding.tool.util.FileUtil;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;

import org.json.JSONException;
import org.json.JSONObject;


public class ChooseActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_GALLERY = 200;
    private long downloadId;







    ImageView imageView;
    TextView file_name;
    String file_path = null;
    Button upload;
    ProgressBar progressBar;
    Button retry;
    ImageView imageView2;

    LottieAnimationView analyzing;
    TextView analyzingtext;

    String resume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        //catching download complete events from android download manager which broadcast message
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));






//
//        Singleton singleton = Singleton.getInstance();
//        String filepath = singleton.getMyPath();
//
//        final File file = new File(filepath);
//
//
//        Uri imageUri = Uri.fromFile(file);


//        InputStream inputStream = getContentResolver().openInputStream(imageUri);
//        InputStream inputStream = new FileInputStream(file);
//
//
//        galleryImage = BitmapFactory.decodeStream(inputStream);
//
//        runPose(galleryImage);


//        try {
//            Singleton singleton = Singleton.getInstance();
//            String filepath = singleton.getMyPath();
//
//
//            Uri imageUri = Uri.parse(filepath);
//            InputStream inputStream = getContentResolver().openInputStream(imageUri);
//            galleryImage = BitmapFactory.decodeStream(inputStream);
//            runPose(galleryImage);
////                    progressBar.setVisibility(View.VISIBLE);
//        } catch (Exception e) {
//            Toast.makeText(ChooseActivity.this, "이미지를 검색하지 못했습니다", Toast.LENGTH_SHORT).show();
//
//        }



//        Button upload_file=findViewById(R.id.upload_file);
//        upload_file.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //check permission greater than equal to marshmeellow we used run time permission
//                if(Build.VERSION.SDK_INT>=23){
//                    if(checkPermission()){
//                        filePicker();
//                    }
//                    else{
//                        requestPermission();
//                    }
//                }
//                else{
//                    filePicker();
//                }
//            }
//        });




        progressBar = findViewById(R.id.progress);
        upload = findViewById(R.id.upload);
        file_name = findViewById(R.id.filename);
        imageView = findViewById(R.id.imageView);
        retry = findViewById(R.id.retry);
        analyzing = findViewById(R.id.analyzingimage);
        analyzingtext = findViewById(R.id.analyzingtext);

        analyzing.setVisibility(View.INVISIBLE);
        analyzingtext.setVisibility(View.INVISIBLE);



        Singleton singleton = Singleton.getInstance();
        String filepath = singleton.getMyPath();


//        File files = new File(filepath);



        try {



            Bitmap bitImg = pathToBitmap(filepath);
            runPose(bitImg);

        } catch (Exception e) {
            e.printStackTrace();        }


        try {



            Uri uri = Uri.parse(filepath);
            imageView.setImageURI(uri);


        } catch (Exception e) {
            e.printStackTrace();
        }

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadFile();

                analyzing.setVisibility(View.VISIBLE);
                analyzingtext.setVisibility(View.VISIBLE);

                upload.setEnabled(false);
                retry.setEnabled(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                EstimationActivity2.class); // 다음 넘어갈 클래스 지정
                        startActivity(intent);
//내가 실행하고 싶은 코드
                    }
                }, 7000);
            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Singleton singleton = Singleton.getInstance();
                String filepath = singleton.getMyPath();
                File file = new File(filepath);
                file.delete();

                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        CameraActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent);


            }
        });






    }

    public Bitmap pathToBitmap(String path) {
        Bitmap bitmap=null;
        try {
            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap ;
    }

    public static Bitmap loadBackgroundBitmap(Context context,

                                              String imgFilePath) throws Exception, OutOfMemoryError {

//        if (!FileUtil.exists(imgFilePath)) {
//
//            throw new FileNotFoundException("background-image file not found : " + imgFilePath);
//
//        }


        // 폰의 화면 사이즈를 구한다.
        Display display = ((WindowManager) context

                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int displayWidth = display.getWidth();

        int displayHeight = display.getHeight();



        // 읽어들일 이미지의 사이즈를 구한다.

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inPreferredConfig = Bitmap.Config.RGB_565;

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imgFilePath, options);


        // 화면 사이즈에 가장 근접하는 이미지의 리스케일 사이즈를 구한다.
        // 리스케일의 사이즈는 짝수로 지정한다. (이미지 손실을 최소화하기 위함.)

        float widthScale = options.outWidth / displayWidth;

        float heightScale = options.outHeight / displayHeight;

        float scale = widthScale > heightScale ? widthScale : heightScale;



        if(scale >= 8) {

            options.inSampleSize = 8;

        } else if(scale >= 6) {

            options.inSampleSize = 6;

        } else if(scale >= 4) {

            options.inSampleSize = 4;

        } else if(scale >= 2) {

            options.inSampleSize = 2;

        } else {

            options.inSampleSize = 1;

        }

        options.inJustDecodeBounds = false;



        return BitmapFactory.decodeFile(imgFilePath, options);

    }





    private void UploadFile() {
        Singleton singleton = Singleton.getInstance();
        String filepath = singleton.getMyPath();

        file_path = filepath;

        UploadTask uploadTask = new UploadTask();
        uploadTask.execute(file_path);
    }



    private void filePicker() {

        //.Now Permission Working
        Toast.makeText(ChooseActivity.this, "File Picker Call", Toast.LENGTH_SHORT).show();
        //Let's Pick File
        Intent opengallery = new Intent(Intent.ACTION_PICK);
        opengallery.setType("image/*");
        startActivityForResult(opengallery, REQUEST_GALLERY);
    }

    //now checking if download complete

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId == id) {
                Toast.makeText(ChooseActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ChooseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(ChooseActivity.this, "Please Give Permission to Upload File", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(ChooseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ChooseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

//    Bitmap galleryImage;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ChooseActivity.this, "Permission Successfull", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChooseActivity.this, "Permission Failed", Toast.LENGTH_SHORT).show();
                }
        }
    }


    Bitmap galleryImage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_GALLERY && resultCode== Activity.RESULT_OK){
            String filePath=getRealPathFromUri(data.getData(),ChooseActivity.this);
            Log.d("File Path : "," "+filePath);
            //now we will upload the file
            //lets import okhttp first
            this.file_path=filePath;

            File file=new File(filePath);
//            file_name.setText(file.getName());
            try {



                File files = new File(filePath);

                if(files.exists()==true) {



                    Uri uri = Uri.parse(filePath);
                    imageView.setImageURI(uri);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    // Pose Detect
    AccuratePoseDetectorOptions options =
            new AccuratePoseDetectorOptions.Builder()
                    .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
                    .build();

    PoseDetector poseDetector = PoseDetection.getClient(options);

    Bitmap resizedBitmap;

    private void runPose(Bitmap bitmap) {

        int rotationDegree = 0;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);

        InputImage image = InputImage.fromBitmap(resizedBitmap, rotationDegree);




        poseDetector.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<Pose>() {
                            @Override
                            public void onSuccess(Pose pose) {
                                processPose(pose);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChooseActivity.this, "포즈가 감지되지 않았습니다.", Toast.LENGTH_SHORT).show();

                                upload.setVisibility(View.GONE);




                            }
                        });
    }


    // Pose Detect
    String angleText;

    private void processPose(Pose pose) {
        try {

            // Omuz
            PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
            PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);

            // Kol Dirsekleri
            PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
            PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);

            // El Bileklerimiz
            PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
            PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);

            // Kalça
            PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
            PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);

            // Ayak Dizleri
            PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
            PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);

            // Ayak Bilekleri
            PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
            PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);


            //Omuz
            PointF leftShoulderP = leftShoulder.getPosition();
            float lShoulderX = leftShoulderP.x;
            float lShoulderY = leftShoulderP.y;
            PointF rightSoulderP = rightShoulder.getPosition();
            float rShoulderX = rightSoulderP.x;
            float rShoulderY = rightSoulderP.y;

            //Dirsek
            PointF leftElbowP = leftElbow.getPosition();
            float lElbowX = leftElbowP.x;
            float lElbowY = leftElbowP.y;
            PointF rightElbowP = rightElbow.getPosition();
            float rElbowX = rightElbowP.x;
            float rElbowY = rightElbowP.y;

            // El Bilek
            PointF leftWristP = leftWrist.getPosition();
            float lWristX = leftWristP.x;
            float lWristY = leftWristP.y;
            PointF rightWristP = rightWrist.getPosition();
            float rWristX = rightWristP.x;
            float rWristY = rightWristP.y;

            // Kalça
            PointF leftHipP = leftHip.getPosition();
            float lHipX = leftHipP.x;
            float lHipY = leftHipP.y;
            PointF rightHipP = rightHip.getPosition();
            float rHipX = rightHipP.x;
            float rHipY = rightHipP.y;

            //Diz
            PointF leftKneeP = leftKnee.getPosition();
            float lKneeX = leftKneeP.x;
            float lKneeY = leftKneeP.y;
            PointF rightKneeP = rightKnee.getPosition();
            float rKneeX = rightKneeP.x;
            float rKneeY = rightKneeP.y;

            // Ayak Bileği
            PointF leftAnkleP = leftAnkle.getPosition();
            float lAnkleX = leftAnkleP.x;
            float lAnkleY = leftAnkleP.y;
            PointF rightAnkleP = rightAnkle.getPosition();
            float rAnkleX = rightAnkleP.x;
            float rAnkleY = rightAnkleP.y;

//                // Angle Text
//                double leftArmAngle = getAngle(leftShoulder, leftElbow,leftWrist);
//                String leftArmAngleText = String.format("%.2f", leftArmAngle);
//
//                double rightArmAngle = getAngle(rightShoulder, rightElbow,rightWrist);
//                String rightArmAngleText = String.format("%.2f", rightArmAngle);
//
//                double leftLegAngle = getAngle(leftHip, leftKnee, leftAnkle);
//                String leftLegAngleText = String.format("%.2f", leftLegAngle);
//
//                double rightLegAngle = getAngle(rightHip, rightKnee, rightAnkle);
//                String rightLegAngleText = String.format("%.2f", rightLegAngle);

//            angleText = "Sol Kol Açısı: "+leftArmAngleText+"\n" +
//                    "Sağ Kol Açısı: "+rightArmAngleText + "\n" +
//                    "Sol Ayak Açısı: "+leftLegAngleText + "\n" +
//                    "Sağ Ayak Açısı: "+rightLegAngleText;


            DisplayAll(lShoulderX, lShoulderY, rShoulderX, rShoulderY,
                    lElbowX, lElbowY, rElbowX, rElbowY,
                    lWristX, lWristY, rWristX, rWristY,
                    lHipX, lHipY, rHipX, rHipY,
                    lKneeX, lKneeY, rKneeX, rKneeY,
                    lAnkleX, lAnkleY, rAnkleX, rAnkleY);


        } catch (Exception e) {
            Toast.makeText(ChooseActivity.this, "포즈가 감지되지 않았습니다.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            upload.setVisibility(View.GONE);

        }
    }

    // Pose Draw
    private void DisplayAll(float lShoulderX, float lShoulderY, float rShoulderX, float rShoulderY,
                            float lElbowX, float lElbowY, float rElbowX, float rElbowY,
                            float lWristX, float lWristY, float rWristX, float rWristY,
                            float lHipX, float lHipY, float rHipX, float rHipY,
                            float lKneeX, float lKneeY, float rKneeX, float rKneeY,
                            float lAnkleX, float lAnkleY, float rAnkleX, float rAnkleY) {

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        float strokeWidth = 10.0f;
        paint.setStrokeWidth(strokeWidth);

        Bitmap drawBitmap = Bitmap.createBitmap(resizedBitmap.getWidth(), resizedBitmap.getHeight(), resizedBitmap.getConfig());

        Canvas canvas = new Canvas(drawBitmap);

        canvas.drawBitmap(resizedBitmap, 0f, 0f, null);

//        Toast.makeText(ChooseActivity.this,"123"+drawBitmap.toString(), Toast.LENGTH_SHORT);

        // Sol Omuzdan Sağ Omuza
        canvas.drawLine(lShoulderX, lShoulderY, rShoulderX, rShoulderY, paint);

        // Sağ Omuzdan Sağ Dirseğe
        canvas.drawLine(rShoulderX, rShoulderY, rElbowX, rElbowY, paint);

        //Sağ Dirsekten Sağ El Bileğine
        canvas.drawLine(rElbowX, rElbowY, rWristX, rWristY, paint);

        // Sol Omuzdan Sol Dirseğe
        canvas.drawLine(lShoulderX, lShoulderY, lElbowX, lElbowY, paint);

        // Sol Dirsekten Sol El Bileğine
        canvas.drawLine(lElbowX, lElbowY, lWristX, lWristY, paint);

        //Sağ Omuzdan Sağ Kalçaya
        canvas.drawLine(rShoulderX, rShoulderY, rHipX, rHipY, paint);

        // Sol Omuzdan Sol Kalçaya
        canvas.drawLine(lShoulderX, lShoulderY, lHipX, lHipY, paint);

        // Kalça (Bel)
        canvas.drawLine(lHipX, lHipY, rHipX, rHipY, paint);

        // Sağ Kalçadan Sağ Ayak Dizine
        canvas.drawLine(rHipX, rHipY, rKneeX, rKneeY, paint);

        // Sol Kalçadan Sol Ayak Dizine
        canvas.drawLine(lHipX, lHipY, lKneeX, lKneeY, paint);

        // Sağ Ayak Dizinden Sağ Ayak Bileğine
        canvas.drawLine(rKneeX, rKneeY, rAnkleX, rAnkleY, paint);

        // Sol Ayak Dizinden Sol Ayak Bileğine
        canvas.drawLine(lKneeX, lKneeY, lAnkleX, lAnkleY, paint);

        // MainActivity to MainActivity2
//        Intent intent = new Intent(ChooseActivity.this, TestEstimation.class);
//        intent.putExtra("Text", angleText);

        try {
            Bitmap rotatedBitmap = getRotatedBitmap(drawBitmap, 90);

            Singleton2 singleton2 = Singleton2.getInstance();
            singleton2.setMyImage(rotatedBitmap);


        } catch (Exception e) {
            e.printStackTrace();
        }






//        startActivity(intent);

    }

    public Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) throws Exception {
        if(bitmap == null) return null;
        if (degrees == 0) return bitmap;

        Matrix m = new Matrix();
        m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }

    public int getOrientationOfImage(String filepath) {
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

        if (orientation != -1) {
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        }

        return 0;
    }





    public String getRealPathFromUri(Uri uri, Activity activity) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(id);
        }
    }


    public class UploadTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            if (s.equalsIgnoreCase("true")) {
//                Toast.makeText(ChooseActivity.this, "File uploaded", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChooseActivity.this, "Failed Upload", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            if (uploadFile(strings[0])) {
                return "true";
            } else {
                return "failed";
            }
        }

        private boolean uploadFile(String path) {
            File file = new File(path);
            try {
                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("files", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                        .addFormDataPart("some_key", "some_value")
                        .addFormDataPart("submit", "submit")
                        .build();

                Request request = new Request.Builder()
                        .url("http://3.37.56.9/httpTest/")
                        .post(requestBody)
                        .build();

//                .url("http://3.37.56.9/httpTest/index/")
//                .url("http://13.209.216.147:8000/home/")



                OkHttpClient client = new OkHttpClient.Builder()
                        .readTimeout(30, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.MINUTES)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build();
                ;





                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }


                    @Override
                    public void onResponse(Call call, Response response) throws IOException {



//                        String resume = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
//                            String resume = jsonObject.getString("feedback");

                            Singleton2 singleton2 = Singleton2.getInstance();
                            singleton2.setMyJson(jsonObject);

                        }catch (IOException | JSONException e){

                        }



//                        Singleton2 singleton2 = Singleton2.getInstance();
//                        singleton2.setMyText(feedback);



//                        JSONObject resume = new JSONObject(resume);


                    }
                });
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
