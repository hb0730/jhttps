<h1 align="center"><a href="https://github.com/hb0730/jhttps" target="_blank">java http integration</a></h1>
<p align="center">
<a href="https://search.maven.org/artifact/com.hb0730/https">
<img alt="Maven Central" src="https://img.shields.io/maven-central/v/com.hb0730/https?style=flat-square">
</a>
<a href="https://github.com/hb0730/jhttps/blob/master/LICENSE">
<img alt="GitHub" src="https://img.shields.io/github/license/hb0730/jhttps?style=flat-square">
</a>
<a href="https://github.com/hb0730/jhttps/actions">
<img alt="GitHub Workflow Status" src="https://img.shields.io/github/actions/workflow/status/hb0730/jhttps/release.yml?style=flat-square">
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

# use

```java
import com.hb0730.https.SimpleHttp;

SimpleHttpResponse response=SimpleHttp.HTTP.get("");
```