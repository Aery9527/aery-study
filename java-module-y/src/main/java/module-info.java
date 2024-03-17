import org.aery.study.java.module.api.TextGraphicsService;

module aery.study.java.module.y {

    requires aery.study.java.module.api;

    provides TextGraphicsService with org.aery.study.java.module.y.YGraphicsService;

}
