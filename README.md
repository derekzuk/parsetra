# parsetra

[![CI](https://github.com/derekzuk/parsetra/actions/workflows/ci.yml/badge.svg)](https://github.com/derekzuk/parsetra/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.derekzuk/parsetra.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.derekzuk/parsetra)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

Lightweight Java library for parsing and formatting human-readable **durations** and **data sizes**. Zero dependencies, Java 8+.

---

## Features

- **Durations** — Parse `"2h 30m"`, `"1.5d"`, `"90m"` into `java.time.Duration`; format back to compact or human-readable strings.
- **Data sizes** — Parse `"10MB"`, `"1.5GB"`, `"2GiB"` into byte counts; format with decimal (KB, MB) or binary (KiB, MiB) units.
- **Strict & lenient** — Choose strict tokenization (no extra spaces) or lenient (trim, multiple spaces).
- **Dependency-free** — No third-party libs. Small footprint, thread-safe, deterministic.

## Installation

**Maven:**

```xml
<dependency>
    <groupId>io.github.derekzuk</groupId>
    <artifactId>parsetra</artifactId>
    <version>0.1.0</version>
</dependency>
```

**Gradle (Kotlin):**

```kotlin
implementation("io.github.derekzuk:parsetra:0.1.0")
```

**Gradle (Groovy):**

```groovy
implementation 'io.github.derekzuk:parsetra:0.1.0'
```

## Usage

### Durations

```java
import io.parsetra.duration.Durations;
import java.time.Duration;

// Parse (lenient: trims, allows spaces)
Duration d = Durations.parse("2h 30m");
Duration d = Durations.parse("1.5d");
Duration d = Durations.parse("1d 4h 10m");

// Strict: no leading/trailing whitespace, single space between segments
Duration d = Durations.parseStrict("2h30m");

// Format
String s = Durations.format(d);         // "2h30m"
String s = Durations.formatCompact(d);  // "150m"
String s = Durations.formatHuman(d);   // "2h 30m"
```

### Data sizes

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
Sizes.format(1536);         // "1.5KB"
Sizes.formatBinary(1536);   // "1.5KiB"
Sizes.formatBytes(1536);    // "1536B"
```

## Supported units

| Duration | Size (decimal) | Size (binary) |
|----------|----------------|---------------|
| `ms`, `s`, `m`, `h`, `d` | `B`, `KB`, `MB`, `GB`, `TB` | `B`, `KiB`, `MiB`, `GiB`, `TiB` |

Multi-segment and fractional values are supported (e.g. `2h 30m`, `1.5d`). Units are case-insensitive.

## Error handling

Parsing throws `IllegalArgumentException` for invalid input: unknown unit, overflow, empty string, or invalid format.

## Documentation

- [Javadoc](https://javadoc.io/doc/io.github.derekzuk/parsetra/latest)
- [Maven Central](https://central.sonatype.com/artifact/io.github.derekzuk/parsetra)

## License

[MIT](LICENSE)
