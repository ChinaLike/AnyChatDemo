package com.tydic.anychatmeeting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tydic.anychatmeeting.R;
import com.tydic.anychatmeeting.bean.DocumentBean;

import java.util.List;

/**
 * 作者：like on 2018/6/13 15:20
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：
 */
public class MeetingMaterialsAdapter extends RecyclerView.Adapter<MeetingMaterialsAdapter.MeetingMaterialsViewHolder>{


    private Context context;

    private List<DocumentBean> mList;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MeetingMaterialsAdapter(Context context, List<DocumentBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public MeetingMaterialsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeetingMaterialsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_materials,null,false));
    }

    @Override
    public void onBindViewHolder(MeetingMaterialsViewHolder holder, int position) {
        final DocumentBean bean = mList.get(position);
        holder.name.setText(bean.getFile_name());
        if ("pdf".equals(bean.getFile_type())){
            holder.img.setBackgroundResource(R.mipmap.meeting_pdf);
        }else if ("pptx".equals(bean.getFile_type())){
            holder.img.setBackgroundResource(R.mipmap.meeting_ppt);
        }else if ("docx".equals(bean.getFile_type())){
            holder.img.setBackgroundResource(R.mipmap.meeting_word);
        }else if ("xlsx".equals(bean.getFile_type())){
            holder.img.setBackgroundResource(R.mipmap.meeting_excel);
        }
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 2 : mList.size();
    }

    public class MeetingMaterialsViewHolder extends RecyclerView.ViewHolder{

        ImageView img;

        TextView name;

        LinearLayout parent;

        public MeetingMaterialsViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.materialsImg);
            name = (TextView) itemView.findViewById(R.id.materialsName);
            parent = (LinearLayout) itemView.findViewById(R.id.parent);
        }
    }

    public interface  OnItemClickListener{
        void onItemClick(DocumentBean bean);
    }
}
