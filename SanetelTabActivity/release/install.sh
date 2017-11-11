#cp -rf /usr/local/nginx /usr/local/nginx.$(date "+%Y%M%d%H%m%s").bak
yum install pcre-devel
yum install zlib-devel
yum install openssl openssl-devel
cd LuaJIT-2.0.4
make
make install

export LUAJIT_LIB=/usr/local/lib
export LUAJIT_INC=/usr/local/include/luajit-2.0.4
export LUA_INCLUDE_DIR=/usr/local/include/luajit-2.0.4
./configure --with-ld-opt=-Wl,-rpath,/usr/local/lib/ --sbin-path=/usr/local/nginx/nginx --conf-path=/usr/local/nginx/nginx.conf --pid-path=/usr/local/nginx/nginx.pid --with-http_ssl_module --with-pcre=pcre-8.41 --with-pcre-jit --with-http_stub_status_module --with-http_sub_module --with-debug --add-module=ngx_devel_kit-0.3.0rc1 --add-module=nginx-module-vts-0.1.15 --add-module=set-misc-nginx-module-0.31 --add-module=lua-nginx-module-0.10.10 --add-module=echo-nginx-module-0.61 --add-module=file-md5-master --add-module=lua-cjson-2.1.0 --add-module=LuaJIT-2.0.4  --add-module=ngx_realtime_request_module-master  --add-module=ngx_req_status-master  --with-zlib=zlib-1.2.11 --with-openssl=openssl-1.1.0g

#ln -s /usr/local/lib/libluajit-5.1.so.2 /lib64/libluajit-5.1.so.2