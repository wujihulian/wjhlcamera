package com.wj.camera.response;

/**
 * FileName: NetConfig
 * Author: xiongxiang
 * Date: 2021/7/12
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class NetConfig {


    /**
     * NetConfig : {"result":"no operation"}
     */

    private NetConfigDTO NetConfig;

    public NetConfigDTO getNetConfig() {
        return NetConfig;
    }

    public void setNetConfig(NetConfigDTO NetConfig) {
        this.NetConfig = NetConfig;
    }

    public static class NetConfigDTO {
        /**
         * result : no operation
         */

        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
