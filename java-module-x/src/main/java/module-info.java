module aery.study.java.module.x {

    requires aery.study.java.module.api;
    requires aery.study.java.module.utils;

    provides org.aery.study.java.module.api.TextGraphicsService
            with org.aery.study.java.module.x.XGraphicsService;

}
