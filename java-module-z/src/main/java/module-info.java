module aery.study.java.module.z {

    requires aery.study.java.module.api;

    provides org.aery.study.java.module.api.TextGraphicsService
            with org.aery.study.java.module.z.ZGraphicsServiceProvider;

}
