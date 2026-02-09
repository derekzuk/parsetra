# ParseTra

A lightweight, dependency-free Java library for parsing and formatting **human-readable durations** and **data sizes**.

## Features

- **Durations**: Parse `"2h 30m"`, `"1.5d"`, `"90m"`, `"1500ms"` → `java.time.Duration`
- **Sizes**: Parse `"10MB"`, `"1.5GB"`, `"2GiB"` → `long` bytes
- **Formatting**: Format durations and sizes back to human-readable strings
- **No dependencies**: Java 8+ only
- **Small & fast**: Deterministic parsing, thread-safe, clear errors

## Quick Start

### Maven

```xml
<dependency>
    <groupId>io.parsetra</groupId>
    <artifactId>parsetra</artifactId>
    <version>1.0.0</version>
</dependency>
```

*(Use `1.0.0-SNAPSHOT` for development builds.)*

### Duration

```java
import io.parsetra.duration.Durations;
import java.time.Duration;

// Parse (lenient: trims, allows spaces)
Duration d = Durations.parse("2h 30m");
Duration d = Durations.parse("1.5d");
Duration d = Durations.parse("90m");
Duration d = Durations.parse("1d 4h 10m");

// Strict: no leading/trailing whitespace, single space between segments
Duration d = Durations.parseStrict("2h30m");

// Format
String s = Durations.format(d);        // "2h30m"
String s = Durations.formatCompact(d); // "150m"
String s = Durations.formatHuman(d);   // "2h 30m"
```

### Size

```java
import io.parsetra.size.Sizes;

// Parse
long bytes = Sizes.parse("10MB");
long bytes = Sizes.parse("1.5GB");
long bytes = Sizes.parse("2GiB");

// Strict / lenient
Sizes.parseStrict("10MB");
Sizes.parseLenient(" 10 mb ");

// Format
Sizes.format(1536);        // "1.5KB"
Sizes.formatBinary(1536);  // "1.5KiB"
Sizes.formatBytes(1536);  // "1536B"
```

## Supported Units

### Duration

| Unit | Meaning   |
|------|-----------|
| ms   | milliseconds |
| s    | seconds   |
| m    | minutes  |
| h    | hours    |
| d    | days     |

Multi-segment and fractional values are supported (e.g. `2h 30m`, `1.5d`). Case-insensitive.

### Size

**Decimal (base 1000):** B, KB, MB, GB, TB  
**Binary (base 1024):** B, KiB, MiB, GiB, TiB  

Case-insensitive.

## Error Handling

Parsing throws `IllegalArgumentException` for:

- Unknown unit
- Overflow
- Empty string
- Invalid format
- Multiple decimals in a segment

## License

Licensed under the [Apache License, Version 2.0](LICENSE).

## Publishing to Maven Central

For maintainers: to publish a release to Maven Central (or Maven Repository):

1. **Version**: Set a release version in `pom.xml` (e.g. `1.0.0`). Do not publish `-SNAPSHOT` to Central.
2. **Build**: `mvn clean package` produces the main JAR, `-sources.jar`, and `-javadoc.jar` (required by Central).
3. **Signing**: All artifacts must be GPG-signed. Use the [Central Publisher Portal](https://central.sonatype.com/) or the `central-publishing-maven-plugin` / `maven-gpg-plugin` and OSSRH.
4. **POM**: This project already includes required metadata: `name`, `description`, `url`, `licenses`, `developers`, `scm`. Update the `<url>`, `<scm>`, and `<developers>` to your repository and identity before publishing.
