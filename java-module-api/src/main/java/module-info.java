module aery.study.java.module.api {

    exports org.aery.study.java.module.api;

    requires transitive aery.study.java.module.utils; // pass the required to the module that requires this module (like gradle "api")

    uses org.aery.study.java.module.api.TextGraphicsService;

}
