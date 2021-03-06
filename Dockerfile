FROM adoptopenjdk:11-jre-hotspot

VOLUME /tmp

ENV TZ=Europe/Berlin
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENV USER booklink-be
RUN useradd -m ${USER}

RUN mkdir /opt/app
WORKDIR /opt/app

COPY target/booklink-backend.jar /opt/app/

USER ${USER}
ENTRYPOINT ["java", "-cp", "/opt/app/booklink-backend.jar"]
CMD ["-Dloader.main=com.github.mrazjava.booklink.BooklinkApp", "org.springframework.boot.loader.PropertiesLauncher"]

EXPOSE 8080