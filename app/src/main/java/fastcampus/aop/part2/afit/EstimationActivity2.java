package fastcampus.aop.part2.afit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;

import java.io.File;
import java.io.FileInputStream;

import android.text.method.ScrollingMovementMethod;

public class EstimationActivity2 extends AppCompatActivity {

            Paint paint = new Paint();
    Paint bodypaint = new Paint();
    Paint armpaint = new Paint();
    Paint shoulderpaint = new Paint();
    Paint kneepaint1 = new Paint();
    Paint sitpaint = new Paint();
    Paint kneepaint2 = new Paint();
    Paint hippaint = new Paint();
            String result;
            TextView resultText;

            String checkResult;
            ImageView resultView;
            Button resultHome;
            Button retry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimation2);
//        String result = singleton2.getMyJson().toString();
        resultHome = findViewById(R.id.Home);
        resultText = findViewById(R.id.textView);
        retry = findViewById(R.id.retry2);
        resultView = findViewById(R.id.imageView3);

        resultText.setMovementMethod(new ScrollingMovementMethod());

        Singleton2 singleton2 = Singleton2.getInstance();


        try{
            Singleton2 singleton22 = Singleton2.getInstance();
            String result = singleton22.getMyJson().toString();

            shoulderpaint.setColor(Color.GREEN);

            if(result.contains("body_angle")){
                kneepaint1.setColor(Color.RED);
                bodypaint.setColor(Color.RED);
            }
            else{
                bodypaint.setColor(Color.GREEN);
            }

            if(result.contains("arm_angle")){
                armpaint.setColor(Color.RED);
            }
            else{
                armpaint.setColor(Color.GREEN);
            }

            if(result.contains("knee_angle")){
                kneepaint1.setColor(Color.RED);
                kneepaint2.setColor(Color.RED);
            }
            else{
                kneepaint2.setColor(Color.GREEN);
            }


            if(result.contains("sit_angle")){
                hippaint.setColor(Color.RED);
                kneepaint1.setColor(Color.RED);
            }
            else{
                hippaint.setColor(Color.GREEN);
            }

            if(!result.contains("sit_angle") & !result.contains("knee_angle") & !result.contains("body_angle")){
                kneepaint1.setColor(Color.GREEN);
            }
        } catch (Exception e) {

        }






        try{
            String result2 = singleton2.getMyJson().toString()
                    .replace(",", "\n")
                    .replace("\"feedback\":", "")
                    .replace("\"arm_angle\":", "")
                    .replace("\"knee_angle\":", "")
                    .replace("\"sit_angle\":", "")
                    .replace("\"body_angle\":", "")
                    .replace("{", "")
                    .replace("}", "")
                    .replace("\"", "")
                    .replace(".", ".\n")
                    .replace("!", "!\n");

            resultText.setText(result2);
        } catch (Exception e) {
            resultText.setText("분석결과를 받지 못했습니다."+ "\n" +" 다시 시도해주세요.");
        }





        resultHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        MenuActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent);
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        CameraActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent);
            }
        });

        Singleton singleton = Singleton.getInstance();
        String filepath = singleton.getMyPath();
        Bitmap bitImg = pathToBitmap(filepath);
        paint.setColor(Color.RED);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runPose(bitImg);
            }
        }, 800);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resultView.setImageBitmap(singleton2.getMyImage());
            }
        }, 1200);
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
                                Toast.makeText(EstimationActivity2.this, "포즈가 감지되지 않았습니다.", Toast.LENGTH_SHORT).show();

//                                upload.setVisibility(View.GONE);




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
            Toast.makeText(EstimationActivity2.this, "포즈가 감지되지 않았습니다.", Toast.LENGTH_SHORT).show();

        }
    }

    // Pose Draw
    private void DisplayAll(float lShoulderX, float lShoulderY, float rShoulderX, float rShoulderY,
                            float lElbowX, float lElbowY, float rElbowX, float rElbowY,
                            float lWristX, float lWristY, float rWristX, float rWristY,
                            float lHipX, float lHipY, float rHipX, float rHipY,
                            float lKneeX, float lKneeY, float rKneeX, float rKneeY,
                            float lAnkleX, float lAnkleY, float rAnkleX, float rAnkleY) {

//        Paint paint = new Paint();
//        paint.setColor(Color.GREEN);
        float strokeWidth = 20.0f;
//        paint.setStrokeWidth(strokeWidth);





        kneepaint1.setStrokeWidth(strokeWidth);
        kneepaint2.setStrokeWidth(strokeWidth);
        bodypaint.setStrokeWidth(strokeWidth);
        armpaint.setStrokeWidth(strokeWidth);
        shoulderpaint.setStrokeWidth(strokeWidth);
        hippaint.setStrokeWidth(strokeWidth);





        Bitmap drawBitmap = Bitmap.createBitmap(resizedBitmap.getWidth(), resizedBitmap.getHeight(), resizedBitmap.getConfig());

        Canvas canvas = new Canvas(drawBitmap);

        canvas.drawBitmap(resizedBitmap, 0f, 0f, null);

//        Toast.makeText(ChooseActivity.this,"123"+drawBitmap.toString(), Toast.LENGTH_SHORT);

        // Sol Omuzdan Sağ Omuza
        canvas.drawLine(lShoulderX, lShoulderY, rShoulderX, rShoulderY,  shoulderpaint);

        // Sağ Omuzdan Sağ Dirseğe
        canvas.drawLine(rShoulderX, rShoulderY, rElbowX, rElbowY, armpaint);

        //Sağ Dirsekten Sağ El Bileğine
        canvas.drawLine(rElbowX, rElbowY, rWristX, rWristY, armpaint);

        // Sol Omuzdan Sol Dirseğe
        canvas.drawLine(lShoulderX, lShoulderY, lElbowX, lElbowY, armpaint);

        // Sol Dirsekten Sol El Bileğine
        canvas.drawLine(lElbowX, lElbowY, lWristX, lWristY, armpaint);

        //Sağ Omuzdan Sağ Kalçaya
        canvas.drawLine(rShoulderX, rShoulderY, rHipX, rHipY, bodypaint);

        // Sol Omuzdan Sol Kalçaya
        canvas.drawLine(lShoulderX, lShoulderY, lHipX, lHipY, bodypaint);

        // Kalça (Bel)
        canvas.drawLine(lHipX, lHipY, rHipX, rHipY, hippaint);

        // Sağ Kalçadan Sağ Ayak Dizine
        canvas.drawLine(rHipX, rHipY, rKneeX, rKneeY, kneepaint1);

        // Sol Kalçadan Sol Ayak Dizine
        canvas.drawLine(lHipX, lHipY, lKneeX, lKneeY, kneepaint1);

        // Sağ Ayak Dizinden Sağ Ayak Bileğine
        canvas.drawLine(rKneeX, rKneeY, rAnkleX, rAnkleY, kneepaint2);

        // Sol Ayak Dizinden Sol Ayak Bileğine
        canvas.drawLine(lKneeX, lKneeY, lAnkleX, lAnkleY, kneepaint2);
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




    }

    public Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) throws Exception {
        if(bitmap == null) return null;
        if (degrees == 0) return bitmap;

        Matrix m = new Matrix();
        m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
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

}