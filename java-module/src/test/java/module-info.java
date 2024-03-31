open module aery.study.java.module.test {

    requires org.junit.jupiter.api;

    requires transitive aery.study.java.module;
//    requires aery.study.java.module.x;
//    requires aery.study.java.module.z;

    uses org.aery.study.java.module.api.TextGraphicsService;
}
