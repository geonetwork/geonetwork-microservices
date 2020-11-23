/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.searching;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.fao.geonet.common.search.ElasticSearchProxy;
import org.fao.geonet.searching.controller.XsltSearchController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class MainSearchTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private XsltSearchController xsltSearchController;

  @Test
  public void nominal() throws Exception {
    xsltSearchController.proxy = Mockito.mock(ElasticSearchProxy.class);
    this.mockMvc.perform(post("/portal/api/search/records/xslt")).andDo(print()).andExpect(status().isOk());
  }


}
