package com.longshihan.baseadapter.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by longshihan.
 *
 * @time 2018/7/16 0016
 * @des
 * @function
 */

public class GroupInfo<P extends Object,C extends Object> implements Serializable {
    public P parent;
    public List<C> children;

    public GroupInfo() {
        children=new ArrayList<>();
    }

    public boolean hasHeader() {
        if (parent != null) {
            return true;
        }
        return false;
    }

    public int getChildrenCount() {
        if (children != null) {
            return children.size();
        }
        return 0;
    }

    public boolean equalParent(Object parent){
        return this.parent==parent;
    }

    public int count() {
        return hasHeader() ? 1 + getChildrenCount() : getChildrenCount();
    }

}
