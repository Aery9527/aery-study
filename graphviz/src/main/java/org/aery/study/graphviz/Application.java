package org.aery.study.graphviz;

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Application {

    public static void main(String[] args) throws IOException {
        Node main = Factory.node("main").with(Label.html("<b>main</b><br/>start"), Color.rgb("1020d0").font());
        Node init = Factory.node(Label.markdown("**_init_**"));
        Node execute = Factory.node("execute");
        Node compare = Factory.node("compare").with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0));
        Node mkString = Factory.node("mkString").with(Label.lines(Label.Justification.LEFT, "make", "a", "multi-line"));
        Node printf = Factory.node("printf");

        Graph g = Factory.graph("example2").directed().with(
                main.link(
                        Factory.to(Factory.node("parse").link(execute)).with(LinkAttr.weight(8)),
                        Factory.to(init).with(Style.DOTTED),
                        Factory.node("cleanup"),
                        Factory.to(printf).with(Style.BOLD, Label.of("100 times"), Color.RED)
                ),
                execute.link(
                        Factory.graph().with(mkString, printf),
                        Factory.to(compare).with(Color.RED)
                ),
                init.link(mkString)
        );

        URL classPathRootUrl = ClassLoader.getSystemClassLoader().getResource("");
        String classPathRoot = classPathRootUrl.toString();
        String anchor = "graphviz";
        int cutIndex = classPathRoot.indexOf(anchor);
        String targetPath = classPathRoot.substring(5, cutIndex + anchor.length() + 1);
        targetPath += "src" + File.separator + "main" + File.separator + "resources" + File.separator;
        String fileName = "sample.png";
        File file = new File(targetPath + fileName);

        Graphviz.fromGraph(g).width(900).render(Format.PNG).toFile(file);
    }

}
