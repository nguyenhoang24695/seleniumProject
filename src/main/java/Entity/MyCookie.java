package Entity;

public class MyCookie {
    private String sid;
    private String ga;
    private String gid;

    public MyCookie() {
    }

    public MyCookie(String sid, String ga, String gid) {
        this.sid = sid;
        this.ga = ga;
        this.gid = gid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getGa() {
        return ga;
    }

    public void setGa(String ga) {
        this.ga = ga;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }
}
