package apiator;

import java.io.File;

public abstract class ApiatorGradleRunner {
    private File apiatorBuildDir;

    /**
     * This method must be implemented to call Apiator rendering
     * All the configuration can be done here with access to settings provided by gradle
     */
    public abstract void execute();

    public File getApiatorBuildDir() {
        return apiatorBuildDir;
    }

    public void setApiatorBuildDir(File apiatorBuildDir) {
        this.apiatorBuildDir = apiatorBuildDir;
    }
}
