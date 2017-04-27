#!/bin/sh

#signature.sh
# 转换系统签名命令
./keytool-importkeypair -k qxxww_1021.jks -p xww_1021 -pk8 platform.pk8 -cert platform.x509.pem -alias droplet

# demo.jks : 签名文件
# 123456 : 签名文件密码
# platform.pk8、platform.x509.pem : 系统签名文件
# demo : 签名文件别名