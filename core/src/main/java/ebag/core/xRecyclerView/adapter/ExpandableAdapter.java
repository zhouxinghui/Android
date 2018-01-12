package ebag.core.xRecyclerView.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ebag.core.xRecyclerView.XRecyclerView;

/**
 * Group 不能为NULL
 * Created by unicho on 2018/1/9.
 */

public abstract class ExpandableAdapter<Group extends ExpandableAdapter.IExpandable<Child>, Child> extends RecyclerView.Adapter<ExpandableViewHolder>
        implements XRecyclerView.OnHeaderSizeChangedListener, View.OnClickListener{
    protected static final int DEFAULT_GROUP_VIEW_TYPE = -1;
    protected static final int DEFAULT_VIEW_TYPE = -2;
    protected static final int VIEW_NOT_FIND = -404;

    private int headerSize = 0;
    private boolean allItemExpanded = true;

    private SparseIntArray layouts = new SparseIntArray();

    private List<Group> mData = new ArrayList<>();

    public ExpandableAdapter(){}

    public ExpandableAdapter(@LayoutRes int groupLayoutResId, @LayoutRes int childLayoutResId){
        addItemViewType(groupLayoutResId,childLayoutResId);
    }

    @Override
    public ExpandableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ExpandableViewHolder viewHolder = new ExpandableViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false));
        viewHolder.setAdapter(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExpandableViewHolder holder, int position) {
        int groupPosition = getGroupPosition(position);

        if(checkForGroupPosition(position)){
            holder.getConvertView().setTag(groupPosition);
            holder.getConvertView().setOnClickListener(this);
            fillGroupData(holder,groupPosition,getGroup(groupPosition));
        }else{
            int childPosition = getChildPosition(position);
            holder.getConvertView().setOnClickListener(holder);
            fillChildData(holder,groupPosition,childPosition,getChild(groupPosition,childPosition));
        }
    }

    @Override
    public void onClick(View v) {
        int groupPosition = (int) v.getTag();
        if(getGroup(groupPosition) == null)
            return;

        if(getGroup(groupPosition).isExpanded()){
            closeGroup(groupPosition);
        }else{
            expandGroup(groupPosition);
        }
    }


    protected abstract void fillGroupData(@NonNull ExpandableViewHolder setter, int groupPosition, Group group);

    protected abstract void fillChildData(@NonNull ExpandableViewHolder setter, int groupPosition, int childPosition, Child child);

    protected void addItemViewType(@LayoutRes int groupLayoutResId, @LayoutRes int childLayoutResId){
        addDefGroupItemType(groupLayoutResId);
        addDefChildItemType(childLayoutResId);
    }

    protected void addDefGroupItemType(@LayoutRes int layoutResId){
        addItemType(DEFAULT_GROUP_VIEW_TYPE,layoutResId);
    }

    protected void addDefChildItemType(@LayoutRes int layoutResId){
        addItemType(DEFAULT_VIEW_TYPE,layoutResId);
    }

    @Override
    public int getItemViewType(int position) {
        if(checkForGroupPosition(position))
            return getGroupItemViewType(position);
        else
            return getChildItemViewType(position);
    }

    public int getGroupItemViewType(int position){
        return DEFAULT_GROUP_VIEW_TYPE;
    }

    public int getChildItemViewType(int position){
        return DEFAULT_VIEW_TYPE;
    }

    /**
     * 多布局时使用，建议在getItemViewType 方法中做
     * @param type
     * @param layoutResId
     */
    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if(layouts.get(type) == 0)
            layouts.put(type, layoutResId);
    }

    /**
     * 当没有多布局设置时，默认使用
     * @param viewType
     * @return
     */
    private int getLayoutId(int viewType) {
        return layouts.get(viewType,VIEW_NOT_FIND);
    }

    public boolean isAllItemExpanded() {
        return allItemExpanded;
    }

    public void setAllItemExpanded(boolean allItemExpanded) {
        this.allItemExpanded = allItemExpanded;
        for(Group group : mData)
            group.setExpanded(allItemExpanded);
        notifyDataSetChanged();
    }

    public void expandGroup(int groupPosition){
        if(getGroup(groupPosition) == null)
            return;
        if(!getGroup(groupPosition).isExpanded()){
            getGroup(groupPosition).setExpanded(true);
            int pos = getPositionGroup(groupPosition);
            notifyItemChanged(pos);
            notifyItemRangeInserted(pos + 1 + headerSize, getChildrenCount(groupPosition,true));
        }
    }

    public void closeGroup(int groupPosition){
        if(getGroup(groupPosition) == null)
            return;
        if (getGroup(groupPosition).isExpanded()){
            getGroup(groupPosition).setExpanded(false);
            int pos = getPositionGroup(groupPosition);
            notifyItemChanged(pos);
            notifyItemRangeRemoved(pos + 1 + headerSize, getChildrenCount(groupPosition, true));
        }
    }

    /**
     * 获取分组数量
     * @return
     */
    public int getGroupCount() {
        return mData.size();
    }

    /**
     *
     * @param groupPosition
     * @param isDataResource 获取数据源的 的 长度 或者是 显示在Item中的长度
     * @return
     */
    public int getChildrenCount(int groupPosition, boolean isDataResource) {
        if(groupPosition < getGroupCount()
                && getGroup(groupPosition) != null
                && getChildList(groupPosition) != null
                ){

            if(isDataResource || getGroup(groupPosition).isExpanded())
                return mData.get(groupPosition).getSubItems().size();
        }

        return 0;
    }

    @Override
    public int getItemCount() {
        int itemCount = getGroupCount();
        for(int i = 0; i < getGroupCount(); i++){
            itemCount += getChildrenCount(i,false);
        }
        return itemCount;
    }

    public Group getGroup(int groupPosition){
        return mData.get(groupPosition);
    }

    public Child getChild(int groupPosition, int childPosition){
        return mData.get(groupPosition).getSubItems().get(childPosition);
    }

    public List<Child> getChildList(int groupPosition){
        return mData.get(groupPosition).getSubItems();
    }

    @Override
    public void setHeaderSize(int size) {
        this.headerSize = size;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    private boolean checkForGroupPosition(int position){
        int count = -1;
        for(int i = 0; i < getGroupCount(); i++){
            count++;
            if(count == position)
                return true;
            count += getChildrenCount(i,false);
        }
        return false;
    }

    /**
     * 获取 某个Group 所在这个列表的 位置
     * @param groupPosition 返回 -1 表示没找到
     * @return
     */
    private int getPositionGroup(int groupPosition){
        int count = -1;
        for(int i = 0; i <= groupPosition; i++){
            count++;
            if(i != groupPosition)
                count += getChildrenCount(i,false);
        }
        return count;
    }

    /**
     * 根据 所在分组的位置 和 其在分组中位置，获取所在列表的位置
     * @param childPosition  返回 -1 表示没找到
     * @return
     */
    private int getPositionChild(int groupPosition, int childPosition){
        int count = -1;
        for(int i = 0; i <= groupPosition && i < getGroupCount(); i++){
            count++;
            if(i != groupPosition)
                count += getChildrenCount(i,false);
            else{
                //如果大于 代表 这组数据收起来了 或者 没有找到
                if(childPosition < getChildrenCount(groupPosition,false))
                    count += childPosition;
                else
                    return -1;
            }
        }
        return count;
    }

    /**
     * 根据所在条目的位置，获取这个条目所在的组的位置
     * @param position 返回 -1 表示没找到
     * @return
     */
    private int getGroupPosition(int position){
        int count = -1;
        for(int i = 0; i < getGroupCount(); i++){
            count++;
            int lastGroupPosition = count;
            count += getChildrenCount(i, false);
            if(lastGroupPosition <= position && position <= count)
                return i;
        }
        return -1;
    }

    /**
     * 根据所在条目的位置，获取这个条目所在Item列表中的位置
     * @param position 返回 -1 表示没找到
     * @return
     */
    private int getChildPosition(int position){
        int count = -1;
        for(int i = 0; i < getGroupCount(); i++){
            count++;
            int lastGroupPosition = count;
            count += getChildrenCount(i,false);
            if(lastGroupPosition < position && position <= count)
                return position - lastGroupPosition -1;//position 从0开始
        }
        return -1;
    }

    /**
     * 从 startPosition 到 endPosition 组 共有多少个元素
     * @return
     */
    private int getGroupsCount(int startPosition, int endPosition){
        int count = 0;
        for(int i = startPosition; i < getGroupCount()  && i <= endPosition; i++){
            count++;
            count += getChildrenCount(i,true);
        }
        return count;
    }

    private int getGroupsCount(int endPosition){
        return getGroupsCount(0,endPosition);
    }

    /**
     * 更新数据源
     * @param data
     */
    public void setData(List<Group> data) {
        if(data != null) {
            this.mData = data;
        } else {
            this.mData.clear();
        }
        this.notifyDataSetChanged();
    }

    /**
     * 清除数据
     */
    public void clear() {
        this.mData.clear();
        this.notifyDataSetChanged();
    }

    public void addGroup(@Nullable Group group){
        this.mData.add(group);
        notifyItemRangeInserted(getPositionGroup(mData.size() - 1) + headerSize, getGroupSize(group));
    }

    public void addGroup(int groupIndex,@Nullable Group group){
        this.mData.add(groupIndex, group);
        notifyItemRangeInserted(getPositionGroup(groupIndex) + headerSize, getGroupSize(group));
    }

    public void addGroups(List<Group> groups){
        if(groups != null){
            this.mData.addAll(groups);
            notifyItemRangeInserted(getPositionGroup(mData.size() - 1) + headerSize, getGroupsSize(groups));
        }
    }

    public void addGroups(int groupIndex, List<Group> groups){
        if(groups != null){
            this.mData.addAll(groupIndex, groups);
            notifyItemRangeInserted(getPositionGroup(groupIndex) + headerSize, getGroupsSize(groups));
        }
    }

    public void removeGroup(int groupIndex){
        Group group = getGroup(groupIndex);
        this.mData.remove(groupIndex);
        notifyItemRangeRemoved(getPositionGroup(groupIndex) + headerSize, getGroupSize(group));
    }

    public void removeGroups(List<Group> groups){
        if(groups != null){
            this.mData.removeAll(groups);
            notifyDataSetChanged();
        }
    }

    public void removeGroup(Group group){
        int groupIndex = this.mData.indexOf(group);
        if(groupIndex != -1){
            this.mData.remove(group);
            notifyItemRangeRemoved(getPositionGroup(groupIndex) + headerSize, getGroupSize(group));
        }
    }

    public void addChild(int groupIndex,@Nullable Child child){
        if(groupIndex < getGroupCount() && getGroup(groupIndex) == null){
            getGroup(groupIndex).getSubItems().add(child);
            if(getGroup(groupIndex).isExpanded())
                notifyItemInserted(getGroupsCount(groupIndex));
        }else{
            throw new IllegalArgumentException("这个位置不能添加  子布局");
        }
    }


    public int getGroupsSize(List<Group> groups){
        int count = 0;
        if(groups != null){
            for(Group group : groups){
                count += getGroupSize(group);
            }
        }
        return count;
    }

    /**
     * 分组的长度 包括 group
     * @param group
     * @return
     */
    public int getGroupSize(@Nullable Group group){
        return 1 + getGroupChildSize(group);
    }

    /**
     * 分组的长度 不包括 group
     * @param group
     * @return
     */
    public int getGroupChildSize(@Nullable Group group){
        if(group == null)
            return 0;
        return group.isExpanded() ? (group.getSubItems() == null ? 0 : group.getSubItems().size()) : 0;
    }


    public interface IExpandable<Child> {

        boolean isExpanded();

        void setExpanded(boolean expanded);

        List<Child> getSubItems();
    }
}
