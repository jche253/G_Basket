package codemeharder.gbasket;

import android.content.Context;

/**
 * Created by Jimmy Chen on 4/11/2016.
 */
public class Triplet {
    Context context;
    String format;
    String content;

    Triplet(Context cont, String pformat, String pcontent) {
        this.context = cont;
        this.format = pformat;
        this.content = pcontent;
    }

    public Context getContext() { return this.context; }
    public void setContext(Context cont) {this.context = cont; }
    public String getformat() { return this.format; }
    public void setformat(String pformat) { this.format = pformat; }
    public String getcontent() {return this.content; }
    public void setcontent(String pcontent) { this.content = pcontent; }

}