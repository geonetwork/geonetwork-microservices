package org.fao.geonet.ogcapi.records.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.fao.geonet.common.search.ElasticSearchProxy;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.domain.Source;
import org.fao.geonet.domain.SourceType;
import org.fao.geonet.ogcapi.records.MvcConfigurer;
import org.fao.geonet.repository.LanguageRepository;
import org.fao.geonet.repository.MetadataRepository;
import org.fao.geonet.repository.SourceRepository;
import org.fao.geonet.repository.UiSettingsRepository;
import org.fao.geonet.view.ViewUtility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@RunWith(SpringRunner.class)
@EnableWebMvc
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {ConfigFileApplicationContextInitializer.class})
@WebAppConfiguration("application.yml")
@ActiveProfiles("withoutSql")
public class ItemApiControllerTest {

    @MockBean
    private ElasticSearchProxy mockEsProxy;

    @MockBean
    private LanguageRepository mockLanguageRepository;

    @MockBean
    private SourceRepository mockSourceRepository;

    @MockBean
    private UiSettingsRepository mockUiSettingsRepository;

    @MockBean
    private MetadataRepository mockMetadataRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void nominal() throws Exception {
        doReturn(new Source().setUuid("test-src").setType(SourceType.portal))
                .when(mockSourceRepository)
                .findOneByUuid("test-src");

        doReturn(new String(this.getClass().getResourceAsStream("es_flow.json").readAllBytes(), StandardCharsets.UTF_8))
                .when(mockEsProxy)
                .searchAndGetResult(any(HttpSession.class), any(HttpServletRequest.class), anyString(), nullable(String.class));

        String content = this.mockMvc
                .perform(
                        get("/collections/test-src/items/recordId")
                                .param("f", "dcat_turtle")
                                .accept("text/turtle", "ld+json", "*/*"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(
                dcatForDiff(new String(this.getClass().getResourceAsStream("dcat.txt").readAllBytes(), StandardCharsets.UTF_8)),
                dcatForDiff(content));
    }

    @TestConfiguration
    @Import({ItemApiController.class, ViewUtility.class, MvcConfigurer.class, SearchConfiguration.class})
    @ComponentScan({"org.fao.geonet.common.search", "org.fao.geonet.index.converter", "org.fao.geonet.ogcapi.records"})
    @EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
    static class testConfig {
    }

    private String dcatForDiff(String dcat) {
        return dcat
                .replaceAll("node([0-9a-zA-Z]+)(?:;| )", "node?????? ")
                // seems there is trouble with ci and time zone
                .replaceAll("2021.+;", "2021?????????");
    }
}
