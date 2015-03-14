# Extracting planners with Maven #

To extract the planners pack (as in external-planners-pack artifact) automatically with your build add following XML to the build part of you POM:

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <version>2.3</version>
    <executions>
        <execution>
            <phase>generate-resources</phase>
            <goals>
                <goal>unpack</goal>
            </goals>
            <configuration>
                <artifactItems>
                    <artifactItem>
                        <groupId>cz.cuni.amis.planning4j.external</groupId>
                        <artifactId>external-planners-pack</artifactId>
                        <outputDirectory>${project.build.directory}</outputDirectory>
                        <includes>planners/**/*</includes>
                    </artifactItem>
                </artifactItems>                            
            </configuration>
        </execution>
    </executions>
</plugin>
```

(note: the planner pack has to be declared as a dependency to your project).