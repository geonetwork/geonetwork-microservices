package org.fao.geonet.authorizing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.fao.geonet.domain.Group;
import org.fao.geonet.domain.Profile;
import org.fao.geonet.domain.User;
import org.fao.geonet.domain.UserGroup;
import org.fao.geonet.repository.GeonetRepositoryImpl;
import org.fao.geonet.repository.GroupRepository;
import org.fao.geonet.repository.UserGroupRepository;
import org.fao.geonet.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GnUserDetailServiceTest.H2JpaConfig.class})
public class GnUserDetailServiceTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  GroupRepository groupRepository;

  @Autowired
  UserGroupRepository userGroupRepository;

  @Autowired
  GnUserDetailsService toTest;

  @Test
  public void nominal() {
    Group group = new Group();
    group.setName("csc");
    groupRepository.save(group);
    User user = new User();
    user.setUsername("csc_editor");
    user.setProfile(Profile.Editor);
    userRepository.save(user);
    UserGroup userGroup = new UserGroup();
    userGroup.setGroup(group);
    userGroup.setUser(user);
    userGroup.setProfile(Profile.Editor);
    userGroupRepository.save(userGroup);

    UserDetails userDetails = toTest.loadUserByUsername("csc_editor");

    assertEquals("csc_editor", userDetails.getUsername());
    assertEquals(1, userDetails.getAuthorities().size());
    assertTrue(userDetails.getAuthorities().stream().findFirst().get() instanceof OAuth2UserAuthority);
    OAuth2UserAuthority authority = (OAuth2UserAuthority) userDetails.getAuthorities().stream().findFirst().get();
    assertEquals("gn", authority.getAuthority());
    Map<String, Object> attributes = authority.getAttributes();
    assertEquals(5, attributes.size());
    assertEquals(Arrays.asList(group.getId()), attributes.get(Profile.Editor.name()));
    assertEquals(Collections.emptyList(), attributes.get(Profile.Reviewer.name()));
    assertEquals(Collections.emptyList(), attributes.get(Profile.UserAdmin.name()));
    assertEquals(Collections.emptyList(), attributes.get(Profile.RegisteredUser.name()));
    assertEquals(Profile.Editor.name(), attributes.get("highest_profile"));
  }

  @Configuration
  @EnableJpaRepositories(
      basePackages = "org.fao.geonet.repository",
      repositoryBaseClass = GeonetRepositoryImpl.class)
  static public class H2JpaConfig {

    @Bean
    public PlatformTransactionManager transactionManager(
        EntityManagerFactory emf){
      JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(emf);

      return transactionManager;
    }

    @Bean
    public DataSource dataSource() {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName("org.h2.Driver");
      dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
      dataSource.setUsername("sa");
      dataSource.setPassword("sa");
      return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
      HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
      vendorAdapter.setDatabase(Database.H2);
      vendorAdapter.setGenerateDdl(true);

      LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
      em.setDataSource(dataSource());
      em.setMappingResources("org.fao.geonet.domain.User");
      em.setJpaVendorAdapter(vendorAdapter);
      em.setJpaProperties(additionalProperties());

      return em;
    }

    @Bean
    public GnUserDetailsService userDetailsService() {
      return new GnUserDetailsService();
    }

    private Properties additionalProperties() {
      Properties properties = new Properties();
      properties.setProperty("hibernate.hbm2ddl.auto", "create");
      properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
      properties.setProperty("hibernate.ddl-auto", "create");
      properties.setProperty("hibernate.use-new-id-generator-mappings", "false");
      properties.setProperty("hibernate.enable_lazy_load_no_trans", "true");
      return properties;
    }
  }
}
