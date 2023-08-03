package org.fao.geonet.common.search.processor.impl;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.fao.geonet.index.converter.FormatterConfiguration;
import org.fao.geonet.index.converter.FormatterConfigurationImpl;
import org.fao.geonet.index.converter.RssConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.junit4.SpringRunner;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.diff.DifferenceEvaluators;

@RunWith(SpringRunner.class)
public class RssResponseProcessorImplTest {
	private static final DifferenceEvaluator EVALUATOR = DifferenceEvaluators.chain(
			DifferenceEvaluators.Default,
			DifferenceEvaluators.ignorePrologDifferencesExceptDoctype());

	@SpyBean
	RssResponseProcessorImpl toTest;

	@Autowired
	FormatterConfiguration formatterConfiguration;

	@Test
	public void channelLinkPointToGnServer() throws Exception {
		formatterConfiguration.setLinkToLegacyGN4(false);

		InputStream is = new ByteArrayInputStream("{}".getBytes(UTF_8));
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		toTest.processResponse(null, is, os,	null, null, null);

		InputStream expected = this.getClass().getResourceAsStream("empty_feed.xml");
		String actual = os.toString(UTF_8);
		Diff diff = DiffBuilder
				.compare(Input.fromStream(expected))
				.withTest(actual)
				.withDifferenceEvaluator(EVALUATOR)
				.ignoreWhitespace()
				.build();
		assertFalse(diff.hasDifferences());
	}

	@Test
	public void nominalOneItem() throws Exception {
		formatterConfiguration.setLinkToLegacyGN4(false);

		InputStream is = this.getClass().getResourceAsStream("es_flow.json");
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		toTest.processResponse(null, is, os,	null, null, null);

		InputStream expected = this.getClass().getResourceAsStream("one_item_feed.xml");
		String actual = os.toString(UTF_8);
		Diff diff = DiffBuilder
						.compare(Input.fromStream(expected))
						.withTest(actual)
						.withDifferenceEvaluator(EVALUATOR)
						.ignoreWhitespace()
						.build();
		assertFalse(diff.hasDifferences());
	}

  @Test
  public void nominalOneItemSxt() throws Exception {
    formatterConfiguration.setLinkToLegacyGN4(false);

    InputStream is = this.getClass().getResourceAsStream("es_flow_sxt.json");
    ByteArrayOutputStream os = new ByteArrayOutputStream();

    toTest.processResponse(null, is, os,	null, null, null);

    /* nothing thrown ? all good */
  }

	@Test
	public void nominalOneItemToGn4() throws Exception {
		formatterConfiguration.setLinkToLegacyGN4(true);

		InputStream is = this.getClass().getResourceAsStream("es_flow.json");
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		toTest.processResponse(null, is, os,	null, null, null);

		InputStream expected = this.getClass().getResourceAsStream("one_item_feed_to_gn4.xml");
		String actual = os.toString(UTF_8);
		Diff diff = DiffBuilder
				.compare(Input.fromStream(expected))
				.withTest(actual)
				.withDifferenceEvaluator(EVALUATOR)
				.ignoreWhitespace()
				.build();
		assertFalse(diff.hasDifferences());
	}

  @Test
  public void nominalOneItemToCustom() throws Exception {
    formatterConfiguration.setLinkToLegacyGN4(true);
    formatterConfiguration.setLinkToCustomUrl(true);

    InputStream is = this.getClass().getResourceAsStream("es_flow.json");
    ByteArrayOutputStream os = new ByteArrayOutputStream();

    toTest.processResponse(null, is, os,	null, null, null);

    InputStream expected = this.getClass().getResourceAsStream("one_item_feed_to_custom.xml");
    String actual = os.toString(UTF_8);
    Diff diff = DiffBuilder
        .compare(Input.fromStream(expected))
        .withTest(actual)
        .withDifferenceEvaluator(EVALUATOR)
        .ignoreWhitespace()
        .build();
    assertFalse(diff.hasDifferences());
  }

	@TestConfiguration
	static class RssResponseProcessorImplTestConf {
		@Bean
		public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
			PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
			Properties properties = new Properties();
			properties.setProperty("gn.legacy.url", "http://gn4:8277/geonetwork");
			properties.setProperty("gn.site.name", "the geonetwork");
			properties.setProperty("gn.site.organization", "momorg");
			properties.setProperty("gn.baseurl", "http://gn5:8277/geonetwork");
			properties.setProperty("gn.linkToLegacyGN4", "true");
			properties.setProperty("gn.linkToCustomMetadataUrl", "false");
      properties.setProperty("gn.customMetadataUrl", "http://gn:8277/custom");
			pspc.setProperties(properties);
			return pspc;
		}

		@Bean
		public FormatterConfiguration rssConfiguration() {
			return new FormatterConfigurationImpl();
		}

		@Bean
		public RssConverter rssConverter() {
			return new RssConverter();
		}
	}
}
