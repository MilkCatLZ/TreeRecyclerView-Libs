package libs.lz.com.expandablerecycler;

import libs.lz.com.expandablerecyclerviewlibs.BaseTreeNodeInterface;

/**
 * Created by liuz on 16-1-25.
 */
public class TestNode implements BaseTreeNodeInterface {

    int id;
    int pid;
    String text = "node";
    boolean hasChild;
    private int level;


    public TestNode(int id, int pid, boolean hasChild,int level) {
        this.id = id;
        this.pid = pid;
        this.hasChild = hasChild;
        this.level=level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getPId() {
        return pid;
    }

    @Override
    public boolean hasChild() {
        return hasChild;
    }

    public int getLevel() {
        return level;
    }
}
