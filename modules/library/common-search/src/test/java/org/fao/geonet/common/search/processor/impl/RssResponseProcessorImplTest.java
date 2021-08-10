package org.fao.geonet.common.search.processor.impl;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.fao.geonet.common.search.processor.impl.rss.RssConfiguration;
import org.fao.geonet.common.search.processor.impl.rss.RssConfigurationWithoutSql;
import org.fao.geonet.common.search.processor.impl.rss.RssResponseProcessorImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
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

	@Test
	public void channelLinkPointToGnServer() throws Exception {
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

	@TestConfiguration
	static class RssResponseProcessorImplTestConf {
		@Bean
		public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
			PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
			Properties properties = new Properties();
			properties.setProperty("gn.legacy.url", "http://host:8277/geonetwork");
			pspc.setProperties(properties);
			return pspc;
		}

		@Bean
		public RssConfiguration rssConfiguration() {
			return new RssConfigurationWithoutSql();
		}
	}
}