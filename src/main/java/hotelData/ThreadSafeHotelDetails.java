package hotelData;

import com.google.gson.JsonObject;

import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/** This class is extending HotelDetails class and implementing locks */
public class ThreadSafeHotelDetails extends HotelDetails {
    private final ReentrantReadWriteLock lock;
    public ThreadSafeHotelDetails() {
        lock = new ReentrantReadWriteLock();

    }

    /** Overridden getHotel() method from HotelDetails class */
    @Override
    public Hotel getHotel(String hotelID) {
        try{
            lock.readLock().lock();
            return super.getHotel(hotelID);
        }
        finally {
            lock.readLock().unlock();
        }
    }
    /** Overridden addHotel() method from HotelDetails class */
    @Override
    public void addHotel(String hotelId, Hotel hotel) {
        try{
            lock.writeLock().lock();
            super.addHotel(hotelId,hotel);
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    /** Overridden getIdForHotel() method from HotelDetails class */
    public Set<String> getIdForHotel() {
        return super.getIdForHotel();
    }

    /** Overridden getHotelInJSONFormat() method from HotelDetails class */
    @Override
    public JsonObject getHotelInJSONFormat(String hotelId) {
        try{
            lock.readLock().lock();
            return super.getHotelInJSONFormat(hotelId);
        }
        finally {
            lock.readLock().unlock();
        }
    }

    /** Overridden getWeatherDataInJSONFormat() method from HotelDetails class */
//    @Override
//    public JsonObject getWeatherDataInJSONFormat(String hotelId) {
//        try{
//            lock.readLock().lock();
//            return super.getWeatherDataInJSONFormat(hotelId);
//        }
//        finally {
//            lock.readLock().unlock();
//        }
//    }
}
