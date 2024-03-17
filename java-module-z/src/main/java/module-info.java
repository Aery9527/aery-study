import org.aery.study.java.module.api.TextGraphicsService;

module aery.study.java.module.z {

    requires aery.study.java.module.api;

    provides TextGraphicsService with org.aery.study.java.module.z.ZGraphicsServiceProvider;

}
