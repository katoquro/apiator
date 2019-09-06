package apiator;

import java.io.File;

public interface ApiatorGradleRunner {
    /**
     * This method must be implemented to call Apiator rendering
     * All the configuration can be done here with access to settings provided by gradle
     */
    void execute(File apiatorBuildDir);
}
