package com.stereo.ignitecacheserver;

import com.mchange.v2.c3p0.DriverManagerDataSource;
import com.stereo.ignitecacheserver.modal.Employee;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;
import org.apache.ignite.cache.store.jdbc.dialect.BasicJdbcDialect;
import org.apache.ignite.cache.store.jdbc.dialect.MySQLDialect;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.sql.DataSource;
import java.sql.Statement;
import java.sql.Types;
import java.util.Iterator;

@SpringBootApplication
public class IgniteCacheServerApplication {

	public static void main(String[] args) {
		IgniteConfiguration configuration = new IgniteConfiguration();
//		configuration.setGridLogger(new Slf4jLogger());
		configuration.setIgniteInstanceName("Instance1");
		configuration.setClientMode(false);

		CacheConfiguration<Integer, Employee> empCache = new CacheConfiguration<>();
		empCache.setName("EmployeeCache");
		configuration.setCacheConfiguration(empCache);
		empCache.setSqlSchema("PUBLIC");
		empCache.setIndexedTypes(Integer.class, Employee.class);
		configuration.setLocalHost("127.0.0.1");


		CacheJdbcPojoStoreFactory<Integer, Employee> factory = new CacheJdbcPojoStoreFactory<Integer, Employee>();
		factory.setDialect(new MySQLDialect());
		factory.setDataSourceFactory(getDataSourceFactory());

		JdbcType employeeType = getJdbcType();
		factory.setTypes(employeeType);
		empCache.setCacheStoreFactory(factory);
		empCache.setReadThrough(true);
		empCache.setWriteThrough(true);
		empCache.setWriteBehindEnabled(true);
//		empCache.setWriteBehindFlushSize(2);
//		empCache.setWriteBehindFlushFrequency(2000);

//		DataStorageConfiguration dsc = new DataStorageConfiguration();
//		dsc.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
//		configuration.setDataStorageConfiguration(dsc);
		Ignite ignite = Ignition.start(configuration);
//		ignite.cluster().state(ClusterState.ACTIVE);
		IgniteCache<Integer, Employee> empC = ignite.getOrCreateCache("EmployeeCache");
		empC.loadCache(null);
		Iterator<Cache.Entry<Integer, Employee>> iterator = empC.iterator();

		if(!iterator.hasNext()){
			System.out.println("No values available");
		} else {
			iterator.forEachRemaining(d -> {
				System.out.printf("value for key %s is %s", d.getKey(), d.getValue());
			});
		}
//		myCache.put(1, "Rahul");
//		myCache.put(2, "Pareek");
	}

	@NotNull
	private static JdbcType getJdbcType() {
		JdbcType employeeType = new JdbcType();
		employeeType.setCacheName("EmployeeCache");
		employeeType.setDatabaseTable("employee");
		employeeType.setKeyType(Integer.class);
		employeeType.setKeyFields(new JdbcTypeField(Types.INTEGER, "id", Integer.class, "id"));
		employeeType.setValueFields(
				new JdbcTypeField(Types.INTEGER, "id", Integer.class, "id"),
				new JdbcTypeField(Types.VARCHAR, "name", String.class, "name"),
				new JdbcTypeField(Types.VARCHAR, "email", String.class, "email")
		);
		employeeType.setValueType(Employee.class);

		return employeeType;
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
