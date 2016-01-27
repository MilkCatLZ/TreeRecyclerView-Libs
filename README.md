# ExpandableRecycler 
A simple way to create tree list with RecyclerView  
简单的实现树形结构的展示

![image](https://github.com/MilkCatLZ/TreeRecyclerView-Libs/blob/master/screenshot/DeepinScreenshot20160125162229.png)  

##Webite  
    https://github.com/MilkCatLZ/TreeRecyclerView-Libs

## Integration

```java
dependencies {
　　compile 'com.lz.lib.treerecyclerview:expandablerecyclerviewlibs:1.0.2' 
}
```

## Usage
##### 1、实现BaseTreeNodeInterface并实现方法
```java
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
```
##### 2、继承BaseTreeAdapter和BaseTreeAdapter.ExpandableHolder

```java
   public class TestAdapter extends BaseTreeAdapter<TestNode, TestAdapter.Holder> {

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, null,
                                                                     false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        super.onBindViewHolder(holder, position);
        TextView textView = (TextView) holder.itemView.findViewById(R.id.txt_view);
        textView.setText(getItem(position)
                                 .getText() + "-" + getItem(position).getId());
        textView.setPadding(getItem(position).getLevel() * 30, 0, 0, 0);
    }

    public class Holder extends BaseTreeAdapter<TestNode,TestAdapter.Holder>.ExpandableHolder {

        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
```
##### 3、将解析好的数据设置进adapter
```java
	adapter.setupList(list);
	recyclerView.setAdapter(adapter);
```
##### 4、Done
