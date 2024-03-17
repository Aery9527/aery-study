module aery.study.java.module {

//    requires aery.study.java.module.api;
//    requires static     aery.study.java.module.api; // compile required, runtime not required
    requires transitive aery.study.java.module.api;
    requires org.slf4j; // pass the required to the module that requires this module

    uses org.aery.study.java.module.api.TextGraphicsService;

}
