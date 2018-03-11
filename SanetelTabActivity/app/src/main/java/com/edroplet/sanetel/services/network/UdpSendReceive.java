package com.edroplet.sanetel.services.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created on 2018/3/10.
 *
 * @author qxs
 * Udp 接送
 */

public class UdpSendReceive {
    private static final String TAG = "UdpSendReceive";
    public static void server(String listenIP, int listenPort, String  destIP, int destPort, String[] sendMessage, String expected[]) {
        DatagramSocket socket = null;
        DatagramPacket dataPacket = null;
        InetSocketAddress address = null;

        try {
            System.out.println("IP " + listenIP);
            address = new InetSocketAddress(listenIP, listenPort);
            socket = new DatagramSocket(address);
            socket.setReuseAddress(true);
            // socket.bind(address);

            byte buf[];
            for (int i = 0; i < sendMessage.length; i++) {
                buf = sendMessage[i].getBytes();
                dataPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress(destIP, destPort));
                socket.send(dataPacket);
                System.out.println("发送结束");

                System.out.println("等待接收客户端数据．．．");
                while (true) {
                    buf = new byte[1024];
                    dataPacket = new DatagramPacket(buf, buf.length);
                    socket.receive(dataPacket);
                    buf = dataPacket.getData();
                    InetAddress addr = dataPacket.getAddress();
                    int port = dataPacket.getPort();
                    String response = new String(buf).trim();
                    System.out.println("客户端发送的数据: " + response + "\r\n");
                    System.out.println("数据来源 " + addr + ":" + port + "\r\n");
                    String iNeed = sendMessage[i].trim();
                    if (response.compareToIgnoreCase(iNeed) == 0) {
                        System.out.println(" " + addr + ":" + port + "\r\n");
                        break;
                    }
                    // SocketAddress toAddress = dataPacket.getSocketAddress();
                    // String sendStr = "server return ok\r\n";
                    // buf = new byte[1024];
                    // buf = sendStr.getBytes();
                    // dataPacket = new DatagramPacket(buf, buf.length);
                    // dataPacket.setSocketAddress(toAddress);
                    // socket.send(dataPacket);
                    // System.out.println("发送结束");
                }
            }

        } catch (UnknownHostException e) {
            System.out.println(TAG + e.getMessage());
            e.printStackTrace();
        } catch (SocketException e) {
            System.out.println(TAG + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(TAG + e.getMessage());
            e.printStackTrace();
        }
    }


    private void send(byte[] data, String ip, int listenPort, int port) throws Exception{
        DatagramSocket s = new DatagramSocket(null);
        s.setReuseAddress(true);
        // 这里是指定发送的客户端端口，因为该协议规定只接收由此端口发出的数据
        // 本地发送端口
        s.bind(new InetSocketAddress(port));

        DatagramPacket p = new DatagramPacket(data,0,data.length, new InetSocketAddress(ip, port));
        s.send(p);
    }
}
