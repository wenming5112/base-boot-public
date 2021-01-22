FROM java:8
VOLUME /tmp
WORKDIR /
ADD ./tld-web.jar tldollar.jar
RUN bash -c 'touch /tldollar.jar'
EXPOSE 8030
ENTRYPOINT ["java","-jar","/tldollar.jar"]
