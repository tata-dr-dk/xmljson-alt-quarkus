# xmljson-alt-quarkus
A Quarkus route testing jaxb to unmarshal xml to pojos, and jackson to marshal into json

# Replacement for program-cache-manager
The legacy Weblogic based DRIP project, program-cache-manager, uses an old deprecated xml2json library that no longer is supported. This template could be a starting point to replace that project's components/adapers.

Basically, the emulated here integrations are the following, taken from program-cache-manager:
```
wo-channel-in               -> wo-cache-in     -> |x|

wo-flow-publication-in      -> wo-cache-in     -> msub-wo-flow-publication-out/gcp  -> |x|

wo-od-publication-in        -> wo-cache-in     -> msub-wo-od-publication-out/gcp    -> |x|

wo-series-in                -> wo-cache-in     -> msub-wo-presentation-series-out/gcp   -> |x|

wo-parent-series-in         -> wo-cache-in     -> msub-wo-parent-presentation-series-out/gcp    -> |x|

wo-production-in            -> wo-cache-in     -> msub-wo-production-out/gcp    -> |x|
```

WhatsOn sends messages through Oracle AQ queues whenever a change is applied to the business objects (publication, production, series etc.). The DRIP sends a message to wo-cache, to keep the data in sync, then uses the reply from wo-cache (which typically returns the same created/updated object, with additional fields or modifications, e.g. the "touched" field, modifiedTime etc.). The replied object data is then sent to the other systems that want to keep track of WO-data (RAP, MA, ATTP etc.).
So, in a nutshell, this is the flow:

```WO -> WO-Cache -> Other systems```

This current project, a PoC replacement for xml2json uses some messages from the actual production audit trail (http://wlsdrip03pack.lx.dr.dk:7003/audit-trail/#/audit), in unit tests to make sure we replicate the transformations correctly, as close as possible to the actual production DRIP.
The xml messages are taken from the 'in' adapters, e.g. flowPublications from wo-flow-publication-in, and the out json messages are taken from the out adapters going to the other systems, in particular the ones that send to Google Cloud Platform, e.g. msub-wo-flow-publication-out/gcp.

Be aware that the xml 'in' messages are taken as-is without reformatting in the IDE (intellij IDEA). This avoids introducing whitespaces etc. that might make the result json not match the exact output, especially in long text fields. 

Because the xml2json might not output the elements in json in the exact same order (e.g. sometimes object.production fields comes before, sometimes after the object.parentPresentationSeries field), for some of the use cases taken from DRIP, a small rearrangement of the expected output is done to match the new jackson consistent field/element order.
Besides this, some few whitespaces in the DRIP production json results have been removed, an exact 1-to-1 match for whitespaces has proven to be too cumbersome and not worthwhile, e.g. no semantics are changed, newlines \n are kept etc. so it is enough. 

Note also that the current PoC doesn't include the logic to actually call wo-cache, and thus some wo-cache added fields, e.g. the "touched" field, are missing. Thus, the unit tests truncate the json before testing, to make sure everything matches except the typically trailing last fields added by wo-cache not present in this PoC. 

# ------------------------
# Quarkus default README:
## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/xmljson-alt-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- Camel JAXB ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/jaxb.html)): Unmarshal XML payloads to POJOs and back using JAXB2 XML marshalling standard
