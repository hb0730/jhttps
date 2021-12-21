<h1 align="center"><a href="https://github.com/hb0730/jhttps" target="_blank">java http integration</a></h1>
<p align="center">
<a href="https://search.maven.org/artifact/com.hb0730/jhttps">
<img alt="Maven Central" src="https://img.shields.io/maven-central/v/com.hb0730/jhttps?style=flat-square">
</a>
<a href="https://github.com/hb0730/jhttps/blob/master/LICENSE">
<img alt="GitHub" src="https://img.shields.io/github/license/hb0730/jhttps?style=flat-square">
</a>
<a href="https://github.com/hb0730/jhttps/actions">
<img alt="GitHub Workflow Status" src="https://img.shields.io/github/workflow/status/hb0730/jhttps/Tag%20Release?style=flat-square">
</a>
<a href="https://www.oracle.com/java/technologies/javase-downloads.html">
<img alt="jdk" src="https://img.shields.io/badge/jdk-8%2B-green?style=flat-square">
</a>
</p>

# jhttps

java http integration (okhttp3,apache client....)

# maven
```xml
<dependency>
  <groupId>com.hb0730</groupId>
  <artifactId>https</artifactId>
  <version>${version}</version>
</dependency>
```

## Support
* sync `Apache httpClient4 -> OkHttp3 v4 -> Hutool-Http` 
* async `Apache httpClient5 ->Okhttp3 v4`
## example
### get
```java
HttpSync http = Https.SYNC.getHttp();
String s = http.get("https://baidu.com");
```
```java
HttpSync http = Https.SYNC.getHttp();
String s = http.get("https://baidu.com");
log.debug(s);
s = http.setHttp(new OkHttp3SyncImpl())
        .get("https://baidu.com");
log.debug(s);
```