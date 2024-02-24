module aery.study.java.module.x {

    requires aery.study.java.module.api;

    provides org.aery.study.java.module.api.TextGraphicsService
            with org.aery.study.java.module.x.XGraphicsService;

}
