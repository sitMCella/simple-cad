# Simple CAD

![Document](https://github.com/sitMCella/simple-cad/wiki/images/simple_cad.png)

## Table of contents

* [Introduction](#introduction)
* [Development](#development)
    * [Requirements](#requirements)
    * [Development](#development)

## Introduction

Simple CAD is a desktop application that aims to provide a general purpuse 2D CAD modeler.

Read the [documentation](https://github.com/sitMCella/simple-cad/wiki) in order to discover the features of Simple CAD.

## Development

### Requirements

Apache Maven >= 3.6.x, Java 21.

### Cheat Sheets

Ikonli Material Design 2:
https://kordamp.org/ikonli/cheat-sheet-materialdesign2.html

### Development

Build the application:

```sh
mvn clean install
```

Format Java code:

```sh
mvn spotless:apply
```

Execute the JavaFX application:

```sh
mvn clean javafx:run
```
