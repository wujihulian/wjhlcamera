package com.wj.camera.response;

import com.google.gson.annotations.SerializedName;

/**
 * FileName: SceneResponse
 * Author: xiongxiang
 * Date: 2021/6/1
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class SceneResponse {


    /**
     * MountingScenario : {"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","mode":"indoor"}
     */

    private MountingScenarioDTO MountingScenario;

    public MountingScenarioDTO getMountingScenario() {
        return MountingScenario;
    }

    public void setMountingScenario(MountingScenarioDTO MountingScenario) {
        this.MountingScenario = MountingScenario;
    }

    public static class MountingScenarioDTO {
        /**
         * -xmlns : http://www.std-cgi.com/ver20/XMLSchema
         * -version : 2.0
         * mode : indoor
         */

        @SerializedName("-xmlns")
        private String xmlns;
        @SerializedName("-version")
        private String version;
        private String mode;

        public String getXmlns() {
            return xmlns;
        }

        public void setXmlns(String xmlns) {
            this.xmlns = xmlns;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }
}
