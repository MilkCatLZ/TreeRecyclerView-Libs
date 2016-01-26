package libs.lz.com.expandablerecyclerviewlibs;

//import android.databinding.ViewDataBinding;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

//import static android.databinding.DataBindingUtil.inflate;


/**
 * Created by liuz on 16-1-22.
 */
public abstract class BaseTreeAdapter<Node extends BaseTreeNodeInterface, Holder
        extends BaseTreeAdapter
                .ExpandableHolder> extends RecyclerView
                                                   .Adapter<Holder> {

    private List<Node> orgTreeList = new ArrayList<>();
    private List<Node> currentOrgTreeList = new ArrayList<>();
    private SparseArray<Boolean> collapseMap = new SparseArray<>();
    private OnItemClickListener<Node> listener;

    public void setListener(OnItemClickListener<Node> listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return currentOrgTreeList.size();
    }

    /**
     * THe original items
     *
     * @param items
     */
    public void setItems(List<Node> items) {
        orgTreeList.addAll(items);

    }

    /**
     * The showing items
     *
     * @param items
     */
    public void setVisibleItems(List<Node> items) {
        currentOrgTreeList.addAll(items);
        notifyDataSetChanged();
    }


    public interface OnItemClickListener<Node> {

        void onItemExpand(Node item, int adapterPosition);

        void onItemCollapsed(Node item, int adapterPosition);

        /**
         * The unexpandable child clicked
         *
         * @param item
         * @param adapterPosition
         */
        void onLastTreeNodeItemClick(Node item, int adapterPosition);

//        void onDataBind(ViewDataBinding binding, Node item);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (currentOrgTreeList != null && currentOrgTreeList.size() > 0) {
            holder.itemView.setTag(currentOrgTreeList.get(position));

        }
    }

    public class ExpandableHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ExpandableHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setClickable(true);
        }

        @Override
        public void onClick(View v) {
            Node item = (Node) v.getTag();
            if (hasChild(item)) {
                if (isCollapsed(item)) {
                    expand(item);

                    if (listener != null) {
                        listener.onItemExpand(item, getAdapterPosition());
                    }

                } else {
                    collapse(item);
                    if (listener != null) {
                        listener.onItemCollapsed(item, getAdapterPosition());
                    }
                }
            } else {

                if (listener != null) {
                    listener.onLastTreeNodeItemClick(item, getAdapterPosition());
                }
            }
            notifyItemChanged(getAdapterPosition());
        }

        /**
         * @param parent
         */
        public void expand(Node parent) {
            List<Node> cacheList = new ArrayList<>();
            for (int i = orgTreeList.indexOf(parent) + 1; i < orgTreeList.size(); i++) {
                Node child = orgTreeList.get(i);
                if (parent.getId() == child.getPId()) {
                    cacheList.add(child);
                }
            }
            int location = currentOrgTreeList.indexOf(parent);
            currentOrgTreeList.addAll(location + 1, cacheList);
            notifyItemRangeInserted(location + 1, cacheList.size());
            setCollapsed(parent, false);
        }

        /**
         * @param node
         */
        public void collapse(Node node) {
            List<Node> cacheList = getChildNode(node);
            if (cacheList.size() > 0) {
                int location = currentOrgTreeList.indexOf(node);
                currentOrgTreeList.removeAll(cacheList);
                notifyItemRangeRemoved(location + 1, cacheList.size());
            }
            setCollapsed(node, true);
        }

        /**
         * @param parent
         * @return
         */
        public List<Node> getChildNode(Node parent) {
            List<Node> cacheList = new ArrayList<>();
            for (int i = currentOrgTreeList.indexOf(parent) + 1; i < currentOrgTreeList.size();
                 i++) {
                Node child = currentOrgTreeList.get(i);
                if (parent.getId() == child.getPId()) {
                    cacheList.add(child);
                    setCollapsed(parent, true);
                    if (hasChild(child))
                        cacheList.addAll(getChildNode(child));
                }
            }
            return cacheList;
        }
    }

    /**
     * @param node
     * @return
     */
    public boolean hasChild(Node node) {
        return node.hasChild();
    }

    /**
     * @param position
     * @return
     */
    public Node getItem(int position) {
        return currentOrgTreeList.get(position);
    }

    /**
     * @param node
     * @param collapsed
     */
    public void setCollapsed(Node node, boolean collapsed) {
        collapseMap.put(node.getId(), collapsed);
    }

    /**
     * @param node
     * @return
     */
    public boolean isCollapsed(Node node) {
        return collapseMap.get(node.getId()) == null ? false : collapseMap.get(node.getId());
    }

    /**
     *
     * @return
     */
    public List<Node> getOrgTreeList() {
        return orgTreeList;
    }

    /**
     *
     * @return
     */
    public List<Node> getCurrentOrgTreeList() {
        return currentOrgTreeList;
    }

    /**
     *
     * @param position
     * @return
     */
    public Node getOrignalItem(int position){
        return currentOrgTreeList.get(position);
    }
}
