package com.edroplet.sanetel.beans;

import android.content.Context;
import android.content.Intent;

import com.edroplet.sanetel.utils.ConvertUtil;

import static com.edroplet.sanetel.services.CommunicateWithDeviceService.EXTRA_PARAM_SEND_CMD;
import static com.edroplet.sanetel.services.communicate.CommunicateDataReceiver.*;

/**
 * Created by qxs on 2017/11/16.
 * 协议类
 * 标点、逗号都是英文，有逗号的没有空格，
 * 发送的数值是字符串但原值是整形或浮点比如值为1字符串里也是显示1、
 * 值位39.253字符串里也是39.253 用sprintf格式化写入字符串的
 */

public class Protocol {
    // 4.1	复位/展开
    public static final String cmdAntennaExplode="$cmd,develop ant*ff\r\n";
    public static final String cmdAntennaReset="$cmd,develop ant*ff\r\n";
    public static final String cmdAntennaResetResult="$cmd,start develop*ff\r\n";
    public static final String cmdAntennaExplodeResult="$cmd,start develop*ff\r\n";
    // 4.2	收藏
    public static final String cmdAntennaFold="$cmd,stow ant*ff\r\n";
    public static final String cmdAntennaFoldResult="$cmd,start stow*ff\r\n";
    // 4.3	停止、见停止寻星
    public static final String cmdAntennaPause="$cmd,stow ant*ff\r\n";
    public static final String cmdAntennaPauseResult="$cmd,start stow*ff\r\n";

    // 4.4	寻星、见自动寻星


    // 4.5	读取设备信息
    public static final String cmdGetEquipmentInfo="$cmd,get equip info*ff\r\n";
    //返回 设备名称，主控板版本, 软件版本, 软件发布时间
    public static final String cmdGetEquipmentInfoResult="$cmd,equip info,%s,%s,%s,%s*ff\r\n";
    // Scanner s = new Scanner("123 asdf sd 45 789 sdf asdfl,sdf.sdfl,asdf    ......asdfkl    las"); 
    // s.useDelimiter(" |,|\\."); 
    // while (s.hasNext()) { 
    // System.out.println(s.next()); 
    // } 

    // 4.6	跟踪模式选择
    // 4.6.1	读取
    public static final String cmdGetTrackMode="$cmd,get track mode*ff\r\n";
    // 返回<模式名称>(0: 信标机, 1: DVB)
    public static final String cmdGetTrackModeResult="$cmd,track mode data,%d*ff\r\n";
    // 4.6.2	设置 <模式名称>(0: 信标机, 1: DVB)
    public static final String cmdSetTrackMode="$cmd,set track mode,%d*ff\r\n";
    // 返回
    public static final String cmdSetTrackModeResult="$cmd,track mode set ok*ff\r\n";

    // 4.7	节能指令
    // 4.7.1	读取(在监视信息中读取。)
    // 4.7.2	设置(节能状态:0: 关 1: 开)
    public static final String cmdSetEnergySave="$cmd,set energysave,%d*ff\r\n";
    public static final String cmdSetEnergySaveResult="$cmd,energysave set ok*ff\r\n";

    // 4.8	监视信息
    // 4.8.1	监视指令
    public static final String cmdGetSystemState="$cmd,get system state*ff\r\n";
    // $cmd,sys state,预置方位角,预置俯仰角,预置发射极化角，预置接收极化角,当前方位角,当前俯仰角,当前发射极化角,当前接收极化角,寻星状态,本地经度,本地纬度,本地高度, GNSS状态,寻星方式,卫星经度,极化方式,寻星门限，AGC电平,信标频率,载波频率、符号率，故障状态,标志 *ff\r\n
    public static final String cmdGetSystemStateResultHead="$cmd,sys state data,";
    public static final String cmdGetSystemStateResult= cmdGetSystemStateResultHead + "%f,%f,%f,%f," +
            "%f,%f,%f,%f," +
            "%d,%f,%f,%f," +
            "%d,%d,%f,%d," +
            "%f,%f,%f,%f," +
            "%f,%d,%d*ff\r\n";

    /** 故障状态
     * 预留通信 D0  1故障 0 正常
     * 手持机 D1  1故障 0正常
     * GNSS  D2  1故障 0正常
     * 信标机  D3  1故障 0正常
     * 倾角  D4  1 故障 0 正常
     * wifi    D5  1 故障 0 正常
     * 功放   D6  1 故障 0 正常
     * 预留通信    D7  1故障  0正常
     * 方位电机  D8  1故障  0正常
     * 俯仰电机    D9  1故障  0正常
     * 方位霍尔   D10  1故障  0 正常
     * 俯仰霍尔   D11  1故障  0 正常
     * 横滚/极化霍尔 D12  1故障  0正常
     * 极化收霍尔 D13  1故障  0正常
     */
    /**    标志
     *    D0:  0:不节能        1：节能
     D1：0:俯仰未锁紧1：俯仰锁紧
     D2:  0:方位未锁紧 1：方位锁紧
     D3:  0:  1：俯仰电气低限位
     D4:  0:  1：俯仰电气高限位
     D5:  0:  1：俯仰软件低限位
     D6:  0:  1：俯仰软件高限位
     D7:  0:  1：方位软件低限位
     D8:  0:  1：方位软件高限位
     D9:  0:  1：极化软件低限位
     D10:  0:  1：极化软件高限位
     */


    // 4.9	获取便携站温湿度信息
    public static final String cmdGetTemperature="$cmd,get system temp*ff\r\n";
    // 返回数据：$cmd,temp data,温度,湿度*ff\r\n
    public static final String cmdGetTemperatureResultHead="$cmd,cmd,temp data,";
    public static final String cmdGetTemperatureResult=cmdGetTemperatureResultHead + "%f,%f*ff\r\n";

    // 4.10	目标星
    // 4.10.1	读取
    public static final String cmdGetTargetState="$cmd,get target sat*ff\r\n";
    // 返回数据：$cmd,target sat data,信标频率,卫星经度,极化方式,符号率,寻星模式,寻星门限 *ff \r\n
    public static final String cmdGetTargetStateResultHead="$cmd,target sat data,";
    public static final String cmdGetTargetStateResult=cmdGetTargetStateResultHead + "%f,%f,%f,%f,%f,%f*ff\r\n";
    // 4.10.2	设置
    // $cmd,set target sat,信标频率,卫星经度,极化方式,符号率,寻星模式,寻星门限*ff\r\n
    // 极化方式
    /**
     * 0-水平极化；
     1-垂直极化；
     2-左旋圆极化；
     3-右旋圆极化；
     */
    public static final String cmdSetTargetState="$cmd,set target sat,%s,%s,%s,%s,%s,%s*ff\r\n";
    public static final String cmdSetTargetStateResultHead="$cmd,target sat set ";
    public static final String cmdSetTargetStateResult=cmdSetTargetStateResultHead + "ok*ff\r\n";

    // 4.11	参考星
    // 4.11.1	读取 
    public static final String cmdGetRefData="$cmd,get ref sat*ff\r\n";
    // 返回数据：$cmd,ref sat data,信标频率,卫星经度,极化方式,符号率,寻星模式,寻星门限,卫星编号*ff\r\n
    public static final String cmdGetRefDataResultHead="$cmd,ref sat data,";
    public static final String cmdGetRefDataResult = cmdGetRefDataResultHead + "%s,%s,%s,%s,%s,%s,%s*ff\r\n";
    // 4.11.2	设置
    // $cmd,set ref sat,卫星经度,极化方式,寻星门限,信标频率,载波频率,符号率,寻星方式*ff\r\n
    public static final String cmdSetRefData="$cmd,set ref sat,%s,%s,%s,%s,%s,%s,%s*ff\r\n";
    public static final String cmdSetRefDataResultHead = "$cmd,ref sat ";
    public static final String cmdSetRefDataResult = cmdSetRefDataResultHead + "set ok*ff\r\n";

    // 4.12	经纬度指令
    // 4.12.1	读取
    public static final String cmdGetPosition="$cmd,get position*ff\r\n";
    // 返回数据：$cmd,position data，本地经度, 本地纬度*ff\r\n
    public static final String cmdGetPositionResultHead="$cmd,position data,";
    public static final String cmdGetPositionResult= cmdGetPositionResultHead + "%s,%s*ff\r\n";
    // 4.12.2	设置
    // 发送指令格式：$cmd,set position,本地经度,本地纬度*ff\r\n
    public static final String cmdSetPosition="$cmd,set position,%s,%s*ff\r\n";
    public static final String cmdSetPositionResultHead="$cmd,position ";
    public static final String cmdSetPositionResult=cmdSetPositionResultHead+"ok*ff\r\n";

    // 4.13	寻星指令
    // 4.13.1	自动寻星指令
    public static final String cmdSetAutoSearch="$cmd,auto search*ff\r\n";
    public static final String cmdSetAutoSearchResultHead="$cmd,start auto search";
    public static final String cmdSetAutoSearchResult=cmdSetAutoSearchResultHead+"*ff\r\n";
    // 4.13.2	手动速度控制指令
    // 发送指令格式：$cmd,manual search,调整方式,速度(范围：0.0～10.0度/秒)*ff\r\n
    // 1—方位角增加；2—方位角减小；
    // 3—俯仰角增加；4—俯仰角减小；
    // 5—备用角增加；
    // 6—备用角减小；
    // 7—接收极化增加；
    // 8—接收极化减小；
    public static final String cmdManualVel="$cmd,manual vel,%s,%s*ff\r\n";
    public static final String cmdManualVelResultHead="$cmd,start manual vel";
    public static final String cmdManualVelResult=cmdManualVelResultHead+"*ff\r\n";
    // 4.13.3	单步位置控制指令
    // $cmd,step control,调整方式,调整角度*ff\r\n
    // 调整方式 同上，  调整角度(范围：0.0～10.0度/秒)
    public static final String cmdManualStep="$cmd,manual step,%s,%s*ff\r\n";
    public static final String cmdManualStepResultHead="$cmd,start manual step";
    public static final String cmdManualStepResult=cmdManualStepResultHead+"*ff\r\n";
    // 4.13.4	手动位置控制指令(已经修改)
    // $cmd,manual position,方位,俯仰,备用,极化角*ff<CR><LF>
    public static final String cmdManualPosition="$cmd,manual position,%s,%s,%s,%s*ff\r\n";
    public static final String cmdManualPositionResultHead="$cmd,start manual position";
    public static final String cmdManualPositionResult=cmdManualPositionResultHead+"*ff\r\n";
    // 4.13.5	停止寻星指令
    public static final String cmdStopSearch="$cmd,stop search*ff\r\n";
    public static final String cmdStopSearchResultHead="$cmd,search stop";
    public static final String cmdStopSearchResult=cmdStopSearchResultHead+"*ff\r\n";


    // 4.14	功放指令
    // 4.14.1	读取功放信息
    public static final String cmdGetBucInfo="$cmd,get buc info*ff\r\n";
    // 返回数据：$cmd, buc info,功放厂家，功放本振，输出功率，温度，状态，邻星保护状态，功放衰减*ff
    public static final String cmdGetBucInfoResultHead="$cmd,buc info,";
    public static final String cmdGetBucInfoResult=cmdGetBucInfoResultHead+"%s,%s,%s,%s,%s,%s,%s*ff\r\n"; // 不参与计算的一律视为string

    // 4.14.2	功放厂家
    // 4.14.2.1	读取
    public static final String cmdGetBucFactory="$cmd,get buc factory*ff\r\n";
    // $cmd, buc factory,功放厂家*ff
    public static final String cmdGetBucFactoryResultHead="$cmd,buc factory,";
    public static final String cmdGetBucFactoryResult=cmdGetBucFactoryResultHead+"%s*ff\r\n";
    // 4.14.2.2	设置
    // 发送指令格式： $cmd,set buc factory,功放厂家*ff\r\n
    public static final String cmdSetBucFactory="$cmd,set buc factory,%s*ff\r\n";
    public static final String cmdSetBucFactoryResultHead="$cmd,buc factory set ";
    public static final String cmdSetBucFactoryResult=cmdSetBucFactoryResultHead+"ok*ff\r\n";

    // 4.14.3	功放本振
    // 4.14.3.1	读取
    // 终端设备发送指令格式： $cmd,get buc lf*ff\r\n	
    // 便携站返回数据：$cmd, buc lf data,本振值*ff
    public static final String cmdGetBucLf="$cmd,get buc lf*ff\r\n";
    public static final String cmdGetBucLfResultHead="$cmd,buc lf data,";
    public static final String cmdGetBucLfResult=cmdGetBucLfResultHead+"%s*ff\r\n";

    // 4.14.3.2	设置
    // 终端设备发送指令格式： $cmd,set buc lf ,本振值*ff\r\n	
    // 便携站返回数据：$cmd, buc lf set ok*ff。
    public static final String cmdSetBucLf="$cmd,set buc lf,%s*ff\r\n";
    public static final String cmdSetBucLfResultHead="$cmd,buc lf set ";
    public static final String cmdSetBucLfResult=cmdSetBucLfResultHead+"ok*ff\r\n";


    // 4.14.4	功放衰减-备用
    // 4.14.4.1	读取
    // 终端设备发送指令格式：$cmd,get buc att*ff\r\n
    // 便携站返回数据：$cmd,buc att data,衰减数据*ff\r\n
    public static final String cmdGetBucAtt="$cmd,get buc att*ff\r\n";
    public static final String cmdGetBucAttResultHead="$cmd,buc att data,";
    public static final String cmdGetBucAttResult=cmdGetBucAttResultHead+"%s*ff\r\n";

    // 4.14.4.2	设置
    // 终端设备发送指令格式：$cmd,set buc att,衰减数据*ff\r\n
    // 便携站返回数据：$cmd,start buc att*ff\r\n
    public static final String cmdSetBucAtt="$cmd,set buc att,%s*ff\r\n";
    public static final String cmdSetBucAttResultHead="$cmd,start buc att";
    public static final String cmdSetBucAttResult=cmdSetBucAttResultHead+"*ff\r\n";

    // 4.14.5	邻星保护
    // 4.14.5.1	读取
    // 终端设备发送指令格式：$cmd, get protect state*ff\r\n	
    // 便携站返回数据：$cmd, protect data,1 *ff\r\n
    public static final String cmdGetProtectState="$cmd,get protect state*ff\r\n";
    public static final String cmdGetProtectStateResultHead="$cmd,protect state data,";
    public static final String cmdGetProtectStateResult=cmdGetProtectStateResultHead+"%s*ff\r\n";
    // 4.14.5.2	设置
    // 终端设备发送指令格式：$cmd, set protect，1*ff\r\n	
    // 便携站返回数据：$cmd, protect set ok*ff\r\n
    public static final String cmdSetProtectState="$cmd,set protect,%s*ff\r\n";
    public static final String cmdSetProtectStateResultHead="$cmd,start protect set ";
    public static final String cmdSetProtectStateResult=cmdSetProtectStateResultHead+"ok*ff\r\n";


    // 4.14.6	发射手动
    // 4.14.6.1	打开
    // 终端设备发送指令格式： $cmd,set buc on*ff\r\n	
    // 便携站返回数据：$cmd,start buc on*ff
    public static final String cmdSetBucOn="$cmd,set buc on*ff\r\n";
    public static final String cmdSetBucOnResultHead="$cmd,start buc on";
    public static final String cmdSetBucOnResult=cmdSetBucOnResultHead+"*ff\r\n";
    // 4.14.6.2	关闭
    // 终端设备发送指令格式： $cmd,set buc off*ff\r\n	
    // 便携站返回数据：$cmd, $cmd,start buc off*ff
    public static final String cmdSetBucOff="$cmd,set buc off*ff\r\n";
    public static final String cmdSetBucOffResultHead="$cmd,start buc off";
    public static final String cmdSetBucOffResult=cmdSetBucOffResultHead+"*ff\r\n";

    // 4.14.7	功放监视
    // 4.14.7.1	读取  
    // 终端设备发送指令格式：$cmd,get bucinfo switch *ff\r\n
    // 便携站返回数据：$cmd, bucinfo switch,功放监视*ff\r\n
    public static final String cmdGetBucInfoSwitch="$cmd,get bucinfo switch*ff\r\n";
    public static final String cmdGetBucInfoSwitchResultHead="$cmd,bucinfo switch data,";
    public static final String cmdGetBucInfoSwitchResult=cmdGetBucInfoSwitchResultHead+"%s*ff\r\n";
    // 4.14.7.2	设置
    // 终端设备发送指令格式：$cmd,set bucinfo switch,功放监视*ff\r\n
    // 便携站返回数据：$cmd, bucinfo switch set ok*ff\r\n
    public static final String cmdSetBucInfoSwitch="$cmd,set bucinfo switch,%s*ff\r\n";
    public static final String cmdSetBucInfoSwitchResultHead="$cmd,bucinfo switch set ";
    public static final String cmdSetBucInfoSwitchResult=cmdSetBucInfoSwitchResultHead+"ok*ff\r\n";

    // 4.15	LNB本振
    // 4.15.1.1	读取
    // 终端设备发送指令格式： $cmd,get lnb lf *ff\r\n	
    // 便携站返回数据：$cmd, lnb lf data,本振值*ff
    public static final String cmdGetLnbLf="$cmd,get lnb lf*ff\r\n";
    public static final String cmdGetLnbLfResultHead="$cmd,nb lf data,";
    public static final String cmdGetLnbLfResult=cmdGetLnbLfResultHead+"%s*ff\r\n";
    // 4.15.1.2	设置
    // 终端设备发送指令格式： $cmd,set lnb lf ,本振值*ff\r\n	
    // 便携站返回数据：$cmd, lnb lf set ok*ff
    public static final String cmdSetLnbLf="$cmd,set lnb lf,%s*ff\r\n";
    public static final String cmdSetLnbLfResultHead="$cmd,lnb lf set ";
    public static final String cmdSetLnbLfResult=cmdSetLnbLfResultHead+"ok*ff\r\n";
    // 4.16	重启
    // 指令格式：$ cmd,reset system*ff\r\n
    // 天线返回数据：$cmd,start reset*ff\r\n
    public static final String cmdResetSystem="$cmd,reset system*ff\r\n";
    public static final String cmdResetSystemResultHead="$cmd,start reset";
    public static final String cmdResetSystemResult=cmdResetSystemResultHead+"*ff\r\n";

    // 4.17	寻零-备用
    // 终端设备发送指令格式：$cmd,research zero*ff\r\n
    // 便携站返回数据：$cmd,start research*ff\r\n
    public static final String cmdResearchZero="$cmd,research zero*ff\r\n";
    public static final String cmdResearchZeroResultHead="$cmd,start research";
    public static final String cmdResearchZeroResult=cmdResearchZeroResultHead+"*ff\r\n";

    // 5	管理员使用指令
    // 5.1	恢复出厂
    // 重新初始化Flash信息。
    // 终端设备发送指令格式：$cmd,reset flash info*ff\r\n
    // 便携站返回数据：$cmd,flash reset ok*ff\r\n
    public static final String cmdResetFlashInfo="$cmd,reset flash info*ff\r\n";
    public static final String cmdResetFlashInfoResultHead="$cmd,flash reset ";
    public static final String cmdResetFlashInfoResult=cmdResetFlashInfoResultHead+"ok*ff\r\n";
    // 5.2	天线标定
    // 5.2.1	读取
    // 终端设备发送指令格式：$cmd,get calib ant*ff\r\n	
    //     便携站返回数据：$cmd,calib ant data,方位,俯仰,备用，极化, 俯仰偏移量*ff\r\n
    public static final String cmdGetCalibAnt="$cmd,get calib ant*ff\r\n";
    public static final String cmdGetCalibAntResultHead="$cmd,calib ant data,";
    public static final String cmdGetCalibAntResult=cmdGetCalibAntResultHead+"%s,%s,%s,%s,%s*ff\r\n";
    // 5.2.2	设置
    // 终端设备发送指令格式：$cmd,set calib ant,方位,俯仰，备用，极化*ff\r\n
    // 便携站返回数据：$cmd,start calib ant*ff\r\n
    public static final String cmdSetCalibAnt="$cmd,set calib ant,%s,%s,%s,%s*ff\r\n";
    public static final String cmdSetCalibAntResultHead="$cmd,start calib ant";
    public static final String cmdSetCalibAntResult=cmdSetCalibAntResultHead+"*ff\r\n";

    // 5.3	展开位置角度
    // 5.3.1	读取
    // 终端设备发送指令格式：$cmd,get lift *ff\r\n
    // 便携站返回数据：$cmd,lift up data,俯仰角*ff\r\n
    public static final String cmdGetLift="$cmd,get lift*ff\r\n";
    public static final String cmdGetLiftResultHead="$cmd,lift up data,";
    public static final String cmdGetLiftResult=cmdGetLiftResultHead+"%s*ff\r\n";
    // 5.3.2	设置
    // 终端设备发送指令格式：$cmd,set lift,俯仰角*ff\r\n
    // 便携站返回数据：$cmd,lift ok*ff\r\n
    public static final String cmdSetLift="$cmd,set lift*ff\r\n";
    public static final String cmdSetLiftResultHead="$cmd,lift";
    public static final String cmdSetLiftResult=cmdSetLiftResultHead+"ok*ff\r\n";

    // 5.4	寻星范围
    // 5.4.1	读取
    // 终端设备发送指令格式：$cmd,get search range *ff\r\n
    // 便携站返回数据：$cmd, search range data, 方位范围,俯仰、备用、极化范围*ff\r\n
    public static final String cmdGetSearchRange="$cmd,get search range*ff\r\n";
    public static final String cmdGetSearchRangeResultHead="$cmd,search range data,";
    public static final String cmdGetSearchRangeResult=cmdGetSearchRangeResultHead+"%s,%s,%s,%s*ff\r\n";
    // 5.4.2	设置
    // 终端设备发送指令格式：$cmd,set search range, 方位范围,俯仰、备用、极化范围*ff\r\n
    // 便携站返回数据：$cmd, search range ok*ff\r\n
    public static final String cmdSetSearchRange="$cmd,set search range,%s,%s,%s,%s*ff\r\n";
    public static final String cmdSetSearchRangeResultHead="$cmd,search range ";
    public static final String cmdSetSearchRangeResult=cmdSetSearchRangeResultHead+"ok*ff\r\n";

    // 5.5	频段切换指令
    // 5.5.1	读取
    // 终端设备发送指令格式：$cmd,get band*ff\r\n
    // 便携站返回数据：$cmd,band data,波段*ff\r\n
    public static final String cmdGetBand="$cmd,get band*ff\r\n";
    public static final String cmdGetBandResultHead="$cmd,band data,";
    public static final String cmdGetBandResult=cmdGetBandResultHead+"%s*ff\r\n";
    // 5.5.2	设置
    // 终端设备发送指令格式：$cmd,set band,波段*ff\r\n
    // 便携站返回数据：$cmd,band set ok*ff\r\n
    public static final String cmdSetBand="$cmd,set band,%s*ff\r\n";
    public static final String cmdSetBandResultHead="$cmd,band set ";
    public static final String cmdSetBandResult=cmdSetBandResultHead+"ok*ff\r\n";

    // 5.6	WIFI名称
    // 5.6.1	读取
    // 终端设备发送指令格式：$cmd, get wifi name*ff\r\n
    // 便携站返回数据：$cmd, wifi name,wifi名称*ff\r\n
    public static final String cmdGetWifiName="$cmd,get wifi name*ff\r\n";
    public static final String cmdGetWifiNameResultHead="$cmd,wifi name,";
    public static final String cmdGetWifiNameResult=cmdGetWifiNameResultHead+"%s*ff\r\n";
    // 5.6.2	设置
    // 终端设备发送指令格式：$cmd,set wifi name*ff\r\n
    // 便携站返回数据：$cmd, wifi name set ok*ff\r\n
    public static final String cmdSetWifiName="$cmd,set wifi name,%s*ff\r\n";
    public static final String cmdSetWifiNameResultHead="$cmd,wifi name set ";
    public static final String cmdSetWifiNameResult=cmdSetWifiNameResultHead+"ok*ff\r\n";

    // 5.7	网络参数指令
    // 5.7.1	读取
    // 终端设备发送指令格式：$cmd, get ip *ff\r\n
    // 便携站返回数据：$cmd, ip data,网络IP，子网掩码，网关*ff\r\n
    public static final String cmdGetIP="$cmd,get ip*ff\r\n";
    public static final String cmdGetIPResultHead="$cmd,ip data,";
    public static final String cmdGetIPResult=cmdGetIPResultHead+"%s,%s,%s*ff\r\n";
    // 5.7.2	设置
    // 终端设备发送指令格式：$cmd, set ip,网络IP，子网掩码，网关*ff\r\n
    // 便携站返回数据：$cmd, ip set ok*ff\r\n
    public static final String cmdSetIP="$cmd,set ip,%s,%s,%s*ff\r\n";
    public static final String cmdSetIPResultHead="$cmd,ip set ";
    public static final String cmdSetIPResult=cmdSetIPResultHead+"ok*ff\r\n";

    // 5.8	网口协议指令
    // 5.8.1	读取
    // 终端设备发送指令格式：$cmd, get net userid *ff\r\n
    // 便携站返回数据：$cmd, net userid data ,端口号,协议*ff\r\n
    // 端口号 目前赋值为：0
    // 40：运行模式
    // 41：转发GNSS报文
    // 42：转发信标机报文
    // 43：转发倾角计报文
    // 44：转发功放报文
    // 45：转发手持机报文
    // 49:调试模式
    public static final String cmdGetNetUserid="$cmd,get net userid*ff\r\n";
    public static final String cmdGetNetUseridResultHead="$cmd,net userid data,";
    public static final String cmdGetNetUseridResult=cmdGetNetUseridResultHead+"%s,%s*ff\r\n";
    // 5.8.2	设置
    // 终端设备发送指令格式：$cmd, set net userid,端口号,协议*ff\r\n
    // 便携站返回数据： $cmd,net userid set ok*ff\r\n。
    public static final String cmdSetNetUserid="$cmd,set net userid,%s,%s*ff\r\n";
    public static final String cmdSetNetUseridResultHead="$cmd,net userid set ";
    public static final String cmdSetNetUseridResult=cmdSetNetUseridResultHead+"ok*ff\r\n";

    // 5.9	串口协议选择
    // 5.9.1	读取  
    // 终端设备发送指令格式：$cmd,get com userid *ff\r\n
    // 便携站返回数据：$cmd,com userid,模式*ff\r\n
    // 0: 运行
    // 1：转发COM2 GNSS报文
    // 2：转发COM3 信标机报文
    // 3：转发COM4 倾角计报文
    // 4：转发COM5 网络报文
    // 5：转发COM6 功放监控报文
    // 9:调试模式
    public static final String cmdGetComUserid="$cmd,get com userid*ff\r\n";
    public static final String cmdGetComUseridResultHead="$cmd,com userid data,";
    public static final String cmdGetComUseridResult=cmdGetComUseridResultHead+"%s*ff\r\n";
    // 5.9.2	设置
    // 终端设备发送指令格式：$cmd,set com userid,模式*ff\r\n
    // 便携站返回数据：$cmd,com userid set ok*ff\r\n
    public static final String cmdSetComUserid="$cmd,set com userid,%s*ff\r\n";
    public static final String cmdSetComUseridResultHead="$cmd,com userid set ";
    public static final String cmdSetComUseridResult=cmdSetComUseridResultHead+"ok*ff\r\n";

    // 5.10	获取LNB状态
    // 终端设备发送指令格式：$ cmd,get lnb state*ff\r\n
    // 便携站返回数据：$cmd,lnb state,状态*ff\r\n
    public static final String cmdGetLnbState="$cmd,get lnb state*ff\r\n";
    public static final String cmdGetLnbStateResultHead="$cmd,lnb state,";
    public static final String cmdGetLnbStateResult=cmdGetLnbStateResultHead+"%s*ff\r\n";

    /**
     * 获取控制状态
     * @param status
     * @param position
     * @return
     */
    public static int getBitValue(int status, int position){
        //        if (position == 0){
        //            return status & 1;
        //        }
        //        return (status & (int)Math.pow(2,position)) >> position;
        return ConvertUtil.getBitValue(status, position, 0);
    }

    /**
     * 获取控制位状态
     * @param status 状态
     * @param position 位置
     * @return 状态位数值
     */
    public static int getBitValue(String status, int position){
        return ConvertUtil.getBitValue(status, position, 0);
    }

    /**
     * 发送广播消息
     * @param context 上下文
     * @param cmd 命令
     */
    public static void sendMessage(Context context, String cmd){
        Intent intent = new Intent();
        intent.setAction(ACTION_RECEIVE_DATA);
        String v = verifyData(cmd);
        String command = cmd;
        int lastIndexStar = cmd.lastIndexOf('*');
        if (lastIndexStar > 0) {
            command = cmd.substring(0, lastIndexStar) + "*" + v + "\r\n";
        }
        intent.putExtra(EXTRA_PARAM_SEND_CMD, command);
        context.sendBroadcast(intent);
    }

    public static String verifyData (String data){
         int indexDollar = data.indexOf('$');
         int indexStar = data.lastIndexOf('*');
         if (indexDollar < 0 || indexStar < 0){
             return "ff";
         }
         String verifyString  = data.substring(indexDollar+1,indexStar);
         int sum = 0;
         for (char c : verifyString.toCharArray()){
             sum += c;
         }
         return String.format("%02x",sum & 0xff);
    }
}
