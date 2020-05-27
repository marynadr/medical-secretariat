package by.bsu.secretariat.exceptions;

public class InvalidElementAddingException extends DataSourceException{
    public InvalidElementAddingException(){
        super();
    }

    public InvalidElementAddingException(String message){
        super(message);
    }
}
