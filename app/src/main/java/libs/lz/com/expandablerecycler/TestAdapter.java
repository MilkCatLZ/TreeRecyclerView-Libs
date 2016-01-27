package libs.lz.com.expandablerecycler;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import libs.lz.com.expandablerecyclerviewlibs.BaseTreeAdapter;

/**
 * Created by liuz on 16-1-25.
 */
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
        if (isCollapsed(getItem(position))) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_black_18dp, 0,
                                                             0, 0);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove_black_18dp, 0,
                                                             0, 0);
        }
    }

    public class Holder extends BaseTreeAdapter<TestNode, TestAdapter.Holder>.ExpandableHolder {

        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
