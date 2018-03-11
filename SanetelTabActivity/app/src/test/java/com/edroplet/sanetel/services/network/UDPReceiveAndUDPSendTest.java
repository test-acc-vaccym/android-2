package com.edroplet.sanetel.services.network;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

/**
 * Created on 2018/3/10.
 *
 * @author qxs
 */
public class UDPReceiveAndUDPSendTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void testSend()throws Exception{
        send("hello".getBytes(), "127.0.0.1", 998);
    }

    final private static String TAG = "SocketUdp: ";
    @Test
    //服务端
    public void server() throws Exception {
        DatagramSocket socket = null;
        DatagramPacket datapacket = null;
        InetSocketAddress address = null;

        try {
            System.out.println("IP " + InetAddress.getLocalHost());
            address = new InetSocketAddress("127.0.0.1", 9090);
            socket = new DatagramSocket(address);
            // socket.bind(address);

            byte buf[] = new byte[1024];
            datapacket = new DatagramPacket(buf, buf.length);
            System.out.println("等待接收客户端数据．．．");
            while (true) {
                socket.receive(datapacket);
                buf = datapacket.getData();
                InetAddress addr = datapacket.getAddress();
                int port = datapacket.getPort();
                System.out.println("客户端发送的数据: " + new String(buf) + "\n\n");
                System.out.println("数据来源 " + addr + ":" + port + "\n\n");

                SocketAddress toAddress = datapacket.getSocketAddress();
                String sendStr = "server return ok\r\n";
                buf = sendStr.getBytes();
                datapacket = new DatagramPacket(buf, buf.length);
                datapacket.setSocketAddress(toAddress);
                socket.send(datapacket);
                System.out.println("发送结束");
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


    private void send(byte[] data, String ip, int port) throws Exception{
        DatagramSocket s = new DatagramSocket(null);
        s.setReuseAddress(true);
        //这里是指定发送的客户端端口，因为该协议规定只接收由此端口发出的数据
        s.bind(new InetSocketAddress(998));

        DatagramPacket p = new DatagramPacket(data,0,data.length, new InetSocketAddress(ip, port));
        s.send(p);
    }
}