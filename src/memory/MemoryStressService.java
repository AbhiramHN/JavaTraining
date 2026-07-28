package memory;

import java.util.ArrayList;

public class MemoryStressService
{
    private final MemoryUsageService memoryUsageService;

    public MemoryStressService()
    {
        memoryUsageService = new MemoryUsageService();
    }

    public void simulateMemoryExhaustion()
    {
        System.out.println("\n===== MEMORY BEFORE STRESS TEST =====");
        memoryUsageService.displayMemoryUsage();

        ArrayList<byte[]> memoryBlocks = new ArrayList<>();

        Runtime runtime = Runtime.getRuntime();

        int allocatedMB = 0;

        try
        {
            while(true)
            {
                long usedMemory = runtime.totalMemory() - runtime.freeMemory();
                long maxMemory = runtime.maxMemory();

                double usagePercentage = ((double) usedMemory / maxMemory) * 100;

                if(usagePercentage >= 90)
                {
                    throw new OutOfMemoryError("Near Memory Exhaustion (Simulated)");
                }

                memoryBlocks.add(new byte[1024 * 1024]); // Allocate 1 MB

                allocatedMB++;

                System.out.println("Allocated : " + allocatedMB + " MB");
            }
        }
        catch(OutOfMemoryError exception)
        {
            System.out.println("\nNear Memory Exhaustion Detected.");
            System.out.println(exception.getMessage());
            System.out.println("Handling memory exhaustion...");
            memoryUsageService.displayMemoryUsage();
        }
        finally
        {
            System.out.println("\nReleasing allocated memory...");

            memoryBlocks.clear();
            memoryBlocks = null;

            System.gc();

            try
            {
                Thread.sleep(2000);
            }
            catch(InterruptedException exception)
            {
                Thread.currentThread().interrupt();
            }

            System.out.println("Memory released successfully.");

            System.out.println("\n===== MEMORY AFTER RECOVERY =====");
            memoryUsageService.displayMemoryUsage();
        }
    }
}