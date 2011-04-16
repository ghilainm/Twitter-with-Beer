package Network;


import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class LoadBalancer {
	public static void main(String[] args) throws InterruptedException{
		OperatingSystemMXBean opsys = ManagementFactory.getOperatingSystemMXBean();
		System.out.println("System load average: " 			+ opsys.getSystemLoadAverage());
		System.out.println("System available processors: " 	+ opsys.getAvailableProcessors());
		System.out.println("System Name: "		 			+ opsys.getName());
		System.out.println("System arch: "		 			+ opsys.getArch());
		
		PerformanceMonitor perfer = new PerformanceMonitor();
		
		System.out.println("Perf test 01: "		 			+ perfer.getCpuUsage());
		Thread.sleep(3000); 
		System.out.println("Perf test 02: "		 			+ perfer.getCpuUsage());
	}
}
