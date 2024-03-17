import org.aery.study.java.module.api.TextGraphicsService;

@Deprecated // this annotation is for test warning and exhibit here can use annotation
module aery.study.java.module.x {

    requires aery.study.java.module.api;

    provides TextGraphicsService with org.aery.study.java.module.x.XGraphicsService;

}
