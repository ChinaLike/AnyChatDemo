package com.tydic.anychatmeeting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.bean.SurfaceConfigBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.model.inf.OnLayoutChangeListener;
import com.tydic.anychatmeeting.util.SharedPreferencesUtil;

import java.util.List;

/**
 * 作者：like on 2018/5/23 16:56
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：
 */
public class LayoutSettingAdapter extends RecyclerView.Adapter<LayoutSettingAdapter.LayoutSettingViewHolder>{

    private Context context;

    private List<SurfaceConfigBean.LayoutConfigListBean> list;

    private OnLayoutChangeListener onLayoutChangeListener;

    public void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
        this.onLayoutChangeListener = onLayoutChangeListener;
    }

    public LayoutSettingAdapter(List<SurfaceConfigBean.LayoutConfigListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public LayoutSettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LayoutSettingAdapter.LayoutSettingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_setting, parent, false));
    }

    @Override
    public void onBindViewHolder(LayoutSettingViewHolder holder, int position) {
        final SurfaceConfigBean.LayoutConfigListBean bean = list.get(position);
        holder.name.setText("☻ "+bean.getName());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLayoutChangeListener != null){
                    SharedPreferencesUtil.putInt(Key.LAYOUT_CONFIG_DEFAULT_KEY,bean.getId());
                    onLayoutChangeListener.layoutChange(bean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
         return list == null ? 0 : list.size();
    }

    public class LayoutSettingViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        LinearLayout parent;

        public LayoutSettingViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_page_setting_text);
            parent = (LinearLayout) itemView.findViewById(R.id.item_parent);
        }
    }
}
