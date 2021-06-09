package com.wj.camera.response;

/**
 * FileName: FocusEntity
 * Author: xiongxiang
 * Date: 2021/6/9
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class FocusEntity {


    /**
     * FocusData : {"focus":"-60"}
     */

    private FocusDataDTO FocusData;

    public FocusDataDTO getFocusData() {
        return FocusData;
    }

    public void setFocusData(FocusDataDTO FocusData) {
        this.FocusData = FocusData;
    }

    public static class FocusDataDTO {
        /**
         * focus : -60
         */

        private String focus;

        public String getFocus() {
            return focus;
        }

        public void setFocus(String focus) {
            this.focus = focus;
        }
    }
}
