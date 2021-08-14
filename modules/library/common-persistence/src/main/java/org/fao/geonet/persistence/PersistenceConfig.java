/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.persistence;

import java.util.Properties;
import javax.sql.DataSource;
import org.fao.geonet.repository.GeonetRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "org.fao.geonet.repository",
    entityManagerFactoryRef = "gnEntityManager",
    repositoryBaseClass = GeonetRepositoryImpl.class)
@Profile("!withoutSql")
public class PersistenceConfig {

  @Autowired
  DataSource dataSource;

  /**
   * Entity manager scan config.
   */
  @Bean(name = "gnEntityManager")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em
        = new LocalContainerEntityManagerFactoryBean();
    em.setPersistenceUnitName("gn-cloud-persistent-default");
    em.setDataSource(dataSource);
    em.setPackagesToScan(new String[]{
        "org.fao.geonet.repository",
        "org.fao.geonet.domain"});
    // TODO: Would be good to only load a subset of the model
    //  depending on service needs

    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);

    Properties jpaProperties = new Properties();
    jpaProperties.put("hibernate.enable_lazy_load_no_trans", true);
    em.setJpaProperties(jpaProperties);

    return em;
  }
}
