package testing;

import apiator.ApiatorGradleRunner;
import com.ainrif.apiator.api.annotation.Api;
import com.ainrif.apiator.core.Apiator;
import com.ainrif.apiator.core.ApiatorConfig;
import com.ainrif.apiator.core.DocletConfig;
import com.ainrif.apiator.provider.jaxrs.JaxRsProvider;
import com.ainrif.apiator.renderer.core.html.CoreHtmlRenderer;
import com.ainrif.apiator.renderer.plugin.jaxrs.JaxRsCompositePlugin;

import java.io.File;

public class TestingJavaPluginApiatorConfig extends ApiatorGradleRunner {
    @Override
    public void execute() {
        CoreHtmlRenderer renderer = new CoreHtmlRenderer();
        renderer.getConfig().setToFile(new File(getApiatorBuildDir(), "api.html").getAbsolutePath());
        renderer.getConfig().getPlugins().add(new JaxRsCompositePlugin());

        DocletConfig docletConfig = new DocletConfig();
        docletConfig.setIncludeBasePackage("com.ainrif");

        ApiatorConfig config = new ApiatorConfig();
        config.setBasePackage("com.ainrif.apiator.test");
        config.setApiClass(Api.class);
        config.setProvider(new JaxRsProvider());
        config.setRenderer(renderer);
        config.setDocletConfig(docletConfig);

        new Apiator(config).render();
    }
}
