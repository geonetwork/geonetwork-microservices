/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package util;

import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.ogcapi.records.util.RecordsEsQueryBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringJUnit4ClassRunner.class)
class RecordsEsQueryBuilderTest {
    @MockBean
    SearchConfiguration configuration;

    @BeforeEach
    public void setUp() {
      String[] fields = {"resourceTitleObject"};
      this.configuration = Mockito.mock(SearchConfiguration.class);
      given(this.configuration.getSources()).willReturn(Arrays.asList(fields));
      given(this.configuration.getQueryFilter()).willReturn("+isTemplate:n");
    }

  @Test
  void buildQuerySingleRecord() {
    RecordsEsQueryBuilder queryBuilder = new RecordsEsQueryBuilder(configuration);
    String query = queryBuilder.buildQuerySingleRecord("abc", null, null);

    Assert.assertEquals(
        "{\"from\": 0, \"size\": 1, \"query\": {\"query_string\": {\"query\": \"+_id:\\\"abc\\\"  +isTemplate:n\"}}, \"_source\": {\"includes\": [\"*\"]}}",
        query);

    query = queryBuilder.buildQuerySingleRecord("abc", "+source:uio", null);

    Assert.assertEquals(
        "{\"from\": 0, \"size\": 1, \"query\": {\"query_string\": {\"query\": \"+_id:\\\"abc\\\" +source:uio +isTemplate:n\"}}, \"_source\": {\"includes\": [\"*\"]}}",
        query);

    String[] fields = {"resourceType", "cl_status"};
    query = queryBuilder.buildQuerySingleRecord("abc", "+source:uio", Arrays.asList(fields));

    Assert.assertEquals(
        "{\"from\": 0, \"size\": 1, \"query\": {\"query_string\": {\"query\": \"+_id:\\\"abc\\\" +source:uio +isTemplate:n\"}}, \"_source\": {\"includes\": [\"resourceType\", \"cl_status\"]}}",
        query);
  }
}