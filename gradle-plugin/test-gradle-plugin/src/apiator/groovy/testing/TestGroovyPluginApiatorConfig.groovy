package testing

import apiator.ApiatorGradleRunner
import com.ainrif.apiator.api.annotation.Api
import com.ainrif.apiator.core.Apiator
import com.ainrif.apiator.core.ApiatorConfig
import com.ainrif.apiator.core.DocletConfig
import com.ainrif.apiator.provider.jaxrs.JaxRsProvider
import com.ainrif.apiator.renderer.core.html.CoreHtmlRenderer
import com.ainrif.apiator.renderer.plugin.jaxrs.JaxRsCompositePlugin

public class TestGroovyPluginApiatorConfig implements ApiatorGradleRunner {
    @Override
    void execute(File apiatorBuildDir) {
        def config = new ApiatorConfig().with {
            basePackage = 'com.ainrif.apiator.test'
            apiClass = Api
            provider = new JaxRsProvider()
            renderer = new CoreHtmlRenderer({
                toFile = new File(apiatorBuildDir, 'api.html')
                plugins << new JaxRsCompositePlugin()
            })
            docletConfig = new DocletConfig(includeBasePackage: 'com.ainrif')

            it
        }

        new Apiator(config).render()
    }
}
