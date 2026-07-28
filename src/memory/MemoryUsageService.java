package memory;

public class MemoryUsageService
{
    public void displayMemoryUsage()
    {
        Runtime runtime = Runtime.getRuntime();

        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();
        long availableMemory = maxMemory - usedMemory;

        System.out.println("\n========== JVM MEMORY USAGE ==========\n");

        System.out.println("Available Memory : " + availableMemory / (1024 * 1024) + " MB");
        System.out.println("Used Memory      : " + usedMemory / (1024 * 1024) + " MB");
        System.out.println("Free Memory      : " + freeMemory / (1024 * 1024) + " MB");
        System.out.println("Allocated Memory : " + totalMemory / (1024 * 1024) + " MB");
        System.out.println("Maximum Memory   : " + maxMemory / (1024 * 1024) + " MB");

        double usage = ((double) usedMemory / maxMemory) * 100;

        System.out.printf("Memory Usage     : %.2f%%\n", usage);
        System.out.println();
    }
}