package com.wj.camera.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * FileName: NetworkInterface
 * Author: xiongxiang
 * Date: 2021/7/8
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class NetworkInterface  implements Serializable {


    /**
     * NetworkInterfaceList : {"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","NetworkInterface":[{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","id":"1","IPAddress":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","ipVersion":"dual","addressingType":"static","ipAddress":"192.168.0.84","subnetMask":"255.255.255.0","ipv6Address":"::","bitMask":"0","DefaultGateway":{"ipAddress":"192.168.0.1","ipv6Address":"::"},"PrimaryDNS":{"ipAddress":"192.168.0.1"},"SecondaryDNS":{"ipAddress":"114.114.114.114"},"Ipv6Mode":{"ipV6AddressingType":"ra","ipv6AddressList":{"v6Address":{"id":"1","type":"manual","address":"::","bitMask":"0"}}}},"Discovery":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","UPnP":{"enabled":"false"},"Zeroconf":{"enabled":"true"}},"Link":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","MACAddress":"08:a1:89:b1:d5:8a","autoNegotiation":"true","speed":"10","duplex":"half","MTU":"1500"},"Wireless":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","enabled":"false","wirelessNetworkMode":"infrastructure","channel":"auto","ssid":"svnlan9","wmmEnabled":"false","WirelessSecurity":{"securityMode":"WPA2-personal","WPA":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","algorithmType":"AES","sharedKey":"d8476f71838a2cb159425f93958c7ed6c84c13bd01941b679724f8a7a39252ff","wpaKeyLength":"10"},"support64bitKey":"WPA-personal,WPA2-personal"}}},{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","id":"2","IPAddress":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","ipVersion":"v4","addressingType":"dynamic","ipAddress":"192.168.8.1","subnetMask":"255.255.255.0","DefaultGateway":{"ipAddress":"0.0.0.0"},"PrimaryDNS":{"ipAddress":"223.5.5.5"},"SecondaryDNS":{"ipAddress":"8.8.8.8"},"MACAddress":"b0-02-47-43-f0-fc"},"Wireless":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","enabled":"false","wirelessNetworkMode":"infrastructure","channel":"auto","ssid":"svnlan9","wmmEnabled":"false","WirelessSecurity":{"securityMode":"WPA2-personal","WPA":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","algorithmType":"AES","sharedKey":"d8476f71838a2cb159425f93958c7ed6c84c13bd01941b679724f8a7a39252ff","wpaKeyLength":"10"},"support64bitKey":"WPA-personal,WPA2-personal"}}}]}
     */

    private NetworkInterfaceListDTO NetworkInterfaceList;

    public NetworkInterfaceListDTO getNetworkInterfaceList() {
        return NetworkInterfaceList;
    }

    public void setNetworkInterfaceList(NetworkInterfaceListDTO NetworkInterfaceList) {
        this.NetworkInterfaceList = NetworkInterfaceList;
    }

    public static class NetworkInterfaceListDTO {
        /**
         * -xmlns : http://www.std-cgi.com/ver20/XMLSchema
         * -version : 2.0
         * NetworkInterface : [{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","id":"1","IPAddress":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","ipVersion":"dual","addressingType":"static","ipAddress":"192.168.0.84","subnetMask":"255.255.255.0","ipv6Address":"::","bitMask":"0","DefaultGateway":{"ipAddress":"192.168.0.1","ipv6Address":"::"},"PrimaryDNS":{"ipAddress":"192.168.0.1"},"SecondaryDNS":{"ipAddress":"114.114.114.114"},"Ipv6Mode":{"ipV6AddressingType":"ra","ipv6AddressList":{"v6Address":{"id":"1","type":"manual","address":"::","bitMask":"0"}}}},"Discovery":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","UPnP":{"enabled":"false"},"Zeroconf":{"enabled":"true"}},"Link":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","MACAddress":"08:a1:89:b1:d5:8a","autoNegotiation":"true","speed":"10","duplex":"half","MTU":"1500"}},{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","id":"2","IPAddress":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","ipVersion":"v4","addressingType":"dynamic","ipAddress":"192.168.8.1","subnetMask":"255.255.255.0","DefaultGateway":{"ipAddress":"0.0.0.0"},"PrimaryDNS":{"ipAddress":"223.5.5.5"},"SecondaryDNS":{"ipAddress":"8.8.8.8"},"MACAddress":"b0-02-47-43-f0-fc"},"Wireless":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","enabled":"false","wirelessNetworkMode":"infrastructure","channel":"auto","ssid":"svnlan9","wmmEnabled":"false","WirelessSecurity":{"securityMode":"WPA2-personal","WPA":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","algorithmType":"AES","sharedKey":"d8476f71838a2cb159425f93958c7ed6c84c13bd01941b679724f8a7a39252ff","wpaKeyLength":"10"},"support64bitKey":"WPA-personal,WPA2-personal"}}}]
         */

        @SerializedName("-xmlns")
        private String xmlns;
        @SerializedName("-version")
        private String version;
        private List<NetworkInterfaceDTO> NetworkInterface;

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

        public List<NetworkInterfaceDTO> getNetworkInterface() {
            return NetworkInterface;
        }

        public void setNetworkInterface(List<NetworkInterfaceDTO> NetworkInterface) {
            this.NetworkInterface = NetworkInterface;
        }

        public static class NetworkInterfaceDTO {
            /**
             * -xmlns : http://www.std-cgi.com/ver20/XMLSchema
             * -version : 2.0
             * id : 1
             * IPAddress : {"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","ipVersion":"dual","addressingType":"static","ipAddress":"192.168.0.84","subnetMask":"255.255.255.0","ipv6Address":"::","bitMask":"0","DefaultGateway":{"ipAddress":"192.168.0.1","ipv6Address":"::"},"PrimaryDNS":{"ipAddress":"192.168.0.1"},"SecondaryDNS":{"ipAddress":"114.114.114.114"},"Ipv6Mode":{"ipV6AddressingType":"ra","ipv6AddressList":{"v6Address":{"id":"1","type":"manual","address":"::","bitMask":"0"}}}}
             * Discovery : {"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","UPnP":{"enabled":"false"},"Zeroconf":{"enabled":"true"}}
             * Link : {"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","MACAddress":"08:a1:89:b1:d5:8a","autoNegotiation":"true","speed":"10","duplex":"half","MTU":"1500"}
             * Wireless : {"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","enabled":"false","wirelessNetworkMode":"infrastructure","channel":"auto","ssid":"svnlan9","wmmEnabled":"false","WirelessSecurity":{"securityMode":"WPA2-personal","WPA":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","algorithmType":"AES","sharedKey":"d8476f71838a2cb159425f93958c7ed6c84c13bd01941b679724f8a7a39252ff","wpaKeyLength":"10"},"support64bitKey":"WPA-personal,WPA2-personal"}}
             */

            @SerializedName("-xmlns")
            private String xmlns;
            @SerializedName("-version")
            private String version;
            private String id;
            private IPAddressDTO IPAddress;
            private DiscoveryDTO Discovery;
            private LinkDTO Link;
            private WirelessDTO Wireless;

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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public IPAddressDTO getIPAddress() {
                return IPAddress;
            }

            public void setIPAddress(IPAddressDTO IPAddress) {
                this.IPAddress = IPAddress;
            }

            public DiscoveryDTO getDiscovery() {
                return Discovery;
            }

            public void setDiscovery(DiscoveryDTO Discovery) {
                this.Discovery = Discovery;
            }

            public LinkDTO getLink() {
                return Link;
            }

            public void setLink(LinkDTO Link) {
                this.Link = Link;
            }

            public WirelessDTO getWireless() {
                return Wireless;
            }

            public void setWireless(WirelessDTO Wireless) {
                this.Wireless = Wireless;
            }

            public static class IPAddressDTO {
                /**
                 * -xmlns : http://www.std-cgi.com/ver20/XMLSchema
                 * -version : 2.0
                 * ipVersion : dual
                 * addressingType : static
                 * ipAddress : 192.168.0.84
                 * subnetMask : 255.255.255.0
                 * ipv6Address : ::
                 * bitMask : 0
                 * DefaultGateway : {"ipAddress":"192.168.0.1","ipv6Address":"::"}
                 * PrimaryDNS : {"ipAddress":"192.168.0.1"}
                 * SecondaryDNS : {"ipAddress":"114.114.114.114"}
                 * Ipv6Mode : {"ipV6AddressingType":"ra","ipv6AddressList":{"v6Address":{"id":"1","type":"manual","address":"::","bitMask":"0"}}}
                 */

                @SerializedName("-xmlns")
                private String xmlns;
                @SerializedName("-version")
                private String version;
                private String ipVersion;
                private String addressingType;
                private String ipAddress;
                private String subnetMask;
                private String ipv6Address;
                private String bitMask;
                private DefaultGatewayDTO DefaultGateway;
                private PrimaryDNSDTO PrimaryDNS;
                private SecondaryDNSDTO SecondaryDNS;
                private Ipv6ModeDTO Ipv6Mode;

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

                public String getIpVersion() {
                    return ipVersion;
                }

                public void setIpVersion(String ipVersion) {
                    this.ipVersion = ipVersion;
                }

                public String getAddressingType() {
                    return addressingType;
                }

                public void setAddressingType(String addressingType) {
                    this.addressingType = addressingType;
                }

                public String getIpAddress() {
                    return ipAddress;
                }

                public void setIpAddress(String ipAddress) {
                    this.ipAddress = ipAddress;
                }

                public String getSubnetMask() {
                    return subnetMask;
                }

                public void setSubnetMask(String subnetMask) {
                    this.subnetMask = subnetMask;
                }

                public String getIpv6Address() {
                    return ipv6Address;
                }

                public void setIpv6Address(String ipv6Address) {
                    this.ipv6Address = ipv6Address;
                }

                public String getBitMask() {
                    return bitMask;
                }

                public void setBitMask(String bitMask) {
                    this.bitMask = bitMask;
                }

                public DefaultGatewayDTO getDefaultGateway() {
                    return DefaultGateway;
                }

                public void setDefaultGateway(DefaultGatewayDTO DefaultGateway) {
                    this.DefaultGateway = DefaultGateway;
                }

                public PrimaryDNSDTO getPrimaryDNS() {
                    return PrimaryDNS;
                }

                public void setPrimaryDNS(PrimaryDNSDTO PrimaryDNS) {
                    this.PrimaryDNS = PrimaryDNS;
                }

                public SecondaryDNSDTO getSecondaryDNS() {
                    return SecondaryDNS;
                }

                public void setSecondaryDNS(SecondaryDNSDTO SecondaryDNS) {
                    this.SecondaryDNS = SecondaryDNS;
                }

                public Ipv6ModeDTO getIpv6Mode() {
                    return Ipv6Mode;
                }

                public void setIpv6Mode(Ipv6ModeDTO Ipv6Mode) {
                    this.Ipv6Mode = Ipv6Mode;
                }

                public static class DefaultGatewayDTO {
                    /**
                     * ipAddress : 192.168.0.1
                     * ipv6Address : ::
                     */

                    private String ipAddress;
                    private String ipv6Address;

                    public String getIpAddress() {
                        return ipAddress;
                    }

                    public void setIpAddress(String ipAddress) {
                        this.ipAddress = ipAddress;
                    }

                    public String getIpv6Address() {
                        return ipv6Address;
                    }

                    public void setIpv6Address(String ipv6Address) {
                        this.ipv6Address = ipv6Address;
                    }
                }

                public static class PrimaryDNSDTO {
                    /**
                     * ipAddress : 192.168.0.1
                     */

                    private String ipAddress;

                    public String getIpAddress() {
                        return ipAddress;
                    }

                    public void setIpAddress(String ipAddress) {
                        this.ipAddress = ipAddress;
                    }
                }

                public static class SecondaryDNSDTO {
                    /**
                     * ipAddress : 114.114.114.114
                     */

                    private String ipAddress;

                    public String getIpAddress() {
                        return ipAddress;
                    }

                    public void setIpAddress(String ipAddress) {
                        this.ipAddress = ipAddress;
                    }
                }

                public static class Ipv6ModeDTO {
                    /**
                     * ipV6AddressingType : ra
                     * ipv6AddressList : {"v6Address":{"id":"1","type":"manual","address":"::","bitMask":"0"}}
                     */

                    private String ipV6AddressingType;
                    private Ipv6AddressListDTO ipv6AddressList;

                    public String getIpV6AddressingType() {
                        return ipV6AddressingType;
                    }

                    public void setIpV6AddressingType(String ipV6AddressingType) {
                        this.ipV6AddressingType = ipV6AddressingType;
                    }

                    public Ipv6AddressListDTO getIpv6AddressList() {
                        return ipv6AddressList;
                    }

                    public void setIpv6AddressList(Ipv6AddressListDTO ipv6AddressList) {
                        this.ipv6AddressList = ipv6AddressList;
                    }

                    public static class Ipv6AddressListDTO {
                        /**
                         * v6Address : {"id":"1","type":"manual","address":"::","bitMask":"0"}
                         */

                        private V6AddressDTO v6Address;

                        public V6AddressDTO getV6Address() {
                            return v6Address;
                        }

                        public void setV6Address(V6AddressDTO v6Address) {
                            this.v6Address = v6Address;
                        }

                        public static class V6AddressDTO {
                            /**
                             * id : 1
                             * type : manual
                             * address : ::
                             * bitMask : 0
                             */

                            private String id;
                            private String type;
                            private String address;
                            private String bitMask;

                            public String getId() {
                                return id;
                            }

                            public void setId(String id) {
                                this.id = id;
                            }

                            public String getType() {
                                return type;
                            }

                            public void setType(String type) {
                                this.type = type;
                            }

                            public String getAddress() {
                                return address;
                            }

                            public void setAddress(String address) {
                                this.address = address;
                            }

                            public String getBitMask() {
                                return bitMask;
                            }

                            public void setBitMask(String bitMask) {
                                this.bitMask = bitMask;
                            }
                        }
                    }
                }
            }

            public static class DiscoveryDTO {
                /**
                 * -xmlns : http://www.std-cgi.com/ver20/XMLSchema
                 * -version : 2.0
                 * UPnP : {"enabled":"false"}
                 * Zeroconf : {"enabled":"true"}
                 */

                @SerializedName("-xmlns")
                private String xmlns;
                @SerializedName("-version")
                private String version;
                private UPnPDTO UPnP;
                private ZeroconfDTO Zeroconf;

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

                public UPnPDTO getUPnP() {
                    return UPnP;
                }

                public void setUPnP(UPnPDTO UPnP) {
                    this.UPnP = UPnP;
                }

                public ZeroconfDTO getZeroconf() {
                    return Zeroconf;
                }

                public void setZeroconf(ZeroconfDTO Zeroconf) {
                    this.Zeroconf = Zeroconf;
                }

                public static class UPnPDTO {
                    /**
                     * enabled : false
                     */

                    private String enabled;

                    public String getEnabled() {
                        return enabled;
                    }

                    public void setEnabled(String enabled) {
                        this.enabled = enabled;
                    }
                }

                public static class ZeroconfDTO {
                    /**
                     * enabled : true
                     */

                    private String enabled;

                    public String getEnabled() {
                        return enabled;
                    }

                    public void setEnabled(String enabled) {
                        this.enabled = enabled;
                    }
                }
            }

            public static class LinkDTO {
                /**
                 * -xmlns : http://www.std-cgi.com/ver20/XMLSchema
                 * -version : 2.0
                 * MACAddress : 08:a1:89:b1:d5:8a
                 * autoNegotiation : true
                 * speed : 10
                 * duplex : half
                 * MTU : 1500
                 */

                @SerializedName("-xmlns")
                private String xmlns;
                @SerializedName("-version")
                private String version;
                private String MACAddress;
                private String autoNegotiation;
                private String speed;
                private String duplex;
                private String MTU;

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

                public String getMACAddress() {
                    return MACAddress;
                }

                public void setMACAddress(String MACAddress) {
                    this.MACAddress = MACAddress;
                }

                public String getAutoNegotiation() {
                    return autoNegotiation;
                }

                public void setAutoNegotiation(String autoNegotiation) {
                    this.autoNegotiation = autoNegotiation;
                }

                public String getSpeed() {
                    return speed;
                }

                public void setSpeed(String speed) {
                    this.speed = speed;
                }

                public String getDuplex() {
                    return duplex;
                }

                public void setDuplex(String duplex) {
                    this.duplex = duplex;
                }

                public String getMTU() {
                    return MTU;
                }

                public void setMTU(String MTU) {
                    this.MTU = MTU;
                }
            }

            public static class WirelessDTO {
                /**
                 * -xmlns : http://www.std-cgi.com/ver20/XMLSchema
                 * -version : 2.0
                 * enabled : false
                 * wirelessNetworkMode : infrastructure
                 * channel : auto
                 * ssid : svnlan9
                 * wmmEnabled : false
                 * WirelessSecurity : {"securityMode":"WPA2-personal","WPA":{"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","algorithmType":"AES","sharedKey":"d8476f71838a2cb159425f93958c7ed6c84c13bd01941b679724f8a7a39252ff","wpaKeyLength":"10"},"support64bitKey":"WPA-personal,WPA2-personal"}
                 */

                @SerializedName("-xmlns")
                private String xmlns;
                @SerializedName("-version")
                private String version;
                private String enabled;
                private String wirelessNetworkMode;
                private String channel;
                private String ssid;
                private String wmmEnabled;
                private WirelessSecurityDTO WirelessSecurity;

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

                public String getEnabled() {
                    return enabled;
                }

                public void setEnabled(String enabled) {
                    this.enabled = enabled;
                }

                public String getWirelessNetworkMode() {
                    return wirelessNetworkMode;
                }

                public void setWirelessNetworkMode(String wirelessNetworkMode) {
                    this.wirelessNetworkMode = wirelessNetworkMode;
                }

                public String getChannel() {
                    return channel;
                }

                public void setChannel(String channel) {
                    this.channel = channel;
                }

                public String getSsid() {
                    return ssid;
                }

                public void setSsid(String ssid) {
                    this.ssid = ssid;
                }

                public String getWmmEnabled() {
                    return wmmEnabled;
                }

                public void setWmmEnabled(String wmmEnabled) {
                    this.wmmEnabled = wmmEnabled;
                }

                public WirelessSecurityDTO getWirelessSecurity() {
                    return WirelessSecurity;
                }

                public void setWirelessSecurity(WirelessSecurityDTO WirelessSecurity) {
                    this.WirelessSecurity = WirelessSecurity;
                }

                public static class WirelessSecurityDTO {
                    /**
                     * securityMode : WPA2-personal
                     * WPA : {"-xmlns":"http://www.std-cgi.com/ver20/XMLSchema","-version":"2.0","algorithmType":"AES","sharedKey":"d8476f71838a2cb159425f93958c7ed6c84c13bd01941b679724f8a7a39252ff","wpaKeyLength":"10"}
                     * support64bitKey : WPA-personal,WPA2-personal
                     */

                    private String securityMode;
                    private WPADTO WPA;
                    private String support64bitKey;

                    public String getSecurityMode() {
                        return securityMode;
                    }

                    public void setSecurityMode(String securityMode) {
                        this.securityMode = securityMode;
                    }

                    public WPADTO getWPA() {
                        return WPA;
                    }

                    public void setWPA(WPADTO WPA) {
                        this.WPA = WPA;
                    }

                    public String getSupport64bitKey() {
                        return support64bitKey;
                    }

                    public void setSupport64bitKey(String support64bitKey) {
                        this.support64bitKey = support64bitKey;
                    }

                    public static class WPADTO {
                        /**
                         * -xmlns : http://www.std-cgi.com/ver20/XMLSchema
                         * -version : 2.0
                         * algorithmType : AES
                         * sharedKey : d8476f71838a2cb159425f93958c7ed6c84c13bd01941b679724f8a7a39252ff
                         * wpaKeyLength : 10
                         */

                        @SerializedName("-xmlns")
                        private String xmlns;
                        @SerializedName("-version")
                        private String version;
                        private String algorithmType;
                        private String sharedKey;
                        private String wpaKeyLength;

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

                        public String getAlgorithmType() {
                            return algorithmType;
                        }

                        public void setAlgorithmType(String algorithmType) {
                            this.algorithmType = algorithmType;
                        }

                        public String getSharedKey() {
                            return sharedKey;
                        }

                        public void setSharedKey(String sharedKey) {
                            this.sharedKey = sharedKey;
                        }

                        public String getWpaKeyLength() {
                            return wpaKeyLength;
                        }

                        public void setWpaKeyLength(String wpaKeyLength) {
                            this.wpaKeyLength = wpaKeyLength;
                        }
                    }
                }
            }
        }
    }
}
