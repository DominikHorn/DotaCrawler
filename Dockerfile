FROM openjdk:11
RUN mkdir /app
WORKDIR /app
COPY ./build/distributions/*.tar .
RUN tar -xvf *.tar && mv DotaCrawler*/** . && rm -rf *.tar DotaCrawler*
CMD ["bin/DotaCrawler"]
