package exception;

public class InvalidLeaveRequestException extends Exception
{
    public InvalidLeaveRequestException(String message)
    {
        super(message);
    }
}