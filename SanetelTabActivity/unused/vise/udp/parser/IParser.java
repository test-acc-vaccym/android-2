package com.vise.udp.parser;

import com.vise.udp.core.UdpOperate;
import com.vise.udp.mode.PacketBuffer;
import com.vise.udp.utils.ByteUtil;

import java.nio.ByteBuffer;

/**
 * @Description:
 * @author: <a href="http://xiaoyaoyou1212.360doc.com">DAWI</a>
 * @date: 2016-12-21 15:58
 */
public interface IParser {
    IParser DEFAULT = new IParser() {
        @Override
        public void write(UdpOperate udpOperate, ByteBuffer buffer, PacketBuffer packetBuffer) {
            if (packetBuffer != null && buffer != null) {
                buffer.put(packetBuffer.getBytes());
            }
        }

        @Override
        public PacketBuffer read(UdpOperate udpOperate, ByteBuffer buffer) {
            PacketBuffer packetBuffer = new PacketBuffer();
            if (buffer != null) {
                packetBuffer.setBytes(ByteUtil.bufferToBytes(buffer));
            }
            return packetBuffer;
        }
    };

    void write(UdpOperate udpOperate, ByteBuffer buffer, PacketBuffer packetBuffer);

    PacketBuffer read(UdpOperate udpOperate, ByteBuffer buffer);
}
