package com.wj.camera.response;

import java.io.Serializable;

/**
 * FileName: ZoomCTRL
 * Author: xiongxiang
 * Date: 2021/6/21
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class ZOOMCTRL implements Serializable {


    public ZOOMMCTRLDTO ZOOMCTRL;

    public ZOOMMCTRLDTO getZOOMCTRL() {
        return ZOOMCTRL;
    }

    public void setZOOMCTRL(ZOOMMCTRLDTO ZOOMCTRL) {
        this.ZOOMCTRL = ZOOMCTRL;
    }

    public static class ZOOMMCTRLDTO {
        //tele变倍-，wide变倍+
        public String mode;
        //1-32
        public int step;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }
    }
}
