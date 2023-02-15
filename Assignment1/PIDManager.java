public class PIDManager
{
    static final int MIN_PID = 300;
    static final int MAX_PID = 500;
    
    boolean[] gAllocatedPIDs;
    
    public PIDManager()
    {
        try
        {
            AllocateMap();
        }
        // Throws type Error not Exception on failed object initialization
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    public void AllocateMap() throws InvalidPIDException
    {
        try
        {
            gAllocatedPIDs = new boolean[MAX_PID - MIN_PID];
        }
        catch(Error e)
        {
            //OutOfMemoryError is class Error and not class Exception
            throw new InvalidPIDException("Failed to initialize PID Manager - Out Of Memory");
        }
        catch(Exception e)
        {
            throw new InvalidPIDException("Unknown error in PID Manager");
        }
        
        for (int i = 0; i < gAllocatedPIDs.length; i++)
        {
            gAllocatedPIDs[i] = false;
        }
    }
    
    public int AllocatePID() throws InvalidPIDException
    {
        //Get First Unset
        int lPossiblePID = GetNextUnsetBit();
        if (lPossiblePID == -1)
        {
            throw new InvalidPIDException("Process ID Manager is full.");
        }
        gAllocatedPIDs[lPossiblePID] = true;
        return lPossiblePID;
    }
    
    private int GetNextUnsetBit()
    {
        for (int i = 0; i < gAllocatedPIDs.length; i++)
        {
            if (gAllocatedPIDs[i] == false)
            {
                return i;
            }
        }
        return -1;
    }
    
    public void ReleasePID(int aIndex) throws InvalidPIDException
    {
        if (aIndex < MIN_PID || aIndex > MAX_PID)
        {
            throw new InvalidPIDException("Given PID: " + (aIndex + 300) + " " + "is out of range: [" + MIN_PID + ", " + MAX_PID + "]");
        }

        gAllocatedPIDs[MapToIndex(aIndex)] = false;
    }
    
    private int MapToIndex(int aPIDIn)
    {
        //[300, 500] -> [0, 200]
        return aPIDIn - 300;
    }
}
