package com.chuyu.gaosuproject.model.interfacemodel;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */

public interface IDailyCheckListFillModel {
    void submitcheckfill(String checkunit, String checkproject, String userid, String checkresult, String content
            , String deductpoint, String checkid, List<File> file,FillListener fillListener);
    interface FillListener{
        void submitSuccess();
        void submitFaile();
        void showExpretion(String msg);
    }
}
