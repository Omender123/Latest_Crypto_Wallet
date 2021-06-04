package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crypto.croytowallet.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ExpandableListAdapter  extends BaseExpandableListAdapter  {

    private Context context;
    private List<String> _listDataHeader;
    private HashMap<String,String> _listChildData;

    public ExpandableListAdapter(Context context, List<String> _listDataHeader, HashMap<String, String> _listChildData) {
        this.context = context;
        this._listDataHeader = _listDataHeader;
        this._listChildData = _listChildData;


    }

    @Override
    public int getGroupCount() {
        return _listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listChildData.get(
                this._listDataHeader.get(groupPosition));

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(inflater).inflate(R.layout.parent_menu_group, null);
        }

        ImageView img_drop_down =  convertView
                .findViewById(R.id.drop_down_arrow);
        if (isExpanded) {
            img_drop_down.setImageResource(R.drawable.readmore);
        } else {
            img_drop_down.setImageResource(R.drawable.ic_group_16268);
        }

        TextView lblListHeader =  convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);

        if (isExpanded)
            convertView.setPadding(0, 0, 0, 0);
        else
            convertView.setPadding(0, 5, 0, 10);
        return convertView;

//        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition,
                childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(inflater).inflate(R.layout.answers_list, null);
        }

        TextView txtListChild =  convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
