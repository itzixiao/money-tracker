#!/bin/bash

# 应用路径
APP_PATH=application/money-tracker-admin
# 应用名
APP_NAME=money-tracker-admin
# 应用端口
APP_PORT=8080

# 应用环境
# 检查$2是否存在，如果不存在（空），则设置APP_PROFILES为'dev'
# 否则，将$2的值赋给APP_PROFILES
APP_PROFILES=$([ -z "$2" ] && echo -n "dev" || echo -n "$2")

# 输出APP_PROFILES的值，用于验证脚本功能
echo "Using application profile: $APP_PROFILES"

# 等待应用启动的时间
APP_START_TIMEOUT=120
# JVM参数
JVM_OPTS="-Duser.timezone=Asia/Shanghai -Xms1024m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps  -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC"

PROG_NAME=$0
ACTION=$1
# 应用健康检查URL
HEALTH_CHECK_URL=http://127.0.0.1:${APP_PORT}/money-tracker-admin/
# 从package.tgz中解压出来的jar包放到这个目录下
APP_HOME=/home/admin/${APP_PATH}
# jar包的名字
JAR_NAME=${APP_HOME}/target/${APP_NAME}.jar
#应用的启动日志
JAVA_OUT=${APP_HOME}/logs/start.log

# 创建出相关目录
mkdir -p ${APP_HOME}
mkdir -p ${APP_HOME}/logs
usage() {
    echo "Usage: $PROG_NAME {start|stop|restart}"
    exit 2
}

health_check() {
    exptime=0
    echo "checking ${HEALTH_CHECK_URL}"
    while true
        do
            status_code=`/usr/bin/curl -L -o /dev/null --connect-timeout 5 -s -w %{http_code}  ${HEALTH_CHECK_URL}`
            if [ "$?" != "0" ]; then
               echo -n -e "\rapplication not started"
            else
                echo "code is $status_code"
                if [ "$status_code" == "200" ];then
                    break
                fi
            fi
            sleep 1
            ((exptime++))

            echo -e "\rWait app to pass health check: $exptime..."

            if [ $exptime -gt ${APP_START_TIMEOUT} ]; then
                echo 'app start failed'
               exit 1
            fi
        done
    echo "check ${HEALTH_CHECK_URL} success"
}
start_application() {
    echo "starting java process"
    nohup java ${JVM_OPTS} -jar -Dserver.port=${APP_PORT} -Dspring.profiles.active=${APP_PROFILES} ${JAR_NAME} > ${JAVA_OUT} 2>&1 &
    echo "started java process"
}

stop_application() {
   check_java_pid=`ps -ef | grep java | grep ${APP_NAME} | grep -v grep |grep -v 'deploy.sh'| awk '{print$2}'`

   if [[ ! $check_java_pid ]];then
      echo -e "\r no java process"
      return
   fi

   echo "stop java process"
   times=60
   for e in $(seq 60)
   do
        sleep 1
        cost_time=$(($times - $e ))
        check_java_pid=`ps -ef | grep java | grep ${APP_NAME} | grep -v grep |grep -v 'deploy.sh'| awk '{print$2}'`
        if [[ $check_java_pid ]];then
            kill -9 $check_java_pid
            echo -e  "\r -- stopping java lasts `expr $cost_time` seconds."
        else
            echo -e "\r java process has exited"
            break;
        fi
   done
   echo ""
}
start() {
    start_application
    health_check
}
stop() {
    stop_application
}
case "$ACTION" in
    start)
        start
    ;;
    stop)
        stop
    ;;
    restart)
        stop
        start
    ;;
    *)
        usage
    ;;
esac
