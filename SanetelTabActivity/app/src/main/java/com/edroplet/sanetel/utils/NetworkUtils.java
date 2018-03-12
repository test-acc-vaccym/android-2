package com.edroplet.sanetel.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.edroplet.sanetel.beans.HLKProtocol;

import org.apache.http.conn.util.InetAddressUtils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by qxs on 2018/1/3.
 */

public class NetworkUtils {
    public static long ip2long(String ip) {
        String[] ips = ip.split("[.]");
        long num = 16777216L * Long.parseLong(ips[0]) + 65536L
                * Long.parseLong(ips[1]) + 256 * Long.parseLong(ips[2])
                + Long.parseLong(ips[3]);
        return num;
    }

    /**
     * 根据掩码位数获取掩码
     */
    public static String getNetMask(String mask) {
        int inetMask =Integer.parseInt(mask);
        if(inetMask > 32){
            return null;
        }
        //子网掩码为1占了几个字节
        int num1 = inetMask/8;
        //子网掩码的补位位数
        int num2 = inetMask%8;
        int array[] = new int[4];
        for (int i = 0; i < num1; i++) {
            array[i] = 255;
        }
        for (int i = num1; i < 4; i++) {
            array[i] = 0;
        }
        for (int i = 0; i < num2; num2--) {
            array[num1] += Math.pow(2, 8-num2);
        }
        String netMask =  array[0] + "." + array[1] + "." + array[2] + "." + array[3];
        return netMask;
    }
    /**
     * 根据ip地址和掩码获取终止IP
    * */
    private String getHighAddr(String ipinfo, String netMask) {
        String lowAddr = getLowAddr(ipinfo, netMask);
        int hostNumber = getHostNumber(netMask);
        if("" == lowAddr || hostNumber == 0){
            return null;
        }
        int lowAddrArray[] = new int[4];
        for (int i = 0; i < 4; i++) {
            lowAddrArray[i] = Integer.parseInt(lowAddr.split("\\.")[i]);
            if(i == 3){
                lowAddrArray[i] = lowAddrArray[i] - 1;
            }
        }
        lowAddrArray[3] = lowAddrArray[3] + (hostNumber - 1);
        if(lowAddrArray[3] >255){
            int k = lowAddrArray[3] / 256;
            lowAddrArray[3] = lowAddrArray[3] % 256;
            lowAddrArray[2] = lowAddrArray[2] + k;
        }
        if(lowAddrArray[2] > 255){
            int  j = lowAddrArray[2] / 256;
            lowAddrArray[2] = lowAddrArray[2] % 256;
            lowAddrArray[1] = lowAddrArray[1] + j;
            if(lowAddrArray[1] > 255){
                int  k = lowAddrArray[1] / 256;
                lowAddrArray[1] = lowAddrArray[1] % 256;
                lowAddrArray[0] = lowAddrArray[0] + k;
            }
        }
        String highAddr = "";
        for(int i = 0; i < 4; i++){
            if(i == 3){
                lowAddrArray[i] = lowAddrArray[i] - 1;
            }
            if("" == highAddr){
                highAddr = lowAddrArray[i]+"";
            }else{
                highAddr += "." + lowAddrArray[i];
            }
        }
        return highAddr;
    }

    /**
     * 实际可用ip数量
     * @param netMask 掩码
     * @return 0
     */
    private int getHostNumber(String netMask) {
        int hostNumber = 0;
        int netMaskArray[] = new int[4];
        for (int i = 0; i < 4 ; i++) {
            netMaskArray[i] = Integer.parseInt(netMask.split("\\.")[i]);
            if(netMaskArray[i] < 255){
                hostNumber =  (int) (Math.pow(256,3-i) * (256 - netMaskArray[i]));
                break;
            }
        }
        return hostNumber;
    }

    /**
     * 根据ip地址和掩码获取起始IP
     * @param ipinfo IP地址
     * @param netMask 掩码
     * @return 最小ip
     */
    public static String getLowAddr(String ipinfo, String netMask) {
        String lowAddr = "";
        int ipArray[] = new int[4];
        int netMaskArray[] = new int[4];
        if(4 != ipinfo.split("\\.").length || "" == netMask){
            return null;
        }
        for (int i = 0; i < 4; i++) {
            try{
                ipArray[i] = Integer.parseInt(ipinfo.split("\\.")[i]);
            }catch(NumberFormatException e){
                String ip = ipinfo.replaceAll("\n", "");
                ipArray[i] = Integer.parseInt(ip.split("\\.")[i]);
            }
            netMaskArray[i] = Integer.parseInt(netMask.split("\\.")[i]);
            if(ipArray[i] > 255 || ipArray[i] < 0 || netMaskArray[i] > 255 || netMaskArray[i] < 0){
                return null;
            }
            ipArray[i] = ipArray[i]&netMaskArray[i];
        }
        //构造最小地址
        for (int i = 0; i < 4; i++){
            if(i == 3){
                ipArray[i] = ipArray[i] + 1;
            }
            if ("" == lowAddr){
                lowAddr +=ipArray[i];
            } else{
                lowAddr += "." + ipArray[i];
            }
        }
        return lowAddr;
    }


    public static class networkInfo {
        String ip;
        String gateway;
        String mask;
        networkInfo(String ip, String gateway,String mask){
            this.ip = ip;
            this.gateway = gateway;
            this.mask = mask;
        }

        networkInfo(){
            ip = CustomSP.DefaultIP;
            gateway = HLKProtocol.Host;
            mask = "255.255.255.0";
        }

        public String getGateway() {
            return HLKProtocol.Host;
            // return "172.28.210.50";
            // return gateway;
        }

        public String getIp() {
            return ip;
        }

        public String getNetMask() {
            return mask;
        }
    }

    public static networkInfo getIPAddress(Context ctx){
        try {
            WifiManager wifi_service = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            DhcpInfo dhcpInfo = new DhcpInfo();
            if (wifi_service != null)
                dhcpInfo = wifi_service.getDhcpInfo();
            //        WifiInfo wifiinfo = wifi_service.getConnectionInfo();
            //        System.out.println("Wifi info----->"+wifiinfo.getIpAddress());
            //        System.out.println("DHCP info gateway----->"+ Formatter.formatIpAddress(dhcpInfo.gateway));
            //        System.out.println("DHCP info netmask----->"+Formatter.formatIpAddress(dhcpInfo.netmask));
            //DhcpInfo中的ipAddress是一个int型的变量，通过Formatter将其转化为字符串IP地址
            return new networkInfo(longToIP(dhcpInfo.ipAddress),longToIP(dhcpInfo.gateway),longToIP(dhcpInfo.netmask));
        } catch (Exception npe){
            npe.printStackTrace();
            return new networkInfo();
        }
    }

    private static String ip2string(long address){
        try {
            byte[] ipAddress = BigInteger.valueOf(address).toByteArray();
            InetAddress myAddr = InetAddress.getByAddress(ipAddress);
            return myAddr.getHostAddress();
        }catch (UnknownHostException uhe){
            uhe.printStackTrace();
        }
        return "";
    }
    //将十进制整数形式转换成127.0.0.1形式的ip地址
    public static String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer("");
        //直接右移24位
        sb.append(longIp&0xFF).append(".");
        //将高8位置0，然后右移16位
        sb.append((longIp>>8)&0xFF).append(".");
        //将高16位置0，然后右移8位
        sb.append((longIp>>16)&0xFF).append(".");
        //将高24位置0
        sb.append((longIp>>24)&0xFF);
        return sb.toString();
    }

    public static String getConnectWifiSsid(Context context){
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            // Log.d("wifiInfo", wifiInfo.toString());
            Log.d("SSID",wifiInfo.getSSID());
            return wifiInfo.getSSID();
        }catch (NullPointerException npe){
            npe.printStackTrace();
            return "";
        }
    }

    public String getLocalHostIp() {
        String ipAddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(ip
                            .getHostAddress())) {
                        return ip.getHostAddress();
                    }
                }
            }
        }
        catch(SocketException e)
        {
            Log.e("Data", "获取本地ip地址失败");
            e.printStackTrace();
        }
        return ipAddress;
    }
}
