package by.bsu.secretariat.exceptions;

public class DataSourceInitializationException extends DataSourceException {
    public DataSourceInitializationException(){
        super();
    }

    public DataSourceInitializationException(String message){
        super(message);
    }
}