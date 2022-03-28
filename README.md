# AspectJ Maven Plugin

## Overview 

This plugin weaves AspectJ aspects into your classes using the AspectJ compiler `ajc`.
Typically, aspects are used in one of two ways within your Maven reactors:

  * As part of a single project, implying aspects and code are defined within the same Maven module. This is the
    simplest approach to start out with. Feel free to examine the "Examples: single-project AspectJ use" documentation
    section to better understand single-project use.

  * As part of a multi-module Maven reactor, where one or more modules contain aspects and other modules within contain
    code using those aspects ("woven by the aspects"). This is a more complex and powerful approach, best suited when
    several Maven projects should be woven by a common set of aspects. The "Examples: multi-module AspectJ use"
    documentation section contains a basic walk-through of this approach.

## Documentation

Plugin documentation for all AspectJ Maven goals and usage examples can be found here:

https://dev-aspectj.github.io/aspectj-maven-plugin/

## Differences to Mojo's AspectJ Maven Plugin by MojoHaus

At the time of writing this, the MojoHaus team is preparing a new release 1.14.0, the first one in ~3.5 years. They
reactivated the original project, accepting some old pull requests which were long ago contained in a fork (see
[History](#history)). This would have been good news before I started maintaining the project. Now the situation is,
that they refused to contribute to the AspectJ.dev fork. When I tried talking about merging back my changes into their
project instead, my requests for a phone conference or online chat were refused. I wanted to make sure that no changes
or new features would get lost, so I started creating some pull requests and issues. Some trivial ones were accepted,
some (to me) more important ones refused. In the end, I closed them, deciding to keep maintaining this project instead,
because I did not want to lose any prior work by Nick Wong or myself. Please by all means feel free to use the MojoHaus
version, if you feel better with it. Just don't get tripped up by their versioning, continuing with 1.14 instead of 1.13
like me, after Nick had released 1.12.1 and 1.12.6 before already, and I had released 1.13.M2 and 1.13.M3. At first, a
MojoHaus team agreed with me that it was a good idea to continue with 1.13, but now I see that someone created a 1.14.0.

### Feature comparison

As far as I know, the MojoHaus plugin does not have any features that this one here does not have. If there are any, it
would be my own oversight not to have noticed, but I do not think that I missed anything important. The AspectJ.dev
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
  * This plugin supports Java 17 for `complianceLevel`, `source` and `target` since version 1.13, while MojoHaus 1.14.0
    only allows version 16 as a maximum. Since 1.13.1, it even depends on a Java-17-enabled AspectJ version (1.9.8.RC1
    or higher) by default.
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
  * Since 1.13.2, both the plugin builds themselves (including sources and javadocs) and the artifacts produced by the
    plugin are [reproducible](https://reproducible-builds.org/), see also the
    [Maven mini guide](https://maven.apache.org/guides/mini/guide-reproducible-builds.html). For more information, read
    the descriptions for options `argumentFileDirectory` and `argumentFileName`. Mojohaus version 1.14.0 does not
    produce reproducible AspectJ artifacts, only the plugin itself has a partially reproducible build (JAR only, not
    javadoc).
  * The documentation for this plugin is somewhat better than the MojoHaus version, if you look at the
    [Maven site](https://dev-aspectj.github.io/aspectj-maven-plugin/), e.g. the additional UML diagram for the
    [multi-module example](https://dev-aspectj.github.io/aspectj-maven-plugin/multimodule/multimodule_strategy.html)
    and some improved configuration parameter descriptions for the
    [`compile`](https://dev-aspectj.github.io/aspectj-maven-plugin/compile-mojo.html) and
    [`test-compile`](https://dev-aspectj.github.io/aspectj-maven-plugin/test-compile-mojo.html) goals. The same
    help texts for all Maven goals of course are also available from the command line, e.g. via
    `mvn dev.aspectj:aspectj-maven-plugin:help -Ddetail=true -Dgoal=compile`. This is of course true for all Maven
    plugins, I am just mentioning it for your convenience.

## History

This plugin was formerly provided by [**Codehaus**](https://www.infoworld.com/article/2892227/codehaus-the-once-great-house-of-code-has-fallen.html),
then migrated to [**Mojohaus**](https://www.mojohaus.org/) where it was no longer maintained after version 1.11.

Because Java 9+ support was missing, [**Nicholas Wong (nickwongdev)**](https://github.com/nickwongdev) forked and
published it under group ID `com.nickwongdev`. He did this until early 2020 and version 1.12.6 containing Java 13
support. Then he announced he would no longer be available to maintain the plugin and
[recommended forking it again](https://github.com/mojohaus/aspectj-maven-plugin/pull/45#issuecomment-803142741).

Presently, [**Alexander Kriegisch (kriegaex)**](https://github.com/kriegaex) has taken responsibility and upgraded the
plugin to support Java 14-17 and AspectJ version 1.9.7, which he co-developed in collaboration with maintainer Andy
Clement. Future plugin releases will have the group ID `dev.aspectj`, hoping to give the plugin a permanent home,
whoever might maintain it in the future.
