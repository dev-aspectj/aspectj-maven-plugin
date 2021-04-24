# AspectJ Maven Plugin

## Overview 
This plugin weaves AspectJ aspects into your classes using the AspectJ compiler `ajc`.
Typically, aspects are used in one of two ways within your Maven reactors:

  * As part of a Single Project, implying aspects and code are defined within the same Maven project.
    This is the simplest approach to start out with; feel free to examine the
    "Examples: Single-project AspectJ use" to better understand single-project use.

  * As part of a Multi-module Maven Reactor where one/some project(s) contains aspects and other
    projects within the Maven reactor contain code using the aspects ("woven by the aspects").
    This is a more complex and powerful approach, best suited when several Maven projects should be woven
    by a common set of aspects. The "Examples: Multi-module AspectJ use" contains a basic walk-through
    of this approach.

## Documentation

[Legacy plugin documentation on Mojohaus](http://www.mojohaus.org/aspectj-maven-plugin/) (up-to-date version available
online shortly). The descriptions and sample projects are still mostly accurate, except for

  * the group ID and plugin version,
  * supported Java versions (Mojohaus says Java 1.3-1.8, this version also supports 9-16),
  * new parameters
      * `<javaModules>` → CLI option `--module-path`
      * `<enablePreview>` → CLI option `--enable-preview`

If you need more up-to-date information now, just add the plugin to your project and then one of the following from the
command line:

  * `mvn aspectj:help` - overview
  * `mvn aspectj:help -Ddetail=true -Dgoal=compile` - details for the 'compile' goal 
  * Similarly, you can get help for the goals `test-compile`, `aspectj-report` and `EclipseAjcMojo` goals. 

## History

This plugin was formerly provided by [**Codehaus**](https://www.infoworld.com/article/2892227/codehaus-the-once-great-house-of-code-has-fallen.html),
then migrated to [**Mojohaus**](https://www.mojohaus.org/) where it was no longer maintained after version 1.11.

Because Java 9+ support was missing, [**Nicholas Wong (nickwongdev)**](https://github.com/nickwongdev) forked and
published it under group ID `com.nickwongdev`. He did this until early 2020 and version 1.12.6 containing Java 13
support. Then he announced he would no longer be available to maintain the plugin and
[recommended forking it again](https://github.com/mojohaus/aspectj-maven-plugin/pull/45#issuecomment-803142741).

Presently, [**Alexander Kriegisch (kriegaex)**](https://github.com/kriegaex) has taken responsibility and upgraded the
plugin to support Java 14-16 and the upcoming AspectJ version 1.9.7, which he also developed in collaboration with
maintainer Andy Clement. Future releases will have the group ID `dev.aspectj`, hoping to give the plugin a permanent
home, whoever might maintain it in the future.
