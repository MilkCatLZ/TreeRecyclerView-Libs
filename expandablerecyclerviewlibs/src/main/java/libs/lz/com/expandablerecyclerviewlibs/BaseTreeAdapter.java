package libs.lz.com.expandablerecyclerviewlibs;

//import android.databinding.ViewDataBinding;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//import static android.databinding.DataBindingUtil.inflate;


/**
 * Created by liuz on 16-1-22.
 */
public abstract class BaseTreeAdapter<Node extends BaseTreeNodeInterface, Holder
        extends BaseTreeAdapter
                .ExpandableHolder> extends RecyclerView
                                                   .Adapter<Holder> {

    /**
     * original list,save all nodes here
     *
     */
    private List<Node> orgTreeList = new ArrayList<>();

    /**
     * The showing nodes
     * When parent node collapsed,the child in {@link #currentOrgTreeList} will remove
     *
     */
    private List<Node> currentOrgTreeList = new ArrayList<>();
    /**
     * The expand/collapse map cache
     */
    private SparseArray<Boolean> collapseMap = new SparseArray<>();
    private OnItemClickListener<Node> listener;
    /**
     * The last clicked no child node
     */
    private Node lastSelectedNode = null;

    public void setListener(OnItemClickListener<Node> listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return currentOrgTreeList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    /**
     * THe original items
     *
     * @param items
     */
    public void setItems(List<Node> items) {
        orgTreeList.clear();
        orgTreeList.addAll(items);
    }

    /**
     * The showing items
     *
     * @param items
     */
    public void setVisibleItems(List<Node> items) {
        currentOrgTreeList.clear();
        currentOrgTreeList.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * setup the Adapter
     * @param items
     */
    public void setupList(List<Node> items) {
        setItems(items);
        setVisibleItems(items);
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
            holder.itemView.setTag(R.id.TreeRecyclerViewLibsTree_node,currentOrgTreeList.get(position));

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
                lastSelectedNode = item;
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
     * @return The item in current showing list on position
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
     * @return The original list
     */
    public List<Node> getOrgTreeList() {
        return Collections.unmodifiableList(orgTreeList);
    }

    /**
     * @return The current showing list
     */
    public List<Node> getCurrentOrgTreeList() {
        return Collections.unmodifiableList(currentOrgTreeList);
    }

    /**
     * @param child
     * @return The parent node,if parent not excise,return the child
     */
    @Keep
    @NonNull
    public Node getParentNode(@NonNull Node child) {
        for (int i = 0; i < getOrgTreeList().size(); i++) {
            Node parent = getOrgTreeList().get(i);
            if (child.getPId() == parent.getId()) {
                return parent;
            }
        }
        return child;
    }

    /**
     * @return The last clicked Node,if nothing clicked yet,return the first node in {@link
     * #orgTreeList}
     */
    @Nullable
    public Node getLastClickedNode() {
        return lastSelectedNode == null ? orgTreeList.get(0) : lastSelectedNode;
    }

    /**
     *
     * @param node
     */
    public void setLastSelectedNode(Node node) {
        this.lastSelectedNode = node;
    }
}
