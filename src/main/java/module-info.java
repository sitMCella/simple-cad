module de.sitmcella.simplecad {
    requires javafx.controls;
    requires org.apache.logging.log4j;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires atlantafx.base;

    exports de.sitmcella.simplecad;
    exports de.sitmcella.simplecad.drawer;
    exports de.sitmcella.simplecad.property;
    exports de.sitmcella.simplecad.menu;
    exports de.sitmcella.simplecad.operation;
    exports de.sitmcella.simplecad.storage;
}
