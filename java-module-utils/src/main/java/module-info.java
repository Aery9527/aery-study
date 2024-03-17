open module aery.study.java.module.utils { // open module for reflection

    exports org.aery.study.java.module.utils.vo;
    exports org.aery.study.java.module.utils.tool;
    exports org.aery.study.java.module.utils.printer to aery.study.java.module, aery.study.java.module.phantom; // depend core logic (X)
//    opens   org.aery.study.java.module.utils.hidden; // or can open specific package for reflection

    requires org.slf4j;

}
