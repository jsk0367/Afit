package fastcampus.aop.part2.afit;

public class Singleton {
    private String filepath;


    private static Singleton singleton;
    private Singleton(){
    }


    public String getMyPath(){
        return filepath;
    }

    public void setMyPath(String filepath){
        this.filepath = filepath;
    }

    public static Singleton getInstance(){
        if (singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }
}