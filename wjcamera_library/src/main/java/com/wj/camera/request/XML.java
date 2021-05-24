package com.wj.camera.request;

/**
 * FileName: XML
 * Author: xiongxiang
 * Date: 2021/1/4
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class XML {

    //RTMP
    public static String RTMP = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<RTMP version=\"2.0\" xmlns=\"http://www.hikvision.com/ver20/XMLSchema\">\n" +
            "<enabled>false</enabled>\n" +
            "<URL>1233</URL>\n" +
            "</RTMP>";
    //主流
    public static String setting101 =
            "<StreamingChannel\n" +
                    "    xmlns=\"http://www.hikvision.com/ver20/XMLSchema\" version=\"2.0\">\n" +
                    "    <id>101</id>\n" +
                    "    <channelName>无极教育云</channelName>\n" +
                    "    <enabled>true</enabled>\n" +
                    "    <Transport>\n" +
                    "        <maxPacketSize>1000</maxPacketSize>\n" +
                    "        <ControlProtocolList>\n" +
                    "            <ControlProtocol>\n" +
                    "                <streamingTransport>RTSP</streamingTransport>\n" +
                    "            </ControlProtocol>\n" +
                    "            <ControlProtocol>\n" +
                    "                <streamingTransport>HTTP</streamingTransport>\n" +
                    "            </ControlProtocol>\n" +
                    "            <ControlProtocol>\n" +
                    "                <streamingTransport>SHTTP</streamingTransport>\n" +
                    "            </ControlProtocol>\n" +
                    "        </ControlProtocolList>\n" +
                    "        <Unicast>\n" +
                    "            <enabled>true</enabled>\n" +
                    "            <rtpTransportType>RTP/TCP</rtpTransportType>\n" +
                    "        </Unicast>\n" +
                    "        <Multicast>\n" +
                    "            <enabled>true</enabled>\n" +
                    "            <destIPAddress>0.0.0.0</destIPAddress>\n" +
                    "            <videoDestPortNo>8860</videoDestPortNo>\n" +
                    "            <audioDestPortNo>8862</audioDestPortNo>\n" +
                    "            <FecInfo>\n" +
                    "                <fecRatio>0</fecRatio>\n" +
                    "                <fecDestPortNo>9860</fecDestPortNo>\n" +
                    "            </FecInfo>\n" +
                    "        </Multicast>\n" +
                    "        <Security>\n" +
                    "            <enabled>true</enabled>\n" +
                    "            <certificateType>digest</certificateType>\n" +
                    "            <SecurityAlgorithm>\n" +
                    "                <algorithmType>MD5</algorithmType>\n" +
                    "            </SecurityAlgorithm>\n" +
                    "        </Security>\n" +
                    "    </Transport>\n" +
                    "    <Audio>\n" +
                    "        <enabled>true</enabled>\n" +
                    "        <audioInputChannelID>1</audioInputChannelID>\n" +
                    "        <audioCompressionType>AAC</audioCompressionType>\n" +
                    "    </Audio>\n" +
                    "    <Video\n" +
                    "        xmlns=\"\">\n" +
                    "        <enabled>true</enabled>\n" +
                    "        <videoInputChannelID>1</videoInputChannelID>\n" +
                    "        <videoCodecType>H.264</videoCodecType>\n" +
                    "        <videoResolutionWidth>1920</videoResolutionWidth>\n" +
                    "        <videoScanType>progressive</videoScanType>\n" +
                    "        <videoResolutionHeight>1080</videoResolutionHeight>\n" +
                    "        <videoQualityControlType>cbr</videoQualityControlType>\n" +
                    "        <constantBitRate>6144</constantBitRate>\n" +
                    "        <maxFrameRate>2500</maxFrameRate>\n" +
                    "        <GovLength>250</GovLength>\n" +
                    "        <H264Profile>Main</H264Profile>\n" +
                    "        <SVC>\n" +
                    "            <enabled>false</enabled>\n" +
                    "        </SVC>\n" +
                    "        <smoothing>50</smoothing>\n" +
                    "        <SmartCodec>\n" +
                    "            <enabled>false</enabled>\n" +
                    "        </SmartCodec>\n" +
                    "    </Video>\n" +
                    "</StreamingChannel>";
    //调焦
    public static String PTZDATA_60_F = "<?xml version: \"1.0\" encoding=\"UTF-8\"?><PTZData><zoom>-1</zoom></PTZData>";
    public static String PTZDATA_60_Z = "<?xml version: \"1.0\" encoding=\"UTF-8\"?><PTZData><zoom>1</zoom></PTZData>";
    public static String PTZDATA_0 = "<?xml version: \"1.0\" encoding=\"UTF-8\"?><PTZData><zoom>0</zoom></PTZData>";

    public static String  Zoom="<?xml version: \"1.0\" encoding=\"UTF-8\"?><PTZData><zoom><ratio>5</ratio></zoom></PTZData>";
}
