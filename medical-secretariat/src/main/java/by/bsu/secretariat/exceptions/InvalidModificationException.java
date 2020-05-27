package by.bsu.secretariat.exceptions;

public class InvalidModificationException extends Exception{
    public InvalidModificationException(){
        super();
    }

    public InvalidModificationException(String message){
        super(message);
    }
}
