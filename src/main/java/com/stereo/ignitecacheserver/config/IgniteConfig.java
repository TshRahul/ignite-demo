package com.stereo.ignitecacheserver.config;

import com.mchange.v2.c3p0.DriverManagerDataSource;
import com.stereo.ignitecacheserver.modal.Ping;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;
import org.apache.ignite.cache.store.jdbc.dialect.MySQLDialect;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.Factory;
import javax.sql.DataSource;
import java.sql.Types;

@Configuration
@EnableIgniteRepositories
public class IgniteConfig {
    @Bean
    public Ignite igniteInstance() {
        IgniteConfiguration configuration = new IgniteConfiguration();
//		configuration.setGridLogger(new Slf4jLogger());
        configuration.setIgniteInstanceName("Instance2");
        configuration.setClientMode(false);

        CacheConfiguration<Integer, Ping> pingCache = new CacheConfiguration<>();
        pingCache.setName("pingCache");
        configuration.setCacheConfiguration(pingCache);
        configuration.setConsistentId("node2");
        pingCache.setSqlSchema("PING");
        pingCache.setIndexedTypes(Integer.class, Ping.class);
        configuration.setLocalHost("127.0.0.1");


        CacheJdbcPojoStoreFactory<Integer, Ping> factory = new CacheJdbcPojoStoreFactory<>();
        factory.setDialect(new MySQLDialect());
        factory.setDataSourceFactory(getDataSourceFactory());

        JdbcType pingType = getJdbcType();
        factory.setTypes(pingType);
        pingCache.setCacheStoreFactory(factory);
        pingCache.setReadThrough(true);
        pingCache.setWriteThrough(true);
        pingCache.setWriteBehindEnabled(true);
        pingCache.setWriteBehindFlushFrequency(30000);


        Ignite ignite = Ignition.start(configuration);
        ignite.cluster().state(ClusterState.ACTIVE);
        return ignite;
    }

    @NotNull
    private static JdbcType getJdbcType() {
        JdbcType pingType = new JdbcType();
        pingType.setCacheName("pingCache");
        pingType.setDatabaseTable("ping");
        pingType.setKeyType(Integer.class);
        pingType.setKeyFields(new JdbcTypeField(Types.INTEGER, "id", Integer.class, "id"));
        pingType.setValueFields(
                new JdbcTypeField(Types.INTEGER, "id", Integer.class, "id"),
                new JdbcTypeField(Types.LONGNVARCHAR, "timestamp", Long.class, "timestamp")
        );
        pingType.setValueType(Ping.class);

        return pingType;
    }


    private static Factory<DataSource> getDataSourceFactory(){
        return () ->  {
            DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
            driverManagerDataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            driverManagerDataSource.setJdbcUrl("");
            driverManagerDataSource.setUser("root");
            driverManagerDataSource.setPassword("");
            return driverManagerDataSource;
        };

    }

}
