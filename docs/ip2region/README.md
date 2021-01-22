
### Maven
```xml
<dependency>
	<groupId>org.lionsoul</groupId>
	<artifactId>ip2region-spring-boot-starter</artifactId>
	<version>${ip2region-boot.version}</version>
</dependency>
```


```java
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableIP2region
@SpringBootApplication
public class Application {
	
	@Autowired
	IP2regionTemplate template;
	
	@PostConstruct
	public void test() throws IOException {
		System.out.println(template.binarySearch("127.0.0.1"));
		System.out.println(template.binarySearch("127.0.0.1"));
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

}
```

```yml
ip2region:
  external: false
  index-block-size: 4096
  total-header-size: 8192
  location: classpath:ip2region/ip2region.db
```