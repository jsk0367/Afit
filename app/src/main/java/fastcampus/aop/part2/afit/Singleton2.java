package fastcampus.aop.part2.afit;

import android.graphics.Bitmap;
import android.graphics.Paint;

import org.json.JSONObject;

public class Singleton2 {
    private static Singleton2 singleton2;

    private Bitmap myImage;
    private String myText;
    private Paint myPaint;
    private JSONObject myJson;



    public Bitmap getMyImage(){
        return myImage;
    }
    public void setMyImage(Bitmap myImage){
        this.myImage = myImage;
    }


    public String getMyText(){
        return myText;
    }
    public void setMyText(String myText){
        this.myText = myText;
    }


    public Paint getMyPaint(){
        return myPaint;
    }
    public void setMyPaint(Paint myPaint){
        this.myPaint = myPaint;
    }


    public JSONObject getMyJson(){
        return myJson;
    }
    public void setMyJson(JSONObject myJson){
        this.myJson = myJson;
    }



    public static Singleton2 getInstance(){
        if (singleton2 == null){
            singleton2 = new Singleton2();
        }
        return singleton2;
    }
}
