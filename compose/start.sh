#
containerName=tldollar:1.0
imagesName=tldollar:1.0

existContainer=`docker inspect --format '{{.State.Running}}' ${containerName}`

function delExistImagesAndContainer() {
    if [ "${existContainer}" == "true" ]; then
        docker stop ${containerName}
        echo "容器：${containerName}已停止"
        docker rm ${containerName}
        echo "容器：${containerName}已删除"

        docker rmi ${imagesName}
        echo "镜像：${containerName}已删除"
    fi
}

function buildJar(){
    echo "正在构建jar镜像"
    docker build -f Dockerfile -t tldollar:1.0 .
    echo "镜像构建完成"
    echo "正在创建并启动容器"
    docker-compose -f ./compose-tldollar.yaml up -d
}


delExistImagesAndContainer

buildJar

docker rmi $(docker images -f "dangling=true" -q)