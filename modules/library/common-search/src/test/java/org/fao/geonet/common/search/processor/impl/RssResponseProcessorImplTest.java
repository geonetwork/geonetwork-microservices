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

    /* the previous call should throw an exception, when trying to deserialize via Jackson:
    com.fasterxml.jackson.databind.exc.MismatchedInputException: Cannot deserialize instance of `java.util.ArrayList<java.lang.Object>` out of VALUE_STRING token
     at [Source: (String)"{"docType":"metadata","document":"","metadataIdentifier":"af124329-2883-4cf6-936f-950ff94a69d9","standardNameObject":{"default":"ISO 19115:2003/19139 - SEXTANT","langfre":"ISO 19115:2003/19139 - SEXTANT"},"standardVersionObject":{"default":"1.0","langfre":"1.0"},"indexingDate":"2021-09-06T14:04:23Z","dateStamp":"2020-06-04T00:37:17.000Z","mainLanguage":"fre","cl_characterSet":[{"key":"utf8","default":"Utf8","langfre":"Utf8","link":"http://standards.iso.org/iso/19139/resources/gmxCodelists.xml#MD"[truncated 12092 chars]; line: 1, column: 12344] (through reference chain: org.fao.geonet.index.model.gn.IndexRecord["groupPublished"])
      at com.fasterxml.jackson.databind.exc.MismatchedInputException.from(MismatchedInputException.java:59)
      at com.fasterxml.jackson.databind.DeserializationContext.reportInputMismatch(DeserializationContext.java:1468)
      at com.fasterxml.jackson.databind.DeserializationContext.handleUnexpectedToken(DeserializationContext.java:1242)
      at com.fasterxml.jackson.databind.DeserializationContext.handleUnexpectedToken(DeserializationContext.java:1148)
      at com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer.handleNonArray(StringCollectionDeserializer.java:274)
      at com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer.deserialize(StringCollectionDeserializer.java:183)
      at com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer.deserialize(StringCollectionDeserializer.java:173)
      at com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer.deserialize(StringCollectionDeserializer.java:21)
      at com.fasterxml.jackson.databind.deser.impl.MethodProperty.deserializeAndSet(MethodProperty.java:129)
      at com.fasterxml.jackson.databind.deser.BeanDeserializer.vanillaDeserialize(BeanDeserializer.java:293)
      at com.fasterxml.jackson.databind.deser.BeanDeserializer.deserialize(BeanDeserializer.java:156)
      at com.fasterxml.jackson.databind.ObjectMapper._readMapAndClose(ObjectMapper.java:4526)
      at com.fasterxml.jackson.databind.ObjectMapper.readValue(ObjectMapper.java:3468)
      at com.fasterxml.jackson.databind.ObjectMapper.readValue(ObjectMapper.java:3436)
      at org.fao.geonet.index.converter.RssConverter.convert(RssConverter.java:52)

    Here is what we have in RssConverter:52:

	      IndexRecord record = new ObjectMapper()
          .readValue(doc.get(IndexRecordFieldNames.source).toString(), IndexRecord.class);

    Somehow the document coming from ES cannot be mapped into an IndexRecord type.
     */
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
