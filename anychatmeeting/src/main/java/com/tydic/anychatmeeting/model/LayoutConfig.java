package com.tydic.anychatmeeting.model;

import android.content.Context;

import com.tydic.anychatmeeting.bean.SurfaceConfigBean;
import com.tydic.anychatmeeting.constant.Key;
import com.tydic.anychatmeeting.constant.config.Config;
import com.tydic.anychatmeeting.util.AssetsUtil;
import com.tydic.anychatmeeting.util.SharedPreferencesUtil;

import java.util.List;

/**
 * 作者：like on 2018/5/31 10:46
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：布局配置文件解析
 */
public class LayoutConfig {

    private Context context;

    public LayoutConfig(Context context) {
        this.context = context;
    }

    /**
     * 获取布局配置文件
     * @return
     */
    public SurfaceConfigBean getLayoutConfig(){
        return AssetsUtil.getObjectFromAssets(Config.XMLConfig,SurfaceConfigBean.class,context);
    }

    /**
     * 获取默认配置ID
     * @return
     */
    public int getCurrentLayoutConfigId(){
        int id  = SharedPreferencesUtil.getInt(Key.LAYOUT_CONFIG_DEFAULT_KEY,-1);
        if (id == -1){
            return getLayoutConfig().getDefaultLayoutID();
        }else {
            return id;
        }
    }

    /**
     * 获取当前使用的布局配置
     * @return
     */
    public SurfaceConfigBean.LayoutConfigListBean getCurrentLayoutConfig(){
        int id = getCurrentLayoutConfigId();
        SurfaceConfigBean surfaceConfigBean = getLayoutConfig();
        List<SurfaceConfigBean.LayoutConfigListBean> bean = surfaceConfigBean.getLayoutConfigList();
        for (SurfaceConfigBean.LayoutConfigListBean item :bean) {
            if (id == item.getId()){
                return  item;
            }
        }
        return null;
    }

    /**
     * 获取所有布局
     * @return
     */
    public List<SurfaceConfigBean.LayoutConfigListBean> getAllLayoutConfig(){
        SurfaceConfigBean surfaceConfigBean = getLayoutConfig();
        return surfaceConfigBean.getLayoutConfigList();
    }

}
