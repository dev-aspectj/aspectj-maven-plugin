# AspectJ Maven Plugin

## Overview 

This plugin weaves AspectJ aspects into your classes using the AspectJ compiler `ajc`. Typically, aspects are used in
one of two ways within your Maven reactors:

  * As part of a _single project_, implying aspects and code are defined within the same Maven project. This is the
    simplest approach to start out with. Feel free to examine the [basic usage](https://dev-aspectj.github.io/aspectj-maven-plugin/usage.html#basic_usage) example to
    better understand single-project use.

  * As part of a _multi-module Maven reactor_ where one or more modules contain aspects and other modules within the
    Maven reactor contain code affected ("woven") by those aspects. This is a more complex and powerful approach, best
    suited when several Maven projects should be woven by a common set of aspects. The
    [AspectJ in a multi-module reactor](https://dev-aspectj.github.io/aspectj-maven-plugin/multimodule/multimodule_strategy.html) example contains a basic walk-through
    of this approach.

## Documentation

Plugin documentation for all AspectJ Maven goals and usage examples can be found
[here](https://dev-aspectj.github.io/aspectj-maven-plugin/).

### How to use the AspectJ and Java versions of your own choice

One of the nicest features of this plugin is that there is no need to upgrade the plugin version, if you just want to
use a new AspectJ version or even a completely new Java language version. The plugin will simply pass on whatever
version you set for `<source>`, `<target>` or `<complianceLevel>` to the AspectJ compiler. The latest supported version
is not hard-coded.

As described in the plugin documentation under
[Upgrading or downgrading AspectJ](https://dev-aspectj.github.io/aspectj-maven-plugin/usage.html#upgrading_or_downgrading_aspectj),
you simply need to set your desired AspectJ tools version - ideally in sync with the AspectJ runtime you use as a module
dependency - as a plugin dependency:

```xml
<project>
  <!-- ... -->
  <properties>
    <!-- Your favourite AspectJ version -->
    <aspectj.version>1.9.21</aspectj.version>
  </properties>

  <dependencies>
    <!-- ... -->
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjrt</artifactId>
      <!-- AspectJ runtime version, in sync with compiler -->
      <version>${aspectj.version}</version>
    </dependency>
    <!-- ... -->
  </dependencies>
  <!-- ... -->
  <build>
    <plugins>
      <plugin>
        <groupId>dev.aspectj</groupId>
        <artifactId>aspectj-maven-plugin</artifactId>
        <version>${project.version}</version>
        <dependencies>
          <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjtools</artifactId>
            <!-- AspectJ compiler version, in sync with runtime -->
            <version>${aspectj.version}</version>
          </dependency>
        </dependencies>
        <configuration>
          <!-- Your favourite Java source/target version -->
          <complianceLevel>21</complianceLevel>
        </configuration>
      </plugin>
      <!-- ... -->
    </plugins>
  </build>
  <!-- ... -->
</project>
```

### FAQ: Why does the plugin not run on my JDK version?

The plugin as such only needs JDK 8. Therefore, if you can build on JDK 8 or need a more recent JDK version only depends
on the `aspectjtools` plugin dependency, i.e. on the AspectJ Compiler (AJC) version. See the previous paragraph for how
to configure that in Maven. In a nutshell, since AJC 1.9.8 needs JDK 11, AJC 1.9.21 needs JDK 17. This is due to the
upstream Eclipse Java Compiler (ECJ) which AJC depends on, not due to AspectJ or AspectJ Maven themselves.

By the way, only AJC has those JDK version requirements. The AspectJ runtime and load time weaver still only need JDK 8,
and even the latest AJC can compile to target Java 8. Please refer to the [AspectJ Java version
compatibility](https://github.com/eclipse-aspectj/aspectj/blob/master/docs/dist/doc/JavaVersionCompatibility.md)
matrix for more details.

## Why is there a fork of the MojoHaus plugin?

The Mojohaus project was inactive for ~3.5 years: no releases, no bugs fixed, to pull requests merged, no reaction to
Nick Wong's fork (see [History](#history)) or my request to make him and/or me Mojohaus committers. Therefore, I
launched this fork, published the first few releases on Maven Central and asked Mojohaus to retire their version.
Suddenly, someone replied and refused to retire their plugin, claiming it was never dead, and work on the plugin should
continue under the Mojohaus umbrella. I agreed under the condition that all of Nick's and my own changes would be merged
into the upstream version, so as not to lose any features or fixes. Some minor stuff was merged, but the review
processes were tedious and some features important to me were refused altogether. So I decided to continue maintaining
the AspectJ.dev version, which ever since has been more up to date with AspectJ and JDK versions and also more
feature-rich.

After a short initial burst of activity and release 1.14.0 in order to catch up with a subset of the changes made to
this fork, not much else has happened at MojoHaus, other than merging Dependabot changes and closing stale tickets.

### Feature comparison MojoHaus vs. AspectJ.dev

As far as I know, the MojoHaus plugin does not have any features that this one here does not have. The AspectJ.dev
version has the following improvements compared to MojoHaus:

  * It is unnecessary to always specify `complianceLevel` in addition to `source` and `target`, because in this plugin
    you can simply use either `complianceLevel` for identical source/target versions or `source` and `target`, if you
    want to use a target byte code level different from the source level. You should not use all three. In MojoHaus you
    have to, because if you do not specify `complianceLevel`, it will counter-intuitively revert to the AspectJ default
    of 1.4, even though you also specified source and target.
  * Since AspectJ 1.9.8.M1, the `--release N` switch works correctly and is supported by a new parameter `release` in
    this plugin. For that purpose, version 1.13 of this plugin depends on AspectJ 1.9.8.M1, while MojoHaus 1.14.0
    depends on AspectJ 1.9.7, which does not have `--release N` support yet. Furthermore, if `release` is used, it
    automatically takes precedence over all of `complianceLevel`, `source`, `target`.
  * This plugin supports Java 20+ for `complianceLevel`, `source` and `target`, while MojoHaus 1.14.0 only allows
    version 16 as a maximum.
  * Since plugin version 1.13.1, there is no upper bounds check for Java versions anymore, i.e. you no longer need to
    upgrade the plugin in order to use a more recent Java compiler source or target version. Simply upgrading AspectJ
    Tools in the plugin dependency configuration will be enough, as long as you do not need any new plugin features. 
  * You can use Java preview features using the `enablePreview` option, which maps to the `--enable-preview` compiler
    option at your own risk. The documentation explains the consequences and limitations, because many Java users are
    unaware of them. I chose to rather add a separate option and document it well than to force users to use the new
    `additionalCompilerArgs` switch, not fully knowing what they are doing.
  * Even though JMS (Java Module System) support is still sketchy, I decided to offer an option `javaModules` in order
    to enable users to at least have a simple way to put Maven dependencies on the module path via the `--module-path`
    compiler option. Robert Scholte [suggested a better way](https://github.com/mojohaus/aspectj-maven-plugin/pull/100#discussion_r646632402)
    to implement JMS support in AspectJ Maven, similar to how Maven Compiler does it, but I am not confident I can
    implement it correctly. He did not volunteer to do it either, so for now this new option is better than nothing.
  * Since 1.14, both the plugin builds themselves (including sources and javadocs) and the artifacts produced by the
    plugin are [reproducible](https://reproducible-builds.org/), see also the
    [Maven mini guide](https://maven.apache.org/guides/mini/guide-reproducible-builds.html). For more information, read
    the descriptions for options `argumentFileDirectory` and `argumentFileName`. Mojohaus version 1.14.0 does not
    produce reproducible AspectJ artifacts, only the plugin itself has a partially reproducible build (JAR only, not
    javadoc).
  * Since 1.14, consider classpath dependencies in "build needed" check. Fixes mojohaus/aspectj-maven-plugin#15.
  * The documentation for this plugin is somewhat better than the MojoHaus version, if you look at the
    [Maven site](https://dev-aspectj.github.io/aspectj-maven-plugin/), e.g. the additional UML diagram for the
    [multi-module example](https://dev-aspectj.github.io/aspectj-maven-plugin/multimodule/multimodule_strategy.html)
    and some improved configuration parameter descriptions for the
    [`compile`](https://dev-aspectj.github.io/aspectj-maven-plugin/compile-mojo.html) and
    [`test-compile`](https://dev-aspectj.github.io/aspectj-maven-plugin/test-compile-mojo.html) goals. The same
    help texts for all Maven goals are also available from the command line, e.g. via
    `mvn dev.aspectj:aspectj-maven-plugin:help -Ddetail=true -Dgoal=compile`. This is of course true for all Maven
    plugins, I am just mentioning it for your convenience.

## History

This plugin was formerly provided by [**Codehaus**](https://www.infoworld.com/article/2892227/codehaus-the-once-great-house-of-code-has-fallen.html),
then migrated to [**Mojohaus**](https://www.mojohaus.org/) where it was no longer maintained after version 1.11.

Because Java 9+ support was missing, [**Nicholas Wong (nickwongdev)**](https://github.com/nickwongdev) forked and
published it under group ID `com.nickwongdev`. He did this until early 2020 and version 1.12.6 containing Java 13
support. Then he announced he would no longer be available to maintain the plugin and
[recommended forking it again](https://github.com/mojohaus/aspectj-maven-plugin/pull/45#issuecomment-803142741).

Presently, [**Alexander Kriegisch (kriegaex)**](https://github.com/kriegaex), who is also the AspectJ maintainer and
project lead, has taken responsibility for the AspectJ.dev plugin. Future plugin releases will have the group ID
`dev.aspectj`, hoping to give the plugin a permanent home, whoever might maintain it in the future.
