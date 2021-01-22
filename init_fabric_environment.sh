#!/bin/bash
######################################################################################
# CentOS7.5                                                                          #
# Initialize the Linux system to deploy Hyperledger Fabric                           #
# 2019/04/14 15:27                                                                   #
# author:jockming-->.                                                            #
# original article                                                                   #
######################################################################################
# 注意:给脚本添加执行权限
# 执行shell可能报错
# 执行.sh脚本时出现$’\r’: 未找到命令
# 是因为脚本在windows下编辑过，windows下的换行带有‘\r’而uninx或者linux下的换行是‘\n’
# 解决：yum install -y dos2unix
# 然后执行命令：dos2unix *.sh
# chmod +x **.sh
# 参数传递：
# echo "执行的文件名：$0";
# echo "第一个参数为：$1";
# echo "第二个参数为：$2";
# echo "第三个参数为：$3";

######################################################################################
#=========================Install Fabric Network Environment=========================#
######################################################################################

# Install tools
function Install_tools(){
  echo
  echo "##########################################################"
  echo "#####                 Install tools                  #####"
  echo "##########################################################"

  yum -y install openssl-devel \
  openssl \
  gcc-c++ \
  make \
  curl \
  wget \
  tree
}


# Shutdown firewalld or setup the rules
function Firewalld(){
  echo
  echo "##########################################################"
  echo "#####               Setup firewalld                  #####"
  echo "##########################################################"

  # 开放端口(PS: 记得在云服务控制台开放端口)
  firewall-cmd --zone=public --permanent \
  --add-port=3306/tcp --add-port=22/tcp --add-port=6379/tcp --add-port=80/tcp --add-port=443/tcp
  # 重新载入
  firewall-cmd --reload
  # 查看所有开放的端口
  firewall-cmd --zone=public --list-ports

  # systemctl stop firewalld
  # systemctl status firewalld 
  # systemctl disable firewalld
}


# Shutdown SElinux 
function Selinux(){
  echo
  echo "##########################################################"
  echo "#####               Setup SElinux                    #####"
  echo "##########################################################"

  status=`grep SELINUX= /etc/selinux/config | tail -1 | awk -F '='  '{print $2}'`

  if [ $status != disabled ] ;then
    sed -i 's#SELINUX=enforcing#SELINUX=disabled#g' /etc/selinux/config
    status=`grep SELINUX= /etc/selinux/config | tail -1 | awk -F '='  '{print $2}'`
    if [ $status == disabled ] ;then
      echo "***********—Modify selinux config success-***********" 
    else
      echo "***********—Modify selinux config Failed!!!-***********"
    fi
  else
    echo "***********—Selinx config already Modified-***********"
  fi

  setenforce 0  
}


# Setup server time synchronization from ntp server
function TimeSync(){
  # yum -y install ntp ntpdate
  # ntpdate cn.pool.ntp.org
  # hwclock --systohc
  # hwclock -w
  echo
  echo "##########################################################"
  echo "#####               Setup time sync                  #####"
  echo "##########################################################"

  rm -rf /etc/localtime
  ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
  echo "ZONE="Asia/Shanghai"
  UTC=false
  ARC=false">>/etc/sysconfig/clock

  yum install -y ntp
  systemctl start ntpd
  systemctl enable ntpd
  echo "/usr/sbin/ntpdate ntp1.aliyun.com > /dev/null 2>&1; /sbin/hwclock -w">>/etc/rc.d/rc.local
  echo "0 */1 * * * ntpdate ntp1.aliyun.com > /dev/null 2>&1; /sbin/hwclock -w">>/etc/crontab
}


# Setup server langurage env
function Langurage(){
  echo
  echo "##########################################################"
  echo "#####              Setup Langurage env               #####"
  echo "##########################################################"

  echo ' LANG="en_US.UTF-8"'  >/etc/sysconfig/i18n 
  source /etc/sysconfig/i18n
  echo LANG
}


# Update yum repo 
function Yumsource(){
  echo
  echo "##########################################################"
  echo "#####                Setup yum repo                  #####"
  echo "##########################################################"

  cd /etc/yum.repos.d/
  mv CentOS-Base.repo CentOS-Base.repo.bak
  wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
  echo
  echo "#####      Yum clean      #####"
  yum -y clean all 
  echo "#####      To generate cache      #####"
  yum makecache 
  echo
}


# Whether to install docker env
function Install_docker(){
    which docker >& /dev/null
  MYDOCKER=$?
  if [ "${MYDOCKER}" == 0 ]; then
    echo
    echo "##########################################################"
    docker -v
    echo "##########################################################"
  else 
    echo
    echo "##########################################################"
    echo "#####                Install docker                  #####"
    echo "##########################################################"
    yum install -y yum-utils device-mapper-persistent-data lvm2 
    # yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo 
    yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
    yum-config-manager --enable docker-ce-edge 
    yum-config-manager --enable docker-ce-test 
    yum install -y docker-ce 
    systemctl start docker 
    docker --version 
    chkconfig docker on
  fi
}


# Speed up docker
# 可以在https://www.daocloud.io/网站有加速地址。
function Speed_up_docker(){
  echo
  echo "##########################################################"
  echo "#####                Speed up docker                 #####"
  echo "##########################################################"
  mkdir -p /etc/docker
  # 阿里云加速：https://j1i67hhm.mirror.aliyuncs.com
  # 打开这个地址：http://cr.console.aliyun.com/cn-hangzhou/instances/mirrors
  # 使用支付宝快捷登录阿里云可以获取镜像地址
  # Docker版本要求≥1.12
  curl -sSL https://get.daocloud.io/daotools/set_mirror.sh | sh -s https://j1i67hhm.mirror.aliyuncs.com
  systemctl daemon-reload
  systemctl restart docker
}


# Install docker-compose env
function Install_docker_compose(){
  which docker-compose >& /dev/null
  MYDOCKERCOMPOSE=$?
  if [ "${MYDOCKERCOMPOSE}" == 0 ]; then
    echo
    echo "##########################################################"
    docker-compose -version
    echo "##########################################################"
  else
    echo
    echo "##########################################################"
    echo "#####              Install docker-compose            #####"
    echo "##########################################################"

    # 建议将此文件下载下来，访问国外网速度很慢
    # curl -L https://github.com/docker/compose/releases/download/1.22.0/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
	  # 使用国内镜像
    curl -L https://get.daocloud.io/docker/compose/releases/download/1.24.0/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
    # mv -f ./docker-compose /usr/local/bin
    chmod +x /usr/local/bin/docker-compose
    docker-compose -version
  fi
}


# Install go env
function Install_go_env(){
  goVersion="go1.13.5.linux-amd64.tar.gz"
  which go >& /dev/null
  MYGO=$?
  if [ "${MYGO}" == 0 ]; then
    echo
    echo "##########################################################"
    go version
    echo "##########################################################"
  else
    echo
    echo "##########################################################"
    echo "#####                 Install go env                 #####"
    echo "##########################################################"
  # 会访问不到国外的网站，建议事先下载好文件
	goPath="/opt/golang"
  mkdir $goPath
	# 国内的下载地址
  # wget https://studygolang.com/dl/golang/go1.10.3.linux-amd64.tar.gz
	wget https://studygolang.com/dl/golang/$goVersion
	mv -f $goPath/$goVersion $goPath
  tar -zxvf $goPath/$goVersion
	mv $goPath/$goVersion go
  rm -rf $goPath/$goVersion

  echo -e "export GOPATH=/opt/gopath 
  export GOROOT=/opt/golang/go 
  export PATH=\$GOROOT/bin:\$PATH">>/etc/profile
  echo
  source /etc/profile
  go version
  echo
  go env
  fi
}


# Install git env
function Install_git(){
  which git >& /dev/null
  MYGIT=$?
  if [ "${MYGIT}" == 0 ]; then
    echo
    echo "##########################################################"
    git --version
    echo "##########################################################"
  else
    echo
    echo "##########################################################"
    echo "#####                 Install git                    #####"
    echo "##########################################################"
    yum install git -y
  fi
}


# Pull fabric docker images
function Pull_fabric_images(){
  echo
  echo "##########################################################"
  echo "#####               Pull fabric image                #####"
  echo "##########################################################"
  # 后期这些镜像都放到镜像私服（后续搭建私服）中，以后拉取镜像直接到私服去拉取就可以了
  . ./docker_pull_fabric_images.sh 1.4.1 1.4.1 0.4.15
  docker images
}


# 主函数
function main(){
  chmod +x ./*.sh
  dos2unix ./*.sh

  Install_tools

  Firewalld

  Selinux

  TimeSync

  Langurage

  Yumsource

  Install_docker

  Speed_up_docker

  Install_docker_compose

  #Install_go_env

  #Install_git

  #Pull_fabric_images
}

main
