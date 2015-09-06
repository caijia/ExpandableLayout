# ExpandableLayout
一个可以伸缩的布局<br>
======
![](https://github.com/caijia/ExpandableLayout/raw/master/screenshots/b.gif)  

ExpandableLayout 里面只可以放一个View(跟ScrollView类似)

app:animDuration="280"  收缩 或者 展开 动画所用的时间，默认是300,单位毫秒<br>
app:isAllowClickCollapsed="true"  点击ExpandableLayout里面的View是否可以收缩 ，默认为false
```Java
<com.caijia.library.ExpandableLayout
        android:id="@+id/expand_layout_parent"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:animDuration="280"
        app:isAllowClickCollapsed="true">

        <TextView
            android:id="@+id/expand_layout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="sans-serif-light"
            android:textColor="#666666"
            android:textSize="16sp" />
            
</com.caijia.library.ExpandableLayout>
```
你可以使用下面方法来调用操作
```Java
ExpandableLayout layout = (ExpandableLayout)findViewById(R.id.expand_layout_parent);
layout.toggle();
```

在ListView中
---
例子中的代码
```Java
public class MyListAdapter extends ArrayAdapter<String> {

    SparseArray<Boolean> mCollapsedStatus;

    public MyListAdapter(Context context, String[] objects) {
        super(context, 0, objects);
        mCollapsedStatus = new SparseArray<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Button toggle = ViewHolder.get(convertView, R.id.toggle);
        final ExpandableLayout layout = ViewHolder.get(convertView, R.id.expand_layout_parent);
        layout.setCollapsed(position, mCollapsedStatus);

        TextView textView = ViewHolder.get(convertView, R.id.expand_layout);
        textView.setText(getItem(position));

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.toggle();
            }
        });

        return convertView;
    }
}
```

1.定义一个
```Java
SparseArray<Boolean> mCollapsedStatus
```
2.在getView中
```Java
layout.setCollapsed(position, mCollapsedStatus);
```
3.操作ExpandableLayout
```Java
layout.toggle();
```



  
  
