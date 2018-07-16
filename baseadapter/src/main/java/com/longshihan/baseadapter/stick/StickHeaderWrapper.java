package com.longshihan.baseadapter.stick;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.longshihan.baseadapter.MultiTypeAdapter;

import java.lang.reflect.Field;
import java.util.Collections;


public abstract   class StickHeaderWrapper implements StickHeaderListener {

    MultiTypeAdapter adapter;
    public StickHeaderWrapper(MultiTypeAdapter adapter) {
        this.adapter=adapter;
    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder viewholder, final int position) {
        adapter.onBindViewHolder(viewholder, position, Collections.emptyList());
    }


    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int position) {
        int itemType = adapter.getItemViewType(position);
        RecyclerView.ViewHolder holder = adapter.onCreateViewHolder(parent, adapter.getItemViewType(position));
        setField(RecyclerView.ViewHolder.class, holder, "mItemViewType", itemType);
        return holder;
    }

    private void setField(Class<?> clazz, Object owner, String fieldName, Object value) {
        Class<?> ownerClass = clazz;
        Field field = null;
        Object obj = new Object();
        try {
            Field[] fields = ownerClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                field = fields[i];
                if (field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    field.set(owner, value);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isHeader(int position) {
        return false;
    }
}
