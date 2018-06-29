package com.tydic.anychatmeeting.model.inf;

import com.tydic.anychatmeeting.bean.SurfaceConfigBean;

/**
 * 作者：like on 2018/6/28 12:15
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：布局帮助类
 */
public interface LayoutHelper {
    //本地视频布局
    void layout(SurfaceConfigBean.LayoutConfigListBean.CellInfoListBean bean);
    //布局完成
    void layoutFinish();
}
