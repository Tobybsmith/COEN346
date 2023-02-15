public class Driver
{
    public static void main(String[] args)
    {
        Manager lManager = new Manager();
        int[] lProcessIDs = new int[200];
        //Implements a very primitive unit test system
        boolean lTestState = true;
        //Should succeed - 200 process IDs available in manager
        try
        {
            for (int i = 0; i < 200; i++)
            {
                lProcessIDs[i] = lManager.CreateProcess();
            }
        }
        catch (Exception e)
        {
            lTestState = false;
            System.out.println("Fail: " + e.toString());
        }
        //Should succeed - valid process termination request
        try
        {
            lManager.TerminateProcess(302);
        }
        catch (Exception e)
        {
            lTestState = false;
            System.out.println("Fail: " + e.toString());
        }
        //Should fail - process ID is out of range [300, 500]
        try
        {
            lManager.TerminateProcess(507);
        }
        catch (Exception e)
        {
            System.out.println("Pass: " + e.toString());
        }
        //Should succeed - we deleted one process so there should be space
        try
        {
            lManager.CreateProcess();
        }
        catch (Exception e)
        {
            lTestState = false;
            System.out.println("Fail: " + e.toString());
        }
        //Should fail - Process ID manager is full
        try
        {
            lManager.CreateProcess();
        }
        catch (Exception e)
        {
            System.out.println("Pass: " + e.toString());
        }
        //Should succeed - 200 processes in manager (PIDs 300 - 500)
        try
        {
            for (int i = 0; i < 200; i++)
            {
                lManager.TerminateProcess(i + 300);
            }
        }
        catch (Exception e)
        {
            lTestState = false;
            System.out.println("Fail: " + e.toString());
        }
        if (lTestState)
        {
            System.out.println("PASSED ALL TESTS");
        }
        else
        {
            System.out.println("FAILED TESTS");
        }
    }
}
