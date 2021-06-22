package com.wj.camera.response;

import java.io.Serializable;

/**
 * FileName: FOCUSCTRL
 * Author: xiongxiang
 * Date: 2021/6/21
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class FOCUSCTRL implements Serializable {
    private FOCUSCTRLDTO FOCUSCTRL;

    public FOCUSCTRLDTO getFOCUSCTRL() {
        return FOCUSCTRL;
    }

    public void setFOCUSCTRL(FOCUSCTRLDTO FOCUSCTRL) {
        this.FOCUSCTRL = FOCUSCTRL;
    }

    public static class  FOCUSCTRLDTO{

        //near聚焦+，far聚焦-
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
