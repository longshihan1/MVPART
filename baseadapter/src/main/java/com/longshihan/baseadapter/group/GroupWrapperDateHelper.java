package com.longshihan.baseadapter.group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LONGHE001.
 *
 * @time 2018/7/16 0016
 * @des
 * @function
 */

public class GroupWrapperDateHelper {
    public boolean isAnimtor = false;
    // 分组源数据
    public List<GroupInfo> sourceList;
    // 打开的分组列表
    public List<GroupInfo> openGroupList = new ArrayList<>();
    // 展示的数据集合
    public List<Object> adapterList;

    /**
     * 判断是否是分组头部
     *
     * @param position
     * @return
     */
    public boolean isGroupHeader(int position) {
        //越界
        if(adapterList.size()<position){
            return false;
        }
        Object currentObj = adapterList.get(position);
        for (GroupInfo group : sourceList) {
            if (group.equalParent(currentObj)) {
                return true;
            }
        }
        return false;
    }
    // 转换数据
    public void calculateList() {
        if (adapterList == null) {
            adapterList = new ArrayList<>();
        }
        adapterList.clear();
        GroupInfo objGroupStructure;
        boolean isEqual = false;
        lableBreak:
        for (int j = 0; j < sourceList.size(); j++) {
            isEqual = false;
            objGroupStructure = sourceList.get(j);
            for (int i = 0; i < openGroupList.size(); i++) {
                if (objGroupStructure.equalParent(openGroupList.get(i).parent)) {
                    isEqual = true;
                    break;
                }
            }

            if (objGroupStructure.hasHeader()) {
                adapterList.add(objGroupStructure.parent);
            }

            if (isEqual) {
                if (objGroupStructure.getChildrenCount() > 0) {
                    adapterList.addAll(objGroupStructure.children);
                }
            }
        }
    }
    //
    public int getAdapterPositionForGroupHeaderPostition(int groupPosition) {
        GroupInfo groupStructure = sourceList.get(groupPosition);
        int position = -1;
        for (int i = 0; i < adapterList.size(); i++) {
            Object group = adapterList.get(i);
            if (group == groupStructure.parent) {
                position = i;
                break;
            }
        }
        return position;
    }
    void notifyDataForExpand(GroupInfo expandObj,int adapterPostiton) {
        openGroupList.add(expandObj);
        if (expandObj.getChildrenCount() > 0) {
            adapterList.addAll(adapterPostiton + 1, expandObj.children);
        }
    }
    void notifyDataForShrik(GroupInfo shrikObj) {
        openGroupList.remove(shrikObj);
        if (shrikObj.getChildrenCount() > 0) {
            adapterList.removeAll(shrikObj.children);
        }
    }
}
