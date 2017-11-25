package com.edroplet.qxx.saneteltabactivity.beans;

import android.content.Context;
import android.content.Intent;

import static com.edroplet.qxx.saneteltabactivity.services.CommunicateWithDeviceService.EXTRA_PARAM_SEND_CMD;
import static com.edroplet.qxx.saneteltabactivity.services.communicate.CommunicateDataReceiver.*;

/**
 * Created by qxs on 2017/11/16.
 * 协议类
 */

public class Protocol {
    // 4.1	复位/展开
    public static final String cmdAntennaExplode="$cmd,develop ant*ff<CR><LF>";
    public static final String cmdAntennaReset="$cmd,develop ant*ff<CR><LF>";
    public static final String cmdAntennaResetResult="$cmd,start develop*ff<CR><LF>";
    public static final String cmdAntennaExplodeResult="$cmd,start develop*ff<CR><LF>";
    // 4.2	收藏
    public static final String cmdAntennaFold="$cmd,stow ant*ff<CR><LF>";
    public static final String cmdAntennaFoldResult="$cmd,start stow*ff<CR><LF>";
    // 4.3	停止、见停止寻星
    public static final String cmdAntennaPause="$cmd,stow ant*ff<CR><LF>";
    public static final String cmdAntennaPauseResult="$cmd,start stow*ff<CR><LF>";

    // 4.4	寻星、见自动寻星


    // 4.5	读取设备信息
    public static final String cmdGeteEquipmentInfo="$cmd,get equip info*ff<CR><LF>";
    //返回 设备名称，主控板版本, 软件版本, 软件发布时间
    public static final String cmdGeteEquipmentInfoResult="$cmd,equip info %s, %s, %s, %s,*ff<CR><LF>";
    // Scanner s = new Scanner("123 asdf sd 45 789 sdf asdfl,sdf.sdfl,asdf    ......asdfkl    las"); 
    // s.useDelimiter(" |,|\\."); 
    // while (s.hasNext()) { 
    // System.out.println(s.next()); 
    // } 

    // 4.6	跟踪模式选择
    // 4.6.1	读取
    public static final String cmdGetTrackMode="$cmd,get track mode*ff<CR><LF>";
    // 返回<模式名称>(0: 信标机, 1: DVB)
    public static final String cmdGetTrackModeResult="$cmd,track mode %d*ff<CR><LF>";
    // 4.6.2	设置 <模式名称>(0: 信标机, 1: DVB)
    public static final String cmdSetTrackMode="$cmd,set track mode,%d*ff<CR><LF>";
    // 返回
    public static final String cmdSetTrackModeResult="$cmd,track mode set ok*ff<CR><LF>";

    // 4.7	节能指令
    // 4.7.1	读取(在监视信息中读取。)
    // 4.7.2	设置(节能状态:0: 关 1: 开)
    public static final String cmdSetEnergySave="$cmd,set energysave, %d*ff<CR><LF>";
    public static final String cmdSetEnergySaveResult="$cmd, energysave set ok*ff<CR><LF>";

    // 4.8	监视信息
    // 4.8.1	监视指令
    public static final String cmdGetSystemState="$cmd,get system state*ff<CR><LF>";
    // $cmd,sys state,预置方位角,预置俯仰角,预置发射极化角，预置接收极化角,当前方位角,当前俯仰角,当前发射极化角,当前接收极化角,寻星状态,本地经度,本地纬度,本地高度, BD/GPS状态,寻星方式,卫星经度,极化方式,寻星门限，AGC电平,信标频率,载波频率、符号率，故障状态,标志 *ff<CR><LF>
    public static final String cmdGetSystemStateResultHead="$cmd,sys state,";
    public static final String cmdGetSystemStateResult= cmdGetSystemStateResultHead + "%f,%f,%f,%f," +
            "%f,%f,%f,%f," +
            "%d,%f,%f,%f," +
            "%d,%d,%f,%d," +
            "%f,%f,%f,%f," +
            "%f,%d,%d*ff<CR><LF>";

    // 4.9	获取便携站温湿度信息
    public static final String cmdGetTemperature="$cmd,get system temp*ff<CR><LF>";
    // 返回数据：$cmd,temp data,温度,湿度*ff<CR><LF>
    public static final String cmdGetTemperatureResultHead="$cmd,cmd,temp data,";
    public static final String cmdGetTemperatureResult=cmdGetTemperatureResultHead + "%f,%f*ff<CR><LF>";

    // 4.10	目标星
    // 4.10.1	读取
    public static final String cmdGetTargetState="$cmd,get target sat*ff<CR><LF>";
    // 返回数据：$cmd,target sat data,信标频率,卫星经度,极化方式,符号率,寻星模式,寻星门限 *ff <CR><LF>
    public static final String cmdGetTargetStateResultHead="$cmd,target sat data,";
    public static final String cmdGetTargetStateResult=cmdGetTargetStateResultHead + "%f,%f,%f,%f,%f,%f*ff<CR><LF>";
    // 4.10.2	设置
    // $cmd,set target sat,信标频率,卫星经度,极化方式,符号率,寻星模式,寻星门限*ff<CR><LF>
    // 极化方式
    /**
     * 0-水平极化；
     1-垂直极化；
     2-左旋圆极化；
     3-右旋圆极化；
     */
    public static final String cmdSetTargetState="$cmd,set target sat,%s,%s,%s,%s,%s,%s*ff<CR><LF>";
    public static final String cmdSetTargetStateResultHead="$cmd,target sat ";
    public static final String cmdSetTargetStateResult=cmdSetTargetStateResultHead + "set ok*ff<CR><LF>";

    // 4.11	参考星
    // 4.11.1	读取 
    public static final String cmdGetRefData="$cmd,get ref sat*ff<CR><LF>";
    // 返回数据：$cmd,ref sat data,信标频率,卫星经度,极化方式,符号率,寻星模式,寻星门限,卫星编号*ff<CR><LF>
    public static final String cmdGetRefDataResultHead="$cmd,ref sat data,";
    public static final String cmdGetRefDataResult = cmdGetRefDataResultHead + "%s,%s,%s,%s,%s,%s,%s*ff<CR><LF>";
    // 4.11.2	设置
    // $cmd,set ref sat,信标频率,卫星经度,极化方式,符号率,寻星模式,寻星门限*ff*ff<CR><LF>
    public static final String cmdSetRefData="$cmd,set ref sat,%s,%s,%s,%s,%s,%s*ff<CR><LF>";
    public static final String cmdSetRefDataResultHead = "$cmd,ref sat ";
    public static final String cmdSetRefDataResult = cmdSetRefDataResultHead + "set ok*ff<CR><LF>";

    // 4.12	经纬度指令
    // 4.12.1	读取
    public static final String cmdGetPosition="$cmd,get position*ff<CR><LF>";
    // 返回数据：$cmd,position data，本地经度, 本地纬度*ff<CR><LF>
    public static final String cmdGetPositionResultHead="$cmd,position data,";
    public static final String cmdGetPositionResult= cmdGetPositionResultHead + "%s,%s*ff<CR><LF>";
    // 4.12.2	设置
    // 发送指令格式：$cmd,set position,本地经度,本地纬度*ff<CR><LF>
    public static final String cmdSetPosition="$cmd,set position,%s,%s*ff<CR><LF>";
    public static final String cmdSetPositionResultHead="$cmd,position ";
    public static final String cmdSetPositionResult=cmdSetPositionResultHead+"ok*ff<CR><LF>";

    // 4.13	寻星指令
    // 4.13.1	自动寻星指令
    public static final String cmdSetAutoSearch="$cmd,auto search*ff<CR><LF>";
    public static final String cmdSetAutoSearchResultHead="$cmd,start auto search ";
    public static final String cmdSetAutoSearchResult=cmdSetAutoSearchResultHead+"ok*ff<CR><LF>";
    // 4.13.2	手动速度控制指令
    // 发送指令格式：$cmd,manual search,调整方式,速度(范围：0.0～10.0度/秒)*ff<CR><LF>
    // 1—方位角增加；2—方位角减小；
    // 3—俯仰角增加；4—俯仰角减小；
    // 5—备用角增加；
    // 6—备用角减小；
    // 7—接收极化增加；
    // 8—接收极化减小；
    public static final String cmdSetManualSearch="$cmd,manual search,%s,%s*ff<CR><LF>";
    public static final String cmdSetManualSearchResultHead="$cmd,start manual search";
    public static final String cmdSetManualSearchResult=cmdSetManualSearchResultHead+"*ff<CR><LF>";
    // 4.13.3	单步位置控制指令
    // $cmd,step control,调整方式,调整角度*ff<CR><LF>
    // 调整方式 同上，  调整角度(范围：0.0～10.0度/秒)
    public static final String cmdStepControl="$cmd,step control,%s,%s*ff<CR><LF>";
    public static final String cmdStepControlResultHead="$cmd,start manual search";
    public static final String cmdStepControlResult=cmdStepControlResultHead+"*ff<CR><LF>";
    // 4.13.4	手动位置控制指令
    // $cmd,manual control,方位，俯仰，备用，极化角*ff<CR><LF>
    public static final String cmdManualControl="$cmd,manual control,%s,%s,%s,%s*ff<CR><LF>";
    public static final String cmdManualControlResultHead="$cmd,start manual control";
    public static final String cmdManualControlResult=cmdManualControlResultHead+"*ff<CR><LF>";
    // 4.13.5	停止寻星指令
    public static final String cmdStopSearch="$cmd,stop search *ff<CR><LF>";
    public static final String cmdStopSearchResultHead="$cmd,search stop";
    public static final String cmdStopSearchResult=cmdStopSearchResultHead+"*ff<CR><LF>";


    // 4.14	功放指令
    // 4.14.1	读取功放信息
    public static final String cmdGetBucInfo="$cmd,get buc info *ff<CR><LF>";
    // 返回数据：$cmd, buc info,功放厂家，功放本振，输出功率，温度，状态，邻星保护状态，功放衰减*ff
    public static final String cmdGetBucInfoResultHead="$cmd, buc info,";
    public static final String cmdGetBucInfoResult=cmdGetBucInfoResultHead+"%s,%s,%s,%s,%s,%s,%s*ff<CR><LF>"; // 不参与计算的一律视为string

    // 4.14.2	功放厂家
    // 4.14.2.1	读取
    public static final String cmdGetBucFactory="$cmd,get buc factory *ff<CR><LF>";
    // $cmd, buc factory,功放厂家*ff
    public static final String cmdGetBucFactoryResultHead="$cmd, buc factory,";
    public static final String cmdGetBucFactoryResult=cmdGetBucFactoryResultHead+"%s*ff<CR><LF>";
    // 4.14.2.2	设置
    // 发送指令格式： $cmd,set buc factory,功放厂家*ff<CR><LF>
    public static final String cmdSetBucFactory="$cmd,set buc factory,功放厂家*ff<CR><LF>";
    public static final String cmdSetBucFactoryResultHead="$cmd,buc factory ";
    public static final String cmdSetBucFactoryResult=cmdSetBucFactoryResultHead+"set ok*ff<CR><LF>";

    // 4.14.3	功放本振
    // 4.14.3.1	读取
    // 终端设备发送指令格式： $cmd,get buc lf*ff<CR><LF>	
    // 便携站返回数据：$cmd, buc lf data,本振值*ff

    // 4.14.3.2	设置
    // 终端设备发送指令格式： $cmd,set buc lf ,本振值*ff<CR><LF>	
    // 便携站返回数据：$cmd, buc lf set ok*ff。


    // 4.14.4	功放衰减-备用
    // 4.14.4.1	读取
    // 终端设备发送指令格式：$cmd,get buc att*ff<CR><LF>
    // 便携站返回数据：$cmd,buc att data,衰减数据*ff<CR><LF>
    // 4.14.4.2	设置
    // 终端设备发送指令格式：$cmd,set buc att,衰减数据*ff<CR><LF>
    // 便携站返回数据：$cmd,start buc att*ff<CR><LF>

    // 4.14.5	邻星保护
    // 4.14.5.1	读取
    // 终端设备发送指令格式：$cmd, get protect state*ff<CR><LF>	
    // 便携站返回数据：$cmd, protect data,1 *ff<CR><LF>
    // 4.14.5.2	设置
    // 终端设备发送指令格式：$cmd, set protect，1*ff<CR><LF>	
    // 便携站返回数据：$cmd, protect set ok*ff<CR><LF>


    // 4.14.6	发射手动
    // 4.14.6.1	打开
    // 终端设备发送指令格式： $cmd,set buc on*ff<CR><LF>	
    // 便携站返回数据：$cmd,start buc on*ff
    // 4.14.6.2	关闭
    // 终端设备发送指令格式： $cmd,set buc off*ff<CR><LF>	
    // 便携站返回数据：$cmd, $cmd,start buc off*ff

    // 4.14.7	功放监视
    // 4.14.7.1	读取  
    // 终端设备发送指令格式：$cmd,get bucinfo switch *ff<CR><LF>
    // 便携站返回数据：$cmd, bucinfo switch,功放监视*ff<CR><LF>
    // 4.14.7.2	设置
    // 终端设备发送指令格式：$cmd,set bucinfo switch,功放监视*ff<CR><LF>
    // 便携站返回数据：$cmd, bucinfo switch set ok*ff<CR><LF>
    // 4.15	LNB本振
    // 4.15.1.1	读取
    // 终端设备发送指令格式： $cmd,get lnb lf *ff<CR><LF>	
    // 便携站返回数据：$cmd, lnb lf data,本振值*ff
    // 4.15.1.2	设置
    // 终端设备发送指令格式： $cmd,set lnb lf ,本振值*ff<CR><LF>	
    // 便携站返回数据：$cmd, lnb lf set ok*ff
    // 4.16	重启
    // 指令格式：$ cmd,reset system*ff<CR><LF>
    // 天线返回数据：$cmd,start reset*ff<CR><LF> 

    // 4.17	寻零-备用
    // 终端设备发送指令格式：$cmd,research zero*ff<CR><LF>
    // 便携站返回数据：$cmd,start research*ff<CR><LF>

    // 5	管理员使用指令
    // 5.1	恢复出厂
    // 重新初始化Flash信息。
    // 终端设备发送指令格式：$cmd,reset flash info*ff<CR><LF>
    // 便携站返回数据：$cmd,flash reset ok*ff<CR><LF>
    // 5.2	天线标定
    // 5.2.1	读取
    // 终端设备发送指令格式：$cmd,get calib ant*ff<CR><LF>	
    //     便携站返回数据：$cmd,calib ant data,方位,俯仰,备用，极化, 俯仰偏移量*ff<CR><LF>

    // 5.2.2	设置
    // 终端设备发送指令格式：$cmd,set calib ant,方位,俯仰，备用，极化, *ff<CR><LF>
    // 便携站返回数据：$cmd,start calib ant*ff<CR><LF>
    // 5.3	展开位置角度
    // 5.3.1	读取
    // 终端设备发送指令格式：$cmd,get lift *ff<CR><LF>
    // 便携站返回数据：$cmd,lift up data,俯仰角*ff<CR><LF>
    // 5.3.2	设置
    // 终端设备发送指令格式：$cmd,set lift,俯仰角*ff<CR><LF>
    // 便携站返回数据：$cmd,lift ok*ff<CR><LF>
    // 5.4	寻星范围
    // 5.4.1	读取
    // 终端设备发送指令格式：$cmd,get search range *ff<CR><LF>
    // 便携站返回数据：$cmd, search range data, 方位范围,俯仰、备用、极化范围*ff<CR><LF>
    // 5.4.2	设置
    // 终端设备发送指令格式：$cmd,set search range, 方位范围,俯仰、备用、极化范围*ff<CR><LF>
    // 便携站返回数据：$cmd, search range ok*ff<CR><LF>
    // 5.5	频段切换指令
    // 5.5.1	读取
    // 终端设备发送指令格式：$cmd,get band*ff<CR><LF>
    // 便携站返回数据：$cmd,band data,波段*ff<CR><LF>
    // 5.5.2	设置
    // 终端设备发送指令格式：$cmd,set band,波段*ff<CR><LF>
    // 便携站返回数据：$cmd,band set ok*ff<CR><LF>
    // 5.6	WIFI名称
    // 5.6.1	读取
    // 终端设备发送指令格式：$cmd, get wifi name*ff<CR><LF>
    // 便携站返回数据：$cmd, wifi name,wifi名称*ff<CR><LF>
    // 5.6.2	设置
    // 终端设备发送指令格式：$cmd,set wifi name*ff<CR><LF>
    // 便携站返回数据：$cmd, wifi name set ok*ff<CR><LF>
    // 5.7	网络参数指令
    // 5.7.1	读取
    // 终端设备发送指令格式：$cmd, get ip *ff<CR><LF>
    // 便携站返回数据：$cmd, ip data,网络IP，子网掩码，网关*ff<CR><LF>
    // 5.7.2	设置
    // 终端设备发送指令格式：$cmd, set ip,网络IP，子网掩码，网关*ff<CR><LF>
    // 便携站返回数据：$cmd, ip set ok*ff<CR><LF>

    // 5.8	网口协议指令
    // 5.8.1	读取
    // 终端设备发送指令格式：$cmd, get net userid *ff<CR><LF>
    // 便携站返回数据：$cmd, net userid data ,端口号,协议*ff<CR><LF>
    // 端口号 目前赋值为：0
    // 40：运行模式
    // 41：转发GPS报文
    // 42：转发信标机报文
    // 43：转发倾角计报文
    // 44：转发功放报文
    // 45：转发手持机报文
    // 49:调试模式
    // 5.8.2	设置
    // 终端设备发送指令格式：$cmd, set net userid,端口号,协议*ff<CR><LF>
    // 便携站返回数据： $cmd,net userid set ok*ff<CR><LF>。

    // 5.9	串口协议选择
    // 5.9.1	读取  
    // 终端设备发送指令格式：$cmd,get com userid *ff<CR><LF>
    // 便携站返回数据：$cmd,com userid,模式*ff<CR><LF>
    // 0: 运行
    // 1：转发COM2 GPS报文
    // 2：转发COM3 信标机报文
    // 3：转发COM4 倾角计报文
    // 4：转发COM5 网络报文
    // 5：转发COM6 功放监控报文
    // 9:调试模式
    // 5.9.2	设置
    // 终端设备发送指令格式：$cmd,set com userid,模式*ff<CR><LF>
    // 便携站返回数据：$cmd,com userid set ok*ff<CR><LF>
    // 5.10	获取LNB状态
    // 终端设备发送指令格式：$ cmd,get lnb state*ff<CR><LF>
    // 便携站返回数据：$cmd,lnb state,状态*ff<CR><LF>



    // 伺服控制通信D0  1故障 0 正常
    // 姿态测量(惯导) D1  1故障 0正常
    // BD/GPS  D2  1故障 0正常
    // 信标机  D3  1故障 0正常
    // 预留  D4  1 故障 0 正常
    // 预留  D5  1 故障 0 正常
    // 方位电机   D6  1 故障 0 正常
    // 俯仰电机   D7  1故障  0正常
    // 横滚/极化发电机 D8  1故障  0正常
    // 极化收电机   D9  1故障  0正常
    // 方位霍尔   D10  1故障  0 正常
    // 俯仰霍尔   D11  1故障  0 正常
    // 横滚/极化霍尔 D12  1故障  0正常
    // 极化收霍尔 D13  1故障  0正常

    public static int getBitValue(int status, int position){
        if (position == 0){
            return status & 1;
        }
        return (status & (int)Math.pow(2,position)) >> position;
    }

    public static String getBitValue(String status, int position){
        int length = status.length();
        if(position >= length - 1 ){
            return status.substring(length-1);
        }
        return status.substring(position, position+1);
    }

    // D0:  0:不节能        1：节能
    // D1：0:俯仰未锁紧1：俯仰锁紧
    // D2:  0:方位未锁紧 1：方位锁紧
    // D3:  0:  1：俯仰电气低限位
    // D4:  0:  1：俯仰电气高限位
    // D5:  0:  1：俯仰软件低限位
    // D6:  0:  1：俯仰软件高限位
    // D7:  0:  1：方位软件低限位
    // D8:  0:  1：方位软件高限位
    // D9:  0:  1：极化软件低限位
    // D10:  0:  1：极化软件高限位

    public static void sendMessage(Context context, String cmd){
        Intent intent = new Intent();
        intent.setAction(ACTION_RECEIVE_DATA);
        intent.putExtra(EXTRA_PARAM_SEND_CMD, cmd);
        context.sendBroadcast(intent);
    }
}
