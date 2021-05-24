package com.wj.camera.response;

import java.io.Serializable;

/**
 * FileName: ZoomResponse
 * Author: xiongxiang
 * Date: 2021/3/11
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class ZoomResponse  implements Serializable {


    /**
     * zoom : {"ratio":"5"}
     */

    private ZoomDTO zoom;

    public ZoomDTO getZoom() {
        return zoom;
    }

    public void setZoom(ZoomDTO zoom) {
        this.zoom = zoom;
    }

    public static class ZoomDTO {
        /**
         * ratio : 5
         */

        private int ratio;

        public int getRatio() {
            return ratio;
        }

        public void setRatio(int ratio) {
            this.ratio = ratio;
        }
    }
}
