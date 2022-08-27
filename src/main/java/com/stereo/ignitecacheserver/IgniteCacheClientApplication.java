package com.stereo.ignitecacheserver;

import com.stereo.ignitecacheserver.modal.Employee;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

import javax.cache.Cache;
import java.util.Iterator;

public class IgniteCacheClientApplication {

	public static void main(String[] args) {
		IgniteConfiguration configuration = new IgniteConfiguration();
//		configuration.setGridLogger(new Slf4jLogger());
		configuration.setIgniteInstanceName("Instance2");
		configuration.setConsistentId("node2");
//		TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
//		TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
//		ipFinder.setAddresses(Arrays.asList("127.0.0.1:47000"));
//		DataStorageConfiguration dsc = new DataStorageConfiguration();
//		dsc.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
//		configuration.setDataStorageConfiguration(dsc);
		configuration.setClientMode(true);
//		configuration.setDiscoverySpi(discoverySpi);
		Ignite ignite = Ignition.start(configuration);
		IgniteCache<Integer, Employee> empC = ignite.cache("EmployeeCache");
		empC.put(5, new Employee(4, "Sandeep", "s_b@optum.com"));
//		empC.loadCache(null);
		Iterator<Cache.Entry<Integer, Employee>> iterator = empC.iterator();

		if(!iterator.hasNext()){
			System.out.println("No values available");
		} else {
			iterator.forEachRemaining(d -> {
				System.out.printf("value for key %s is %s", d.getKey(), d.getValue());
			});
		}
//		System.out.println(empC.get(4));

	}

}
