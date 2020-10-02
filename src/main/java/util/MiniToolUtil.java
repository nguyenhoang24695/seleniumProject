package util;

public class MiniToolUtil {

    private static MiniToolUtil miniToolUtilInstance;

    public synchronized static MiniToolUtil getInstance() {

        if (miniToolUtilInstance == null) {
            miniToolUtilInstance = new MiniToolUtil();
        }
        return miniToolUtilInstance;
    }

    public void BarProgress(int now, int total) {
        StringBuilder sb = new StringBuilder();

        try {
            if(total == 0){
                total = now;
            }
            int per = (now * 100) / total;
            for (int i = 0; i < per; i++) {
                sb.append("=");
            }
            System.out.print("\r" + sb.toString() + " : " + now + "/" + total);
        } catch (Exception e) {
            System.err.print("Error when create BarProcess");
        }


    }

}
