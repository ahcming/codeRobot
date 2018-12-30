package ahcming.kit.gc;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "gen", defaultPhase = LifecyclePhase.PACKAGE)
public class GenCodeMojo extends AbstractMojo {

    @Parameter
    private String gcc;

    @Override
    public void execute() {
        getLog().warn("-------- GC start --------");
        getLog().info("Config path:" + gcc);
        getLog().warn("-------- GC end --------");

    }
}
